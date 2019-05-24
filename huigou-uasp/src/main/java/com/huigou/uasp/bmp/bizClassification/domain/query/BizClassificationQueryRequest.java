package com.huigou.uasp.bmp.bizClassification.domain.query;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;

public class BizClassificationQueryRequest extends QueryAbstractRequest {
    /**
     * 父ID
     */
    private String parentId;

    /**
     * 排除ID
     */
    private String excludeIds;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务分类ID
     */
    private String bizClassificationId;

    private String bizType;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizClassificationId() {
        return bizClassificationId;
    }

    public void setBizClassificationId(String bizClassificationId) {
        this.bizClassificationId = bizClassificationId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public void checkBizClassificationId() {
        Assert.hasText(bizClassificationId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_NOT_BLANK));
    }
}
