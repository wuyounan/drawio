package com.huigou.uasp.wsutil;

import java.net.Authenticator;

import com.huigou.util.ClassHelper;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * webservice 动态调用客户端
 * 
 * @author xiexin
 */
public class WebServiceClient {
    private String url;

    private String method;

    private Object[] args;

    private String username;

    private String password;

    public WebServiceClient(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 调用webservice
     * 
     * @return
     */
    private Object callService() {
        if (StringUtil.isNotBlank(username) && StringUtil.isNotBlank(password)) {
            Authenticator.setDefault(new BasicAuthenticator(username, password));
        }
        return WebServiceClientHelper.callService(url, method, args);
    }

    /**
     * 调用webservice 获取返回参数字符串
     * 
     * @return
     */
    public String call() {
        Object obj = callService();
        return ClassHelper.convert(obj, String.class);
    }

    /**
     * 调用webservice 将返回结果解析为SDO
     * 
     * @return
     */
    public SDO callToSDO() {
        Object obj = callService();
        String json = ClassHelper.convert(obj, String.class);
        return new SDO(json);
    }

}
