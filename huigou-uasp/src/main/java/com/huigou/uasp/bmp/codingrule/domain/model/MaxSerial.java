package com.huigou.uasp.bmp.codingrule.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 最多序列
 * 
 * @author
 *         SA_MAXSERIAL
 * @date 2018-07-19 09:34
 */
@Entity
@Table(name = "SA_MaxSerial")
public class MaxSerial extends AbstractEntity {

    private static final long serialVersionUID = -5344384179019503277L;

    /**
     * 编码规则明细_Id
     **/
    @Column(name = "codingRuleDetail_id", length = 32)
    private String codingRuleDetailId;

    /**
     * 分类项目值
     **/
    @Column(name = "sort_item_value", length = 128)
    private String sortItemValue;

    /**
     * 序列值
     **/
    @Column(name = "serial_number", length = 22)
    private Integer serialNumber;

    /**
     * 分段属性值
     **/
    @Column(name = "segment_attribute_value", length = 128)
    private String segmentAttributeValue;

    /**
     * 调整后的初始值
     **/
    @Column(name = "initial_value", length = 22)
    private Integer initialValue;

    public String getCodingRuleDetailId() {
        return this.codingRuleDetailId;
    }

    public void setCodingRuleDetailId(String codingRuleDetailId) {
        this.codingRuleDetailId = codingRuleDetailId;
    }

    public String getSortItemValue() {
        return this.sortItemValue;
    }

    public void setSortItemValue(String sortItemValue) {
        this.sortItemValue = sortItemValue;
    }

    public Integer getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSegmentAttributeValue() {
        return this.segmentAttributeValue;
    }

    public void setSegmentAttributeValue(String segmentAttributeValue) {
        this.segmentAttributeValue = segmentAttributeValue;
    }

    public Integer getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

}
