package com.tmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * guava本地缓存
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";

   //LRU算法//1000设置缓存初始化容量
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
           .initialCapacity(1000)
           .maximumSize(10000)
           .expireAfterAccess(12, TimeUnit.HOURS)
           .build(new CacheLoader<String, String>() {
               @Override
               public String load(String s) throws Exception {
                   return "null";//为了防止对比forgetToken时，出现空指异常，改成字符串的“null”
               }
           });

    public static void setKey(String key, String value) {
        localCache.put(key,value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
                return value;
        } catch (Exception e) {
            logger.error("localCache Get Error:",e);
        }
        return null;
    }
}
