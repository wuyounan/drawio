package com.huigou.uasp.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author yonghuan
 * @since 1.1.1
 */
public class OutputFileInterceptorComposite implements OutputFileInterceptor {

    private List<OutputFileInterceptor> delegationInterceptors;

    public OutputFileInterceptorComposite(List<OutputFileInterceptor> delegationInterceptors) {
        this.delegationInterceptors = Collections.unmodifiableList(delegationInterceptors);
    }

    @Override
    public InputStream intercept(InputStream is, String type, Object target) throws IOException {
        for (OutputFileInterceptor interceptor : delegationInterceptors) {
            is = interceptor.intercept(is, type, target);
        }
        return is;
    }

}
