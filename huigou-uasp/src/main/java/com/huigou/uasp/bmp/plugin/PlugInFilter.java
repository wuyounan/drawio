package com.huigou.uasp.bmp.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.huigou.cache.SystemCache;
import com.huigou.cache.service.ICache;
import com.huigou.express.ExpressManager;
import com.huigou.express.ExpressUtil;
import com.huigou.express.LoadExpressClasses;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.SpringBeanFactory;

/**
 * 系统初始化过滤器
 * 
 * @author gongmm
 */
public class PlugInFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        String contextPath = servletContext.getContextPath();
        String realPath = servletContext.getRealPath("/");
        ApplicationContextWrapper.init(WebApplicationContextUtils.getWebApplicationContext(servletContext));
        try {
            // 初始化系统数据缓存
            SystemCache.setCache(SpringBeanFactory.getBean(servletContext, "sysDataCache", ICache.class));
            SystemCache.setRealPath(realPath);
            SystemCache.setContextPath(contextPath);
            SystemCache.setStartTime(DateUtil.getDateFormat("yyyyMMddHH", DateUtil.getTimestamp()));
            initPath(SystemCache.getContextPath(), SystemCache.getRealPath());// 创建JS系统文件
            ExpressManager.initExpress(SpringBeanFactory.getBean(servletContext, "expressUtil", ExpressUtil.class),
                                       SpringBeanFactory.getBean(servletContext, "loadExpressClasses", LoadExpressClasses.class));
            PlugInManager pm = SpringBeanFactory.getBean(servletContext, "plugInManager", PlugInManager.class);
            pm.init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
        // 不让.view的请求经过其他过滤器 iwebOffice使用免得inputStream被提前读取了
        filterchain.doFilter(servletrequest, servletresponse);
    }

    public void destroy() {

    }

    /**
     * 创建JS文件系统应用名
     */
    private void initPath(String contextPath, String realPath) {
        Constants.WEB_APP = contextPath;
        OutputStream fout = null;
        StringBuffer sb = new StringBuffer();
        sb.append("var web_app={name:'");
        sb.append(contextPath);
        sb.append("'};");
        try {
            File f1 = new File(realPath + "/javaScript/WEB_APP.js");
            f1.createNewFile();
            fout = new FileOutputStream(f1);
            fout.write(sb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
