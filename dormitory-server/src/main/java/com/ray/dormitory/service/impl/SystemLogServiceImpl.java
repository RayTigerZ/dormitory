package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.SystemLog;
import com.ray.dormitory.mapper.SystemLogMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.SystemLogService;
import com.ray.dormitory.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {
    @Autowired
    private UserMapper userMapper;
    private static Map<String, String> operateType;

    @Override
    public void addLog(String requestUri, String remoteAddr, String httpMethod, String params, String outBody, long useTime, String browser, String account) {
        SystemLog log = new SystemLog();
        String type = requestUri;
        if (operateType != null && requestUri != null) {
            type = operateType.get(requestUri.replaceAll("/zuul", ""));
        }
        if ("/user/logout".equals(requestUri)) {
            type = "退出";
        }
        if ("/user/login".equals(requestUri)) {
            type = "登录";
        }

        log.setOptype(type);
        log.setRemoteAddr(remoteAddr);
        log.setRequestUri(requestUri);
        log.setHttpMethod(httpMethod);
        log.setParams(params);
        log.setOutBody(outBody);
        log.setUseTime(useTime);
        log.setBrowser(browser);
        log.setAccount(account);
        log.setCreateTime(DateUtil.getCurrentDate());

        baseMapper.insert(log);
    }

    public static Map<String, String> getOperateType() {
        return operateType;
    }

    public static void setOperateType(Map<String, String> operateType) {
        SystemLogServiceImpl.operateType = operateType;
    }
}
