package com.huigou.uasp.client.views;

import com.huigou.util.ClassHelper;

public class InputTextColTag extends InputTextTag implements ColAttribute {

    private static final long serialVersionUID = 5379467489810868748L;

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

    public InputTextColTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "commonLabelCol";
    }

    @Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        Integer labelCol_ = ClassHelper.convert(labelCol, Integer.class, 2);
        Integer fieldCol_ = ClassHelper.convert(fieldCol, Integer.class, 2);
        addParameter("labelCol", labelCol_);
        addParameter("fieldCol", fieldCol_);
        addParameter("templateName",super.getDefaultTemplate());
    }

}
