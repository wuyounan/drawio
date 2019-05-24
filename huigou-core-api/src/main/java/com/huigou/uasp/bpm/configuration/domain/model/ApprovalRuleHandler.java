package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.OrgUnit;
import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleHandler")
public class ApprovalRuleHandler extends AbstractEntity {

    private static final long serialVersionUID = 2511961525884324904L;

    /**
     * 子环节ID
     */
    @Column(name = "handler_kind_id")
    private String handlerKindId;

    /**
     * 环节描述
     */
    private String description;

    @Column(name = "handler_kind_code")
    private String handlerKindCode;

    /**
     * 处理人ID
     */
    @Column(name = "handler_id")
    private String handlerId;

    /**
     * 处理人名称
     */
    @Column(name = "handler_name")
    private String handlerName;

    @Column(name = "biz_handler_param")
    private String bizHandlerParam;

    /**
     * 允许加、减签
     */
    @Column(name = "allow_add")
    private Integer allowAdd;

    /**
     * 允许转发
     */
    @Column(name = "allow_transfer")
    private Integer allowTransfer;

    /**
     * 允许终止
     */
    @Column(name = "allow_abort")
    private Integer allowAbort;

    /**
     * 是否计时
     */
    @Column(name = "need_timing")
    private Integer needTiming;

    /**
     * 限制时间
     */
    @Column(name = "limit_time")
    private Integer limitTime;

    /**
     * 主审ID
     */
    @Column(name = "chief_id")
    private String chiefId;

    /**
     * 组ID
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 是否必经节点
     */
    @Column(name = "must_pass")
    private Integer mustPass;

    @Column(name = "send_message")
    private Integer sendMessage;

    @Column(name = "HELP_SECTION")
    private String helpSection;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_handler_id")
    private List<ApprovalRuleHandlerAssist> assists;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_handler_id")
    private List<ApprovalRuleHandlerUIElmentPermission> uiElmentPermissions;

    /**
     * 排序号
     */
    private Integer sequence;

    @Transient
    private Boolean merged;

    public String getHandlerKindId() {
        return handlerKindId;
    }

    public void setHandlerKindId(String handlerKindId) {
        this.handlerKindId = handlerKindId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBizHandlerParam() {
        return bizHandlerParam;
    }

    public void setBizHandlerParam(String bizHandlerParam) {
        this.bizHandlerParam = bizHandlerParam;
    }

    public String getHandlerKindCode() {
        return handlerKindCode;
    }

    public void setHandlerKindCode(String handlerKindCode) {
        this.handlerKindCode = handlerKindCode;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Integer getAllowAdd() {
        return allowAdd;
    }

    public void setAllowAdd(Integer allowAdd) {
        this.allowAdd = allowAdd;
    }

    public Integer getAllowTransfer() {
        return allowTransfer;
    }

    public void setAllowTransfer(Integer allowTransfer) {
        this.allowTransfer = allowTransfer;
    }

    public Integer getAllowAbort() {
        return allowAbort;
    }

    public void setAllowAbort(Integer allowAbort) {
        this.allowAbort = allowAbort;
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

    public String getChiefId() {
        return chiefId;
    }

    public void setChiefId(String chiefId) {
        this.chiefId = chiefId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getMustPass() {
        return mustPass;
    }

    public void setMustPass(Integer mustPass) {
        this.mustPass = mustPass;
    }

    public Integer getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Integer sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getHelpSection() {
        return helpSection;
    }

    public void setHelpSection(String helpSection) {
        this.helpSection = helpSection;
    }

    @JsonIgnore
    public List<ApprovalRuleHandlerUIElmentPermission> getUIElmentPermissions() {
        return uiElmentPermissions;
    }

    public void setUIElmentPermissions(List<ApprovalRuleHandlerUIElmentPermission> uiElmentPermissions) {
        this.uiElmentPermissions = uiElmentPermissions;
    }

    @JsonIgnore
    public List<ApprovalRuleHandlerAssist> getAssists() {
        return assists;
    }

    public void setAssists(List<ApprovalRuleHandlerAssist> assists) {
        this.assists = assists;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public HandlerKind getHandlerKind() {
        return HandlerKind.fromId(this.handlerKindCode);
    }

    @Transient
    private List<OrgUnit> orgUnits;

    public List<OrgUnit> getOrgUnits() {
        if (orgUnits == null) {
            orgUnits = new ArrayList<OrgUnit>();
        }
        return orgUnits;
    }

    @JsonIgnore
    public boolean isSelection() {
        return getHandlerKind().isSelection();
    }

    @JsonIgnore
    public Boolean getMerged() {
        return merged;
    }

    public void setMerged(Boolean merged) {
        this.merged = merged;
    }

    @JsonIgnore
    public boolean isMustPassed() {
        if (this.mustPass == null) {
            return true;
        }

        return this.mustPass == 1;
    }

    public void copyProperties(ApprovalRuleHandler from) {
        this.description = from.description;
        this.handlerKindCode = from.handlerKindCode;
        this.handlerId = from.handlerId;
        this.handlerName = from.handlerName;
        this.bizHandlerParam = from.bizHandlerParam;
        this.allowAdd = from.allowAdd;
        this.allowTransfer = from.allowTransfer;
        this.allowAbort = from.allowAbort;
        this.needTiming = from.needTiming;
        this.limitTime = from.limitTime;
        this.chiefId = from.chiefId;
        this.groupId = from.groupId;
        this.mustPass = from.mustPass;
        this.helpSection = from.helpSection;
    }
}
