package com.huigou.uasp.bmp.bizconfig.chart.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.uasp.bmp.bizconfig.chart.domain.ProcessNodeInterface;
import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;

/**
 * 活动节点
 * 
 * @author xx
 *         BPM_PROCESS_NODE
 * @date 2017-04-17 09:23
 */
@Entity
@Table(name = "BPM_PROCESS_NODE")
@EntityListeners({ CreatorAndModifierListener.class })
public class BpmProcessNode extends BaseInfoAbstractEntity implements ProcessNodeInterface {

    private static final long serialVersionUID = -7730751187577397829L;

    /**
     * business_process_id
     **/
    @Column(name = "view_id", length = 32)
    private String viewId;

    /**
     * business_process_id
     **/
    @Column(name = "business_process_id", length = 32)
    private String businessProcessId;

    /**
     * node_kind
     **/
    @Column(name = "object_kind_code", length = 32)
    private String objectKindCode;

    /**
     * xaxis
     **/
    @Column(name = "xaxis", length = 22)
    private BigDecimal xaxis;

    /**
     * yaxis
     **/
    @Column(name = "yaxis", length = 22)
    private BigDecimal yaxis;

    /**
     * rule_kind
     **/
    @Column(name = "rule_kind", length = 32)
    private String ruleKind;

    /**
     * owner_id
     **/
    @Column(name = "owner_id", length = 32)
    private String ownerId;

    /**
     * owner_name
     **/
    @Column(name = "owner_name", length = 64)
    private String ownerName;

    /**
     * 包含对象
     **/
    @Column(name = "link_kind_codes", length = 256)
    private String linkKindCodes;

    /**
     * 接口类型
     */
    @Column(name = "interface_kind", length = 256)
    private String interfaceKind;

    /**
     * 引用节点ID
     */
    @Column(name = "quote_id", length = 256)
    private String quoteId;

    /**
     * 创建人信息
     */
    @Embedded
    @JsonIgnore
    private Creator creator;

    /**
     * 最后修改人信息
     */
    @Embedded
    @JsonIgnore
    private Modifier modifier;

    /**
     * 备注
     **/
    @Column(name = "remark", length = 256)
    private String remark;

    /**
     * 对应功能编号
     */
    @Column(name = "function_code", length = 32)
    private String functionCode;

    @Column(name = "en_name", length = 64)
    private String enName;

    /**
     * 节点编码
     */
    @Column(name = "node_code", length = 32)
    private String nodeCode;

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getBusinessProcessId() {
        return this.businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getObjectKindCode() {
        return this.objectKindCode;
    }

    public void setObjectKindCode(String objectKindCode) {
        this.objectKindCode = objectKindCode;
    }

    public BigDecimal getXaxis() {
        return this.xaxis;
    }

    public void setXaxis(BigDecimal xaxis) {
        this.xaxis = xaxis;
    }

    public BigDecimal getYaxis() {
        return this.yaxis;
    }

    public void setYaxis(BigDecimal yaxis) {
        this.yaxis = yaxis;
    }

    public String getRuleKind() {
        return this.ruleKind;
    }

    public void setRuleKind(String ruleKind) {
        this.ruleKind = ruleKind;
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

    public String getLinkKindCodes() {
        return this.linkKindCodes;
    }

    public void setLinkKindCodes(String linkKindCodes) {
        this.linkKindCodes = linkKindCodes;
    }

    public String getInterfaceKind() {
        return interfaceKind;
    }

    public void setInterfaceKind(String interfaceKind) {
        this.interfaceKind = interfaceKind;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    @JsonIgnore
    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    @JsonIgnore
    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

}
