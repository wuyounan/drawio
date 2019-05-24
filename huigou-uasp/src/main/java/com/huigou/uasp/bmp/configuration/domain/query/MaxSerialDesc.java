package com.huigou.uasp.bmp.configuration.domain.query;

public class MaxSerialDesc {
    private String id;

    private String sortItemValue;

    private Integer serialNumber;

    private String codingRuleDetailId;

    private Integer initialValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortItemValue() {
        return sortItemValue;
    }

    public void setSortItemValue(String sortItemValue) {
        this.sortItemValue = sortItemValue;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCodingRuleDetailId() {
        return codingRuleDetailId;
    }

    public void setCodingRuleDetailId(String codingRuleDetailId) {
        this.codingRuleDetailId = codingRuleDetailId;
    }

    public Integer getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

}
