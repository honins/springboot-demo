package com.hy.demo.common.util;

import com.hy.demo.component.exception.CustomUnauthorizedException;
import com.hy.demo.data.dto.SysUserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 *
 * @author macro
 * @date 2018/4/26
 */
@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_ACCOUNT = "account";
    public static final String CLAIM_KEY_CREATED = "created";

    public static Logger getLOGGER() {
        return LOGGER;
    }

    private static String secret;
    private static Long expiration;

    @Value("${jwt.secret}")
    public void setSecret(String secret){
        JwtUtil.secret = secret;
    }
    @Value("${jwt.expiration}")
    public void setExpiration(Long expiration){
        JwtUtil.expiration = expiration;
    }

    public static String getSecret() {
        return secret;
    }

    public static Long getExpiration() {
        return expiration;
    }

    /**
     * 根据claims 负责生成JWT的token
     */
    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}",token);
            throw new CustomUnauthorizedException("JWT格式验证失败:"+token);
        }
        return claims;
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/9/7 16:54
     */
    public static String getClaim(String token, String claim) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(claim, String.class);
    }

    /**
     * 生成token的过期时间
     */
    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public static String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param user 从数据库中查询出来的用户信息
     */
    public static boolean validateToken(String token, SysUserDTO user) {
        String username = getUserNameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param account      账户
     */
    public static boolean validateToken(String token, String account) {
        String jwtAccount = getClaim(token, account);
        return account.equals(jwtAccount);
    }

    /**
     * 判断token是否已经失效
     */
    private static boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private static Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public static String generateToken(Integer uid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ACCOUNT, String.valueOf(uid));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public static String createToken(Integer uid, String currentTimeMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ACCOUNT, String.valueOf(uid));
        claims.put(CLAIM_KEY_CREATED, currentTimeMillis);
        return generateToken(claims);
    }


    /**
     * 判断token是否可以被刷新
     */
    public static boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public static String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

}

