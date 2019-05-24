package com.huigou.uasp.client.views;

public class TitleTag extends AbstractTag {

    private static final long serialVersionUID = -6463922199883244761L;

    protected String hideTable;

    protected String needLine;

    protected String hideIndex;

    protected String isHide;

    public String getHideTable() {
        return hideTable;
    }

    public void setHideTable(String hideTable) {
        this.hideTable = hideTable;
    }

    public String getNeedLine() {
        return needLine;
    }

    public void setNeedLine(String needLine) {
        this.needLine = needLine;
    }

    public String getHideIndex() {
        return hideIndex;
    }

    public void setHideIndex(String hideIndex) {
        this.hideIndex = hideIndex;
    }

    public String getIsHide() {
        return isHide;
    }

    public void setIsHide(String isHide) {
        this.isHide = isHide;
    }

    public TitleTag() {
        super();
    }

    protected String getDefaultTemplate() {
        return "title";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != hideTable) {
            addParameter("hideTable", hideTable);
        }
        if (null != needLine) {
            addParameter("needLine", this.isTrue(needLine));
        } else {
            addParameter("needLine", true);
        }
        if (null != hideIndex) {
            addParameter("hideIndex", hideIndex);
        }
        if (null != isHide) {
            addParameter("hideIcon", isHide.equals("true") ? "fa-angle-double-up" : "fa-angle-double-down");
        }
    }

}
