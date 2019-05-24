package com.huigou.uasp.bmp.securitypolicy.domain.query;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;

public class MachinesQueryRequest extends CodeAndNameQueryRequest {

    private String ip;

    private String securityGrade;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

}
