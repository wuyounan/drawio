package com.huigou.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.huigou.util.HttpClientUtil;
import com.huigou.util.LogHome;

/**
 * 请求返回之后的处理抽象类
 */
public interface HttpCallbackMapper<T> extends HttpCallback<T> {
    static Logger logger = LogHome.getLog(HttpBuilder.class);

    /**
     * 此方法为请求返回之后数据转换为字符串
     * 
     * @param responseContent
     */
    T map(String content);

    default T callback(HttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            String responseContent = EntityUtils.toString(entity, HttpClientUtil.CHARSET_NAME);
            logger.info(responseContent);
            return this.map(responseContent);
        } catch (Exception e) {
            this.exception(e);
        }
        return null;
    }

}
