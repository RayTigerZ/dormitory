package com.ray.dormitory.controller;

import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/test")
    public Role test(int id) {
        return roleService.getById(id);
    }
}
