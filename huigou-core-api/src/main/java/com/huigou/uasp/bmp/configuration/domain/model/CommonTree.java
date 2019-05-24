package com.huigou.uasp.bmp.configuration.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.model.TreeEntity;

/**
 * 通用树
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_CommonTree")
public class CommonTree extends TreeEntity {

    private static final long serialVersionUID = 3070753255174743897L;

    /**
     * 树类别ID
     */
    @Column(name = "kind_id")
    private int kindId;

    /**
     * 节点类别ID
     */
    @Column(name = "node_kind_id")
    @Enumerated(EnumType.STRING)
    private NodeKind nodeKind;

    /**
     * 简码
     */
    @Column(name = "short_code")
    private String shortCode;

    private String remark;

    public int getKindId() {
        return kindId;
    }

    public void setKindId(int kindId) {
        this.kindId = kindId;
    }

    public NodeKind getNodeKind() {
        return nodeKind;
    }

    public void setNodeKind(NodeKind nodeKind) {
        this.nodeKind = nodeKind;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void checkConstraints(BaseInfoAbstractEntity other) {
        Assert.hasText(this.getCode(), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_BLANK));
        Assert.hasText(this.getName(), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_BLANK));
        if (other != null) {
            Assert.isTrue(!this.getName().equalsIgnoreCase(other.getName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
            Assert.isTrue(!this.getCode().equalsIgnoreCase(other.getCode()), MessageSourceContext.getMessage(MessageConstants.CODE_NOT_DUPLICATE));
        }
    }

    @Override
    @JsonIgnore
    public String getTenantField_() {
        return "kindId";
    }

    @Override
    @JsonIgnore
    public Serializable getTenantId_() {
        return this.kindId;
    }

    public enum NodeKind {
        FOLDER
    }
}
