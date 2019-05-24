package com.huigou.uasp.bpm.engine.domain.model;

/**
 * 审批不同意处理类别
 * 
 * @author gongmm
 */
public enum ApproveNotPassedHandleKind {
    //TODO BACK_TO_APPLY 逻辑错误
    ABORT, BACK_TO_APPLY, CONTINUE,  // 终止、回退到申请环节、继续流转、回退到申请环节
}
