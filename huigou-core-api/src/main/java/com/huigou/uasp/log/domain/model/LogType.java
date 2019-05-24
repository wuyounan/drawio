package com.huigou.uasp.log.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志类型
 * 
 * @author yuanwf
 */
public enum LogType {
    /**
     * 业务日志
     */
    BIZ("BIZ", "业务日志"),
    /**
     * 系统日志
     */
    SYS("SYS", "系统日志"),

    /**
     * 审计员日志 ???
     */
    // AUDITOR,
    /**
     * 集成日志
     */
    INTEGRATION("INTEGRATION", "集成日志"),
    /**
     * 流程日志
     */
    WORKFLOW("WORKFLOW", "流程日志"),
    /**
     * 权限日志
     */
    PERMISSION("PERMISSION", "权限日志");

    private String id;

    private String name;

    private LogType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static LogType fromId(String id) {
        for (LogType logType : LogType.values()) {
            if (logType.id.equalsIgnoreCase(id)) {
                return logType;
            }
        }

        throw new RuntimeException(String.format("无效业务日志类型“%s”。", new Object[] { String.valueOf(id) }));
    }

    public static Map<String, String> getData() {
        Map<String, String> map = new HashMap<String, String>(LogType.values().length);
        for (LogType c : LogType.values()) {
            map.put(String.valueOf(c.getId()), c.getName());
        }
        return map;
    }
}
