package com.huigou.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.context.Operator;
import com.huigou.shiro.token.LdapUserToken;

import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

/**
 * Ldap 单点登录
 * 
 * @author xx
 *         集成JndiLdapRealm并引入StandardRealm
 *         调用:必须使用LdapUserToken
 *         LdapUserToken token=new LdapUserToken(loginName, decodedPassword);
 *         subject.login(token);
 */
public class LdapRealm extends JndiLdapRealm {
    private static Logger log = LoggerFactory.getLogger(LdapRealm.class);

    @Autowired
    private StandardRealm standardRealm;

    @Override
    public Class<? extends AuthenticationToken> getAuthenticationTokenClass() {
        return LdapUserToken.class;
    }

    @Override
    public String getName() {
        return "LdapRealm";
    }

    /**
     * 授权
     */
    protected AuthorizationInfo queryForAuthorizationInfo(PrincipalCollection principals, LdapContextFactory ldapContextFactory) throws NamingException {
        return standardRealm.doGetAuthorizationInfo(principals);
    }

    /**
     * 认证
     */
    protected void authenticate(AuthenticationToken token, LdapContextFactory ldapContextFactory) throws NamingException {
        Object principal = token.getPrincipal();
        Object credentials = token.getCredentials();
        log.debug("Authenticating user '{}' through LDAP", principal);
        principal = getLdapPrincipal(token);
        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getLdapContext(principal, credentials);
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            authenticate(token, getContextFactory());
            String loginName = token.getPrincipal().toString();
            // 创建系统用户
            Operator operator = standardRealm.createOperator(loginName);
            // 保存登录用户信息
            standardRealm.cacheOperator(operator);
            info = new SimpleAuthenticationInfo(operator, token.getCredentials(), getName());
        } catch (AuthenticationNotSupportedException e) {
            String msg = "Unsupported configured authentication mechanism";
            log.error(msg, e);
            throw new UnsupportedAuthenticationMechanismException(msg, e);
        } catch (javax.naming.AuthenticationException e) {
            log.error("LDAP authentication failed.", e);
            throw new AuthenticationException("LDAP authentication failed.", e);
        } catch (NamingException e) {
            String msg = "LDAP naming error while attempting to authenticate user.";
            log.error(msg, e);
            throw new AuthenticationException(msg, e);
        }
        return info;
    }
}
