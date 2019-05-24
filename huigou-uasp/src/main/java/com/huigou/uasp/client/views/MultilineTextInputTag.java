package com.huigou.uasp.client.views;

public class MultilineTextInputTag extends AbstractTag {

    private static final long serialVersionUID = 7438572783727933377L;

    protected String rows;

    public void setRows(String rows) {
        this.rows = rows;
    }

    public MultilineTextInputTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "multilineTextInput";
    }

    @Override
    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != rows) {
            addParameter("rows", rows);
        }
        if (disabled != null) {
            Boolean flag = this.isTrue(disabled);
            if (flag) {
                addParameter("disabled", true);
                addParameter("readonly", true);
            }
        }
    }

}
