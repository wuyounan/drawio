package com.huigou.uasp.client.views;

public class InputCheckBoxTag extends AbstractTag {
   
	private static final long serialVersionUID = 5756386189221782058L;
	
	protected String checked;

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public InputCheckBoxTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "inputCheckBox";
    }

    protected Class<String> getValueClassType() {
        return String.class;
    }

    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (value == null) {
            value = "1";
        }
        addParameter("value", value);
        if (name != null) {
            Object v = findValue(name);
            if (v != null) {
                String[] vs = v.toString().split(",");
                for (String t : vs) {
                    if (t.equals(value)) {
                        addParameter("checked", true);
                        break;
                    }
                }
            }
        }
        if (null != checked) {
            addParameter("checked", this.isTrue(checked));
        }
    }
}
