package com.huigou.uasp.bmp.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.huigou.context.TmspmConifg;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.plugin.StartPlugIn;

/**
 * 三员管理插件
 * <p>
 * 隐藏超级管理员
 * 
 * @author gongm
 */
@Service("tmspmPlugIn")
public class TmspmPlugIn implements StartPlugIn {

    @Autowired
    private TmspmConifg tmspmConifg;
    
    @Autowired
    @Qualifier("accessApplicationProxy")
    private AccessApplicationProxy accessApplication;

    @Override
    public void init() {
        if (tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm() && tmspmConifg.isDoHideSuperAdministrator()) {
            accessApplication.hideSuperAdministrator();
        }
    }

}
