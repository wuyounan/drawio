package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;

/**
 *界面元素查询通用模型
 * 
 * @author Administrator
 */

public class UIElementsQueryRequest extends FolderAndCodeAndNameQueryRequest {

    private String kindId;

    private Integer status;

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
