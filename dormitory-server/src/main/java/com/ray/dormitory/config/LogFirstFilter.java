package com.ray.dormitory.config;


import com.google.gson.Gson;
import com.ray.dormitory.service.LogService;
import com.ray.dormitory.util.JWTUtil;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 增加的这个Filter，用于记录请求的内容及时间
 * 原来的
 */

public class LogFirstFilter extends AdviceFilter {

    @Autowired
    private LogService logService;

    @Override
    protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        System.out.println("LogFirstFilter is into preHandle?");
        servletRequest.setAttribute("startTime", System.currentTimeMillis());

        return true;
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String httpMethod = ((HttpServletRequest) request).getMethod();

        //http预请求，不进行处理
        if ("OPTIONS".equals(httpMethod)) {
            return;
        }

        String requestUri = httpServletRequest.getRequestURI();//获取URI
        String basePath = httpServletRequest.getContextPath();//获取basePath
        if (null != requestUri && requestUri.startsWith(basePath)) {
            requestUri = requestUri.replaceFirst(basePath, "");
        }
//        System.out.println("LogFirstFilter postHandle request uri:"+requestUri);
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long useTime = endTime - startTime; // log it

        String remoteAddr = request.getRemoteAddr();//返回发出请求的IP地址

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


        String addUsername = null;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null) {
            if (addAccount == null)//登录时，已经赋值了
            {
                addAccount = JWTUtil.getUsername(authorization);
            }
            addUsername = JWTUtil.getTruename(authorization);
        }
        logService.addLog(requestUri, remoteAddr, httpMethod, params, null, useTime, browser, addAccount, addUsername);
    }
}
