package com.huigou.data.cache;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.huigou.cache.service.ICache;
import com.huigou.util.LogHome;
import com.huigou.util.Md5Builder;

/**
 * Spring 缓存适配系统已有缓存体系
 *
 * @author xx
 */
public class SpringAdaptingCache implements Cache {

    private String name;

    private ICache iCache;

    public void setName(String name) {
        this.name = name;
    }

    public void setiCache(ICache iCache) {
        this.iCache = iCache;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ICache getNativeCache() {
        return iCache;
    }

    private String getCacheKey(Object key) {
        return Md5Builder.getMd5(this.getName() + key.toString());
    }

    @Override
    public ValueWrapper get(Object key) {
        String cacheKey = this.getCacheKey(key);
        ValueWrapper result = null;
        Object thevalue = iCache.get(cacheKey);
        if (thevalue != null) {
            result = new SimpleValueWrapper(thevalue);
        }
        return result;

    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        String cacheKey = this.getCacheKey(key);
        return iCache.get(cacheKey, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void put(Object key, Object value) {
        String cacheKey = this.getCacheKey(key);
        if (value != null && Serializable.class.isInstance(value)) {
            try {
                Serializable val = (Serializable) value;// value必须是Serializable类型
                iCache.put(cacheKey, val);
            } catch (Exception e) {
                LogHome.getLog(this).error(e);
            }
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper result = this.get(key);
        if (result == null) {
            result = new SimpleValueWrapper(value);
            this.put(key, value);
        }
        return result;
    }

    @Override
    public void evict(Object key) {
        String cacheKey = this.getCacheKey(key);
        iCache.delete(cacheKey);
    }

    @Override
    public void clear() {
        iCache.removeAll();
    }

}
