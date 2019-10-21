package com.ray.dormitory.config;


import com.ray.dormitory.bean.Role;
import com.ray.dormitory.bean.User;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.JWTUtil;
import com.ray.dormitory.util.bean.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String account = JWTUtil.getAccount(principals.toString());
        User user = userService.getUserByAccount(account);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Set<String> roleNames = new HashSet<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        simpleAuthorizationInfo.addRoles(roleNames);


        simpleAuthorizationInfo.addStringPermissions(userService.getUserPermission(account));
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String account = JWTUtil.getAccount(token);
        if (account == null) {
            throw new AuthenticationException("token invalid");
        }

        User user = userService.getUserByAccount(account);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (!JWTUtil.verify(token, account)) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}