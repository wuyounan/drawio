package com.huigou.uasp.bpm;

/**
 * 流程动作
 * 
 * @author gongmm
 */
public class ProcessAction {
    /**
     * 流程动作
     */
    public static final String PROCESS_ACTION = "processAction";
    /**
     * 查询处理人
     */
    public static final String QUERY_HANDLERS = "queryHandlers";

    /**
     * 流转
     */
    public static final String ADVANCE = "advance";

    /**
     * 询问流转
     */
    public static final String QUERY_ADVANCE = "queryAdvance";

    /**
     * 转交
     */
    public static final String TRANSMIT = "transmit";

    /**
     * 协审
     */
    public static final String ASSIST = "assist";

    /**
     * 抄送
     */
    public static final String MAKE_A_COPYFOR = "makeACopyFor";

    /**
     * 回退
     */
    public static final String BACK = "back";

    /**
     * 打回
     */
    public static final String REPLENISH = "replenish";

    /**
     * 撤回
     */
    public static final String WITHDRAW = "withdraw";

    /**
     * 撤销流程
     */
    public static final String RECALL_PROCESS_INSTANCE = "recallProcessInstance";

    /**
     * 删除流程实例
     */
    public static final String DELETE_PROCESS_INSTANCE = "deleteProcessInstance";

    /**
     * 终止流程实例
     */
    public static final String ABORT_PROCESS_INSTANCE = "abortProcessInstance";

    /**
     * 终止任务
     */
   // public static final String ABORT = "abort";

    /**
     * 保存
     */
    public static final String SAVE = "save";

    /**
     * 暂缓
     */
    public static  final String SLEEP = "sleep";

}
