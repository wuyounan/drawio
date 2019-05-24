package com.huigou.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Service;

import com.huigou.shiro.authc.LimitLoginAccountException;

/**
 * 限制登录次数
 * 
 * @author gongmm
 */
@Service("limitLoginAuthenticationFilter")
public class LimitLoginAuthenticationFilter extends FormAuthenticationFilter {

    private static final String LOGIN_TIME_ATTRIBUTE = "_loginTime_";

    private Integer allowLoginTimes = 5;

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        Session session = getSubject(request, response).getSession();
        Integer loginTimes = (Integer) session.getAttribute(LOGIN_TIME_ATTRIBUTE);

        if (loginTimes == null) {
            loginTimes = new Integer(1);
            session.setAttribute(LOGIN_TIME_ATTRIBUTE, loginTimes);
        }

        return super.executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Session session = getSubject(request, response).getSession(false);
        Integer loginTimes = (Integer) session.getAttribute(LOGIN_TIME_ATTRIBUTE);

        if (loginTimes >= allowLoginTimes) {
            throw new LimitLoginAccountException("登录出错%d次，用户已锁定。");
        }

        session.setAttribute(LOGIN_TIME_ATTRIBUTE, ++loginTimes);
        return super.onLoginFailure(token, e, request, response);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        Session session = subject.getSession(false);
        session.removeAttribute(LOGIN_TIME_ATTRIBUTE);
        return super.onLoginSuccess(token, subject, request, response);
    }

}
