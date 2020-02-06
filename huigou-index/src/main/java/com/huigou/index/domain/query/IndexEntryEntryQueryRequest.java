package com.huigou.index.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 指标明细
 * 
 * @author gongmm
 */
public class IndexEntryEntryQueryRequest extends QueryAbstractRequest {

    private String entryId;

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    @Override
    public void checkConstraints() {
        Assert.hasText(entryId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "entryId"));
    }
}
