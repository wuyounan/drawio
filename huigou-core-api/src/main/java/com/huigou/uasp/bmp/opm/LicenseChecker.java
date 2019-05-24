package com.huigou.uasp.bmp.opm;

public interface LicenseChecker {
         
    boolean checkValidTime();

    boolean checkOnlineUser(int onlineUser);

    boolean checkRegistUser(int registUser);

    boolean checkTask(int task);
}
