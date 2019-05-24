package com.huigou.uasp.bmp.operator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.uasp.bmp.operator.OperatorUIElementPermissionBuilder;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.query.TaskDetail;
import com.huigou.util.StringUtil;

/**
 * 界面元素权限查询
 * 
 * @author xx
 */

@Service("operatorUIElementPermissionBuilder")
public class OperatorUIElementPermissionBuilderImp implements OperatorUIElementPermissionBuilder {

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Resource
    protected WorkflowApplication workflowApplication;

    @Override
    public List<Map<String, Object>> queryUIElementPermissionsByFunction(String function, String personId, boolean isId) {
        if (personId == null) {
            return new ArrayList<>();
        }
        return accessApplication.queryUIElementPermissionsByFunction(function, personId, isId);
    }

    @Override
    public List<Map<String, Object>> queryUIElementPermissionsByProcUnitHandlerId(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }
        return workflowApplication.queryUIElmentPermissionsByProcUnitHandlerId(id);
    }

    @Override
    public List<Map<String, Object>> queryUIElementPermissionsByTaskId(String taskId) {
        TaskDetail taskDetail = workflowApplication.queryTaskDetail(taskId);
        if (taskDetail == null) {
            return null;
        }
        return this.queryUIElementPermissionsByProcUnitHandlerId(taskDetail.getProcUnitHandlerId());
    }

}
