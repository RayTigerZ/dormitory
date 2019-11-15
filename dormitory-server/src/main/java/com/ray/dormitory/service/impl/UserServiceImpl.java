package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.User;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Ray Z
 * @date 2019/10/27 21:54:57
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User getUserByAccount(String account) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("account", account);
        return baseMapper.selectOne(userWrapper);
    }

    @Override
    public Set<String> getUserPermission(String account) {
        return baseMapper.getUserPermissions(account);
    }
}
