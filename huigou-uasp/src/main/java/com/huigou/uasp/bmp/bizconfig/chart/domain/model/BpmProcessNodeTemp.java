package com.huigou.uasp.bmp.bizconfig.chart.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.uasp.bmp.bizconfig.chart.domain.ProcessNodeInterface;
import com.huigou.data.domain.model.AbstractEntity;

/**
 * 活动节点临时存储
 * 
 * @author xx
 *         BPM_PROCESS_NODE_TEMP
 * @date 2017-04-17 09:23
 */
@Entity
@Table(name = "BPM_PROCESS_NODE_TEMP")
public class BpmProcessNodeTemp extends AbstractEntity implements ProcessNodeInterface {

    private static final long serialVersionUID = -7914739528062217255L;

    /**
     * node_kind
     **/
    @Column(name = "object_kind_code", length = 32)
    private String objectKindCode;

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

    public String getObjectKindCode() {
        return objectKindCode;
    }

    public void setObjectKindCode(String objectKindCode) {
        this.objectKindCode = objectKindCode;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getBusinessProcessId() {
        return businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLinkKindCodes() {
        return linkKindCodes;
    }

    public void setLinkKindCodes(String linkKindCodes) {
        this.linkKindCodes = linkKindCodes;
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
