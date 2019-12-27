package com.huigou.uasp.bmp.intercept;

import java.io.IOException;
import java.io.Serializable;

import com.huigou.exception.ApplicationException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

import com.huigou.cache.service.ICache;
import com.huigou.util.ConfigFileVersion;
import com.huigou.util.ResourceLoadManager;

/**
 * EhCache 缓存拦截器
 *
 * @author xx
 */
public class EhCacheInterceptor implements MethodInterceptor, InitializingBean {

    private ICache icache;

    public void setIcache(ICache icache) {
        this.icache = icache;
    }

    public void afterPropertiesSet() throws Exception {

    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object[] arguments = invocation.getArguments();
        Object result = null;

        String cacheKey = getCacheKey(targetName, methodName, arguments);
        Object element = null;
        synchronized (this) {
            element = icache.get(cacheKey);
            if (element == null) {
                result = invocation.proceed();
                icache.put(cacheKey, (Serializable) result);
            } else {
                ConfigFileVersion versions = (ConfigFileVersion) element;
                long lastModified = ResourceLoadManager.maxLastModified(versions.getFilePaths());
                if (lastModified > versions.getVersion()) {
                    result = invocation.proceed();
                    icache.put(cacheKey, (Serializable) result);
                }
            }
        }
        return result == null ? element : result;
    }

    /**
     * 返回具体的方法全路径名称 参数
     *
     * @param targetName 全路径
     * @param methodName 方法名称
     * @param arguments  参数
     * @return 完整方法名称
     */
    private String getCacheKey(String targetName, String methodName, Object[] arguments) {
        StringBuffer sb = new StringBuffer();
        sb.append(targetName).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }
}
