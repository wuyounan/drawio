package com.huigou.uasp.log.domain.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作类型
 *
 * @author gongmm
 */
public enum OperationType {

    LIST("LIST", "进入页面"),

    DETALL("DETALL", "进入明细页面"),

    ADD("ADD", "添加"),

    UPDATE("UPDATE", "修改"),

    DELETE("DELETE", "删除"),

    MOVE("MOVE", "移动"),

    QUERY("QUERY", "查询"),

    SAVE("SAVE", "保存"),

    UPLOAD("UPLOAD", "上传"),

    DOWNLOAD("DOWNLOAD", "下载"),

    VIEW("VIEW", "查看"),

    FUN("FUN", "功能"),

    INIT("INIT", "初始化"),

    CACHE("CACHE", "缓存管理");

    private String id;

    private String name;

    private OperationType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OperationType fromId(String id) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.id.equalsIgnoreCase(id)) {
                return operationType;
            }
        }
        throw new RuntimeException(String.format("无效操作类型类型“%s”。", new Object[] { String.valueOf(id) }));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Map<String, String> getData() {
        Map<String, String> map = new HashMap<String, String>(OperationType.values().length);
        for (OperationType c : OperationType.values()) {
            map.put(String.valueOf(c.getId()), c.getName());
        }
        return map;
    }
}
