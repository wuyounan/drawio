package com.huigou.uasp.bpm;

import org.activiti.engine.delegate.ExecutionListener;

public interface ExecutionListenerExt extends ExecutionListener {
    /**
     * 撤销
     */
    String EVENTNAME_RECALL_PROCESS_INSTANCE = "recallProcessInstance";

}
