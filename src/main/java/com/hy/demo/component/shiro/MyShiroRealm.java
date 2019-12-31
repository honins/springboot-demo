package com.hy.demo.component.shiro;


import com.hy.demo.component.constant.RedisConstant;
import com.hy.demo.pojo.dto.JwtToken;
import com.hy.demo.pojo.dto.SysUserDTO;
import com.hy.demo.service.SysRoleService;
import com.hy.demo.service.UserService;
import com.hy.demo.util.JedisUtil;
import com.hy.demo.util.JwtRedisUtil;
import com.hy.demo.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    public boolean isAuthorizationCachingEnabled() {
        return false;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        logger.info("---------------- 执行 Shiro 凭证认证 ----------------------");
        String jwtToken = String.valueOf(authcToken.getPrincipal());
        String account = JwtUtil.getClaim(jwtToken, JwtUtil.CLAIM_KEY_ACCOUNT);
        // 从数据库获取对应用户
        SysUserDTO user = userService.getUser(Integer.valueOf(account));
        if (user == null) {
            throw new AuthenticationException("token验证失败");
        }
        // 用户为禁用状态
        if (user.getStatus() != 1) {
            throw new DisabledAccountException();
        }
        logger.info("---------------- Shiro 凭证认证成功 ----------------------");
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JedisUtil.exists(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = JedisUtil.getObject(RedisConstant.PREFIX_SHIRO_CACHE_ACCOUNT + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(jwtToken, JwtUtil.CLAIM_KEY_CREATED).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(jwtToken, jwtToken, getName());
            } else {
                throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
            }
        } else {
            return new SimpleAuthenticationInfo(jwtToken, jwtToken, getName());
        }
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //取redis中的角色权限信息
        SimpleAuthorizationInfo cacheSimpleAuthorizationInfo = (SimpleAuthorizationInfo) JwtRedisUtil.getAuthorizationInfoCacheByJwt(principals.toString());
        if (cacheSimpleAuthorizationInfo != null) {
            return cacheSimpleAuthorizationInfo;
        } else {
            //从数据库中取角色权限信息
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            String account = JwtUtil.getClaim(principals.toString(), RedisConstant.ACCOUNT);
            Set<String> roles = roleService.listRoleNameByUserId(Integer.valueOf(account));
            simpleAuthorizationInfo.addRoles(roles);
            Set<String> permissions = roleService.listPermissionByUserId(Integer.valueOf(account));
            simpleAuthorizationInfo.addStringPermissions(permissions);
            logger.info("---- 获取到以下权限 ----");
            logger.info(simpleAuthorizationInfo.getStringPermissions().toString());
            JwtRedisUtil.cacheAuthorizationInfoByJwt(principals.toString(), simpleAuthorizationInfo);
            return simpleAuthorizationInfo;
        }
    }

    /**
     * 重写方法,清除当前用户的的 授权缓存
     *
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     *
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}