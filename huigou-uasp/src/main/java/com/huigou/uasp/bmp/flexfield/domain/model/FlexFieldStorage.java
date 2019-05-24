package com.huigou.uasp.bmp.flexfield.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 弹性域存储
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_FlexFieldStorage")
public class FlexFieldStorage extends AbstractEntity {

    private static final long serialVersionUID = -5535062243759788510L;

    /**
     * 业务编码
     */
    @Column(name = "biz_kind_id")
    private String bizKindId;

    /**
     * 业务ID
     */
    @Column(name = "biz_id")
    private String bizId;

    /**
     * 明细ID
     */
    @Column(name = "detail_id")
    private String detailId;

    /**
     * 分组ID
     */
    @Column(name = "flexfieldgroup_id")
    private String flexFieldGroupId;

    /**
     * 弹性域字段ID
     */
    @Column(name = "flexfielddefinition_id")
    private String flexFieldDefinitionId;

    @Column(name = "field_value")
    private String fieldValue;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "look_up_value")
    private String lookUpValue;

    private Integer sequence;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizKindId() {
        return bizKindId;
    }

    public void setBizKindId(String bizKindId) {
        this.bizKindId = bizKindId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFlexFieldGroupId() {
        return flexFieldGroupId;
    }

    public void setFlexFieldGroupId(String flexFieldGroupId) {
        this.flexFieldGroupId = flexFieldGroupId;
    }

    public String getFlexFieldDefinitionId() {
        return flexFieldDefinitionId;
    }

    public void setFlexFieldDefinitionId(String flexFieldDefinitionId) {
        this.flexFieldDefinitionId = flexFieldDefinitionId;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLookUpValue() {
        return lookUpValue;
    }

    public void setLookUpValue(String lookUpValue) {
        this.lookUpValue = lookUpValue;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
