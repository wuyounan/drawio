package com.huigou.uasp.bpm.engine.domain.model;

import com.huigou.exception.ApplicationException;

public enum FreeFlowProcUnitKind {
    START("start", "开始"), ACTIVITY("activity", "环节"), END("end", "结束");

    private final String id;

    private final String displayName;

    private FreeFlowProcUnitKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static FreeFlowProcUnitKind fromId(String id) {
        switch (id) {
        case "start":
            return START;
        case "activity":
            return ACTIVITY;
        case "end":
            return END;
        default:
            throw new ApplicationException(String.format("无效的流程环节类别“%s”！", new Object[] { Integer.valueOf(id) }));
        }
    }
}
