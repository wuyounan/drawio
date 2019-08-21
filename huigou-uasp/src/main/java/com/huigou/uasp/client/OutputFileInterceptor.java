package com.huigou.uasp.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件输出拦截器。
 *
 * @author yonghuan
 * @see CommonController#outputFile(File, String)
 * @since 1.1.1
 */
public interface OutputFileInterceptor {

    /**
     * 对文件输出进行拦截处理。
     *
     * @param is     文件内容
     * @param type   文件类型
     * @param target 被拦截的对象
     * @return 拦截处理结果
     * @throws IOException
     */
    InputStream intercept(InputStream is, String type, Object target) throws IOException;

}
