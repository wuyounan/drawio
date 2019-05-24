package com.huigou.uasp.bmp.configuration.domain.query;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 审批驳回理由维护
 * 
 * @author xx
 *         SA_APPROVAL_REJECTED_REASON
 * @date 2017-09-11 11:16
 */
public class ApprovalRejectedReasonQueryRequest extends QueryAbstractRequest {

    /**
     * 分类ID
     **/
    protected String folderId;

    /**
     * content
     **/
    protected String content;

    protected String rejectedReasonKind;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRejectedReasonKind() {
        return rejectedReasonKind;
    }

    public void setRejectedReasonKind(String rejectedReasonKind) {
        this.rejectedReasonKind = rejectedReasonKind;
    }

    public void checkConstraints() {
        Assert.hasText(rejectedReasonKind, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "rejectedReasonKind"));
    }
}
