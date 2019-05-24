package com.huigou.uasp.bpm.event;

public class ProcessEvent {

    /**
     * 启动流程后事件
     */
    public static final String AFTER_STARTUP_PROCESS_EVENT = "afterStartupProcess";

    /**
     * 完成流程任务后事件
     */
    public static final String AFTER_COMPLETE_PROCESS_TASK_EVENT = "afterCompleteProcessTask";

    /**
     * 完成非流程任务后事件
     */
    public static final String AFTER_COMPLETE_NOT_PROCESS_TASK_EVENT = "afterCompleteNotProcessTask";

    /**
     * 删除非流程任务后事件
     */
    public static final String AFTER_DELETE_NOT_PROCESS_TASK_EVENT = "afterDeleteNotProcessTask";

    /**
     * 暂停任务后事件
     */
    public static final String AFTER_SUSPEND_TASK_EVENT = "afterSuspendTask";

    /**
     * 
     */
    public static final String AFTER_RECOVER_TASK_EVENT = "afterRecoverTask";

    public static final String AFTER_SLEEP_TASK_EVENT = "afterSleepTask";

    public static final String AFTER_ABORT_TASK = "abortTask";

    public static final String AFTER_ABORT_PROCESS_INSTANCE_EVENT = "afterAbortProcessInstance";

    public static final String AFTER_RECALL_PROCESS_INSTANCE_EVENT = "afterRecallProcessInstance";

    public static final String AFTER_BACK_EVENT = "afterBack";

    public static final String AFTER_WITHDRAW_TASK_EVENT = "afterWithdrawTask";

    public static final String AFTER_TRANSMIT_EVENT = "afterTransmit";

    public static final String AFTER_CLAIM_EVENT = "afterClaim";

    public static final String AFTER_MAKEACOPYFOR_EVENT = "afterMakeACopyFor";

    public static final String AFTER_CREATE_NOTICE_TASK_EVENT = "afterCreateNoticeTask";

    public static final String AFTER_CREATE_COORDINATION_TASK_EVENT = "afterCreateCoordinationTask";

    public static final String AFTER_ASSIST_EVENT = "afterAssist";

    public static final String AFTER_UPDATE_HISTORIC_TASK_INSTANCE_STATUS_EVENT = "afterUpdateHistoricTaskInstanceStatus";

    public static final String AFTER_UPDATE_TASK_EXTENSION_STATUS_EVENT = "afterUpdateTaskExtensionStatus";

    public static final String AFTER_LAUNCH_MENDTASK_EVENT = "afterLaunchMendTask";

    public static final String AFTER_REPLENISH_EVENT = "afterReplenish";
    
    public static final String AFTER_UPDATE_TASK_HANDLER = "afterUpdateTaskHandler";

    public static final String AFTER_HAND_TASKS_EVENT = "afterHandTasks";

    private final String type;

    private final Object data;

    public ProcessEvent(String type, Object data) {
        super();
        this.type = type;
        this.data = data;
    }

    public ProcessEvent(String type) {
        this(type, null);
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

}
