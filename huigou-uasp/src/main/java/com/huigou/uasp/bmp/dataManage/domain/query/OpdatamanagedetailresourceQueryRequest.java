package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 数据管理权限包含维度资源
 * 
 * @author xx
 *         SA_OPDATAMANAGDETAILRESOURCE
 * @date 2018-09-05 17:15
 */
public class OpdatamanagedetailresourceQueryRequest extends QueryAbstractRequest {

    /**
     * 数据取值定义ID
     **/
    protected String dataManagedetalId;

    public String getDataManagedetalId() {
        return dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

}
