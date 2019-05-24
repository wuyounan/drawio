package com.huigou.http;

import org.apache.http.HttpResponse;

/**
 * http 请求返回
 * 
 * @author xx
 */
public interface HttpCallback<T> {
    /**
     * 此方法为请求返回之后的处理,交给客户端处理,
     *
     * @param response
     *            返回报文实体
     */
    T callback(HttpResponse response);

    /**
     * 无论execute方法是否出异常,此方法都会最终执行,所以,如果打开io流,请在这里关闭
     */
    default void finallz() {

    }

    /**
     * 当execute方法出异常的时候,会调用此方法,
     */
    default void exception(Exception e) {
        throw new RuntimeException(e);
    }
}
