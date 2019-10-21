package com.ray.dormitory.config;

import com.google.gson.Gson;
import com.ray.dormitory.bean.ResponseBean;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.util.JWTUtil;
import com.ray.dormitory.util.bean.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
//JWT过滤器
public class JWTFilter extends BasicHttpAuthenticationFilter {


    @Autowired
    private RedisService redisService;


    /**
     * 判断用户是否已经登陆。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());

        String uri = httpServletRequest.getRequestURI();//获取URI
        String basePath = httpServletRequest.getContextPath();//获取basePath
        if (null != uri && uri.startsWith(basePath)) {
            uri = uri.replaceFirst(basePath, "");
        }
        /**
         *  两步校验
         *  (1) token是否正确
         * （2）单点登陆，从缓存中读取该用户对应的token
         */
        boolean flag = false;

        if (authorization == null) {
            return flag;
        }

        if (JWTUtil.verifyToken(authorization)) {

            String userId = JWTUtil.getUsername(authorization);
//校验登陆的token是否与缓存中的token保持一致
            if (((String) redisService.get(userId)).equalsIgnoreCase(authorization)) {//token需一致
                String sysType = JWTUtil.getSysType(authorization);
                String lastOperateTime = (String) redisService.get(JWTUtil.getAccountTimePrex(sysType, userId));
//                System.out.println("************************:"+sysType+","+lastOperateTime);
                if (JWTUtil.verifyOperateTime(lastOperateTime)) {//最近一次操作时间，不能超过30分钟
                    flag = true;
                    redisService.set(JWTUtil.getAccountTimePrex(sysType, userId), String.valueOf(System.currentTimeMillis()));
                } else {
                    return false;
                }
            }
        }
        return JWTUtil.verifyToken(authorization);
    }

//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//
//        PrintWriter out = null;
//        HttpServletResponse res = (HttpServletResponse) response;
//        try {
//            res.setCharacterEncoding("UTF-8");
//            res.setContentType("application/json");
//            out = response.getWriter();
//            out.println(new Gson().toJson(new ResponseBean(ErrorEnum.ERROR_10002.getErrorCode(),ErrorEnum.ERROR_10002.getErrorMsg(),"")));
//        } catch (Exception e) {
//        } finally {
//            if (null != out) {
//                out.flush();
//                out.close();
//            }
//        }
//        return false;
//    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                return false;
            }

        } else {
            return false;
        }
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        //处理跨域问题，跨域的请求首先会发一个options类型的请求
        if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        boolean isAccess = isAccessAllowed(request, response, "");

        if (isAccess) {
            return true;
        }
        servletResponse.setCharacterEncoding("UTF-8");
        //Subject subject = getSubject(request, response);
        PrintWriter printWriter = servletResponse.getWriter();
        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        servletResponse.setHeader("Vary", "Origin");

        String respStr = new Gson().toJson(new ResponseBean(ErrorEnum.ERROR_301.getErrorCode(), ErrorEnum.ERROR_301.getErrorMsg(), ""));
        printWriter.write(respStr);
        printWriter.flush();
        servletResponse.setHeader("content-Length", respStr.getBytes().length + "");
//        return super.preHandle(request, response);
        return false;
    }

}
