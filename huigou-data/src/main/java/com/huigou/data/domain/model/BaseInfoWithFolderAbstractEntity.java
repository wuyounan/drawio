package com.huigou.data.domain.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseInfoWithFolderAbstractEntity extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 6512870568162122262L;

    /**
     * 文件夹ID
     */
    @Column(name = "folder_id")
    private String folderId;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}
