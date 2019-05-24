package com.huigou.uasp.bpm.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "WF_ApprovalRuleHandlerUIPerm")
public class ApprovalRuleHandlerUIElmentPermission extends AbstractEntity {

    private static final long serialVersionUID = -6782883148321542419L;
    /**
     * 字段编码
     */
    private String code;
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    @Column(name="kind_id")
    private String kindId;
    
    /**
     * 字段权限
     */
    @Column(name="operation_id")
    private String operationId;
    
    /**
     * 排序号
     */
    private Integer sequence;
    
   
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
