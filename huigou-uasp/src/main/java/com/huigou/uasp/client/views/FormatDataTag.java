package com.huigou.uasp.client.views;

public class FormatDataTag extends AbstractTag {

	private static final long serialVersionUID = 8501158820546683831L;

	private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FormatDataTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "formatData";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        Object obj = findValue(name);
        Object formatData = TaglibUtil.formatData(obj, type);
        addParameter("nameValue", formatData);
    }

}
