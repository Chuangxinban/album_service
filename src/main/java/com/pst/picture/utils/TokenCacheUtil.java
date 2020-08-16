package com.pst.picture.utils;


import org.ehcache.Cache;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 使用EhCache作为缓存实现，
 * Cache底层使用CurrentHashMap
 * @author RETURN
 * @date 2020/8/14 14:45
 */
@Component
public class TokenCacheUtil {

    private static final String TOKEN_PREFIX = "token_user_";
    private static Cache<String, String> cache;

    @Resource(name = "tokenCache")
    public void setCache(Cache<String, String> cache){
        TokenCacheUtil.cache = cache;
    }

    public static void put(@NonNull String key,@NonNull String value){
        cache.put(TOKEN_PREFIX + key,value);
    }

    public static String putIfAbsent(@NonNull String key,@NonNull String value){
        return cache.putIfAbsent(TOKEN_PREFIX + key,value);
    }

    public static String get(@NonNull String key){
       return cache.get(TOKEN_PREFIX + key);
    }

    public static void delete(@NonNull String key){
        cache.remove(key);
    }

    @PreDestroy
    private void destroy(){
        cache.clear();
    }
}
