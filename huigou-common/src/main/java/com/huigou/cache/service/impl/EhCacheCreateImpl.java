package com.huigou.cache.service.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 通过 ehcache cacheManager 创建
 * 
 * @author xiex
 */
public class EhCacheCreateImpl extends EhCacheImpl implements InitializingBean, BeanNameAware {
    private CacheManager cacheManager;

    private String beanName;

    private String name;

    private Long timeToIdleSeconds;

    private Long timeToLiveSeconds;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTimeToIdleSeconds(Long timeToIdleSeconds) {
        this.timeToIdleSeconds = timeToIdleSeconds;
    }

    public void setTimeToLiveSeconds(Long timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cacheName = getName();
        if (cacheName == null) {
            cacheName = beanName;
            setName(cacheName);
        }
        if (cacheManager == null) {
            cacheManager = CacheManager.getInstance();
        }
        synchronized (cacheManager) {
            boolean cacheExists = cacheManager.cacheExists(cacheName);
            Cache rawCache;
            if (cacheExists) {
                rawCache = cacheManager.getCache(cacheName);
            } else {
                rawCache = new Cache(cacheManager.getConfiguration().getDefaultCacheConfiguration());
            }
            if (!cacheExists) {
                rawCache.getCacheConfiguration().setName(cacheName);
                cacheManager.addCache(rawCache);
            }
            if (timeToIdleSeconds != null) {
                rawCache.getCacheConfiguration().setTimeToIdleSeconds(timeToIdleSeconds);
            }
            if (timeToLiveSeconds != null) {
                rawCache.getCacheConfiguration().setTimeToLiveSeconds(timeToLiveSeconds);
            }
            this.setEhCache(rawCache);
        }
    }

}
