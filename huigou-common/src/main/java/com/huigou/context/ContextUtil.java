package com.huigou.context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.huigou.exception.ApplicationException;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 环境工具类
 * 
 * @author gongmm
 */
public class ContextUtil {

    private static AbstractSessionDAO sessionDAO;

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static SDO getSDO() {
        SDO result = new SDO(true);
        HttpServletRequest request = ContextUtil.getRequest();
        Enumeration<?> em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String key = (String) em.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null && values.length > 0) {
                if (values.length == 1) {
                    result.putProperty(key, StringUtil.decode(values[0]));
                } else {
                    result.putProperty(key, values);
                }
            }
        }
        return result;
    }

    public static SDO getSdoFromJsonBody() {
        SDO paramsSdo = getSDO();
        SDO result;
        HttpServletRequest request = ContextUtil.getRequest();
        String json;
        try {
            json = new String(IOUtils.toByteArray(request.getInputStream()), "utf-8");
            if (StringUtil.isNotBlank(json)) {
                Map<String, Object> map = JSONUtil.toMap(json);
                result = new SDO(map);
            } else {
                result = new SDO();
            }
            result.getProperties().putAll(paramsSdo.getProperties());
        } catch (IOException e) {
            throw new ApplicationException("消息体格式错误。", e);
        }

        return result;
    }

    public static Operator getOperatorByToken() {
        SDO params = (SDO) getRequest().getAttribute(Constants.SDO);
        String token;
        if (params == null) {
            token = getRequest().getParameter(Constants.TOKEN);
        } else {
            token = params.getString(Constants.TOKEN);
        }
        if (StringUtil.isNotBlank(token)) {
            try {
                Session session = getSessionDAO().readSession(token);
                Object object = session.getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
                if (object != null) {
                    return (Operator) object;
                }
            } catch (UnknownSessionException e) {
                throw new ApplicationException(String.format("没有找到“%s”对应的Session", token));
            }
        }
        return null;
    }

    public static Operator getOperator() {
        Operator operator = findOperator();
        Assert.notNull(operator, "获取环境参数出错，没有找到操作员环境对象。");
        return operator;
    }

    public static Operator findOperator() {
        if (isAppRequest()) {
            return getOperatorByToken();
        } else {
            Subject subject = SecurityUtils.getSubject();
            Object object = subject.getSession().getAttribute(Constants.SESSION_OPERATOR_ATTRIBUTE);
            if (object == null) {
                return null;
            }
            return (Operator) object;
        }
    }

    public static boolean isAjaxRequest() {
        String header = getRequest().getHeader("X-Requested-With");
        return header != null && "XMLHttpRequest".equals(header);
    }

    public static boolean isAjaxRequestURI() {
        return getRequest().getRequestURI().indexOf(".ajax") > 0;
    }

    public static boolean isWebAppRequest() {
        return getRequest().getRequestURI().indexOf(".webApp") > 0;
    }

    public static boolean isAppRequest() {
        return getRequest().getRequestURI().indexOf(".appJob") > 0 || isWebAppRequest();
    }

    public static boolean isDoRequest() {
        return getRequest().getRequestURI().indexOf(".do") > 0;
    }

    public static boolean isJobRequest() {
        return getRequest().getRequestURI().indexOf(".job") > 0;
    }

    public static boolean isRtxAuth(HttpServletRequest request) {
        return false;
    }

    public static boolean isLoadRequest() {
        return getRequest().getRequestURI().indexOf(".load") > 0;
    }

    public static boolean isJsonpCall() {
        HttpServletRequest request = ContextUtil.getRequest();
        String callback = ClassHelper.convert(request.getParameter("callback"), String.class);
        return ContextUtil.isWebAppRequest() && !StringUtil.isBlank(callback);
    }

    public static boolean isPrintRequest() {
        return getRequest().getRequestURI().indexOf(".print") > 0;
    }

    public static AbstractSessionDAO getSessionDAO() {
        if (sessionDAO == null) {
            sessionDAO = ApplicationContextWrapper.getBean("shiroSessionDAO", AbstractSessionDAO.class);
        }
        return sessionDAO;
    }

    public static String getRequestIP() {
        HttpServletRequest request = getRequest();
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
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip.split(",")[0];
    }

    public static String getMACAddress(String ip) {
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            for (int i = 1; i < 100; i++) {
                line = input.readLine();
                if (line != null) {
                    if (line.indexOf("MAC Address") > 1 || line.indexOf("MAC 地址") > 0) {
                        String[] splits = line.split("=");
                        macAddress = splits[1];
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress.trim();
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
}
