package com.huigou.util;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import com.huigou.exception.NotFoundException;
import com.huigou.exception.ResourceLoadException;

/**
 * 资源文件加载管理类
 * 
 * @author xx
 * @param <T>
 */
public abstract class ResourceLoadManager<T> {

    /**
     * 加载配置资源文件
     * 
     * @param path
     *            路径
     * @return
     * @throws NotFoundException
     */
    public abstract T loadConfigFile(String path) throws ResourceLoadException;

    /**
     * 从classpath(jar包等)中找到目标资源
     * 
     * @param name
     * @return
     * @throws IOException
     */
    protected ClassPathResource getResource(String name) throws IOException {
        return new ClassPathResource(name);
    }

    /**
     * 获取配置文件最后修改时间
     * 
     * @param name
     *            文件名
     * @return
     * @throws IOException
     */
    public static Long getLastModified(String name) throws IOException {
        return new ClassPathResource(name).lastModified();
    }
}
