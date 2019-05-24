package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 审批驳回理由维护
 * 
 * @author xx
 *         SA_APPROVAL_REJECTED_REASON
 * @date 2017-09-11 11:16
 */
@Entity
@Table(name = "SA_APPROVAL_REJECTED_REASON")
public class ApprovalRejectedReason extends AbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 3289389435187793053L;

    /**
     * 分类ID
     **/
    @Column(name = "folder_id", length = 32)
    private String folderId;

    /**
     * content
     **/
    @Column(name = "content", length = 256)
    private String content;

    /**
     * sequence
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    /**
     * status
     **/
    @Column(name = "status", length = 22)
    private Integer status;

    @Column(name = "rejected_reason_kind", length = 32)
    private String rejectedReasonKind;

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRejectedReasonKind() {
        return rejectedReasonKind;
    }

    public void setRejectedReasonKind(String rejectedReasonKind) {
        this.rejectedReasonKind = rejectedReasonKind;
    }

}
