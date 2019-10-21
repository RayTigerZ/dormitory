package com.ray.dormitory.service.impl;

import com.ray.dormitory.bean.User;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByAccount(String account) {
        return null;
    }

    @Override
    public Set<String> getUserPermission(String account) {
        return userMapper.getUserPermissions(account);
    }
}
