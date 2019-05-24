package com.huigou.data.domain.query;

/**
 * 查询通用模型
 * 
 * @author Administrator
 */

public class ParentAndCodeAndNameQueryRequest extends CodeAndNameQueryRequest {

    protected String parentId;

    protected String isFullLike;

    public String getParentId() {
        if (isFullLike != null && isFullLike.equals("1")) {
            return null;
        }
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsFullLike() {
        return isFullLike;
    }

    public void setIsFullLike(String isFullLike) {
        this.isFullLike = isFullLike;
    }

}
