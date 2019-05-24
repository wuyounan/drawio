package com.huigou.uasp.bmp.plugin;

import java.util.List;

/**
 * 插件管理类
 */
public class PlugInManager {
    private List<StartPlugIn> plugIns;

    public void setPlugIns(List<StartPlugIn> plugIns) {
        this.plugIns = plugIns;
    }

    /**
     * 初始化
     */
    public void init() {
        if (plugIns == null || plugIns.size() <= 0) {
            return;
        }
        for (StartPlugIn plugIn : plugIns) {
            plugIn.init();
        }
    }

}
