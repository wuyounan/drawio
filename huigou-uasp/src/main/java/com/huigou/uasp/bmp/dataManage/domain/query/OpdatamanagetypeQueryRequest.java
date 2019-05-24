package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;

/**
 * 数据管理权限类型
 * 
 * @author xx
 *         SA_OPDATAMANAGETYPE
 * @date 2018-09-04 11:58
 */
public class OpdatamanagetypeQueryRequest extends ParentAndCodeAndNameQueryRequest {

    /**
     * full_id
     **/
    protected String fullId;

    protected String excludeIds;

    protected String nodeKindId;

    protected String paramValue;

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

}
