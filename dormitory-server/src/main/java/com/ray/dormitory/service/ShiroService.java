package com.ray.dormitory.service;


import com.ray.dormitory.bean.po.Permission;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.mapper.PermissionMapper;
import com.ray.dormitory.service.impl.SystemLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ray
 * @date 2019/11/23 18:54
 */
@Slf4j
@Service
public class ShiroService {

    @Autowired
    private PermissionMapper permissionMapper;

    //获取所有权限对应的角色，用于shiro动态权限加载
    public Map<String, String> getApiPermissionMap() {
        Map<String, String> apiPermissionMap = new LinkedHashMap<>();

        // 所有请求通过我们自己的JWT Filter
        apiPermissionMap.put("/user/login", "anon");
        apiPermissionMap.put("/logout", "anon");
        apiPermissionMap.put("/test", "jwt");
        apiPermissionMap.put("/token", "anon");
        apiPermissionMap.put("/shiro/updatePermission", "anon");
        //门户获取系统数据
        apiPermissionMap.put("/portal/portal/**", "anon");
        //门户登录
        apiPermissionMap.put("/mhLogin/auth/**", "anon");
        // 访问401和404页面不通过我们的Filter
        apiPermissionMap.put("/401", "anon");
        apiPermissionMap.put("/500", "anon");
        //门户数据上报
        apiPermissionMap.put("/portal/mhDataUpload/**", "jwt");
        apiPermissionMap.put("/admin/**", "jwt");

        apiPermissionMap.put("/getUser", "jwt");

        List<Permission> permissionList = permissionMapper.getApiPermissionList();
        log.info("{}", permissionList);
        Map<String, String> operateType = new HashMap<>();
        for (Permission permission : permissionList) {
            String format = "roles[%s]";
            StringBuffer stringBuffer = new StringBuffer("");
            int index = 0;
            List<Role> roles = permission.getRoles();
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    if (index > 0) {
                        stringBuffer.append(",").append(role.getName());
                    }
                    index++;
                }
            }

            operateType.put(permission.getPath(), String.format(format, stringBuffer.toString()));
        }
        SystemLogServiceImpl.setOperateType(operateType);

        log.info(apiPermissionMap.toString());
        return apiPermissionMap;
    }

    //更新权限信息
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空旧权限控制
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(getApiPermissionMap());
            // 重新构建生成新权限
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

            System.out.println("更新权限成功！！");
        }
    }
}