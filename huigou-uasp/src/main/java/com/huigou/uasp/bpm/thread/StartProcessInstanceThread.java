package com.huigou.uasp.bpm.thread;

import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 通过线程启动流程；
 * 
 * @author xx
 */
public class StartProcessInstanceThread implements Runnable {

    protected WorkflowApplication workflowApplication;

    /**
     * 流程文件名称
     */
    private String processDefinitionKey;

    /**
     * 流程主键ID
     */
    private String bizId;

    /**
     * 线程局部变量
     */
    private SDO localSdo;

    /**
     * 业务参数
     */
    private Map<String, Object> variable;

    private Operator operator;

    public StartProcessInstanceThread(WorkflowApplication workflowApplication, String processDefinitionKey, String bizId, SDO localSdo,
                                          Map<String, Object> variable, Operator operator) {
        Assert.hasText(bizId);
        Assert.hasText(processDefinitionKey);
        Assert.notNull(workflowApplication);

        this.workflowApplication = workflowApplication;
        this.processDefinitionKey = processDefinitionKey;
        this.bizId = bizId;
        this.localSdo = localSdo;
        this.variable = variable;
        this.operator = operator;
    }

    @Override
    public void run() {
        // 设置线程局部变量
        ThreadLocalUtil.putVariable(Constants.SDO, localSdo);
        ThreadLocalUtil.putOperator(operator);
        // 启动流程
        workflowApplication.startProcessInstanceByKey(processDefinitionKey, bizId, variable);
    }
}
