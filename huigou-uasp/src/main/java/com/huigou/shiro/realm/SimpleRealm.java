package com.huigou.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;

import com.huigou.context.Operator;
import com.huigou.shiro.token.SimpleUserToken;

import com.huigou.util.StringUtil;

/**
 * 不需要密码直接登录
 * 
 * @author xx
 *         需要使用SimpleUserToken
 */
public class SimpleRealm extends StandardRealm {

    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return SimpleUserToken.class;
    }

    @Override
    public String getName() {
        return "SimpleRealm";
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleUserToken adToken = (SimpleUserToken) token;
        String userName = (String) adToken.getPrincipal();
        if (StringUtil.isBlank(userName)) {
            throw new UnknownAccountException();
        }
        Operator operator = this.createOperator(userName);
        this.cacheOperator(operator);
        SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(operator, adToken.getCredentials(), getName());
        return result;
    }

}
