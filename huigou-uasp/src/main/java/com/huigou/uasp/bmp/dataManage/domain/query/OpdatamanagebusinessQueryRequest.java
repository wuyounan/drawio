package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;

/**
 * 数据管理权限业务类型
 * 
 * @author xx
 *         SA_OPDATAMANAGEBUSINESS
 * @date 2018-09-27 12:04
 */
public class OpdatamanagebusinessQueryRequest extends ParentAndCodeAndNameQueryRequest {

    /**
     * full_id
     **/
    protected String fullId;

    protected String excludeIds;

    protected String nodeKindId;

    protected String paramValue;

    /**
     * 数据管理权限ID
     **/
    private String dataManageId;

    private String dataManageFullId;

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDataManageId() {
        return dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getDataManageFullId() {
        return dataManageFullId;
    }

    public void setDataManageFullId(String dataManageFullId) {
        this.dataManageFullId = dataManageFullId;
    }

}