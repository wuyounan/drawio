package com.huigou.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 不需要密码直接登录时使用
 * 
 * @author xx
 */
public class SimpleUserToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 471753581611779369L;

    private static final String password = "nopassword";

    private String username;

    public SimpleUserToken(final String username) {
        super(username, password.toCharArray(), false, null);
        this.username = username;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
