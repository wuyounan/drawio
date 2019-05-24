package com.huigou.uasp.bpm.engine.domain.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.cache.SystemCache;
import com.huigou.uasp.bpm.ActivityKind;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.TaskStatus;
import com.huigou.util.ClassHelper;
import com.huigou.util.DateUtil;
import com.huigou.util.StringUtil;

/**
 * 流程环节处理人分组
 * 
 * @author gongmm
 */
public class ProcUnitHandlerGroup implements Serializable {

    private static final long serialVersionUID = -7073182612408659234L;

    private List<String> groupName;

    private List<Map<String, Object>> handlers;

    /**
     * 当前组ID
     */
    private Integer groupId;

    /**
     * 是否当前审批组
     */
    private boolean isCurrentGroup;

    public ProcUnitHandlerGroup() {
        groupName = new java.util.ArrayList<String>();
        handlers = new java.util.ArrayList<Map<String, Object>>();
    }

    public ProcUnitHandlerGroup(Integer groupId) {
        this();
        this.groupId = groupId;
    }

    public String getGroupName() {
        int l = groupName.size();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < l; i++) {
            sb.append(groupName.get(i));
            if (i < l - 1) {
                sb.append("<br/>");
            }
        }
        return sb.toString();
    }

    public int getSize() {
        return handlers.size();
    }

    public List<Map<String, Object>> getHandlers() {
        return handlers;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public boolean isCurrentGroup() {
        return isCurrentGroup;
    }

    public void setCurrentGroup(boolean isCurrentGroup) {
        this.isCurrentGroup = isCurrentGroup;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("groupName", getGroupName());
        map.put("rowSpan", getSize() * 2);
        map.put("handlers", getHandlers());
        return map;
    }

    public void addHandler(Map<String, Object> map, String personId, String procUnitId, String currentTaskId) {
        String subProcUnitName = ClassHelper.convert(map.get("subProcUnitName"), String.class);
        Integer handleResult = ClassHelper.convert(map.get("result"), Integer.class, 0);
        String resultDisplayName = "";
        if (handleResult != 0) {
            resultDisplayName = SystemCache.getDictionaryDetailText("handleResult", handleResult);
        }
        map.put("resultDisplayName", resultDisplayName);
        String cooperationModelId = ClassHelper.convert(map.get("cooperationModelId"), String.class, CooperationModelKind.CHIEF);
        if (cooperationModelId.equals(CooperationModelKind.CHIEF) || cooperationModelId.equals(CooperationModelKind.MEND)) {
            if (groupName.indexOf(subProcUnitName) == -1) {
                groupName.add(subProcUnitName);
            }
        }

        // 审批环节的处理状态 1 已处理 0 未处理
        String status = ClassHelper.convert(map.get("status"), String.class, "0");
        // 任务状态
        String taskStatusId = ClassHelper.convert(map.get("statusId"), String.class, "");
        String taskId = ClassHelper.convert(map.get("taskId"), String.class, "");
        if (TaskStatus.isToDoStatus(taskStatusId)) {
            // 未提交的数据认为未处理
            if (status.equals("0")) {
                map.put("handleTime", "");
                map.put("resultDisplayName", "");
                map.put("opinion", "");
                map.put("isCurrentProcUnit", true);
            }
            if (!this.isCurrentGroup) {
                isCurrentGroup = true;
            }
        }
        if (!ActivityKind.isApplyActivity(procUnitId) && map.get("handlerId").toString().startsWith(String.format("%s@", personId))) {
            if (currentTaskId.equals(taskId) && TaskStatus.isToDoStatus(taskStatusId)) {
                map.put("readonly", false);
                map.put("isCurrentProcUnit", false);
                if (!this.isCurrentGroup) {
                    isCurrentGroup = true;
                }
            }
        }
        // 转换日期为字符串
        Object handleTime = map.get("handleTime");
        if (handleTime != null && !StringUtil.isBlank(handleTime.toString())) {
            map.put("handleTime", DateUtil.getDateFormat(3, (java.util.Date) handleTime));
        }

        handlers.add(map);
    }
}
