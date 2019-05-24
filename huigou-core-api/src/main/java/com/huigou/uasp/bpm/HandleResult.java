package com.huigou.uasp.bpm;

/**
 * 处理结果
 * 
 * @author gongmm
 */
public enum HandleResult {
    NOT_FILL_IN(0, "未填写"), AGREE(1, "同意"), DISAGREE(2, "不同意"), HAVE_PASSED(3, "已阅"), REPLENISH(4, "打回"), SYSTEM_COMPLETE(5, "系统完成");

    private final int id;

    private final String displayName;

    private HandleResult(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static HandleResult fromId(int id) {
        switch (id) {
        case 0:
            return NOT_FILL_IN;
        case 1:
            return AGREE;
        case 2:
            return DISAGREE;
        case 3:
            return HAVE_PASSED;
        case 4:
            return REPLENISH;
        case 5:
            return SYSTEM_COMPLETE;
        default:
            throw new RuntimeException(String.format("无效的处理结果“%s”。", new Object[] { Integer.valueOf(id) }));
        }
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
