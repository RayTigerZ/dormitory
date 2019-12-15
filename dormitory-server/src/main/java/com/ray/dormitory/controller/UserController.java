package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.UploadDataListener;
import com.ray.dormitory.util.bean.MD5Util;
import com.ray.dormitory.util.bean.ResponseBean;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Ray
 * @date 2019/12/04 11:11
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
    public ResponseBean login(@RequestParam String account, @RequestParam String password) {
        try {
            User user = userService.getUserByAccount(account);
            log.info("{}", user);
            if (user == null) {
                return new ResponseBean(204, "用户名或密码错误", "用户名或密码错误");
            }

            String str = password + user.getSalt();
            if (user.getPassword().equals(MD5Util.getMD5(str))) {
                if (user.getIsUsable() == null && !user.getIsUsable()) {
                    throw new UnsupportedEncodingException("该用户已被禁用");
                }

                Algorithm algorithm = Algorithm.HMAC256(JwtUtil.getSecret());
                String token = JWT.create()
                        .withClaim("id", user.getId())
                        .withClaim("account", account)
                        .withClaim("name", user.getName())
                        .withClaim("password", user.getPassword())
                        .withClaim("salt", user.getSalt())
                        .withClaim("isUsable", user.getIsUsable())
                        .withClaim("roles", new Gson().toJson(user.getRoles()))
                        .sign(algorithm);

                redisService.remove(JwtUtil.getAccountUserKey(account));
                redisService.set(JwtUtil.getAccountUserKey(account), token);
                //记录用户的登录时间
                redisService.set(JwtUtil.getAccountTimeKey(account), String.valueOf(System.currentTimeMillis()));
                Map<String, String> map = new HashMap<>(2);
                map.put("token", token);
                map.put("name", user.getName());

                return new ResponseBean(200, "登录成功", map);
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

            redisService.remove(JwtUtil.getAccountUserKey(account));
            redisService.remove(JwtUtil.getAccountTimeKey(account));
            return new ResponseBean(200, "退出登录成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseBean(200, "退出登录失败：" + e.getMessage());
        }
    }


    @GetMapping("/list")
    public IPage<User> userPage(Integer roleId, String account, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, Integer[] classId) {

        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(account), "account", account)
                .inSql(roleId != null, "user.id", "select user_id from user_role where role_id='" + roleId + "'")
                .in(classId != null, "class_id", classId);

        return userService.page(page, queryWrapper);


    }


    @PostMapping("/editPsw")
    public boolean editPsw(HttpServletRequest request, String oldPsw, String newPsw) {

        String token = request.getHeader("Authorization");
        String account = JwtUtil.getAccount(token);
        return userService.updatePassword(account, newPsw, oldPsw);


    }

    @PostMapping("/batchSave")
    public boolean batchSave(MultipartFile file, String time) throws IOException {

        EasyExcel.read(file.getInputStream(), User.class, new UploadDataListener(userService, time)).sheet().doRead();
        return true;


    }

    @PostMapping("/save")
    public boolean save(User user, Integer[] roleId) {

        List<Role> roleList = new ArrayList<>();
        for (int rid : roleId) {
            Role role = new Role();
            role.setId(rid);
            roleList.add(role);
        }
        user.setRoles(roleList);

        return userService.saveOrUpdate(user);

    }

    @PostMapping("/enable")
    public boolean enable(@NonNull Integer id) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).setSql("is_usable=!is_usable");
        return userService.update(updateWrapper);
    }


}
