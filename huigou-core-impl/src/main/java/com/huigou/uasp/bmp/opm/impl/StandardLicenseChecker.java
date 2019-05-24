package com.huigou.uasp.bmp.opm.impl;

import com.huigou.system.common.SystemUtils;
import com.huigou.uasp.bmp.opm.LicenseChecker;

public class StandardLicenseChecker implements LicenseChecker {

    static {
        String fileName = SystemUtils.normalizeLib("huigou-license");
        System.load(fileName);
        StandardLicenseChecker.initLicense();
    }

    private static LicenseChecker instance;

    public static LicenseChecker getInstance() {
        if (instance == null) {
            synchronized (StandardLicenseChecker.class) {
                if (instance == null) {
                    instance = new StandardLicenseChecker();
                }
            }
        }
        return instance;
    }

    public static native void initLicense();

    @Override
    public native boolean checkValidTime();

    @Override
    public native boolean checkOnlineUser(int onlineUser);

    @Override
    public native boolean checkRegistUser(int registUser);

    @Override
    public native boolean checkTask(int task);

}
