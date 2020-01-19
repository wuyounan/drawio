package com.huigou.uasp.bpm.engine.domain.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.OrgUnit;
import com.huigou.data.domain.listener.VersionListener;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bpm.CooperationModelKind;
import com.huigou.uasp.bpm.configuration.domain.model.TaskExecuteMode;
import com.huigou.util.StringUtil;

/**
 * 环节处理人
 *
 * @author gongmm
 */
@MappedSuperclass
@EntityListeners({VersionListener.class})
public class ProcUnitHandlerBase implements IdentifiedEntity, Cloneable, Serializable {

    private static final long serialVersionUID = 7627740680619707939L;

    @Id
    private String id;

    private Long version;

    /**
     * 业务ID
     */
    @Column(name = "biz_id")
    private String bizId;

    /**
     * 业务编码
     */
    @Column(name = "biz_code")
    private String bizCode;

    /**
     * 流程环节ID
     */
    @Column(name = "proc_unit_id")
    private String procUnitId;

    /**
     * 流程环节名称
     */
    @Column(name = "proc_unit_name")
    private String procUnitName;

    /**
     * 子环节ID
     */
    @Column(name = "sub_proc_unit_id")
    private String subProcUnitId;

    /**
     * 子环节名称
     */
    @Column(name = "sub_proc_unit_name")
    private String subProcUnitName;

    /**
     * 处理人ID全路径
     */
    @Column(name = "full_id")
    private String fullId;

    /**
     * 处理人名称全路径
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 处理人ID
     */
    @Column(name = "handler_id")
    private String handlerId;

    /**
     * 处理人
     */
    @Column(name = "handler_name")
    private String handlerName;

    /**
     * 岗位ID
     */
    @Column(name = "position_id")
    private String positionId;

    /**
     * 岗位名称
     */
    @Column(name = "position_name")
    private String positionName;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private String deptId;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 公司ID
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 公司名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 处理日期
     */
    @Column(name = "handled_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date handledDate;

    /**
     * 发送消息
     */
    @Column(name = "send_message")
    private Integer sendMessage;

    /**
     * 处理结果
     */
    private Integer result;

    /**
     * 处理意见
     */
    private String opinion;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 协作模式 chief 主审 assistant 协审
     */
    @Column(name = "cooperation_model_id")
    private String cooperationModelId;

    /**
     * 任务执行模式
     * preempt 抢占模式
     * simultaneous 并行执行
     * 后期扩展用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "task_execute_mode_id")
    private TaskExecuteMode taskExecuteMode;

    @Column(name = "execution_times")
    private Integer executionTimes;

    /**
     * 分组ID
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 主审人ID
     */
    @Column(name = "chief_id")
    private String chiefId;

    /**
     * 审批规则处理人
     */
    @Column(name = "approval_rule_handler_id")
    private String approvalRuleHandlerId;

    @Column(name = "principal_id")
    private String clientId;

    @Column(name = "principal_name")
    private String clientName;

    private Integer sequence;
    /**
     * 分组最少审批人数。
     *
     * @since 1.1.3
     */
    @Column(name = "limit_handler")
    private Integer limitHandler;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Long getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    public String getBizId() {
        return this.bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getProcUnitId() {
        return this.procUnitId;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

    public String getProcUnitName() {
        return procUnitName;
    }

    public void setProcUnitName(String procUnitName) {
        this.procUnitName = procUnitName;
    }

    public String getSubProcUnitId() {
        return this.subProcUnitId;
    }

    public void setSubProcUnitId(String subProcUnitId) {
        this.subProcUnitId = subProcUnitId;
    }

    public String getSubProcUnitName() {
        return this.subProcUnitName;
    }

    public void setSubProcUnitName(String subProcUnitName) {
        this.subProcUnitName = subProcUnitName;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHandlerId() {
        return this.handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return this.handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getPositionId() {
        return this.positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public java.lang.String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getHandledDate() {
        return this.handledDate;
    }

    public void setHandledDate(Date handledDate) {
        this.handledDate = handledDate;
    }

    public Integer getResult() {
        return this.result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getOpinion() {
        return this.opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCooperationModelId() {
        return cooperationModelId;
    }

    public void setCooperationModelId(String cooperationModelId) {
        this.cooperationModelId = cooperationModelId;
    }

    public TaskExecuteMode getTaskExecuteMode() {
        return taskExecuteMode;
    }

    public void setTaskExecuteMode(TaskExecuteMode taskExecuteModeId) {
        this.taskExecuteMode = taskExecuteModeId;
    }

    public Integer getExecutionTimes() {
        return executionTimes;
    }

    public void setExecutionTimes(Integer executionTimes) {
        this.executionTimes = executionTimes;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getChiefId() {
        return chiefId;
    }

    public void setChiefId(String chiefId) {
        this.chiefId = chiefId;
    }

    public String getApprovalRuleHandlerId() {
        return approvalRuleHandlerId;
    }

    public void setApprovalRuleHandlerId(String approvalRuleHandlerId) {
        this.approvalRuleHandlerId = approvalRuleHandlerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Integer sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Integer getLimitHandler() {
        return limitHandler;
    }

    public void setLimitHandler(Integer limitHandler) {
        this.limitHandler = limitHandler;
    }

    public void updateHandleResult(int result, String opinion, int status) {
        this.setResult(result);
        this.setOpinion(opinion);
        this.setHandledDate(new Date());
        this.setStatus(status);
    }

    public void updateClient(String clientId, String clientName) {
        this.setClientId(clientId);
        this.setClientName(clientName);
    }

    public void buildOrgNodeData(OrgUnit orgUnit) {
        this.fullId = orgUnit.getFullId();
        this.fullName = orgUnit.getFullName();

        this.orgId = orgUnit.getOrgId();
        this.orgName = orgUnit.getOrgName();

        this.deptId = orgUnit.getDeptId();
        this.deptName = orgUnit.getDeptName();

        this.positionId = orgUnit.getPositionId();
        this.positionName = orgUnit.getPositionName();

        this.handlerId = orgUnit.getPersonMemberId();
        this.handlerName = orgUnit.getPersonMemberName();
    }

    @Override
    public boolean isNew() {
        return StringUtil.isBlank(this.id);
    }

    @JsonIgnore
    public boolean isPreempt() {
        return TaskExecuteMode.PREEMPT.equals(taskExecuteMode);
    }

    @JsonIgnore
    public boolean isChief() {
        return CooperationModelKind.isChief(cooperationModelId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result;
                    result = readMethod.invoke(this, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
            throw new ApplicationException("对象转换Map错误：" + e.getMessage());
        }
        return returnMap;
    }

    public enum Status {
        INITIAL(-2, "补审初始化状态"), MERGED(-1, "已合并"), READY(0, "未处理"), COMPLETED(1, "已处理"), RETURNED(2, "已回退"),
        ;
        private final int id;

        private final String displayName;

        private Status(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public String toString() {
            return String.valueOf(this.id);
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static Status fromId(int id) {
            switch (id) {
                case -2:
                    return INITIAL;
                case -1:
                    return MERGED;
                case 0:
                    return READY;
                case 1:
                    return COMPLETED;
                case 2:
                    return RETURNED;
                default:
                    throw new ApplicationException(String.format("无效的流程环节处理状态“%s”。", new Object[]{Integer.valueOf(id)}));
            }
        }
    }

    @Override
    public void setUpdateFields_(Collection<String> names) {
        // TODO Auto-generated method stub

    }

//    @Override
//    public void setUpdateFields_(String... updateFields) {
//        // TODO Auto-generated method stub
//
//    }
}
