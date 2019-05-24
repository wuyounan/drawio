package com.huigou.uasp.client.views;

import com.huigou.util.ClassHelper;

public class InputRadioColTag extends InputRadioTag implements ColAttribute {

    private static final long serialVersionUID = 4250045903724606494L;

    protected String fieldCol;

    protected String labelCol;

    public String getFieldCol() {
        return fieldCol;
    }

    public void setFieldCol(String fieldCol) {
        this.fieldCol = fieldCol;
    }

    public String getLabelCol() {
        return labelCol;
    }

    public void setLabelCol(String labelCol) {
        this.labelCol = labelCol;
    }

    public InputRadioColTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "commonLabelCol";
    }

    @Override
    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        Integer labelCol_ = ClassHelper.convert(labelCol, Integer.class, 2);
        Integer fieldCol_ = ClassHelper.convert(fieldCol, Integer.class, 2);
        addParameter("labelCol", labelCol_);
        addParameter("fieldCol", fieldCol_);
        addParameter("automaticHeight", true);
        addParameter("templateName", super.getDefaultTemplate());
    }

}
