package com.huigou.uasp.bpm.managment.application.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.OrgUnit;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.uasp.bpm.event.ProcessEvent;
import com.huigou.uasp.bpm.event.ProcessEventContext;
import com.huigou.uasp.bpm.event.ProcessEventSupport;
import com.huigou.uasp.bpm.managment.application.ProcessInstanceOperationApplication;

@Service("processInstanceOperationApplication")
public class ProcessInstanceOperationApplicationImpl implements ProcessInstanceOperationApplication {

    @Resource(name = "sqlQuery")
    private SQLQuery sqlQuery;

    @Autowired
    private ActApplication actApplication;

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private ProcUnitHandlerApplication procUnitHandlerApplication;

    @Autowired(required = false)
    private ProcessEventSupport processEventSupport;

    @Override
    @Transactional
    public void updateTaskHandler(String taskId, String personMemberId) {
        // 参数验证
        Assert.hasText(taskId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "taskId"));
        Assert.hasText(personMemberId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "personMemberId"));

        // 1、处理人有效性检查
        Org org = this.orgApplication.loadEabledOrg(personMemberId);

        OrgUnit orgUnit = new OrgUnit(org.getFullId(), org.getFullName());
        this.actApplication.updateTaskHanlder(taskId, orgUnit, false);

        HistoricTaskInstanceExtension htie = this.actApplication.loadHistoricTaskInstanceExtension(taskId);

        if (!ActivityKind.isApplyActivity(htie.getTaskDefinitionKey())) {
            this.procUnitHandlerApplication.updateProcUnitHandlerOrgData(htie.getProcUnitHandlerId(), orgUnit);
        }

        if (processEventSupport != null) {
            ProcessEventContext.addUpdatedHandlerTask(taskId);
            ProcessEvent processEvent = new ProcessEvent(ProcessEvent.AFTER_UPDATE_TASK_HANDLER);
            processEventSupport.fireProcessEvent(processEvent);
        }
    }

}
