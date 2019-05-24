package com.huigou.uasp.bmp.securitypolicy.domain.query;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;

public class ApplicationSystemQueryRequest extends CodeAndNameQueryRequest {

    private String classPrefix;

    public String getClassPrefix() {
        return classPrefix;
    }

    public void setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
    }

}
