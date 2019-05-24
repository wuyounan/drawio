package com.huigou.uasp.bmp.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.uasp.bmp.configuration.application.I18npropertiesApplication;
import com.huigou.uasp.bmp.plugin.StartPlugIn;
import com.huigou.util.LogHome;

/**
 * 国际化资源插件
 * 
 * @author Admin
 */
@Service("i18npropertiesPlugIn")
public class I18npropertiesPlugInImpl implements StartPlugIn {

    @Autowired
    private I18npropertiesApplication i18npropertiesApplication;

    @Override
    public void init() {
        LogHome.getLog(this).info("数据库国际化资源初始化开始;");
        i18npropertiesApplication.syncCache();
        LogHome.getLog(this).info("数据库国际化资源初始化结束;");
    }

}
