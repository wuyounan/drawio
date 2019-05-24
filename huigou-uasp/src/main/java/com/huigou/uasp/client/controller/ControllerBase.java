package com.huigou.uasp.client.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.huigou.util.StringUtil;

/**
 * Action基类
 * 
 * @author xx
 */
public class ControllerBase {

    public final static String NONE = null;

    private static final ThreadLocal<HttpServletRequest> requestContainer = new ThreadLocal<HttpServletRequest>();

    private static final ThreadLocal<HttpServletResponse> responseContainer = new ThreadLocal<HttpServletResponse>();

    private static final ThreadLocal<HttpSession> sessionContainer = new ThreadLocal<HttpSession>();

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        requestContainer.set(request);
        responseContainer.set(response);
        sessionContainer.set(request.getSession());
    }

    /**
     * 获取Session内数据
     *
     * @return
     */
    protected Object getSessionAttribute(String key) {
        return this.getSession().getAttribute(key);
    }

    protected void setSessionAttribute(String key, Object obj) {
        this.getSession().setAttribute(key, obj);
    }

    /**
     * 获取request Parameter
     *
     * @param key
     * @return String[]
     */
    protected String[] getParameterArray(String key) {
        return (String[]) this.getRequest().getParameterValues(key);
    }

    /**
     * 获取request Parameter
     *
     * @param key
     * @return String
     */
    protected String getParameter(String key) {
        String[] para = (String[]) this.getParameterArray(key);
        if (para != null && para.length > 0) {
            //为解决xss攻击 这里增加过滤方法
            return StringUtil.stripXSS(para[0]);
        }
        return null;
    }

    /**
     * 获取请求的IP地址
     *
     * @return
     */
    protected String getRequestIP() {
        HttpServletRequest request = this.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return ip.split(",")[0];
    }

    protected HttpServletRequest getRequest() {
        return requestContainer.get();
    }

    protected HttpServletResponse getResponse() {
        return responseContainer.get();
    }

    protected HttpSession getSession() {
        return sessionContainer.get();
    }

    protected ServletContext getServletContext() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        return servletContext;
    }

}