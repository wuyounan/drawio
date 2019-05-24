package com.huigou.uasp.bmp.securitypolicy.domain.query;

import java.io.Serializable;

public class PersonLoginLimitDesc implements Serializable {

    private static final long serialVersionUID = 6718257249330264875L;

    private String loginName;

    private String machineCode;

    private String machineName;

    private String machineIp;

    private String machineMacAddress;

    private String securityGrade;

    private Integer machineStatus;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getMachineMacAddress() {
        return machineMacAddress;
    }

    public void setMachineMacAddress(String machineMacAddress) {
        this.machineMacAddress = machineMacAddress;
    }

    public String getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(Integer machineStatus) {
        this.machineStatus = machineStatus;
    }

}
