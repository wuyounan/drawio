package com.huigou.shiro.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huigou.uasp.bmp.opm.LoginStatus;
import com.huigou.util.Constants;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CasFilter 拷贝来源于 org.apache.shiro.cas.CasFilter
 * 
 * @author xx
 *         由于登录验证时本地用户可能不存在或被禁用,在登录过程中避免重复重定向,需要改造过滤器再登录验证错误后跳转到错误显示页面
 */
public class CasFilter extends AuthenticatingFilter {

    private static Logger logger = LoggerFactory.getLogger(CasFilter.class);

    // the url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp)
    private String failureUrl;

    /**
     * The token created for this authentication is a CasToken containing the CAS service ticket received on the CAS service url (on which
     * the filter must be configured).
     * 
     * @param request
     *            the incoming request
     * @param response
     *            the outgoing response
     * @throws Exception
     *             if there is an error processing the request.
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ticket = httpRequest.getParameter(Constants.CAS_TICKET_PARAMETER);
        return new CasToken(ticket);
    }

    /**
     * Execute login by creating {@link #createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse) token} and logging subject
     * with this token.
     * 
     * @param request
     *            the incoming request
     * @param response
     *            the outgoing response
     * @throws Exception
     *             if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        /** 主要改造登录错误后执行一次捕获，并跳转到系统错误显示页面 **/
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        // 从session读取登录错误信息
        LoginStatus loginStatus = (LoginStatus) session.getAttribute(Constants.CAS_VALIDATOR_ERROR);
        if (loginStatus == null) {
            return executeLogin(request, response);
        } else {
            httpRequest.setAttribute("tip", loginStatus.getMessage());
            httpRequest.setAttribute("location", true);
            // 清除错误信息
            session.removeAttribute(Constants.CAS_VALIDATOR_ERROR);
            // 跳转错误显示页面
            httpRequest.getRequestDispatcher(String.format("%s.jsp", Constants.AUTH_ERROR_RESULT_WITHOUT_SUFFIX)).forward(httpRequest, httpResponse);
        }
        return false;
    }

    /**
     * Returns <code>false</code> to always force authentication (user is never considered authenticated by this filter).
     * 
     * @param request
     *            the incoming request
     * @param response
     *            the outgoing response
     * @param mappedValue
     *            the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>false</code>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    /**
     * If login has been successful, redirect user to the original protected url.
     * 
     * @param token
     *            the token representing the current authentication
     * @param subject
     *            the current authenticated subjet
     * @param request
     *            the incoming request
     * @param response
     *            the outgoing response
     * @throws Exception
     *             if there is an error processing the request.
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }

    /**
     * If login has failed, redirect user to the CAS error page (no ticket or ticket validation failed) except if the user is already
     * authenticated, in which case redirect to the default success url.
     * 
     * @param token
     *            the token representing the current authentication
     * @param ae
     *            the current authentication exception
     * @param request
     *            the incoming request
     * @param response
     *            the outgoing response
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request, ServletResponse response) {
        // is user authenticated or in remember me mode ?
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                logger.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                logger.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
}
