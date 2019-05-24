package com.huigou.cache.service.impl;

import java.io.Serializable;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import com.huigou.cache.service.ICache;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * ehcache 实现
 * 
 * @author xiex
 */
public class EhCacheImpl implements ICache {
    private Cache ehCache;

    public void setEhCache(Cache ehCache) {
        this.ehCache = ehCache;
    }

    @Override
    public void put(String cacheKey, Serializable obj) {
        Element element = new Element(cacheKey, obj);
        ehCache.put(element);
    }

    @Override
    public Object get(String cacheKey) {
        if (ehCache == null) return null;
        Element element = ehCache.get(cacheKey);
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    @Override
    public <T> T get(String cacheKey, Class<T> cls) {
        if (ehCache == null) return null;
        Element element = ehCache.get(cacheKey);
        if (element != null) {
            return ClassHelper.convert(element.getObjectValue(), cls);
        }
        return null;
    }

    @Override
    public void remove(String kind, String separator) {
        String separatorkey = kind;
        if (StringUtil.isNotBlank(separator)) {
            separatorkey += separator;
        }
        List<?> l = ehCache.getKeys();
        for (Object o : l) {
            String key = ClassHelper.convert(o, String.class);
            if (!StringUtil.isBlank(key) && key.startsWith(separatorkey)) {
                ehCache.remove(o);
            }
        }
    }

    @Override
    public void removeAll() {
        ehCache.removeAll();
    }

    @Override
    public void delete(String key) {
        ehCache.remove(key);
    }
}
