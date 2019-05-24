package com.huigou.uasp.client.views;

public class ButtonTag extends AbstractTag {

    private static final long serialVersionUID = -1584163120230030634L;

    private String power;

    private String icon;

    private String type;

    private String onclick;

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public ButtonTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "inputButton";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != id) {
            addParameter("id", id);
        }
        if (null != name) {
            addParameter("name", name);
        }
        if (null != icon) {
            addParameter("icon", icon);
        }
        if (null != type) {
            addParameter("type", type);
        }
        if (null != cssClass) {
            addParameter("cssClass", cssClass);
        }
        if (null != value) {
            addParameter("value", this.getMessage(value));
        }
        if (null != onclick) {
            addParameter("onclick", onclick);
        }
        if (null != cssStyle) {
            addParameter("cssStyle", cssStyle);
        }
    }

}
