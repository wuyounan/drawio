package com.huigou.uasp.bmp.plugin.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.cache.ApplicationSystemDesc;
import com.huigou.cache.SystemCache;
import com.huigou.uasp.bmp.plugin.StartPlugIn;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * 应用系统插件
 * 
 * @author Admin
 */
@Service("applicationSystemPlugIn")
public class ApplicationSystemPlugInImpl implements StartPlugIn {

    @Autowired
    private ApplicationSystemApplication applicationSystemApplication;

    @Override
    public void init() {
        LogHome.getLog(this).info("开始加载应用系统信息;");
        List<ApplicationSystemDesc> applicationSystemDescs = this.applicationSystemApplication.queryAllDesc();
        HashMap<String, Object> applicationSystemMap = new HashMap<String, Object>();
        if (applicationSystemDescs != null && applicationSystemDescs.size() > 0) {
            for (ApplicationSystemDesc applicationSystem : applicationSystemDescs) {
                applicationSystemMap.put(applicationSystem.getCode(), applicationSystem);
            }
            for (ApplicationSystemDesc applicationSystem : applicationSystemDescs) {
                if (StringUtil.isNotBlank(applicationSystem.getClassPrefix())) {
                    applicationSystemMap.put(applicationSystem.getClassPrefix(), applicationSystem);
                }
            }
        }
        SystemCache.setApplicationSystem(applicationSystemMap);
        LogHome.getLog(this).info("加载应用系统信息完成;");
    }

}
