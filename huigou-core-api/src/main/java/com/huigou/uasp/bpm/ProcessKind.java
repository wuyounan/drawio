package com.huigou.uasp.bpm;

import com.huigou.exception.ApplicationException;

/**
 * 流程类别
 */
public enum ProcessKind {
    APPROVAL, // 审批流程
    BUSINESS, // 业务流程
    FREE;// 自由流

    public static ProcessKind fromId(String id) {
        switch (id) {
        case "APPROVAL":
            return APPROVAL;
        case "BUSINESS":
            return BUSINESS;
        case "FREE":
            return FREE;
        default:
            throw new ApplicationException(String.format("无效的流程类别“%s”。", id));
        }
    }
}
