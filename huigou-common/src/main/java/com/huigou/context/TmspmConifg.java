package com.huigou.context;

/**
 * 三员安全保密管理配置
 * <p>
 * Three member security and privacy management
 * 
 * @author gongmm
 */
public class TmspmConifg {

    /**
     * 是否使用三员安全管理
     */
    private boolean useTspm;

    /**
     * 是否启用三员安全管理
     */
    private boolean enableTspm;

    /**
     *  是否隐藏超级管理角色和超级管理员
     */
    private boolean doHideSuperAdministrator = false;

    public boolean isUseTspm() {
        return useTspm;
    }

    public void setUseTspm(boolean useTspm) {
        this.useTspm = useTspm;
    }

    public boolean isEnableTspm() {
        return enableTspm;
    }

    public void setEnableTspm(boolean enableTspm) {
        this.enableTspm = enableTspm;
    }

    public boolean isDoHideSuperAdministrator() {
        return doHideSuperAdministrator;
    }

    public void setDoHideSuperAdministrator(boolean doHideSuperAdministrator) {
        this.doHideSuperAdministrator = doHideSuperAdministrator;
    }

}
