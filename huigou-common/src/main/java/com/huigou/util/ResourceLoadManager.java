package com.huigou.util;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.huigou.exception.ApplicationException;
import org.springframework.core.io.ClassPathResource;

import com.huigou.exception.NotFoundException;
import com.huigou.exception.ResourceLoadException;
import org.springframework.core.io.Resource;

/**
 * 资源文件加载管理类
 *
 * @param <T>
 * @author xx
 */
public abstract class ResourceLoadManager<T> {

    /**
     * 加载配置资源文件
     *
     * @param path 路径
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
     * @param name 文件名
     * @return
     * @throws IOException
     */
    public static Long getLastModified(String name) throws IOException {
        return new ClassPathResource(name).lastModified();
    }

    /**
     * 获取最大的修改时间。
     *
     * @param resources 资源。
     * @return 最大的修改时间
     * @throws ApplicationException
     * @since 1.1.3
     */
    public static long maxLastModified(Collection<String> resources) {
        List<Resource> rs = resources.stream()
                .filter(Objects::nonNull)
                .map(ClassPathResource::new)
                .collect(Collectors.toList());
        return maxLastModified(rs);
    }

    /**
     * 获取最大的修改时间。
     *
     * @param resources 资源。
     * @return 最大的修改时间
     * @throws ApplicationException
     * @since 1.1.3
     */
    public static long maxLastModified(List<Resource> resources) {
        return resources.stream()
                .filter(Objects::nonNull)
                .filter(Resource::exists)
                .mapToLong(r -> {
                    try {
                        return r.lastModified();
                    } catch (IOException ioe) {
                        throw new ApplicationException(ioe);
                    }
                }).max().getAsLong();
    }
}
