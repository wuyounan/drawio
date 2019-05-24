package com.huigou.uasp.bpm;

import org.activiti.engine.delegate.TaskListener;

public interface TaskListenerExt extends TaskListener {

    /**
     * 保存业务数据
     */
    String EVENTNAME_SAVE_BIZ_DATA = "saveBizData";

    /**
     * 查询处理人
     */
    String EVENTNAME_QUERY_HANDLERS = "queryHandlers";

    /**
     * 检查约束
     */
    String EVENTNAME_CHECK_CONSTRAINTS = "checkConstraints";

}
