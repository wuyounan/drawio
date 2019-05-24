package com.huigou.uasp.bpm;

/**
 * 加签类别
 * 
 * @author gongmm
 */
public enum CounterSignKind {
    CHIEF("chief", "主审加签"), MEND("mend", "补审加签"), MANUAL_SELECTION("manualSelection", "手工选择");

    private final String id;

    private final String displayName;

    private CounterSignKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;

    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public boolean isSpecifiedKind(String value) {
        return this.id.equalsIgnoreCase(value);
    }

}
