package com.huigou.uasp.bmp.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.configuration.application.DictionaryApplication;
import com.huigou.uasp.bmp.plugin.StartPlugIn;
import com.huigou.util.LogHome;

/**
 * 数据字典插件
 * 
 * @author gongmm
 */
@Service("sysDictionaryPlugIn")
public class SysDictionaryPlugIn implements StartPlugIn {

    @Autowired
    private DictionaryApplication dictionaryApplication;

    public void init() throws ApplicationException {
        LogHome.getLog(this).info("开始加载系统字典;");
        dictionaryApplication.syncCache();
        LogHome.getLog(this).info("系统字典加载完成;");
    }

}
