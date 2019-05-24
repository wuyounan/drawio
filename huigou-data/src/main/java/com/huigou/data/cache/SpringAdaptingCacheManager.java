package com.huigou.data.cache;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

/**
 * 缓存管理器
 * 
 * @author xx
 */
public class SpringAdaptingCacheManager extends AbstractTransactionSupportingCacheManager {

    public SpringAdaptingCacheManager() {
    }

    private Collection<? extends Cache> caches;

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

}
