package com.ray.dormitory.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.SystemLog;


public interface SystemLogService extends IService<SystemLog> {

    void addLog(String requestUri, String remoteAddr, String httpMethod, String params, String outBody, long useTime, String browser, String account);

}