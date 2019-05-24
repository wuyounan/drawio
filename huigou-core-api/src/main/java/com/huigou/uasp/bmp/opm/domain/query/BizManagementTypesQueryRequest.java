package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.util.StringUtil;

/**
 * 查询业务权限类别
 * 
 * @author Administrator
 */

public class BizManagementTypesQueryRequest extends QueryAbstractRequest {
    private String parentId;

    private String param;

    private Integer nodeKindId;

    private String fullId;

    private String queryManageCodes;

    private String keyValue;

    private String manageOrgId;

    private String subordinationOrgId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(Integer nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getQueryManageCodes() {
        return queryManageCodes;
    }

    public void setQueryManageCodes(String queryManageCodes) {
        this.queryManageCodes = queryManageCodes;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getManageOrgId() {
        if (StringUtil.isBlank(manageOrgId)) {
            return null;
        }
        return OpmUtil.getPersonIdFromPersonMemberId(manageOrgId);
    }

    public void setManageOrgId(String manageOrgId) {
        this.manageOrgId = manageOrgId;
    }

    public String getSubordinationOrgId() {
        return subordinationOrgId;
    }

    public void setSubordinationOrgId(String subordinationOrgId) {
        this.subordinationOrgId = subordinationOrgId;
    }

}
