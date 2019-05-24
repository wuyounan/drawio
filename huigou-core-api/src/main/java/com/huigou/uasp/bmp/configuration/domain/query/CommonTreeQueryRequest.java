package com.huigou.uasp.bmp.configuration.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class CommonTreeQueryRequest extends QueryAbstractRequest{

    private Integer kindId;

    private String parentId;

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
