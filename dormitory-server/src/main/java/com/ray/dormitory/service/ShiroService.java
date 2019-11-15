package com.ray.dormitory.service;

import com.ray.dormitory.bean.PermissionRole;
import com.ray.dormitory.mapper.UserMapper;
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

@Slf4j
@Service
public class ShiroService {

    @Autowired
    private UserMapper userMapper;

    //获取所有权限对应的角色，用于shiro动态权限加载
    public Map<String, String> getAllRolesByPermission() {
        Map<String, String> filterRuleMap = new LinkedHashMap<>();

        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/user/login", "anon");
        filterRuleMap.put("/logout", "anon");
        filterRuleMap.put("/test", "jwt");
        filterRuleMap.put("/token", "anon");
        filterRuleMap.put("/shiro/updatePermission", "anon");
        filterRuleMap.put("/portal/portal/**", "anon");//门户获取系统数据
        filterRuleMap.put("/mhLogin/auth/**", "anon");//门户登录
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/500", "anon");

        filterRuleMap.put("/portal/mhDataUpload/**", "jwt");//门户数据上报
        filterRuleMap.put("/admin/**", "jwt");

        filterRuleMap.put("/getUser", "jwt");

        List<PermissionRole> roles = userMapper.getAllRolesByPermission();
        Map<String, String> operateType = new HashMap<>();
        for (PermissionRole role : roles) {
            if (role.getName() != null) {
//                System.out.println(role.getPermissionUrl() + ":" + role.getRoleName());
//                filterRuleMap.put(role.getPermissionUrl(), role.getRoleName());
            }

            //对于所有的子系统请求，加上这个可以判断是否登录
//            if (StringUtils.isEmpty(role.getSysType())) {
//                filterRuleMap.put(role.getPermissionUrl() + "/**", "jwt");
//            }
            if (role.getType() == 2) {
                operateType.put(role.getUrl(), role.getName());
            }
        }
        SystemLogServiceImpl.setOperateType(operateType);

        //filterRuleMap.put("/**", "roles[]");//没有配置的请求，全部转到这个上面
//        System.out.println(JSON.toJSONString(filterRuleMap));
        log.info(filterRuleMap.toString());
        return filterRuleMap;
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
            shiroFilterFactoryBean.setFilterChainDefinitionMap(getAllRolesByPermission());
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