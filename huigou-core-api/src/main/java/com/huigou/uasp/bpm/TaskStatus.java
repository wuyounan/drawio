package com.huigou.uasp.bpm;

import java.util.ArrayList;
import java.util.List;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

public enum TaskStatus {
    READY("ready", "尚未处理"),
    EXECUTING("executing", "正在处理"),
    COMPLETED("completed", "已完成"),
    SLEEPING("sleeping", "暂缓处理"),
    CANCELED("canceled", "已取消"),
    ABORTED("aborted", "已终止"),
    RETURNED("returned", "已回退"),
    WAITED("waited", "等待中"),
    TRANSMITED("transmited", "已转交"),
    SUSPENDED("suspended", "已暂停"),
    DELETED("deleted", "已删除");

    private final String id;

    private final String displayName;

    private TaskStatus(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static TaskStatus fromId(String id) {
        if (StringUtil.isBlank(id)) {
            throw new ApplicationException("任务状态不能为空。");
        }
        if ("ready".equalsIgnoreCase(id)) {
            return TaskStatus.READY;
        } else if ("executing".equalsIgnoreCase(id)) {
            return TaskStatus.EXECUTING;
        } else if ("completed".equalsIgnoreCase(id)) {
            return TaskStatus.COMPLETED;
        } else if ("sleeping".equalsIgnoreCase(id)) {
            return TaskStatus.SLEEPING;
        } else if ("canceled".equalsIgnoreCase(id)) {
            return TaskStatus.CANCELED;
        } else if ("aborted".equalsIgnoreCase(id)) {
            return TaskStatus.ABORTED;
        } else if ("returned".equalsIgnoreCase(id)) {
            return TaskStatus.RETURNED;
        } else if ("transmited".equalsIgnoreCase(id)) {
            return TaskStatus.TRANSMITED;
        } else if ("waited".equalsIgnoreCase(id)) {
            return TaskStatus.WAITED;
        } else if ("suspended".equalsIgnoreCase(id)) {
            return TaskStatus.SUSPENDED;
        } else if ("deleted".equalsIgnoreCase(id)) {
            return TaskStatus.DELETED;
        } else {
            throw new ApplicationException(String.format("无效的任务状态“%s”", id));
        }
    }

    public static List<TaskStatus> getTaskStatusesFromIds(String ids) {
        List<TaskStatus> result = new ArrayList<TaskStatus>();
        if (!StringUtil.isBlank(ids)) {
            String[] split = ids.split(",");
            for (String id : split) {
                boolean found = false;
                for (TaskStatus status : values()) {
                    if (id.equalsIgnoreCase(status.getId())) {
                        result.add(status);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new ApplicationException(String.format("无效的任务状态“%s”。", id));
                }
            }
        }
        return result;
    }

    public static boolean isToDoStatus(String status) {
        return status.equalsIgnoreCase(READY.getId()) || status.equalsIgnoreCase(SLEEPING.getId()) || status.equalsIgnoreCase(EXECUTING.getId());

    }
}
