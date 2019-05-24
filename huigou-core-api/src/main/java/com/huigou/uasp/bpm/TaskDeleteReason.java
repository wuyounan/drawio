package com.huigou.uasp.bpm;

public class TaskDeleteReason {

    /**
     * 执行中
     */
    public static String EXECUTING = "executing";

    /**
     * 任务已删除
     */

    public static String COMPLETED = "completed";

    /**
     * 任务已中止
     */
    public static String ABORTED = "aborted";

    /**
     * 流程实例已删除
     */
    public static String PROCESSINSTANCE_DELETED = "ProcessInstancedeleted";

    /**
     * 任务已删除
     */
    public static String DELETED = "deleted";

}
