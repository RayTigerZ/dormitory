package com.ray.dormitory.service;


import com.ray.dormitory.bean.SysLogger;
import com.ray.dormitory.bean.User;
import com.ray.dormitory.mapper.LogMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogService {

    private static Map<String, String> operateType;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private UserMapper userMapper;

    public void addLog(String requestUri, String remoteAddr, String httpMethod, String params, String outBody, long useTime, String browser, String addAccount, String addUsername) {
        SysLogger log = new SysLogger();
        String type = requestUri;
        if (operateType != null && requestUri != null) {
            type = operateType.get(requestUri.replaceAll("/zuul", ""));
        }
        if ("/logout".equals(requestUri)) {
            type = "退出";
        }
        if ("/login/auth".equals(requestUri)) {
            type = "登录";
        }
        if ("/login/getSystemInfo".equals(requestUri)) {
            type = "子系统切换";
        }
        log.setOptype(type);
        log.setRemoteAddr(remoteAddr);
        log.setRequestUri(requestUri);
        log.setHttpMethod(httpMethod);
        log.setParams(params);
        log.setOutBody(outBody);
        log.setUseTime(useTime);
        log.setBrowser(browser);
        log.setAddAccount(addAccount);
        if (addUsername == null) {
            User user = userMapper.getUserByAccount(addAccount);
            if (user != null) {
                addUsername = user.getName();
            }
        }
        log.setAddUsername(addUsername);
        log.setAddTime(DateUtil.getCurrentDate());

        logMapper.insert(log);
    }

    public static Map<String, String> getOperateType() {
        return operateType;
    }

    public static void setOperateType(Map<String, String> operateType) {
        LogService.operateType = operateType;
    }
}