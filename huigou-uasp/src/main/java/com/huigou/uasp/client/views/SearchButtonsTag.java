package com.huigou.uasp.client.views;

public class SearchButtonsTag extends AbstractTag {

    private static final long serialVersionUID = -1584163120230030634L;

    private String col;

    private String queryType;

    private String resetType;

    public SearchButtonsTag() {
        super();
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }

    protected String getDefaultTemplate() {
        return "searchButtons";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        if (null != queryType) {
            addParameter("queryType", queryType);
        }
        if (null != resetType) {
            addParameter("resetType", resetType);
        }
        if (null != col) {
            addParameter("col", col);
        }
    }

}
