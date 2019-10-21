package com.ray.dormitory.service.impl;

import com.ray.dormitory.bean.Role;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getById(int id) {
        return roleMapper.selectById(id);
    }
}
