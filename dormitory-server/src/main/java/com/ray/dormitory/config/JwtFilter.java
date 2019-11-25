package com.ray.dormitory.config;

import com.google.gson.Gson;
import com.ray.dormitory.util.bean.ResponseBean;
import com.ray.dormitory.service.RedisService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author Ray Z
 */
@Slf4j
//JWT过滤器
public class JwtFilter extends BasicHttpAuthenticationFilter {


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

        //BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        //获取URI
        String uri = httpServletRequest.getRequestURI();
        //获取basePath
        String basePath = httpServletRequest.getContextPath();
        if (null != uri && uri.startsWith(basePath)) {
            uri = uri.replaceFirst(basePath, "");
        }
        /*
         *  两步校验
         *  (1) token是否正确
         * （2）单点登陆，从缓存中读取该用户对应的token
         */
        boolean result = false;


        if (authorization != null && JwtUtil.verifyToken(authorization)) {

            String account = JwtUtil.getAccount(authorization);
            //校验登陆的token是否与缓存中的token保持一致
            if (((String) redisService.get(JwtUtil.getAccountUserKey(account))).equalsIgnoreCase(authorization)) {

                String lastOperateTime = (String) redisService.get(JwtUtil.getAccountTimeKey(account));
                //最近一次操作时间，不能超过30分钟
                if (JwtUtil.verifyOperateTime(lastOperateTime)) {

                    redisService.set(JwtUtil.getAccountTimeKey(account), String.valueOf(System.currentTimeMillis()));
                    result = true;
                }
            }
        }
        return result;
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
        boolean result = false;
        if (isLoginAttempt(request, response)) {
            try {
                result = executeLogin(request, response);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
        return result;

    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        String url = servletRequest.getServletPath();
        log.info("JwtFilter preHandle url: {}", url);

        //处理跨域问题，跨域的请求首先会发一个options类型的请求
        if (servletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }


        boolean isAccess = isAccessAllowed(request, response, "");
        if (isAccess) {
            return true;
        }

        //Subject subject = getSubject(request, response);
        ResponseBean responseBean = new ResponseBean(ErrorEnum.ERROR_301.getErrorCode(), ErrorEnum.ERROR_301.getErrorMsg(), "");
        //ResponseUtil.writeJson(servletResponse, responseBean);

        servletResponse.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = servletResponse.getWriter();


        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        servletResponse.setHeader("Vary", "Origin");

        String respStr = new Gson().toJson(responseBean);

        printWriter.write(respStr);
        printWriter.flush();
        servletResponse.setHeader("content-Length", String.valueOf(respStr.getBytes().length));

        return false;
    }

}
