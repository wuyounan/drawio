package com.huigou.uasp.bpm.event;

import java.util.List;

/**
 * 流程事件支持
 * 
 * @author gongmm
 */
public class ProcessEventSupport {

    private List<ProcessEventListener> listeners;

    public void setListeners(List<ProcessEventListener> listeners) {
        this.listeners = listeners;
    }

    public void fireProcessEvent(ProcessEvent processEvent) {
        if (listeners != null) {
            for (ProcessEventListener listener : listeners) {
                listener.notify(processEvent);
            }
        }
    }
}
