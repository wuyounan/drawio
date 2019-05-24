package com.huigou.uasp.bpm.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huigou.util.StringUtil;

/**
 * 流程事件环境
 * 
 * @author gongmm
 */
public class ProcessEventContext {

    private static final String NEW_PROCESS_TASK_EXTENSIONS_KEY = "newProcessTaskExtensions";

    private static final String NEW_NOT_PROCESS_TASK_EXTENSIONS_KEY = "newNotProcessTaskExtensions";

    private static final String COMPLETED_TASKEXTENSIONS_KEY = "completedTaskExtensions";

    private static final String DELETED_TASKEXTENSIONS_KEY = "deletedTaskExtensions";

    private static final String UPDATED_STATUS_TASKEXTENSIONS_KEY = "updatedStatusTaskExtensions";

    private static final String UPDATED_HANDLER_TASKEXTENSIONS_KEY = "updatedHandlerTaskExtensions";

    private static ThreadLocal<Map<String, Object>> processEventThreadLocal = new ThreadLocal<Map<String, Object>>();

    private static Map<String, Object> getProcessEventThreadLocalData() {
        Map<String, Object> result = processEventThreadLocal.get();
        if (result == null) {
            result = new HashMap<String, Object>();
            processEventThreadLocal.set(result);
        }
        return result;
    }

    public static void put(String key, Object object) {
        if (object != null) {
            getProcessEventThreadLocalData().put(key, object);
        }
    }

    public static Object get(String key) {
        if (!StringUtil.isBlank(key)) {
            return getProcessEventThreadLocalData().get(key);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> cls) {
        if (!StringUtil.isBlank(key)) {
            return (T) getProcessEventThreadLocalData().get(key);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<ProcessEventTaskInstance> getProcessEventTaskInstanceList(String dataName) {
        List<String> taskIds = (ArrayList<String>) get(dataName);
        if (taskIds == null) {
            return null;
        }
        List<ProcessEventTaskInstance> eventTasks = new ArrayList<>(taskIds.size());
        for (String taskId : taskIds) {
            ProcessEventTaskInstance eventTask = ProcessEventTaskInstance.newInstance(taskId);
            if (eventTask != null) {
                eventTasks.add(eventTask);
            }
        }
        return eventTasks;
    }

    @SuppressWarnings("unchecked")
    public static void addNewProcessTask(String taskId) {
        List<String> tasks = (ArrayList<String>) get(NEW_PROCESS_TASK_EXTENSIONS_KEY);
        if (tasks == null) {
            tasks = new ArrayList<String>();
            put(NEW_PROCESS_TASK_EXTENSIONS_KEY, tasks);
        }
        if (!tasks.contains(taskId)) {
            tasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getNewProcessTasks() {
        return getProcessEventTaskInstanceList(NEW_PROCESS_TASK_EXTENSIONS_KEY);
    }

    @SuppressWarnings("unchecked")
    public static void addNewNotProcessTask(String taskId) {
        List<String> tasks = (ArrayList<String>) get(NEW_NOT_PROCESS_TASK_EXTENSIONS_KEY);
        if (tasks == null) {
            tasks = new ArrayList<String>();
            put(NEW_NOT_PROCESS_TASK_EXTENSIONS_KEY, tasks);
        }
        if (!tasks.contains(taskId)) {
            tasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getNewNotProcessTasks() {
        return getProcessEventTaskInstanceList(NEW_NOT_PROCESS_TASK_EXTENSIONS_KEY);
    }

    @SuppressWarnings("unchecked")
    public static void addCompletedTask(String taskId) {
        List<String> completedTasks = (ArrayList<String>) get(COMPLETED_TASKEXTENSIONS_KEY);
        if (completedTasks == null) {
            completedTasks = new ArrayList<String>();
            put(COMPLETED_TASKEXTENSIONS_KEY, completedTasks);
        }
        if (!completedTasks.contains(taskId)) {
            completedTasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getCompletedTasks() {
        return getProcessEventTaskInstanceList(COMPLETED_TASKEXTENSIONS_KEY);
    }

    @SuppressWarnings("unchecked")
    public static void addDeletedTask(String taskId) {
        List<String> deletedTasks = (ArrayList<String>) get(DELETED_TASKEXTENSIONS_KEY);
        if (deletedTasks == null) {
            deletedTasks = new ArrayList<String>();
            put(DELETED_TASKEXTENSIONS_KEY, deletedTasks);
        }
        if (!deletedTasks.contains(taskId)) {
            deletedTasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getDeletedTasks() {
        return getProcessEventTaskInstanceList(DELETED_TASKEXTENSIONS_KEY);
    }

    @SuppressWarnings("unchecked")
    public static void addUpdatedStatusTask(String taskId) {
        List<String> updatedStatusTasks = (ArrayList<String>) get(UPDATED_STATUS_TASKEXTENSIONS_KEY);
        if (updatedStatusTasks == null) {
            updatedStatusTasks = new ArrayList<String>();
            put(UPDATED_STATUS_TASKEXTENSIONS_KEY, updatedStatusTasks);
        }
        if (!updatedStatusTasks.contains(taskId)) {
            updatedStatusTasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getUpdatedStatusTasks() {
        return getProcessEventTaskInstanceList(UPDATED_STATUS_TASKEXTENSIONS_KEY);
    }

    @SuppressWarnings("unchecked")
    public static void addUpdatedHandlerTask(String taskId) {
        List<String> updatedHandlerTasks = (ArrayList<String>) get(UPDATED_HANDLER_TASKEXTENSIONS_KEY);
        if (updatedHandlerTasks == null) {
            updatedHandlerTasks = new ArrayList<String>();
            put(UPDATED_HANDLER_TASKEXTENSIONS_KEY, updatedHandlerTasks);
        }
        if (!updatedHandlerTasks.contains(taskId)) {
            updatedHandlerTasks.add(taskId);
        }
    }

    public static List<ProcessEventTaskInstance> getUpdatedHandlerTasks() {
        return getProcessEventTaskInstanceList(UPDATED_HANDLER_TASKEXTENSIONS_KEY);
    }

    public static void remove() {
        Map<String, Object> localData = processEventThreadLocal.get();
        if (localData != null) {
            localData.clear();
        }
        processEventThreadLocal.set(null);
        processEventThreadLocal.remove();
    }
}
