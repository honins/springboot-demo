package com.hy.springbootdemo.util;

import com.hy.springbootdemo.component.constant.RedisConstant;
import org.apache.shiro.authz.AuthorizationInfo;
import org.springframework.stereotype.Component;

/**
 * Created by hy on 2019/9/17 12:45
 * @author hy
 */
@Component
public class JwtRedisUtil {

    public static Object getAuthorizationInfoCache(String account) {
        return JedisUtil.getObject(RedisConstant.PREFIX_SHIRO_CACHE_PERMISSIONS + account);
    }

    public static void cacheAuthorizationInfo(String account, AuthorizationInfo authorizationInfo) {
        JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_CACHE_PERMISSIONS + account, authorizationInfo);
    }

    public static Object getAuthorizationInfoCacheByJwt(String token) {
        String account = JwtUtil.getClaim(token, RedisConstant.ACCOUNT);
        return JedisUtil.getObject(RedisConstant.PREFIX_SHIRO_CACHE_PERMISSIONS + account);
    }

    public static void cacheAuthorizationInfoByJwt(String token, AuthorizationInfo authorizationInfo) {
        String account = JwtUtil.getClaim(token, RedisConstant.ACCOUNT);
        JedisUtil.setObject(RedisConstant.PREFIX_SHIRO_CACHE_PERMISSIONS + account, authorizationInfo);
    }


}
