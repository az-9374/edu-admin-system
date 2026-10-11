package com.az9374.eduadmin.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 密钥（和测试类一致）
    private static final String SECRET_KEY = "Zx7Qm2Lp9Rt4Vy6Wb1Nc5Df0Gh3Jk8As";
    // 过期时间：12小时（单位：毫秒）
    private static final long EXPIRATION_TIME = 12 * 3600 * 1000;

    /**
     * 生成 JWT 令牌
     * @param claims 要存储的数据（Map）
     * @return JWT 字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析 JWT 令牌
     * @param jwt JWT 字符串
     * @return Claims 对象（包含所有存储的数据）
     */
    public static Claims parseJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
