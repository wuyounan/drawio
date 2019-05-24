package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.HashMap;
import java.util.Map;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

/**
 * 任务执行模式
 * 
 * @author gongmm
 */
public enum TaskExecuteMode {
    PREEMPT("PREEMPT", "抢占模式"), SIMULTANEOUS("SIMULTANEOUS", "同时模式");

    private final String id;

    private final String displayName;

    private TaskExecuteMode(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static TaskExecuteMode fromId(String id) {
        if (StringUtil.isBlank(id)) {
            return null;
        }

        switch (id) {
        case "PREEMPT":
            return PREEMPT;
        case "SIMULTANEOUS":
            return SIMULTANEOUS;
        default:
            throw new ApplicationException(String.format("无效的任务执行模式“%s”。", new Object[] { Integer.valueOf(id) }));
        }
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new HashMap<String, String>(2);
        for (TaskExecuteMode item : TaskExecuteMode.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }
}
