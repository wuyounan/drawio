package com.huigou.uasp.bpm.event;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;

public abstract class ProcessEventBase implements ProcessEventListener {

    @Override
    public void notify(ProcessEvent processEvent) {
        Assert.notNull(processEvent, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "processEvent"));
        switch (processEvent.getType()) {
        case ProcessEvent.AFTER_STARTUP_PROCESS_EVENT:
            afterStartupProcess();
            break;
        case ProcessEvent.AFTER_COMPLETE_PROCESS_TASK_EVENT:
            afterCompleteProcessTask();
            break;
        case ProcessEvent.AFTER_COMPLETE_NOT_PROCESS_TASK_EVENT:
            afterCompleteNotProcessTask();
            break;
        case ProcessEvent.AFTER_DELETE_NOT_PROCESS_TASK_EVENT:
            afterDeleteNotProcessTask();
            break;
        case ProcessEvent.AFTER_SUSPEND_TASK_EVENT:
            afterSuspendTask();
            break;
        case ProcessEvent.AFTER_RECOVER_TASK_EVENT:
            afterRecoverTask();
            break;
        case ProcessEvent.AFTER_SLEEP_TASK_EVENT:
            afterSleepTask();
            break;
        case ProcessEvent.AFTER_ABORT_TASK:
            this.afterAbortTask();
            break;
        case ProcessEvent.AFTER_ABORT_PROCESS_INSTANCE_EVENT:
            afterAbortProcessInstance();
            break;
        case ProcessEvent.AFTER_RECALL_PROCESS_INSTANCE_EVENT:
            afterRecallProcessInstance();
            break;
        case ProcessEvent.AFTER_BACK_EVENT:
            afterBack();
            break;
        case ProcessEvent.AFTER_WITHDRAW_TASK_EVENT:
            afterWithdrawTask();
            break;
        case ProcessEvent.AFTER_TRANSMIT_EVENT:
            afterTransmit();
            break;
        case ProcessEvent.AFTER_CLAIM_EVENT:
            afterClaim();
            break;
        case ProcessEvent.AFTER_MAKEACOPYFOR_EVENT:
            afterMakeACopyFor();
            break;
        case ProcessEvent.AFTER_CREATE_NOTICE_TASK_EVENT:
            afterCreateNoticeTask();
            break;
        case ProcessEvent.AFTER_CREATE_COORDINATION_TASK_EVENT:
            afterCreateCoordinationTask();
            break;
        case ProcessEvent.AFTER_ASSIST_EVENT:
            afterAssist();
            break;
        case ProcessEvent.AFTER_UPDATE_HISTORIC_TASK_INSTANCE_STATUS_EVENT:
            afterUpdateHistoricTaskInstanceStatus();
            break;
        case ProcessEvent.AFTER_UPDATE_TASK_EXTENSION_STATUS_EVENT:
            afterUpdateTaskExtensionStatus();
            break;
        case ProcessEvent.AFTER_LAUNCH_MENDTASK_EVENT:
            afterLaunchMendTask();
            break;
        case ProcessEvent.AFTER_REPLENISH_EVENT:
            afterReplenish();
            break;
        case ProcessEvent.AFTER_UPDATE_TASK_HANDLER:
            afterUpdateTaskHandler();
            break;
        case ProcessEvent.AFTER_HAND_TASKS_EVENT:
            afterHandTasks();
            break;
        }
    }

    protected void afterStartupProcess() {
    }

    protected void afterCompleteProcessTask() {
    }

    protected void afterCompleteNotProcessTask() {
    }

    protected void afterDeleteNotProcessTask() {
    }

    protected void afterSuspendTask() {
    }

    protected void afterRecoverTask() {
    }

    protected void afterSleepTask() {
    }

    protected void afterAbortTask() {
    }

    protected void afterAbortProcessInstance() {
    }

    protected void afterRecallProcessInstance() {
    }

    protected void afterBack() {
    }

    protected void afterWithdrawTask() {
    }

    protected void afterTransmit() {
    }

    protected void afterClaim() {
    }

    protected void afterMakeACopyFor() {
    }

    protected void afterCreateNoticeTask() {
    }

    protected void afterCreateCoordinationTask() {
    }

    protected void afterAssist() {
    }

    protected void afterUpdateHistoricTaskInstanceStatus() {
    }

    protected void afterUpdateTaskExtensionStatus() {
    }

    protected void afterLaunchMendTask() {
    }

    protected void afterReplenish() {
    }

    protected void afterUpdateTaskHandler() {
    }

    protected void afterHandTasks() {
        // not implement
    }

}
