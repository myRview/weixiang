package com.hk.utils;

import com.hk.constants.BaseConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Map;

/**
 * @author 20231
 */
public class JwtUtil {
    private static final SecretKey SIGNING_KEY = Jwts.SIG.HS256.key().build();

    /**
     * 生成令牌
     *
     * @param claims
     * @return
     */
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder().claims(claims)  // 设置所有声明
                .signWith(SIGNING_KEY)  // 使用密钥签名
                .compact();
    }

    /**
     * 解析令牌
     *
     * @return
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().verifyWith(SIGNING_KEY)  // 设置验证密钥
                .build().parseSignedClaims(token)  // 解析签名令牌
                .getPayload();  // 获取声明内容
    }

    /**
     * 获取用户ID
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return String.valueOf(claims.get(BaseConstant.USER_ID));
    }
}
