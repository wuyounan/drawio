package com.huigou.uasp.bmp.plugin.impl;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.plugin.StartPlugIn;
import com.huigou.util.LogHome;

/**
 * IP过滤数据插件
 * 
 * @author gongmm
 */
public class IPRegPlugIn implements StartPlugIn {

    public void init() throws ApplicationException {
        LogHome.getLog(this).info("开始加载IP地址;");
    }

}
