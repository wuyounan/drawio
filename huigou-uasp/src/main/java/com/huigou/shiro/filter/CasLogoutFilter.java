package com.huigou.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 如果请求中包含了ticket参数，记录ticket和sessionID的映射
 * 如果请求中包含logoutRequest参数，标记session为无效
 * 如果session不为空，且被标记为无效，则登出
 * 
 * @author xiex
 */
public class CasLogoutFilter extends AdviceFilter {
    private static final Logger log = LoggerFactory.getLogger(CasLogoutFilter.class);

    private SingleSignOutHandler singleSignOutHandler;

    public void setSingleSignOutHandler(SingleSignOutHandler singleSignOutHandler) {
        this.singleSignOutHandler = singleSignOutHandler;
    }

    /**
     * 请求过滤
     * 
     * @param request
     *            the incoming ServletRequest
     * @param response
     *            the outgoing ServletResponse
     * @return 是logoutRequest请求返回false，否则返回true
     * @throws Exception
     *             if there is any error.
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        if (singleSignOutHandler.isTokenRequest((HttpServletRequest) req)) {
            // 通过浏览器发送的请求，链接中含有token参数，记录token和sessionID
            // 在Realm 中记录
            // singleSignOutHandler.recordSession(req);
            return true;
        } else if (singleSignOutHandler.isLogoutRequest(req)) {
            // cas服务器发送的请求，链接中含有logoutRequest参数，在之前记录的session中设置logoutRequest参数为true
            // 因为Subject是和线程是绑定的，所以无法获取登录的Subject直接logout
            singleSignOutHandler.invalidateSession(req);
            // Do not continue up filter chain
            return false;
        } else {
            log.trace("Ignoring URI " + req.getRequestURI());
        }
        return true;
    }
}
