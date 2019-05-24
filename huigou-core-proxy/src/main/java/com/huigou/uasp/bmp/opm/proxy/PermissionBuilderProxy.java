package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.data.query.QueryPermissionBuilder;
import com.huigou.uasp.bmp.opm.application.PermissionBuilder;
import com.huigou.uasp.bmp.opm.impl.PermissionBuilderImpl;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;

@Service("permissionBuilderProxy")
public class PermissionBuilderProxy {

    @Autowired
    private WorkflowApplication workflowApplication;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    @Autowired
    private QueryPermissionBuilder queryPermissionBuilder;

    private PermissionBuilder permissionBuilder;

    void initProperties(PermissionBuilderImpl permissionBuilderImpl) {
        permissionBuilderImpl.setQueryPermissionBuilder(queryPermissionBuilder);
        permissionBuilderImpl.setWorkflowApplication(workflowApplication);
    }

    public PermissionBuilder getPermissionBuilder() {
        if (permissionBuilder == null) {
            // synchronized (PermissionBuilderProxy.class) {
            if (permissionBuilder == null) {
                PermissionBuilderImpl permissionBuilderImpl = coreApplicationFactory.getPermissionBuilder();
                permissionBuilder = permissionBuilderImpl;
            }
            // }
        }
        return permissionBuilder;
    }

    public List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId) {
        return getPermissionBuilder().queryUIElementPermissionsByFunction(function, personId, isId);
    }

    public List<Map<String, Object>> queryUIElementPermissionsByTaskId(String taskId) {
        TaskDetail taskDetail = workflowApplication.queryTaskDetail(taskId);
        if (taskDetail == null) {
            return null;
        }
        return this.queryUIElementPermissionsByProcUnitHandlerId(taskDetail.getProcUnitHandlerId());
    }

    public List<Map<String, Object>> queryUIElementPermissionsByProcUnitHandlerId(String procUnitHandlerId) {
        return getPermissionBuilder().queryUIElementPermissionsByProcUnitHandlerId(procUnitHandlerId);
    }

    public void removeCache() {
        getPermissionBuilder().removeCache();
    }

    public void removeCacheByKind(String kind) {
        getPermissionBuilder().removeCacheByKind(kind);
    }

}
