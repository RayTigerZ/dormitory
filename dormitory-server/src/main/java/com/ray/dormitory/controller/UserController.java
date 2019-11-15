package com.ray.dormitory.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ray.dormitory.bean.ResponseBean;
import com.ray.dormitory.bean.User;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * @author Ray
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public ResponseBean login(String account, String password) {
        try {
            User user = userService.getUserByAccount(account);

            if (user == null) {
                return new ResponseBean(204, "用户名或密码错误", "用户名或密码错误");
            }

            String str = password + user.getSalt();
            if (user.getPassword().equals(MD5Util.getMD5(str))) {
                if (user.getIsUsable() != null && user.getIsUsable()) {
                    throw new UnsupportedEncodingException("该用户已被禁用");
                }

                Algorithm algorithm = Algorithm.HMAC256(JwtUtil.getSecret());
                String token = JWT.create()
                        .withClaim("account", account)
                        .withClaim("name", user.getName())
                        .withClaim("password", user.getPassword())
                        .withClaim("salt", user.getSalt())
                        .withClaim("IsUsable", user.getIsUsable())
                        .withClaim("roles", user.getRoles().toString())
                        .sign(algorithm);

                redisService.remove(account);

                redisService.set(JwtUtil.getAccountUserKey(account), token);
                //记录用户的登录时间
                redisService.set(JwtUtil.getAccountTimeKey(account), String.valueOf(System.currentTimeMillis()));

                return new ResponseBean(200, "登录成功", initMenu(user));
            }
            return new ResponseBean(204, "用户名或密码错误", "用户名或密码错误");

        } catch (UnsupportedEncodingException e) {
            return new ResponseBean(204, "登录异常:" + e.getMessage());
        }

    }

    @PostMapping("/logout")
    public ResponseBean logout(HttpServletRequest servletRequest) {
        try {
            String authorization = servletRequest.getHeader("Authorization");
            String account = null;

            //校验登陆的token是否与缓存中的token保持一致
            if (authorization != null && JwtUtil.verifyToken(authorization)) {
                account = JwtUtil.getAccount(authorization);
            }
            if (account == null) {
                return new ResponseBean(200, "退出登录失败：验证失败，账号为空");
            }

            redisService.remove(account);
            return new ResponseBean(200, "退出登录成功");
        } catch (Exception e) {
            return new ResponseBean(200, "退出登录失败：" + e.getMessage());
        }
    }


    private Map<String, Object> initMenu(User user) {

        return null;
    }
}
