package com.huigou.uasp.bmp.bizClassification.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;


/**
 * 业务分类配置明细
 */
@Entity
@Table(name = "SA_Bizclassificationdetail")
public class BizClassificationDetail extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 业务属性配置ID
     */
    @Column(name = "biz_classification_id")
    private String bizClassificationId;

    /**
     * 业务参数/业务数据
     */
    @Column(name = "biz_type")
    private String bizType;

    @Column(name = "biz_name")
    private String bizName;

    /**
     * 业务属性ID
     */
    @Column(name = "biz_property_id")
    private String bizPropertyId;

    /**
     * 对应实体资源类名
     */
    @Column(name = "entity_class_name")
    private String entityClassName;

    @Column(name = "dialog_width")
    private Integer dialogWidth;

    /**
     * 排序号
     */
    private Integer sequence;

    public String getBizClassificationId() {
        return bizClassificationId;
    }

    public void setBizClassificationId(String bizClassificationId) {
        this.bizClassificationId = bizClassificationId;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizType() {
        return this.bizType;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public void setBizPropertyId(String bizPropertyId) {
        this.bizPropertyId = bizPropertyId;
    }

    public String getBizPropertyId() {
        return this.bizPropertyId;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public Integer getDialogWidth() {
        return dialogWidth;
    }

    public void setDialogWidth(Integer dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    public void setSequence(Integer squence) {
        this.sequence = squence;
    }

    public Integer getSequence() {
        return this.sequence;
    }
}
