package com.huigou.uasp.bmp.opm;

import org.springframework.context.ApplicationEvent;

public class OrgUpdateEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String sourceOrgId;

    private String destOrgId;

    private EventKind eventKind;

    public String getSourceOrgId() {
        return sourceOrgId;
    }

    public void setSourceOrgId(String sourceOrgId) {
        this.sourceOrgId = sourceOrgId;
    }

    public String getDestOrgId() {
        return destOrgId;
    }

    public void setDestOrgId(String destOrgId) {
        this.destOrgId = destOrgId;
    }

    public OrgUpdateEvent(Object source) {
        super(source);
    }

    public String toString() {
        return "OrgUpdateEvent";
    }

    public EventKind getEventKind() {
        return eventKind;
    }

    public void setEventKind(EventKind eventKind) {
        this.eventKind = eventKind;
    }

    /**
     * 组织机构事件类别
     * 
     * @author gongmm
     */
    public enum EventKind {
        INSERT, UPDATE, DELETE, ADJUST
    }

}
