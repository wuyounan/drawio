package com.huigou.uasp.bpm.thread;

import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.bpm.engine.domain.model.CoordinationTask;

/**
 * 发送非流程任务线程
 * 
 * @author xiexin
 */
public class CoordinationtaskSenderThread implements Runnable {

    private WorkflowApplication workflowApplication;

    private CoordinationTask coordinationTask;

    public CoordinationtaskSenderThread(WorkflowApplication workflowApplication, CoordinationTask coordinationTask) {
        this.workflowApplication = workflowApplication;
        this.coordinationTask = coordinationTask;
    }

    @Override
    public void run() {
        try {
            this.workflowApplication.createCoordinationTask(this.coordinationTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
