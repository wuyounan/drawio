package com.huigou.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;

import com.huigou.http.HttpBuilder;
import com.huigou.http.HttpCallbackMapper;

/**
 * HttpClientUtil 工具类
 * 
 * @author xx
 */
public class HttpClientUtil {

    public static final String CHARSET_NAME = "UTF-8";

    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    public static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
                                                                    .setConnectionRequestTimeout(150000).build();

    public static HttpBuilder builder() {
        return new HttpBuilder();
    }

    public static HttpBuilder builder(String user, String pwd) {
        return new HttpBuilder(user, pwd);
    }

    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("name", "admin");
        param.put("dictionary", "哈哈");
        String httpUrl = "http://127.0.0.1:9090/tech/ws/bmp/management/data/94E7FC5EEEB84F828C90A2423EEE7075/rbkind/";
        // String data = HttpClientUtil.builder("user", "123456").get(httpUrl).setParams(param).execute(new HttpCallbackMapper<String>() {
        // @Override
        // public String map(String responseContent) {
        // return responseContent;
        // }
        // });
        String data = HttpClientUtil.builder("user", "123456").get(httpUrl).setParams(param).execute((HttpCallbackMapper<String>) (responseContent) -> {
            return responseContent;
        });

        System.out.println(data);
        // List<Map<String, Object>> list = HttpClientUtil.builder("user", "123456").get(httpUrl).execute(new HttpCallbackMapper<List<Map<String, Object>>>() {
        // @Override
        // public List<Map<String, Object>> map(String content) {
        // return JSONUtil.toListMap(content);
        // }
        // });
        List<Map<String, Object>> list = HttpClientUtil.builder("user", "123456").get(httpUrl)
                                                       .execute((HttpCallbackMapper<List<Map<String, Object>>>) (content) -> {
                                                           return JSONUtil.toListMap(content);
                                                       });
        System.out.println(list.size());
    }
}
