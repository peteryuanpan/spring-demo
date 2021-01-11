package com.peter.xqwlight.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 官方文档：https://github.com/jwtk/jjwt#quickstart<br/><br/>
 *
 * 此类负责JWTToken的生成、解析等过程，可采用的加密算法有许多，比如RS256、HS512，这里先采用简单的HS512
 */
@Component
public class JwtTokenUtil {

    private static String secret;

    private static Long expiration;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    /**
     * JWT 生成 token。一般 subject 是 username
     * @param subject
     * @return
     */
    public static String generateToken(String subject) {
        return generateToken(subject, expiration);
    }

    /**
     * JWT 生成 token。一般 subject 是 username。expirationSeconds 是 过期时间，单位是 s
     * @param subject
     * @param expirationSeconds
     * @return
     */
    public static String generateToken(String subject, long expirationSeconds) {
        return Jwts.builder()
                .setClaims(null)
                .setSubject(subject)
                // 设置token时间，单位是 ms
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 通过 token 解析出 result。一般 result 是 username
     * @param token
     * @return
     */
    public static String pasreToken(String token) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
            subject = claims.getSubject();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return subject;
    }
}
