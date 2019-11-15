package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.PermissionRole;
import com.ray.dormitory.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    /**
     * 通过用户ID获取该用户具有的权限
     *
     * @param account
     * @return
     */
    Set<String> getUserPermissions(String account);

    /**
     * 获取所有权限及对
     *
     * @return
     */
    List<PermissionRole> getAllRolesByPermission();


}
