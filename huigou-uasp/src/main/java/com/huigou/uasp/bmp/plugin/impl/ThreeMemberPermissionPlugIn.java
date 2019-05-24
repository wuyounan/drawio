package com.huigou.uasp.bmp.plugin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.huigou.context.TmspmConifg;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.plugin.StartPlugIn;

/**
 * 三员权限插件
 * 
 * @author gongmm
 */
@Service("threeMemberPermissionPlugIn")
public class ThreeMemberPermissionPlugIn implements StartPlugIn {

    @Autowired
    private TmspmConifg tmspmConifg;
    
    @Autowired
    @Qualifier("accessApplicationProxy")
    private AccessApplicationProxy accessApplication;

    @Override
    public void init() {
        if (tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm()) {
            accessApplication.synThreeMemberPermission();
        }
    }

}
