package com.huigou.data.domain.query;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;

public class ParentIdQueryRequest extends QueryAbstractRequest {
    protected String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void checkParentId() {
        Assert.hasText(parentId, MessageSourceContext.getMessage(MessageConstants.PARENT_ID_FIELD_NAME_NOT_BLANK));
    }
}
