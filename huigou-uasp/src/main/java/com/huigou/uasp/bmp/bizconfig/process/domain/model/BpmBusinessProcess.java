package com.huigou.uasp.bmp.bizconfig.process.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;
import com.huigou.data.domain.model.TreeEntity;

/**
 * 流程定义
 *
 * @author EM_BUSINESS_PROCESS
 * @date 2017-04-07 14:00
 */
@Entity
@Table(name = "BPM_BUSINESS_PROCESS")
@EntityListeners({ CreatorAndModifierListener.class })
public class BpmBusinessProcess extends TreeEntity {

    private static final long serialVersionUID = 9134450233759063781L;

    /**
     * 对象类型编码
     **/
    @Column(name = "object_kind_code", length = 32)
    private String objectKindCode;

    /**
     * 流程类型
     **/
    @Column(name = "process_kind", length = 32)
    private String processKind;

    /**
     * 是否末级
     **/
    @Column(name = "is_final", length = 22)
    private Integer isFinal;

    /**
     * E化
     **/
    @Column(name = "is_electronization", length = 22)
    private Integer isElectronization;

    /**
     * 是否包含流程图
     **/
    @Column(name = "IS_FLOW_CHART", length = 22)
    private Integer isFlowChart;

    /**
     * 绘图方向
     */
    @Column(name = "CHART_DIRECTION", length = 32)
    private String chartDirection;

    /**
     * 用户编号
     **/
    @Column(name = "user_code", length = 32)
    private String userCode;

    /**
     * 版本编码
     **/
    @Column(name = "version_code", length = 32)
    private String versionCode;

    /**
     * 创建人信息
     */
    @Embedded
    private Creator creator;

    /**
     * 最后修改人信息
     */
    @Embedded
    private Modifier modifier;

    /**
     * 所属人ID
     **/
    @Column(name = "owner_id", length = 32)
    private String ownerId;

    /**
     * 所属人名称
     **/
    @Column(name = "owner_name", length = 64)
    private String ownerName;

    /**
     * 流程清单
     **/
    @Column(name = "flow_list", length = 512)
    private String flowList;

    /**
     * 流程目的
     **/
    @Column(name = "flow_aim", length = 512)
    private String flowAim;

    /**
     * 流程范围
     **/
    @Column(name = "flow_range", length = 512)
    private String flowRange;

    /**
     * 流程对象
     **/
    @Column(name = "flow_object", length = 512)
    private String flowObject;

    /**
     * 流程职责
     **/
    @Column(name = "flow_duty", length = 512)
    private String flowDuty;

    /**
     * 流程输入条件
     **/
    @Column(name = "flow_input_condition", length = 512)
    private String flowInputCondition;

    /**
     * 流程输出
     **/
    @Column(name = "flow_output", length = 512)
    private String flowOutput;

    /**
     * 流程指标
     **/
    @Column(name = "flow_index", length = 512)
    private String flowIndex;

    /**
     * 备注
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    @Transient
    private String parentName;

    @Transient
    private Integer hasChildren;

    public String getObjectKindCode() {
        return objectKindCode;
    }

    public void setObjectKindCode(String objectKindCode) {
        this.objectKindCode = objectKindCode;
    }

    public String getProcessKind() {
        return this.processKind;
    }

    public void setProcessKind(String processKind) {
        this.processKind = processKind;
    }

    public Integer getIsFinal() {
        return this.isFinal;
    }

    public void setIsFinal(Integer isFinal) {
        this.isFinal = isFinal;
    }

    public Integer getIsElectronization() {
        return isElectronization;
    }

    public void setIsElectronization(Integer isElectronization) {
        this.isElectronization = isElectronization;
    }

    public Integer getIsFlowChart() {
        return isFlowChart;
    }

    public void setIsFlowChart(Integer isFlowChart) {
        this.isFlowChart = isFlowChart;
    }

    public String getChartDirection() {
        return chartDirection;
    }

    public void setChartDirection(String chartDirection) {
        this.chartDirection = chartDirection;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getFlowList() {
        return flowList;
    }

    public void setFlowList(String flowList) {
        this.flowList = flowList;
    }

    public String getFlowAim() {
        return flowAim;
    }

    public void setFlowAim(String flowAim) {
        this.flowAim = flowAim;
    }

    public String getFlowRange() {
        return flowRange;
    }

    public void setFlowRange(String flowRange) {
        this.flowRange = flowRange;
    }

    public String getFlowObject() {
        return flowObject;
    }

    public void setFlowObject(String flowObject) {
        this.flowObject = flowObject;
    }

    public String getFlowDuty() {
        return flowDuty;
    }

    public void setFlowDuty(String flowDuty) {
        this.flowDuty = flowDuty;
    }

    public String getFlowInputCondition() {
        return flowInputCondition;
    }

    public void setFlowInputCondition(String flowInputCondition) {
        this.flowInputCondition = flowInputCondition;
    }

    public String getFlowOutput() {
        return flowOutput;
    }

    public void setFlowOutput(String flowOutput) {
        this.flowOutput = flowOutput;
    }

    public String getFlowIndex() {
        return flowIndex;
    }

    public void setFlowIndex(String flowIndex) {
        this.flowIndex = flowIndex;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

}
