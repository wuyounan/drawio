package com.huigou.uasp.bmp.codingrule.domain.query;

import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail.AttributeKind;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail.AttributeUseKind;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail.DirectionKind;
import com.huigou.util.StringUtil;

public class CodingRuleDetailDesc {

    private String id;

    private String codingRuleId;

    private Integer codeSerialNumber;

    private String attributeKind;

    private String attributeValue;

    private String attributeUseKind;

    private String format;

    private Integer step;

    private Integer initialValue;

    private Integer length;

    private boolean isDisplay;

    private String fillSign;

    private String fillSignDirection;

    private Integer interceptPos;

    private String interceptDirection;

    private Integer interceptLength;

    private boolean isUseDelimiter;

    private boolean isSortByItem;

    private String description;

    private Integer sequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodingRuleId() {
        return this.codingRuleId;
    }

    public void setCodingRuleId(String codingRuleId) {
        this.codingRuleId = codingRuleId;
    }

    public Integer getCodeSerialNumber() {
        return this.codeSerialNumber;
    }

    public void setCodeSerialNumber(Integer codeSerialNumber) {
        this.codeSerialNumber = codeSerialNumber;
    }

    public String getAttributeKind() {
        return this.attributeKind;
    }

    public void setAttributeKind(String attributeKind) {
        this.attributeKind = attributeKind;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeUseKind() {
        return this.attributeUseKind;
    }

    public void setAttributeUseKind(String attributeUseKind) {
        this.attributeUseKind = attributeUseKind;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getStep() {
        return this.step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getInitialValue() {
        return this.initialValue;
    }

    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public boolean getIsDisplay() {
        return this.isDisplay;
    }

    public void setIsDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getFillSign() {
        if (AttributeKind.SERIAL_NUMBER.equals(attributeKind)) {
            return "0";
        }

        return this.fillSign;
    }

    public boolean needFillSign(String value) {
        return StringUtil.isNotBlank(fillSign) || (AttributeKind.SERIAL_NUMBER.equals(attributeKind)) && this.length > value.length();
    }

    public void setFillSign(String fillSign) {
        this.fillSign = fillSign;
    }

    public String getFillSignDirection() {
        return this.fillSignDirection;
    }

    public void setFillSignDirection(String fillSignDirection) {
        this.fillSignDirection = fillSignDirection;
    }

    public Integer getInterceptPos() {
        return this.interceptPos;
    }

    public void setInterceptPos(Integer interceptPos) {
        this.interceptPos = interceptPos;
    }

    public String getInterceptDirection() {
        return this.interceptDirection;
    }

    public void setInterceptDirection(String interceptDirection) {
        this.interceptDirection = interceptDirection;
    }

    public Integer getInterceptLength() {
        return this.interceptLength;
    }

    public void setInterceptLength(Integer interceptLength) {
        this.interceptLength = interceptLength;
    }

    public boolean getIsUseDelimiter() {
        return this.isUseDelimiter;
    }

    public void setIsUseDelimiter(boolean isUseDelimiter) {
        this.isUseDelimiter = isUseDelimiter;
    }

    public boolean getIsSortByItem() {
        return this.isSortByItem;
    }

    public void setIsSortByItem(boolean isSortByItem) {
        this.isSortByItem = isSortByItem;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public AttributeKind getAttributeKindEnum() {
        return AttributeKind.fromName(this.attributeKind);
    }

    public DirectionKind getFillSignDirectionEnum() {
        return DirectionKind.fromName(this.fillSignDirection);
    }

    public AttributeUseKind getAttributeUseKindEnum() {
        return AttributeUseKind.fromName(this.attributeUseKind);
    }

    public boolean useDelimiter() {
        return this.getSequence() != 1 && this.getIsUseDelimiter();
    }

}
