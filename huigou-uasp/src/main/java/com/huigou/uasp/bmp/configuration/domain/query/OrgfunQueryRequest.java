package com.huigou.uasp.bmp.configuration.domain.query;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;

/**
 * 系统可用组织机构函数
 * 
 * @author xx
 *         SA_ORGFUN
 * @date 2018-03-09 10:47
 */
public class OrgfunQueryRequest extends CodeAndNameQueryRequest {

    /**
     * parent_id
     **/
    protected String parentId;

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
