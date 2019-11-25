package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Permission;
import com.ray.dormitory.bean.po.RolePermission;
import com.ray.dormitory.mapper.PermissionMapper;
import com.ray.dormitory.mapper.RolePermissionMapper;
import com.ray.dormitory.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author : Ray
 * @date : 2019.11.21 12:46
 */
@Repository
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public Permission getMenuOfUser(int userId) {
        List<Permission> permissions = baseMapper.getMenusByUserId(userId);
        Permission permission = formatMenu(permissions);

        return permission;
    }

    private Permission formatMenu(List<Permission> permissions) {
        Map<String, Permission> menuMap = new HashMap<>();
        for (Permission permission : permissions) {
            menuMap.put(String.valueOf(permission.getId()), permission);
        }

        Iterator<Map.Entry<String, Permission>> it = menuMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Permission> entry = it.next();
            Integer parentId = entry.getValue().getParentId();
            if (parentId != null) {
                menuMap.get(String.valueOf(parentId)).addChild(entry.getValue());
            }
        }

        Permission permission = menuMap.get("1");
        //除去无子树的节点，叶子节点除外

        return permission;
    }

    void deleteNoChildNode(Permission permission) {
        if (permission.getChildren() == null && permission.getType() < 3) {
            permission = null;
        }
        for (Permission permission1 : permission.getChildren()) {
            deleteNoChildNode(permission1);
        }
    }

    @Override
    public Permission getAllPermission() {
        List<Permission> permissions = baseMapper.selectList(null);
        return formatMenu(permissions);
    }

    @Override
    public List<Integer> getMenuIdsOfRole(int roleId) {

        return baseMapper.getMenuIdsByRoleId(roleId);
    }

    @Override
    public boolean setPermissionForRole(int roleId, int[] permissionIds) {
        rolePermissionMapper.delete(new UpdateWrapper<RolePermission>().ge("role_id", roleId));

        for (int permissionId : permissionIds) {
            rolePermissionMapper.insert(new RolePermission(null, roleId, permissionId));
        }

        return true;
    }


}
