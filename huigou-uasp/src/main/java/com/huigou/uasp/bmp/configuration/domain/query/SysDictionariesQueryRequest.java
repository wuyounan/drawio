package com.huigou.uasp.bmp.configuration.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;

public class SysDictionariesQueryRequest extends FolderAndCodeAndNameQueryRequest {

    private Integer kindId;

    private Integer status;

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
