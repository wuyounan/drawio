package com.huigou.uasp.tool.dataimport.domain.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel导入状态
 * 
 * @author xx
 */
public enum ExcelImportStatus {
    START(0, "未处理"), SUCCESS(1, "成功"), ERROR(2, "失败"), REPEAT(3, "重做");

    private final Integer id;

    private final String name;

    private ExcelImportStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ExcelImportStatus fromId(Integer id) {
        if (null == id) {
            return null;
        }
        switch (id) {
        case 0:
            return START;
        case 1:
            return SUCCESS;
        case 2:
            return ERROR;
        case 3:
            return REPEAT;
        default:
            return null;
        }
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>(ExcelImportStatus.values().length);
        for (ExcelImportStatus c : ExcelImportStatus.values()) {
            map.put(String.valueOf(c.getId()), c.getName());
        }
        return map;
    }

    public static Map<String, String> getQueryViewMap() {
        Map<String, String> map = new LinkedHashMap<String, String>(ExcelImportStatus.values().length);
        map.put(String.valueOf(START.getId()), START.getName());
        map.put(String.valueOf(SUCCESS.getId()), SUCCESS.getName());
        map.put(String.valueOf(ERROR.getId()), ERROR.getName());
        map.put("-1", "全部");
        return map;
    }
}
