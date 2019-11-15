package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.User;

import java.util.Set;

public interface UserService extends IService<User> {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);


    Set<String> getUserPermission(String account);
}
