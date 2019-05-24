package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 数据管理权限取值定义
 * 
 * @author xx
 *         SA_OPDATAMANAGDETAIL
 * @date 2018-09-05 17:15
 */
public class OpdatamanagedetailQueryRequest extends QueryAbstractRequest {

    /**
     * 数据管理权限类别
     **/
    protected String dataManageId;

    /**
     * 名称
     **/
    protected String name;

    protected String code;

    protected String fullId;

    public String getDataManageId() {
        return this.dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

}
