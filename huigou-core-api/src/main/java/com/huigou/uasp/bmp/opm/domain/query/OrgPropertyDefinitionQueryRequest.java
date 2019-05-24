package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class OrgPropertyDefinitionQueryRequest extends QueryAbstractRequest {

    private String orgKindId;

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

}
