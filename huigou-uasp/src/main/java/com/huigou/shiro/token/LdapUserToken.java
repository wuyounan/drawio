package com.huigou.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Ldap 单点认证使用Token
 * 
 * @author xx
 */
public class LdapUserToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 9101032261134824373L;

    private String username;

    private String password;

    public LdapUserToken(final String username, final String password) {
        super(username, password != null ? password.toCharArray() : null, false, null);
        this.username = username;
        this.password = password;
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
