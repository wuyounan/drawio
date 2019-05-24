package com.huigou.uasp.bpm.event;

import com.huigou.uasp.bpm.engine.application.ActApplication;
import com.huigou.uasp.bpm.engine.domain.model.HistoricProcessInstanceExtension;
import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 流程事件任务模型
 * 
 * @author xx
 */
public class ProcessEventTaskInstance extends HistoricTaskInstanceExtension {
    private static final long serialVersionUID = -2254741687837858024L;

    private HistoricProcessInstanceExtension processInstanceExtension;

    public void setProcessInstanceExtension(HistoricProcessInstanceExtension processInstanceExtension) {
        this.processInstanceExtension = processInstanceExtension;
    }

    public HistoricProcessInstanceExtension getProcessInstanceExtension() {
        return processInstanceExtension;
    }

    public static ProcessEventTaskInstance newInstance(String taskId) {
        ActApplication actApplication = ApplicationContextWrapper.getBean("actApplication", ActApplication.class);
        HistoricTaskInstanceExtension historicTaskInstanceExtension = null;
        if (StringUtil.isNotBlank(taskId)) {
            historicTaskInstanceExtension = actApplication.loadHistoricTaskInstanceExtension(taskId);
            if (historicTaskInstanceExtension == null) {
                return null;
            }
        } else {
            return null;
        }
        ProcessEventTaskInstance taskInstance = new ProcessEventTaskInstance();
        ClassHelper.copyProperties(historicTaskInstanceExtension, taskInstance);
        String processInstanceId = taskInstance.getProcessInstanceId();
        if (StringUtil.isNotBlank(processInstanceId)) {
            String catalogId = StringUtil.tryThese(taskInstance.getCatalogId(), "task");
            if (catalogId.equalsIgnoreCase("process")) {
                HistoricProcessInstanceExtension historicProcessInstanceExtension = ProcessEventContext.get(processInstanceId,
                                                                                                            HistoricProcessInstanceExtension.class);
                if (historicProcessInstanceExtension == null) {
                    historicProcessInstanceExtension = actApplication.loadHistoricProcessInstanceExtension(processInstanceId);
                    ProcessEventContext.put(processInstanceId, historicProcessInstanceExtension);
                }
                taskInstance.setProcessInstanceExtension(historicProcessInstanceExtension);
            }
        }
        return taskInstance;
    }

}
