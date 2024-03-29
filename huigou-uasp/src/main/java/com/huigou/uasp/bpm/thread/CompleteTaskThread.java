package com.huigou.uasp.bpm.thread;

import org.springframework.util.Assert;

import com.huigou.uasp.bpm.engine.application.WorkflowApplication;

public class CompleteTaskThread implements Runnable {

    protected WorkflowApplication workflowApplication;

    private String taskId;

    public CompleteTaskThread(WorkflowApplication workflowApplication, String taskId) {
        Assert.hasText(taskId);
        Assert.notNull(workflowApplication);
        this.workflowApplication = workflowApplication;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        workflowApplication.completeTask(taskId);
    }
}
