package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.util.StringUtil;

public class RolesQueryRequestQueryRequest extends QueryAbstractRequest {

    private String parentId;

    private String keyword;

    private String tenantId;

    private String tenantKindId;

    private Integer nodeKindId;

    public String getParentId() {
        if (parentId != null && parentId.equals(CommonDomainConstants.DEFAULT_ROOT_PARENT_ID)) {
            return null;
        }
        if(StringUtil.isNotBlank(keyword)){
            return null;
        }
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantKindId() {
        return tenantKindId;
    }

    public void setTenantKindId(String tenantKindId) {
        this.tenantKindId = tenantKindId;
    }

    public Integer getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(Integer nodeKindId) {
        this.nodeKindId = nodeKindId;
    }
}
