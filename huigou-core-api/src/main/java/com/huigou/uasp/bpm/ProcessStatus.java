package com.huigou.uasp.bpm;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

/**
 * 流程状态
 * 
 * @author gongmm
 */
public enum ProcessStatus {
    EXECUTING("executing", "正在处理"), COMPLETED("completed", "已完成"), ABORTED("aborted", "已终止"), DELETED("deleted", "已删除");

    private final String id;

    private final String displayName;

    private ProcessStatus(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static ProcessStatus fromId(String id) {
        if (StringUtil.isBlank(id)) {
            throw new ApplicationException("流程状态不能为空。");
        }
        if ("executing".equalsIgnoreCase(id)) {
            return ProcessStatus.EXECUTING;
        } else if ("completed".equalsIgnoreCase(id)) {
            return ProcessStatus.COMPLETED;
        } else if ("aborted".equalsIgnoreCase(id)) {
            return ProcessStatus.ABORTED;
        } else if ("deleted".equalsIgnoreCase(id)) {
            return ProcessStatus.DELETED;
        } else {
            throw new ApplicationException(String.format("无效的流程状态“%s”", id));
        }
    }

}
