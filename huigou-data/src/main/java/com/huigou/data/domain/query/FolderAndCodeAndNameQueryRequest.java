package com.huigou.data.domain.query;

/**
 * 查询通用模型
 * 
 * @author Administrator
 */

public class FolderAndCodeAndNameQueryRequest extends CodeAndNameQueryRequest {

    protected String folderId;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}
