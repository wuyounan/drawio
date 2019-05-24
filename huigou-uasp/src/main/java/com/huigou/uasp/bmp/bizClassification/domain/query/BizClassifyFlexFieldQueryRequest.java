package com.huigou.uasp.bmp.bizClassification.domain.query;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;

public class BizClassifyFlexFieldQueryRequest extends QueryAbstractRequest {
    private String tableName;

    private String orgId;

    private String bizCode;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public void checkParams() {
        Assert.hasText(tableName, MessageSourceContext.getMessage(MessageConstants.TABLE_NAME_NOT_BLANK));
        Assert.hasText(orgId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
        Assert.hasText(bizCode, MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
    }
}
