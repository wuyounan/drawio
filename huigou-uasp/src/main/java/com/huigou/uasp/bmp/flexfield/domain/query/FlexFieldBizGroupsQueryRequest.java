package com.huigou.uasp.bmp.flexfield.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;


public class FlexFieldBizGroupsQueryRequest extends FolderAndCodeAndNameQueryRequest {

    private String bizCode;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

}
