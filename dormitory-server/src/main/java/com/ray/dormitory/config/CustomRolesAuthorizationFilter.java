package com.ray.dormitory.config;

import com.google.gson.Gson;
import com.ray.dormitory.util.bean.ResponseBean;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {


    @Override
    //判断用户是否有能够访问该url的角色
    // 有则放行允许访问，没有则不能访问
    public boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String authorization = httpServletRequest.getHeader("Authorization");
        //获取要访问URI
        String uri = httpServletRequest.getRequestURI();
        //获取basePath
        String basePath = httpServletRequest.getContextPath();
        if (null != uri && uri.startsWith(basePath)) {
            uri = uri.replaceFirst(basePath, "");
        }

        log.info("request uri:" + uri);

        Map<String, Object> paramMap = new HashMap<>();
        Enumeration paramNames = httpServletRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = httpServletRequest.getParameterValues(paramName);
            if (paramValues.length > 0) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    log.info(paramName + "=" + paramValue);
                    paramMap.put(paramName, paramValue);
                }
            }
        }

        //对于登录、登出的请求，直接放行
        if ("/logout".equals(uri) || uri.startsWith("/login") || "/shiro/updatePermission".equals(uri)) {
            return true;
        }

        //token为空，不放行
        if (authorization == null) {
            return false;
        }

        String roles = JwtUtil.getRolesByToken(authorization);
        Subject subject = getSubject(req, resp);

        //url所需的角色
        String[] rolesArray = (String[]) mappedValue;
//        logger.info("roles:"+roles);
//        if(rolesArray!= null && rolesArray.length > 0){
//            for (String aRolesArray : rolesArray) {
//                logger.info("aRolesArray:"+aRolesArray);
//            }
//        }

        //
        if (rolesArray == null || rolesArray.length == 0) {
            return false;
        }

        // 若当前用户拥有 rolesArray中的任何一个角色，则放行
        for (String aRolesArray : rolesArray) {
            if (roles.contains(aRolesArray)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        //处理跨域问题，跨域的请求首先会发一个options类型的请求
        //options请求直接放行
        if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        //其余类型请求需要鉴权
        boolean isAccess = isAccessAllowed(request, response, mappedValue);
        if (isAccess) {
            //放行
            return true;
        } else {
            //不放行，向浏览器发送json
            servletResponse.setCharacterEncoding("UTF-8");
            //Subject subject = getSubject(request, response);
            PrintWriter printWriter = servletResponse.getWriter();
            servletResponse.setContentType("application/json;charset=UTF-8");
            servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
            servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            servletResponse.setHeader("Vary", "Origin");
            ResponseBean responseBean = new ResponseBean(ErrorEnum.ERROR_201.getErrorCode(), ErrorEnum.ERROR_201.getErrorMsg(), "");
            printWriter.write(new Gson().toJson(responseBean));
            printWriter.flush();
            servletResponse.setHeader("content-Length", String.valueOf(10));
            return false;
        }

    }
}
