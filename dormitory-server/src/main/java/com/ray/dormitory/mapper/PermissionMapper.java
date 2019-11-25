package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Ray
 * @date : 2019.11.21 13:21
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {


    /**
     * 获取用户对应的
     *
     * @param userId
     * @return
     */
    List<Permission> getMenusByUserId(int userId);


    /**
     * 获取角色对应的前端菜单（权限）ID
     *
     * @param roleId
     * @return
     */
    List<Integer> getMenuIdsByRoleId(int roleId);

    /**
     * 通过用户ID获取该用户具有的API 路径
     *
     * @param userId 用户ID
     * @return
     */
    Set<String> getApiPermissionsOfUser(int userId);

    /**
     * 获取后端系统API的权限
     *
     * @return
     */
    List<Permission> getApiPermissionList();


}
