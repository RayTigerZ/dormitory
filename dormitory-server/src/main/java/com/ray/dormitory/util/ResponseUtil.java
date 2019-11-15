package com.ray.dormitory.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    public static String getContent(HttpServletResponse response) {
        return null;

    }

    public static void writeJson(HttpServletResponse response, Object object) {

        try {
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            String jsonStr = new Gson().toJson(object);


            response.setContentType("application/json;charset=UTF-8");
            //servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Vary", "Origin");
            response.setHeader("content-Length", String.valueOf(jsonStr.getBytes().length));
            printWriter.write(jsonStr);
            printWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
