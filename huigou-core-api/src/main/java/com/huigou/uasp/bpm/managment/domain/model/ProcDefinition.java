package com.huigou.uasp.bpm.managment.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.TreeEntity;

/**
 * 流程定义
 * 
 * @author gongmm
 */
@Entity
@Table(name = "WF_ProcDef")
public class ProcDefinition extends TreeEntity {

    public static String ROOT_ID = "1";

    private static final long serialVersionUID = 7149413572469994406L;

    @Column(name = "proc_id")
    private String procId;

    @Column(name = "proc_Name")
    private String procName;

    /**
     * 描述
     */
    private String description;

    /**
     * 审批规则流程ID
     */
    @Column(name = "approval_bridge_proc_Id")
    private String approvalBridgeProcId;

    /**
     * 节点类型
     */
    @Column(name = "node_kind_id")
    private String nodeKindId;

    /**
     * 是否计时
     */
    @Column(name = "need_Timing")
    private Integer needTiming;

    /**
     * 限制时间
     */
    @Column(name = "limit_time")
    private Integer limitTime;

    /**
     * 预览处理人
     */
    @Column(name = "show_query_handlers")
    private Integer previewHandler;

    /**
     * 协审必须审批
     */
    @Column(name = "assistant_must_approve")
    private Integer assistantMustApprove;

    /**
     * 处理人合并类型
     */
    @Column(name = "merge_handler_kind")
    private Integer mergeHandlerKind;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovalBridgeProcId() {
        return approvalBridgeProcId;
    }

    public void setApprovalBridgeProcId(String approvalBridgeProcId) {
        this.approvalBridgeProcId = approvalBridgeProcId;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public Integer getNeedTiming() {
        return needTiming;
    }

    public void setNeedTiming(Integer needTiming) {
        this.needTiming = needTiming;
    }

    public Integer getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(Integer limitTime) {
        this.limitTime = limitTime;
    }

    public Integer getPreviewHandler() {
        return previewHandler;
    }

    public void setPreviewHandler(Integer previewHandler) {
        this.previewHandler = previewHandler;
    }

    public Integer getAssistantMustApprove() {
        return assistantMustApprove;
    }

    public void setAssistantMustApprove(Integer assistantMustApprove) {
        this.assistantMustApprove = assistantMustApprove;
    }

    public Integer getMergeHandlerKind() {
        return mergeHandlerKind;
    }

    public void setMergeHandlerKind(Integer mergeHandlerKind) {
        this.mergeHandlerKind = mergeHandlerKind;
    }

    public ProcDefinition() {
        // TODO Auto-generated constructor stub
    }

    public ProcDefinition(String code, Integer sequence) {
        this.setCode(code);
        this.setSequence(sequence);
    }

}
