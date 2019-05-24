package com.huigou.data.datamanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huigou.data.query.parser.model.PermissionModel;
import com.huigou.util.StringUtil;

/**
 * 数据管理权限包含维度资源
 * 
 * @author xx
 */
public class DataManageFieldsGroup implements Serializable {

    private static final long serialVersionUID = 5296602818789549657L;

    private String businessCode;

    private String manageCode;

    private List<PermissionModel> dataModels;

    private List<PermissionModel> orgModels;

    public DataManageFieldsGroup() {
        dataModels = new ArrayList<PermissionModel>();
        orgModels = new ArrayList<PermissionModel>();
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getManageCode() {
        return manageCode;
    }

    public void setManageCode(String manageCode) {
        this.manageCode = manageCode;
    }

    public List<PermissionModel> getDataModels() {
        return dataModels;
    }

    public void addDataModels(PermissionModel model) {
        this.dataModels.add(model);
    }

    public List<PermissionModel> getOrgModels() {
        return orgModels;
    }

    public void addOrgModels(PermissionModel model) {
        this.orgModels.add(model);
    }

    public boolean hasOrgModels() {
        return this.orgModels.size() > 0;
    }

    public boolean hasDataModels() {
        if (StringUtil.isBlank(manageCode)) {
            return false;
        }
        return this.dataModels.size() > 0;
    }

}
