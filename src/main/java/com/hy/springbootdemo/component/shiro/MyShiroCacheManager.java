package com.hy.springbootdemo.component.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

/**
 * 重写Shiro缓存管理器
 * @author dolyw.com
 * @date 2018/9/4 17:41
 */
public class MyShiroCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new MyShiroCache<K,V>();
    }
}
