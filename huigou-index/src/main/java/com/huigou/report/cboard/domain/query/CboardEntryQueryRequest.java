package com.huigou.report.cboard.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;

public class CboardEntryQueryRequest extends QueryAbstractRequest {

    private String definitionId;

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(definitionId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "definitionId"));
    }
}
