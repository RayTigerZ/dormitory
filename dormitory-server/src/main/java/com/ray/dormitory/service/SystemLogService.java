package com.ray.dormitory.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.SystemLog;


public interface SystemLogService extends IService<SystemLog> {

    void addLog(String requestUri, String remoteAddr, String httpMethod, String params, String outBody, long useTime, String browser, String account);

    IPage<SystemLog> getPage(int pageNum, int pageSize, String account, String begin, String end);
}