package com.huigou.uasp.bmp.opm.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.huigou.cache.service.ICache;
import com.huigou.context.OrgUnit;
import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.data.query.model.SQLModel;
import com.huigou.uasp.bmp.opm.application.AccessApplication;
import com.huigou.uasp.bmp.opm.application.PermissionBuilder;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.util.Constants;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

public class PermissionBuilderImpl implements PermissionBuilder {

    private ICache icache;

    private AccessApplication accessApplication;

    private QueryPermissionBuilder queryPermissionBuilder;

    private WorkflowApplication workflowApplication;

    public void setIcache(ICache icache) {
        this.icache = icache;
    }

    public void setAccessApplication(AccessApplication accessApplication) {
        this.accessApplication = accessApplication;
    }

    public void setQueryPermissionBuilder(QueryPermissionBuilder queryPermissionBuilder) {
        this.queryPermissionBuilder = queryPermissionBuilder;
    }

    public void setWorkflowApplication(WorkflowApplication workflowApplication) {
        this.workflowApplication = workflowApplication;
    }

    @Override
    public List<OrgUnit> findSubordinations(String personId, List<String> fullIds, String manageType) {
        return queryPermissionBuilder.findSubordinations(personId, fullIds, manageType);
    }

    @Override
    public SQLModel applyManagementPermission(String sql, String manageType) {
        return queryPermissionBuilder.applyManagementPermission(sql, manageType);
    }

    @Override
    public SQLModel applyManagementPermissionForTree(String sql, String manageType) {
        return queryPermissionBuilder.applyManagementPermissionForTree(sql, manageType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId) {
        if (personId == null) {
            return null;
        }
        String cacheKey = Constants.PERMISSION_FIELD_BY_FUNCTION + "|" + function + "|" + personId + "|" + String.valueOf(isId);
        Object element = null;
        List<Map<String, Object>> list = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                list = accessApplication.queryUIElementPermissionsByFunction(function, personId, isId);
                icache.put(cacheKey, (Serializable) list);
            } else {
                list = (List<Map<String, Object>>) element;
            }
            return list;
        } catch (Exception e) {
            LogHome.getLog(this).error("获取数据权限", e);
        }
        return null;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<Map<String, Object>> queryUIElementPermissionsByProcUnitHandlerId(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        String cacheKey = Constants.PERMISSION_FIELD_BY_PROCUNITHANDLERID + "|" + id;
        Object element = null;
        List<Map<String, Object>> list = null;
        try {
            element = icache.get(cacheKey);
            if (element == null) {
                list = workflowApplication.queryUIElmentPermissionsByProcUnitHandlerId(id);
                icache.put(cacheKey, (Serializable) list);
            } else {
                list = (List<Map<String, Object>>) element;
            }
            return list;
        } catch (Exception e) {
            LogHome.getLog(this).error("获取数据权限", e);
        }
        return null;
    }

    @Override
    public boolean hasManagementPermission(String manageType, String fullId) {
        return queryPermissionBuilder.hasManagementPermission(manageType, fullId);
    }

    @Override
    public void removeCacheByKind(String kind) {
        queryPermissionBuilder.removeCacheByKind(kind);
    }

    @Override
    public void removeCache() {
        queryPermissionBuilder.removeCache();
    }
}
