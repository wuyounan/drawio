package com.huigou.uasp.bmp.configuration.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 数据库数据国际化支持
 * 
 * @author xx
 *         SA_I18NPROPERTIES
 * @date 2017-09-29 10:23
 */
public class I18npropertiesQueryRequest extends QueryAbstractRequest {

    /**
     * i18n 编码
     **/
    protected String code;

    protected String name;

    /**
     * 类别
     **/
    protected String folderId;

    protected String resourceKind;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getResourceKind() {
        return resourceKind;
    }

    public void setResourceKind(String resourceKind) {
        this.resourceKind = resourceKind;
    }

}
