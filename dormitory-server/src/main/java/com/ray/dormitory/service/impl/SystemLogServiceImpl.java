package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.SystemLog;
import com.ray.dormitory.mapper.SystemLogMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.service.SystemLogService;
import com.ray.dormitory.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogService {
    private static Map<String, String> operateType;
    @Autowired
    private UserMapper userMapper;

    public static Map<String, String> getOperateType() {
        return operateType;
    }

    public static void setOperateType(Map<String, String> operateType) {
        SystemLogServiceImpl.operateType = operateType;
    }

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

    @Override
    public IPage<SystemLog> getPage(int pageNum, int pageSize, String account, String begin, String end) {
        IPage<SystemLog> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        QueryWrapper queryWrapper = new QueryWrapper<SystemLog>();
        queryWrapper.orderByDesc("create_time");
        if (StringUtils.isNotBlank(account)) {


            queryWrapper.like("account", account);
        }
        if (StringUtils.isNotBlank(begin)) {
            queryWrapper.ge("create_time", begin);
        }
        if (StringUtils.isNotBlank(end)) {
            queryWrapper.le("create_time", end);
        }
        return baseMapper.selectPage(page, queryWrapper);

    }
}
