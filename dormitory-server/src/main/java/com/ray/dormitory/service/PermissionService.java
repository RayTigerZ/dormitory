package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.Permission;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.21 12:45
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 获取用户对应的前端菜单
     *
     * @param userId
     * @return
     */
    Permission getMenuOfUser(int userId);

    /**
     * 获取系统所有的权限，包括后端API和前端菜单
     *
     * @return
     */
    Permission getAllPermission();

    /**
     * 获取角色对应的前端菜单ID
     *
     * @param roleId
     * @return
     */
    List<Integer> getMenuIdsOfRole(int roleId);

    /**
     * 为角色设置菜单
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    boolean setPermissionForRole(Integer roleId, Integer[] permissionIds);
}
