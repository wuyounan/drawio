package com.huigou.data.query.parser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.huigou.data.datamanagement.DataManageResourceGroup;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 权限条件分组模型
 * 
 * @author xx
 */
public class PermissionGroup implements Serializable {

    private static final long serialVersionUID = -9063719550114473272L;

    private String businessCode;

    private String manageCode;

    private String label;

    private String operator;

    private List<PermissionModel> permissionModels;

    private List<PermissionGroup> permissionGroups;

    private DataManageResourceGroup resourceGroup;

    public PermissionGroup() {
        permissionModels = new ArrayList<PermissionModel>();
        permissionGroups = new ArrayList<PermissionGroup>();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperator() {
        if (StringUtil.isBlank(operator)) {
            if (StringUtil.isNotBlank(manageCode)) {
                return "and";// 存在数据管理权限编码组合为and
            } else {
                return "or";
            }
        }
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<PermissionModel> getPermissionModels() {
        return permissionModels;//ListUtil.distinct(permissionModels);
    }

    public void setPermissionModels(List<PermissionModel> permissionModels) {
        this.permissionModels = permissionModels;
    }

    public void addPermissionModel(PermissionModel permissionModel) {
        this.permissionModels.add(permissionModel);
    }

    public void addPermissionModels(List<PermissionModel> permissionModels) {
        if (permissionModels != null && permissionModels.size() > 0) {
            this.permissionModels.addAll(permissionModels);
        }
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public void setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }

    public void addPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroups.add(permissionGroup);
    }

    public DataManageResourceGroup getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(DataManageResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    /**
     * 数据权限分组定义是否存在数据
     * 
     * @return
     */
    public boolean isNoPermissionGroup() {
        if (StringUtil.isNotBlank(manageCode)) {
            if (resourceGroup == null) {
                return true;
            }
        }
        return false;
    }

    public static PermissionGroup newInstance(Object obj) {
        PermissionGroup model = new PermissionGroup();
        ClassHelper.copyProperties(obj, model);
        return model;
    }

}
