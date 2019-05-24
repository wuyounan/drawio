package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;

/**
 * 组织类型查询通用模型
 * 
 * @author Administrator
 */

public class OrgTypeQueryRequest extends FolderAndCodeAndNameQueryRequest {

    private String orgKindId;

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public String getFolderId() {
        if (this.folderId != null && this.folderId.equals("0")) {
            return null;
        }
        return folderId;
    }
}
