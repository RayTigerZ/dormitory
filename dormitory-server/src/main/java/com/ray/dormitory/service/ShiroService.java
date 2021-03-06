package com.ray.dormitory.service;


import com.ray.dormitory.bean.po.Operation;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.mapper.OperationMapper;
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
    @Autowired
    private OperationMapper operationMapper;

    /**
     * 获取所有后端API对应的角色name，用于shiro动态权限加载
     *
     * @return
     */

    public Map<String, String> getApiPermissionMap() {
        Map<String, String> apiPermissionMap = new LinkedHashMap<>();

        // 所有请求通过我们自己的JWT Filter
        apiPermissionMap.put("/user/login", "anon");
        apiPermissionMap.put("/user/logout", "jwt");

        apiPermissionMap.put("/shiro/updatePermission", "anon");
        //门户获取系统数据
        apiPermissionMap.put("/portal/portal/**", "anon");
        //门户登录
        apiPermissionMap.put("/mhLogin/auth/**", "anon");
        // 访问401和404页面不通过我们的Filter
        apiPermissionMap.put("/401", "anon");
        apiPermissionMap.put("/500", "anon");


        List<Operation> operations = operationMapper.getApiPermission();


        log.info("apiPermission: {}", operations);
        Map<String, String> operateType = new HashMap<>(16);
        String format = "roles[%s]";
        for (Operation operation : operations) {

            StringBuffer stringBuffer = new StringBuffer("");
            int index = 0;
            List<Role> roles = operation.getRoleList();
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    if (index > 0) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(role.getName());
                    index++;
                }
            }

            operateType.put(operation.getUri() + "::" + operation.getMethod(), operation.getOpName());
            //需要角色认证的uri也需要jwt认证
            apiPermissionMap.put(operation.getUri() + "::" + operation.getMethod(), "jwt," + String.format(format, stringBuffer.toString()));
        }
        SystemLogServiceImpl.setOperateType(operateType);

        log.info("{}", apiPermissionMap);
        return apiPermissionMap;
    }

    /**
     * 更新权限信息
     *
     * @param shiroFilterFactoryBean
     */

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

            log.info("更新权限成功！！");
        }
    }
}