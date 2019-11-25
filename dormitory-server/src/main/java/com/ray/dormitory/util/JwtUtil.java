package com.ray.dormitory.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 过期时间30分钟
     */

    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    /**
     * redis缓存中帐号登陆 时间值的key前缀
     */
    private static final String SESSION_TIME_PREFIX = "TIME_";

    /**
     * redis缓存中帐号登陆 账户值的key前缀
     */
    private static final String SESSION_USER_PREFIX = "USER_";

    /**
     * 设置私钥
     */
    private static final String SECRET = "ray_dormitory";

    public static String getSecret() {
        return SECRET;
    }

    public static String getAccountTimeKey(String account) {
        return SESSION_TIME_PREFIX + account;
    }

    public static String getAccountUserKey(String account) {
        return SESSION_TIME_PREFIX + account;
    }


    /**
     * 操作时间不能超过30分钟
     *
     * @param lastOperateTimeStr
     * @return
     */
    public static boolean verifyOperateTime(String lastOperateTimeStr) {
        if (lastOperateTimeStr == null || "".equals(lastOperateTimeStr)) {
            return false;
        }
        try {
            long lastOperateTime = Long.parseLong(lastOperateTimeStr);
            long now = System.currentTimeMillis();
            //如果间隔不超过30分钟，返回TRUE，不需要重新登录
            return now - lastOperateTime < EXPIRE_TIME;

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username) {

        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("roles", "SELECT,DROP,DELETE")
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * 校验token是否有效
     *
     * @param token
     * @return
     */
    public static boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getSysType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("sysType").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static int getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            return 0;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的部门ID
     */
    public static Long getOrgID(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("organizeId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的地区ID
     */
    public static Long getAreaId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("areaId").asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getTruename(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("trueName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 通过Token解析出roles
     *
     * @param token
     * @return
     */
    public static String getRolesByToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roles").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过Token解析出roleIds
     *
     * @param token
     * @return
     */
    public static String getRoleIdsByToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roleIds").asString();
        } catch (JWTDecodeException e) {
            e.printStackTrace();
            return null;
        }
    }


    //测试
    public static void main(String[] args) {
        String str = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0cnVlTmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsIm9yZ2FuaXplSWQiOjEsInJvbGVJZHMiOiIxLDIsNiwyNCIsImFyZWFJZCI6MSwic3lzVHlwZSI6InRqIiwicm9sZXMiOiLnrqHnkIblkZjop5LoibIs5pmu6YCa55So5oi357uf5LiA6KeS6ImyLEFwcOinkuiJsizlhbHkuqvnm67lvZXop5LoibIiLCJ1c2VybmFtZSI6ImFkbWluIn0.FMWpMb5F_9X-0bMFmHxfo1hLdn44b_usIr3D-_EGN-U";
        System.out.println(JwtUtil.getRolesByToken(str));
    }
}