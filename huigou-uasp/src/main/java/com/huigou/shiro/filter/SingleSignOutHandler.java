package com.huigou.shiro.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

import com.huigou.cache.service.ICache;
import com.huigou.util.Constants;

/**
 * 单点登出控制器
 * 
 * @author xiex
 */
public final class SingleSignOutHandler {

    /** Logger instance */
    private final Log log = LogFactory.getLog(getClass());

    /** The name of the artifact parameter. This is used to capture the session identifier. */
    private String artifactParameterName = "ticket";

    /** Parameter name that stores logout request */
    private String logoutParameterName = "logoutRequest";

    private ICache icache;

    private SessionManager sessionManager;

    protected SingleSignOutHandler() {
        init();
    }

    /**
     * @param name
     *            Name of the authentication token parameter.
     */
    public void setArtifactParameterName(final String name) {
        this.artifactParameterName = name;
    }

    /**
     * @param name
     *            Name of parameter containing CAS logout request message.
     */
    public void setLogoutParameterName(final String name) {
        this.logoutParameterName = name;
    }

    public void setIcache(ICache icache) {
        this.icache = icache;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Initializes the component for use.
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
    }

    /**
     * Determines whether the given request contains an authentication token.
     *
     * @param request
     *            HTTP reqest.
     * @return True if request contains authentication token, false otherwise.
     */
    public boolean isTokenRequest(final HttpServletRequest request) {
        return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
    }

    /**
     * Determines whether the given request is a CAS logout request.
     *
     * @param request
     *            HTTP request.
     * @return True if request is logout request, false otherwise.
     */
    public boolean isLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request)
               && CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }

    /**
     * 记录请求中的token和sessionID的映射对
     * 
     * @param request
     *            HTTP request containing an authentication token.
     */
    public void recordSession(final HttpServletRequest request) {
        Session session = SecurityUtils.getSubject().getSession();

        final String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
        if (log.isDebugEnabled()) {
            log.debug("Recording session for token " + token);
        }
        session.setAttribute(Constants.SERVICE_TICKET, token);
        icache.put(token, session.getId());
    }

    /**
     * 记录请求中的token和sessionID的映射对
     * 
     * @param token
     */
    public void recordSession(final String token) {
        Session session = SecurityUtils.getSubject().getSession();
        if (log.isDebugEnabled()) {
            log.debug("Recording session for token " + token);
        }
        session.setAttribute(Constants.SERVICE_TICKET, token);
        icache.put(token, session.getId());
    }

    /**
     * 从logoutRequest参数中解析出token，根据token获取到sessionID，再根据sessionID获取到session，执行session.stop();
     * 从而标记此session已经失效。
     *
     * @param request
     *            HTTP request containing a CAS logout message.
     */
    public void invalidateSession(final HttpServletRequest request) {
        final String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
        if (log.isTraceEnabled()) {
            log.trace("Logout request:\n" + logoutMessage);
        }

        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
        if (CommonUtils.isNotBlank(token)) {
            String sessionId = icache.get(token, String.class);
            if (sessionId != null) {
                try {
                    Session session = sessionManager.getSession(new DefaultSessionKey(sessionId));
                    if (session != null) {
                        session.stop();
                        log.error("Invalidating session [" + sessionId + "] for token [" + token + "]");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }
}
