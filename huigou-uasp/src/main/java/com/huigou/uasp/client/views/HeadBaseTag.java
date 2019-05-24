package com.huigou.uasp.client.views;

import javax.servlet.jsp.PageContext;

import com.huigou.util.Constants;

public class HeadBaseTag extends AbstractTag {

    private static final long serialVersionUID = 5070404679322295999L;

    private String include;

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public HeadBaseTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "headBase";
    }

    /**
     * 传入的include 如 a,b,c,d 拆分后在 模板中处理
     */
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != include) {
            String[] a = include.split(",");
            for (String s : a) {
                // 加入重复引用过滤
                if (pageContext.getAttribute(String.format("headBase_%s", s), PageContext.REQUEST_SCOPE) == null) {
                    pageContext.setAttribute(String.format("headBase_%s", s), true, PageContext.REQUEST_SCOPE);
                    addParameter(s, true);
                }
            }
        }
        addParameter("webApp", Constants.WEB_APP);
    }

}
