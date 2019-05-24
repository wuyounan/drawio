package com.huigou.uasp.bmp.securitypolicy.domain.query;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;

public class SecurityPoliciesQueryRequest extends CodeAndNameQueryRequest {

    private String securityGrade;

    public String getSecurityGrade() {
        return securityGrade;
    }

    public void setSecurityGrade(String securityGrade) {
        this.securityGrade = securityGrade;
    }

}
