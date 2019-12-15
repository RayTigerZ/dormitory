package com.ray.dormitory.controller;

import com.ray.dormitory.bean.po.Permission;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.service.PermissionService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Ray
 * @date : 2019.11.21 11:38
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/menu")
    private Permission initMenu(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        int userId = 0;

        //校验登陆的token是否与缓存中的token保持一致
        if (authorization != null && JwtUtil.verifyToken(authorization)) {
            userId = JwtUtil.getId(authorization);
            return permissionService.getMenuOfUser(userId);
        } else {
            throw new CustomException(ErrorEnum.ERROR_302);
        }

    }

    /**
     * 获取前端菜单树形结构
     *
     * @return
     */
    @GetMapping("/all")
    public Permission tree() {

        return permissionService.getAllPermission();

    }

    @PostMapping("/save")
    public boolean addOrUpdate(Permission permission) {

        return permissionService.saveOrUpdate(permission);

    }


}
