package com.ray.dormitory.util.bean;

import org.springframework.util.DigestUtils;

/**
 * @author Ray Z
 * @date 2019.10.27 20:37
 */
public class MD5Util {

    public static String getMD5(String str) {

        return DigestUtils.md5DigestAsHex(str.getBytes());

    }

    public static void main(String[] args) {
        String password = getMD5("admin123");
        System.out.println("password: " + password);


        System.out.println("password2: " + getMD5(password + "123456"));
    }
}
