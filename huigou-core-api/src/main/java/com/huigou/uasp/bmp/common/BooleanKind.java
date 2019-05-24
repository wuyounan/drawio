package com.huigou.uasp.bmp.common;

import com.huigou.exception.ApplicationException;

public enum BooleanKind {

    NO(0, "否"), YES(1, "是");

    private final int id;

    private final String displayName;

    private BooleanKind(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public int getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static BooleanKind fromId(int id) {
        switch (id) {
        case 0:
            return NO;
        case 1:
            return YES;
        default:
            throw new ApplicationException(String.format("无效的Boolean类型“%s”。", new Object[] { Integer.valueOf(id) }));
        }
    }

}
