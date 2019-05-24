package com.huigou.cache.service;

import java.io.Serializable;

/**
 * 系统缓存对象使用接口
 * 
 * @author xx
 */
public interface ICache {
    void put(String cacheKey, Serializable obj);

    Object get(String cacheKey);

    <T> T get(String cacheKey, Class<T> cls);

    void remove(String key, String separator);

    void removeAll();

    void delete(String key);
}
