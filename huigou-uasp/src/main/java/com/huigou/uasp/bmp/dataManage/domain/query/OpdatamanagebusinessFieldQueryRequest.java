package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.ParentIdQueryRequest;

/**
 * 数据管理权限业务过滤字段定义
 * 
 * @author xx
 *         SA_OPDATAMANAGEBUSINESS_FIELD
 * @date 2018-09-27 12:04
 */
public class OpdatamanagebusinessFieldQueryRequest extends ParentIdQueryRequest {

    protected Integer isOrgCondition;

    public Integer getIsOrgCondition() {
        return isOrgCondition;
    }

    public void setIsOrgCondition(Integer isOrgCondition) {
        this.isOrgCondition = isOrgCondition;
    }
}