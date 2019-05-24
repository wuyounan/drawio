package com.huigou.uasp.bpm.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.uasp.bpm.MessageSenderManager;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;

public class LogProcessEvent extends ProcessEventBase {

    private static final Logger logger = LoggerFactory.getLogger(LogProcessEvent.class);

    @Autowired
    private MessageSenderManager messageSenderManager;

    @Autowired
    private ProcUnitHandlerApplication procUnitHandlerApplication;


    private void printProcessInstance() {

    }

    private void printNewProcessTasks() {
        List<ProcessEventTaskInstance> tasks = ProcessEventContext.getNewProcessTasks();
        for (ProcessEventTaskInstance item : tasks) {
            logger.info(item.toString());
        }
    }

    private void printNewNotProcessTasks() {
        List<ProcessEventTaskInstance> tasks = ProcessEventContext.getNewNotProcessTasks();
        for (ProcessEventTaskInstance item : tasks) {
            logger.info(item.toString());
        }
    }

    private void printCompletedTasks() {
        List<ProcessEventTaskInstance> tasks = ProcessEventContext.getCompletedTasks();
       // String assistantProcUnitHandlerId;
        for (ProcessEventTaskInstance item : tasks) {
            /*
             * if (CooperationModelKind.ASSISTANT.equals(item.getCooperationModelId())){
             * assistantProcUnitHandlerId = item.getProcUnitHandlerId();
             * ProcUnitHandler chiefProcUnitHandler = procUnitHandlerApplication.queryChiefHandler(assistantProcUnitHandlerId);
             * List<ProcessEventTaskInstance> historicTaskInstanceExtensions = actApplication.queryHiTaskInstExtensionByProcUnitHandlerId(chiefProcUnitHandler.getId());
             * //actApplication.query
             * MessageSendModel messageSendModel = new MessageSendModel(item);
             * messageSendModel.setSenderId(item.getExecutorPersonMemberId());
             * messageSendModel.setReceiverId(chiefProcUnitHandler.getHandlerId());
             * messageSenderManager.send(messageSendModel);
             * }
             */
            logger.info(item.toString());
        }
    }

    private void printUpdatedStatusTasks() {
        List<ProcessEventTaskInstance> tasks = ProcessEventContext.getUpdatedStatusTasks();
        for (ProcessEventTaskInstance item : tasks) {
            logger.info(item.toString());
        }
    }

    private void printDeletedTasks() {
        List<ProcessEventTaskInstance> tasks = ProcessEventContext.getDeletedTasks();
        for (ProcessEventTaskInstance item : tasks) {
            logger.error(item.toString());
        }
    }

    public void afterStartupProcess() {
        this.printProcessInstance();
        this.printNewProcessTasks();
    }

    public void afterCompleteProcessTask() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks(); // 可能有抄送、协同任务、通知

    }

    public void afterCompleteNotProcessTask() {
        this.printCompletedTasks();
    }

    public void afterDeleteNotProcessTask() {
        this.printDeletedTasks();
    }

    public void afterSuspendTask() {
        this.printUpdatedStatusTasks();
    }

    public void afterRecoverTask() {
        this.printUpdatedStatusTasks();
    }

    public void afterSleepTask() {
        this.printUpdatedStatusTasks();
    }

    public void afterAbortTask() {
        this.printCompletedTasks();
    }

    public void afterAbortProcessInstance() {
        this.printProcessInstance();
        this.printDeletedTasks();
    }

    public void afterRecallProcessInstance() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
    }

    public void afterBack() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
    }

    public void afterWithdrawTask() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
    }

    public void afterTransmit() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
        // 协同任务
    }

    public void afterClaim() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
    }

    public void afterMakeACopyFor() {
        this.printNewNotProcessTasks();
    }

    public void afterCreateNoticeTask() {
        this.printNewNotProcessTasks();
    }

    public void afterCreateCoordinationTask() {
        this.printNewNotProcessTasks();
    }

    public void afterAssist() {
        this.printProcessInstance();
        this.printCompletedTasks();
        this.printNewProcessTasks();
    }

    public void afterUpdateHistoricTaskInstanceStatus() {
        this.printUpdatedStatusTasks();
    }

    public void afterUpdateTaskExtensionStatus() {
        this.printUpdatedStatusTasks();
    }

    public void afterLaunchMendTask() {
        this.printNewNotProcessTasks();
    }

    public void afterReplenish() {
        this.printCompletedTasks();
        this.printNewProcessTasks();
        this.printNewNotProcessTasks();
    }

    public void afterHandTasks() {
        // not implement
    }

}
