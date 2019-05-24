package com.huigou.uasp.client.views;

import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

public class InputTextTag extends AbstractTag {

    private static final long serialVersionUID = -7156869748388490067L;

    // 新加入的属性
    protected String wrapper;

    protected String mask;

    protected String match;

    protected String dataOptions;

    protected String spinner;

    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public void setDataOptions(String dataOptions) {
        this.dataOptions = dataOptions;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

    public InputTextTag() {
        super();
    }

    @Override
    protected String getDefaultTemplate() {
        return "inputText";
    }

    @Override
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        String valueMask = null;
        if (null != wrapper) {
            addParameter("wrapper", wrapper);
            // 根据wrapper匹配ICON
            addParameter("fontAwesome", TaglibUtil.getFontAwesome(wrapper));
            if (wrapper.equals("date") || wrapper.equalsIgnoreCase("dateTime")) {
                valueMask = wrapper;
            }
        }
        if (null != mask) {
            valueMask = mask;
            addParameter("mask", valueMask);
        }
        if (!StringUtil.isBlank(valueMask)) {
            Object obj = findValue(name);
            Object formatData = TaglibUtil.formatData(obj, valueMask);
            addParameter("nameValue", ClassHelper.convert(formatData, String.class, ""));
        }
        if (null != match) {
            addParameter("match", match);
        }
        if (null != dataOptions) {
            addParameter("dataOptions", dataOptions);
        }
        if (null != spinner) {
            addParameter("spinner", this.isTrue(spinner));
        }
    }

}
