package com.huigou.uasp.bmp.opm;

import org.springframework.context.ApplicationEvent;

/**
 * 管理权限事件
 * 
 * @author gongmm
 */
public class ManagmentEvent extends ApplicationEvent {

    /**
     * 事件类别
     * 
     * @author gongmm
     */
    public enum EventKind {
        ALLOCATE, DELETE
    }

    private static final long serialVersionUID = 3483582757232858941L;

    private EventKind eventKind;

    /**
     * 管理者
     */
    private String[] managerIds;

    /**
     * 管理权限类型
     */
    private Long manageTypeId;

    /**
     * 下属
     */
    // private String[] subordinationIds;

    private Long[] managmentIds;

    public ManagmentEvent(Object source) {
        super(source);
    }

    public EventKind getEventKind() {
        return eventKind;
    }

    public void setEventKind(EventKind eventKind) {
        this.eventKind = eventKind;
    }

    public String[] getManagerIds() {
        return managerIds;
    }

    public void setManagerIds(String[] managerIds) {
        this.managerIds = managerIds;
    }

    public Long getManageTypeId() {
        return manageTypeId;
    }

    public void setManageTypeId(Long manageTypeId) {
        this.manageTypeId = manageTypeId;
    }


    public Long[] getManagmentIds() {
        return managmentIds;
    }

    public void setManagmentIds(Long[] managmentIds) {
        this.managmentIds = managmentIds;
    }

}
