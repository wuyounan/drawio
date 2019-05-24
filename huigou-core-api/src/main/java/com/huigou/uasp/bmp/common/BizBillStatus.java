package com.huigou.uasp.bmp.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用业务单据状态
 * 
 * @author gongmm
 */
public enum BizBillStatus {
    APPLYING(0, "申请"), APPROVING(1, "审批中"), SUSPENDED(2, "已挂起"), COMPLETED(3, "已完成"), ARCHIVED(4, "已归档"), ABORTED(5, "已中止");

    private int id;

    private String displayName;

    private BizBillStatus(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static BizBillStatus fromId(int id) {
        switch (id) {
        case 0:
            return APPLYING;
        case 1:
            return APPROVING;
        case 2:
            return SUSPENDED;
        case 3:
            return COMPLETED;
        case 4:
            return ARCHIVED;
        case 5:
            return ABORTED;
        }
        throw new RuntimeException(String.format("无效的业务状态“%s”！", id));
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>(BizBillStatus.values().length);
        for (BizBillStatus c : BizBillStatus.values()) {
            map.put(String.valueOf(c.getId()), c.getDisplayName());
        }
        return map;
    }
}
