package com.huigou.uasp.bpm.cmd;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.huigou.context.OrgUnit;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bpm.TaskKind;
import com.huigou.uasp.bpm.TaskScope;
import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;
import com.huigou.uasp.bpm.event.ProcessEventContext;

/**
 * 通知任务命令
 * 
 * @author gongmm
 */
public class NotifyTaskCmd implements Command<Void> {

    private String DEFAULT_FORM_KEY = "workflowController/showTaskDetail.do";

    private String name;

    private String description;

    private String formKey;

    private String businessKey;

    private List<String> receivers;

    private ActApplication actApplication;

    private TaskService getTaskService() {
        return Context.getProcessEngineConfiguration().getTaskService();
    }

    public NotifyTaskCmd(String name, String description, String formKey, String businessKey, List<String> receivers, ActApplication actApplication) {
        this.name = name;
        this.description = description;
        this.formKey = formKey;
        this.businessKey = businessKey;

        this.receivers = receivers;
        this.actApplication = actApplication;
    }

    public NotifyTaskCmd(String name, String description, List<String> receivers, ActApplication actApplication) {
        this.name = name;
        this.description = description;

        this.formKey = DEFAULT_FORM_KEY;

        this.receivers = receivers;
        this.actApplication = actApplication;
    }

    @SuppressWarnings("unchecked")
    public Void execute(CommandContext commandContext) {
        for (String receiver : receivers) {
            try {
                List<OrgUnit> orgUnits = (List<OrgUnit>) ExpressManager.evaluate("findPersonMembersInOrg('" + receiver + "', true)");

                for (OrgUnit orgUnit : orgUnits) {
                    TaskEntity task = (TaskEntity) getTaskService().newTask();

                    task.setNameWithoutCascade(name);
                    task.setDescriptionWithoutCascade(description);
                    task.setPriorityWithoutCascade(50);
                    task.setOwnerWithoutCascade(orgUnit.getFullId());
                    task.setAssigneeWithoutCascade(orgUnit.getFullId());

                    getTaskService().saveTask(task);

                    RuntimeTaskExtension runtimeTaskExtension = new RuntimeTaskExtension(task.getId(), TaskScope.TASK, TaskKind.NOTICE);

                    runtimeTaskExtension.setBusinessKey(businessKey);
                    runtimeTaskExtension.setExecutorUrl(formKey);

                    runtimeTaskExtension.setExecutorFullId(orgUnit.getFullId());
                    runtimeTaskExtension.setExecutorFullName(orgUnit.getFullName());

                    actApplication.saveTaskExtension(runtimeTaskExtension);

                    ProcessEventContext.addNewNotProcessTask(task.getId());
                }
            } catch (Exception e) {
                throw new ApplicationException(e.getMessage());
            }
        }
        return null;
    }
}
