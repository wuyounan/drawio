package com.huigou.uasp.bmp.dataManage.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 数据管理权限授权
 * 
 * @author xx
 *         SA_OPDATAMANAGEMENT
 * @date 2018-09-05 17:15
 */
public class OpdatamanagementQueryRequest extends QueryAbstractRequest {

    /**
     * 数据管理权限类别
     **/
    protected String dataManageId;

    /**
     * 数据取值定义ID
     **/
    protected String dataManagedetalId;

    /**
     * 管理者ID
     **/
    protected String managerId;

    protected String code;

    protected String name;

    protected String fullId;

    protected String orgFullId;

    protected String orgName;

    protected String resourceValue;

    public String getDataManageId() {
        return this.dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getDataManagedetalId() {
        return this.dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

    public String getManagerId() {
        return this.managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getCode() {
        return code;
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

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getOrgFullId() {
        return orgFullId;
    }

    public void setOrgFullId(String orgFullId) {
        this.orgFullId = orgFullId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getResourceValue() {
        return resourceValue;
    }

    public void setResourceValue(String resourceValue) {
        this.resourceValue = resourceValue;
    }

}
