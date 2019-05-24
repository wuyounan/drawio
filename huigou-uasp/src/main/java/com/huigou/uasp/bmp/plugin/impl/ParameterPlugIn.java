package com.huigou.uasp.bmp.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.uasp.bmp.configuration.application.ParameterApplication;
import com.huigou.uasp.bmp.plugin.StartPlugIn;
import com.huigou.util.LogHome;

/**
 * 系统参数插件
 * 
 * @ClassName: ParameterPlugIn
 * @author
 * @date 2014-2-24 上午10:51:46
 * @version V1.0
 */
@Service("parameterPlugIn")
public class ParameterPlugIn implements StartPlugIn {

    @Autowired
    private ParameterApplication parameterApplication;

    public void init() {
        LogHome.getLog(this).info("开始加载系统参数;");
        parameterApplication.syncCache();
        LogHome.getLog(this).info("系统参数加载完成;");
    }

}
