package com.huigou.uasp.bmp.opm.domain.model.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 组织机构类别
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPOrgPropertyDefinition")
public class OrgPropertyDefinition extends AbstractEntity {

    private static final long serialVersionUID = -166451733997714959L;

    /**
     * 组织机构类别
     */
    @Column(name = "org_kind_id")
    private String orgKindId;

    /**
     * 属性名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 属性描述
     */
    private String description;

    /**
     * 数据源配置
     */
    @Column(name = "data_source")
    private String dataSource;

    private Integer sequence;

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
