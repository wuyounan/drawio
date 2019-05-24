package com.huigou.uasp.bmp.fn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.datamanagement.DataManageResource;
import com.huigou.express.VariableContainer;
import com.huigou.uasp.annotation.Expression;
import com.huigou.uasp.bmp.fn.AbstractDaoFunction;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.util.StringUtil;

/**
 * 数据管理权限函数
 * 
 * @author xiexin
 */
@Expression
@Service("dataManagementFun")
public class DataManagementFun extends AbstractDaoFunction {

    @Autowired
    protected OrgApplicationProxy orgApplication;

    /**
     * 创建数据权限对象
     * 
     * @param id
     * @return
     */
    private DataManageResource createDataManageResource(String id) {
        Org org = orgApplication.loadOrg(id);
        DataManageResource data = new DataManageResource();
        data.setKey(org.getId());
        data.setValue(org.getName());
        data.setFullId(org.getFullId());
        data.setFullName(org.getFullName());
        return data;
    }

    private Org getOrg() {
        String personId = VariableContainer.getVariable("personId", String.class);
        if (StringUtil.isBlank(personId)) {
            return null;
        }
        String key = String.format("currentOrg_%s", personId);
        Org org = ThreadLocalUtil.getVariable(key, Org.class);
        if (org == null) {
            org = orgApplication.loadMainOrgByPersonId(personId);
            if (org != null) {
                ThreadLocalUtil.putVariable(key, org);
            }
        }
        return org;
    }

    /**
     * 当前机构
     * 
     * @return
     */
    public DataManageResource currentOrgData() {
        Org org = this.getOrg();
        if (org == null) {
            return null;
        }
        return createDataManageResource(org.getOrgId());
    }

    /**
     * 当前部门
     * 
     * @return
     */
    public DataManageResource currentDeptData() {
        Org org = this.getOrg();
        if (org == null) {
            return null;
        }
        return createDataManageResource(org.getDeptId());
    }

}
