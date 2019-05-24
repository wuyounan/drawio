package com.huigou.uasp.bmp.flexfield.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.MessageConstants;

/**
 * 弹性域定义
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_FlexFieldDefinition")
public class FlexFieldDefinition extends AbstractEntity {

    private static final long serialVersionUID = -5535062243759788510L;

    /**
     * 文件夹ID
     */
    @Column(name = "folder_id")
    private String folderId;

    /**
     * 字段名称
     */
    @Column(name = "field_name")
    private String fieldName;

    /**
     * 描述
     */
    private String description;

    /**
     * 字段类型
     */
    @Column(name = "field_type")
    private String fieldType;

    /**
     * 字段长度
     */
    @Column(name = "field_length")
    private Integer fieldLength;

    /**
     * 字段精度
     */
    @Column(name = "field_precision")
    private Integer fieldPrecision;

    /**
     * 是否可以为空
     */
    private Integer nullable;

    /**
     * 默认值
     */
    @Column(name = "default_value")
    private String defaultValue;

    /**
     * 最小值
     */
    @Column(name = "min_value")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @Column(name = "max_value")
    private BigDecimal maxValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 控件类型
     */
    @Column(name = "control_type")
    private Integer controlType;

    /**
     * 控件宽度
     */
    @Column(name = "control_width")
    private Integer controlWidth;

    /**
     * 控件高度
     */
    @Column(name = "control_height")
    private Integer controlHeight;

    /**
     * 数据源类型
     */
    @Column(name = "data_source_kind_id")
    private String dataSourceKindId;

    /**
     * 数据源
     */
    @Column(name = "data_source")
    private String dataSource;

    /**
     * 是否只读
     */
    @Column(name = "read_only")
    private Integer readOnly;

    /**
     * 是否显示
     */
    @Column(name = "visible")
    private Integer visible;

    /**
     * 标签宽度
     */
    @Column(name = "label_width")
    private Integer labelWidth;

    @Column(name = "new_line")
    private Integer newLine;

    /**
     * 跨列数
     */
    @Column(name = "col_span")
    private Integer colSpan;

    /**
     * 排序号
     */
    private Integer sequence;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public Integer getFieldPrecision() {
        return fieldPrecision;
    }

    public void setFieldPrecision(Integer fieldPrecision) {
        this.fieldPrecision = fieldPrecision;
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getControlType() {
        return controlType;
    }

    public void setControlType(Integer controlType) {
        this.controlType = controlType;
    }

    public Integer getControlWidth() {
        return controlWidth;
    }

    public void setControlWidth(Integer controlWidth) {
        this.controlWidth = controlWidth;
    }

    public Integer getControlHeight() {
        return controlHeight;
    }

    public void setControlHeight(Integer controlHeight) {
        this.controlHeight = controlHeight;
    }

    public String getDataSourceKindId() {
        return dataSourceKindId;
    }

    public void setDataSourceKindId(String dataSourceKindId) {
        this.dataSourceKindId = dataSourceKindId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Integer readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getNewLine() {
        return newLine;
    }

    public void setNewLine(Integer newLine) {
        this.newLine = newLine;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public void checkConstraints(FlexFieldDefinition other) {
        super.checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getFieldName().equalsIgnoreCase(other.getFieldName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
    }

}
