package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huigou.context.OrgUnit;
import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleHandlerAssist")
public class ApprovalRuleHandlerAssist extends AbstractEntity {

    private static final long serialVersionUID = 2511961525884324904L;

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

    /**
     * 审批类别ID
     */
    @Column(name = "kind_id")
    private String kindId;

    /**
     * 排序号
     */
    private Integer sequence;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
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

}
