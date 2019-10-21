package com.ray.dormitory.service;

import com.ray.dormitory.bean.User;

import java.util.Set;

public interface UserService {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);


    Set<String> getUserPermission(String account);
}
