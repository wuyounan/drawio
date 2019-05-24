package com.huigou.uasp.bmp.flexfield.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 弹性域分组字段
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_FlexFieldBizGroupField")
public class FlexFieldBizGroupField extends AbstractEntity {

    private static final long serialVersionUID = -5535062243759788510L;

    /**
    *
    **/
    @Column(name = "flexfieldbizgroup_id", length = 32)
    private String flexfieldbizgroupId;

    /**
    *
    **/
    @Column(name = "flexfielddefinition_id", length = 32)
    private String flexfielddefinitionId;

    /**
    *
    **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    /**
    *
    **/
    @Column(name = "nullable", length = 22)
    private Integer nullable;

    /**
    *
    **/
    @Column(name = "control_width", length = 22)
    private Integer controlWidth;

    /**
    *
    **/
    @Column(name = "control_height", length = 22)
    private Integer controlHeight;

    /**
    *
    **/
    @Column(name = "read_only", length = 22)
    private Integer readOnly;

    /**
    *
    **/
    @Column(name = "visible", length = 22)
    private Integer visible;

    /**
    *
    **/
    @Column(name = "new_line", length = 22)
    private Integer newLine;

    /**
    *
    **/
    @Column(name = "label_width", length = 22)
    private Integer labelWidth;

    /**
    *
    **/
    @Column(name = "col_span", length = 22)
    private Integer colSpan;

    public String getFlexfieldbizgroupId() {
        return this.flexfieldbizgroupId;
    }

    public void setFlexfieldbizgroupId(String flexfieldbizgroupId) {
        this.flexfieldbizgroupId = flexfieldbizgroupId;
    }

    public String getFlexfielddefinitionId() {
        return this.flexfielddefinitionId;
    }

    public void setFlexfielddefinitionId(String flexfielddefinitionId) {
        this.flexfielddefinitionId = flexfielddefinitionId;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getNullable() {
        return this.nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public Integer getControlWidth() {
        return this.controlWidth;
    }

    public void setControlWidth(Integer controlWidth) {
        this.controlWidth = controlWidth;
    }

    public Integer getControlHeight() {
        return this.controlHeight;
    }

    public void setControlHeight(Integer controlHeight) {
        this.controlHeight = controlHeight;
    }

    public Integer getReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(Integer readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getVisible() {
        return this.visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getNewLine() {
        return this.newLine;
    }

    public void setNewLine(Integer newLine) {
        this.newLine = newLine;
    }

    public Integer getLabelWidth() {
        return this.labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Integer getColSpan() {
        return this.colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }
}
