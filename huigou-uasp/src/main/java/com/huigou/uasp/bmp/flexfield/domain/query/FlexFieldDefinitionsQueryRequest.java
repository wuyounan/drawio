package com.huigou.uasp.bmp.flexfield.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 分页查询弹性域定义
 * 
 * @author Administrator
 */

public class FlexFieldDefinitionsQueryRequest extends QueryAbstractRequest {
    private String folderId;

    private String fieldName;

    private String description;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
