package com.huigou.uasp.wsutil;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 设置代理权限认证类
 * 
 * @author xiexin
 */
public class BasicAuthenticator extends Authenticator {
    private String username, password;

    public BasicAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
