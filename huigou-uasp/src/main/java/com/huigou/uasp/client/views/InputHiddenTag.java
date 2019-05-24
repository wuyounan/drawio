package com.huigou.uasp.client.views;

import com.huigou.util.ClassHelper;

public class InputHiddenTag extends AbstractTag {
  
	private static final long serialVersionUID = 4606274697212549079L;
	
	private String type;

    public void setType(String type) {
        this.type = type;
    }

    public InputHiddenTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "inputHidden";
    }

    @Override
    public void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (name != null) {
            Object obj = findValue(name);
            if (obj != null) {
                Object formatData = TaglibUtil.formatData(obj, type);
                addParameter("nameValue", ClassHelper.convert(formatData, String.class, ""));
            }
        }
    }

}
