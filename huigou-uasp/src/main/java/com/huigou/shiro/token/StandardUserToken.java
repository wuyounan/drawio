package com.huigou.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 系统默认登录认证使用Token
 * 
 * @author xx
 */
public class StandardUserToken extends UsernamePasswordToken {

    private static final long serialVersionUID = -2465386753732243161L;

    public StandardUserToken(final String username, final String password) {
        super(username, password != null ? password.toCharArray() : null, false, null);
    }
}
