package com.huigou.uasp.bpm.managment.application;


/**
 * 流程运维中心应用
 * 
 * @author gongmm
 */
public interface ProcessInstanceOperationApplication {

    /**
     * 更改流程任务处理人
     * 
     * @param taskId
     *            任务ID
     * @param personMemberId
     *            人员成员ID
     */
    void updateTaskHandler(String taskId, String personMemberId);

}
