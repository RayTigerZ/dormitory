package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Permission;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.service.PermissionService;
import com.ray.dormitory.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ray
 */
@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 分页角色列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperation(value = "分页角色", notes = "增加角色时参数id为空,更新角色必须传id")
    @GetMapping("")
    public IPage<Role> getRoles(@RequestParam(defaultValue = "1") Long pageSize, @RequestParam(defaultValue = "10") Long pageNum) {
        IPage<Role> rolePage = new Page<>(pageNum, pageSize);
        return roleService.page(rolePage);

    }


    @ApiOperation(value = "增加/更新角色", notes = "增加角色时参数id为空,更新角色必须传id")
    @PostMapping("")
    public boolean addOrUpdate(@Validated Role role) {
        log.info("Add or Update {}", role);
        return roleService.saveOrUpdate(role);

    }

    @ApiOperation(value = "删除角色", notes = "通过id删除角色")
    @DeleteMapping("/{rid}")
    public boolean delete(@PathVariable Integer rid) {

        return roleService.removeById(rid);

    }


    @ApiOperation(value = "获取角色对应的菜单权限")
    @GetMapping("/{rid}/permissions")
    public Map<String, Object> getRolePermissions(@PathVariable Integer rid) {
        List<Integer> ids = permissionService.getMenuIdsOfRole(rid);
        Permission menus = permissionService.getAllPermission();
        Map<String, Object> map = new HashMap<>(2);
        map.put("ids", ids);
        map.put("menus", menus);
        return map;
    }


    @ApiOperation(value = "更新角色对应的菜单权限")
    @PostMapping("/{rid}/permissions")
    public boolean updateRolePermissions(@PathVariable Integer rid, Integer[] ids) {

        return permissionService.setPermissionForRole(rid, ids);

    }

}
