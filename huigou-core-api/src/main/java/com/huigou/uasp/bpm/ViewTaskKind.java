package com.huigou.uasp.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 查询任务状态分类
 * 
 * @author gongmm
 */
public enum ViewTaskKind {
    WAITING(1, "待办任务"),
    COMPLETED(2, "已办任务"),
    SUBMITED(3, "提交任务"),
    DRAFT(4, "待发任务"),
    INITIATE(5, "本人发起"),
    COLLECTED(6, "收藏任务"),
    TRACKING(7, "跟踪任务"),
    PROC_INST_COMPLETED(8, "办结任务");

    private final int id;

    private final String displayName;

    private ViewTaskKind(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<Integer, String> getData() {
        Map<Integer, String> result = new HashMap<Integer, String>(3);
        for (ViewTaskKind item : ViewTaskKind.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

    public static Map<Integer, String> getQueryUseData() {
        Map<Integer, String> result = new HashMap<Integer, String>(3);
        for (ViewTaskKind item : ViewTaskKind.values()) {
            if (item == WAITING || item == COMPLETED) {
                result.put(item.getId(), item.getDisplayName());
            }
        }
        return result;
    }

    public static List<ViewTaskKind> getViewTaskKindsFromString(String value) {
        List<ViewTaskKind> result = new ArrayList<ViewTaskKind>();
        if (!StringUtil.isBlank(value)) {
            String[] split = value.split(",");
            for (String item : split) {
                boolean found = false;
                for (ViewTaskKind kind : values()) {
                    if (ClassHelper.convert(item, Integer.class) == kind.getId()) {
                        result.add(kind);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new ApplicationException("无效的任务分类: " + item);
                }
            }
        }
        return result;
    }

    public static ViewTaskKind fromId(String id) {
        if (StringUtil.isNotBlank(id)) {
            for (ViewTaskKind c : ViewTaskKind.values()) {
                if (String.valueOf(c.getId()).equals(id)) {
                    return c;
                }
            }
        }
        return null;
    }

}
