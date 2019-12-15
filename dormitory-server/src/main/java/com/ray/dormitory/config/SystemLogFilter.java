package com.ray.dormitory.config;


import com.google.gson.Gson;
import com.ray.dormitory.service.SystemLogService;
import com.ray.dormitory.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Aop Filter，用于记录请求的内容及时间等
 * 会在 请求开始前 和 结束后 进行操作
 *
 * @author Ray
 * @date 2019/11/23 15:15
 */

@Slf4j
public class SystemLogFilter extends AdviceFilter {

    private final static String OPTIONS = "OPTIONS";
    @Autowired
    private SystemLogService systemLogService;

    public SystemLogFilter(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    /**
     * 前置处理
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) {
        servletRequest.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    /**
     * 后置处理
     *
     * @param request
     * @param response
     */
    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String httpMethod = httpServletRequest.getMethod();

        //http预请求，不进行处理
        if (OPTIONS.equals(httpMethod)) {
            return;
        }

        //获取URI
        String requestUri = httpServletRequest.getRequestURI();
        if (requestUri.startsWith("/druid")) {
            return;
        }

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long useTime = endTime - startTime;

        //返回发出请求的IP地址
        String remoteAddr = request.getRemoteAddr();

        Map<String, Object> paramMap = new HashMap<>();
        Enumeration paramNames = httpServletRequest.getParameterNames();
        String addAccount = null;
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = httpServletRequest.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    paramMap.put(paramName, paramValue);
                    if ("username".equals(paramName) && "/login/auth".equals(requestUri)) {
                        addAccount = paramValue;
                    }
                }
            }
        }
        String params = new Gson().toJson(paramMap);

        String browser = ((HttpServletRequest) request).getHeader("User-Agent");


        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null) {
            //登录时，已经赋值了
            if (addAccount == null) {
                addAccount = JwtUtil.getAccount(authorization);
            }

        }
        systemLogService.addLog(requestUri, remoteAddr, httpMethod, params, null, useTime, browser, addAccount);
    }
}
