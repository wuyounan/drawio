package com.huigou.uasp.client.views;

import com.huigou.util.ClassHelper;

public class MultilineTextInputColTag extends MultilineTextInputTag implements ColAttribute {

    private static final long serialVersionUID = 7557576511432011149L;

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

    public MultilineTextInputColTag() {
        super();
    }

    // 这块是ftl模板文件名。
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
        Integer height = ClassHelper.convert(rows, Integer.class, 1) * 28;
        addParameter("colCssStyle", String.format("height:%spx;", height));
        addParameter("templateName", super.getDefaultTemplate());
    }

}
