package com.huigou.uasp.bmp.opm.domain.model.org;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 组织机构模板实体
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPOrgTemplate")
public class OrgTemplate extends AbstractEntity {

    private static final long serialVersionUID = 7647305321129319483L;

    public static final String ROOT_PARENT_ID_VALUE = "root";

    public static final String ROOT_ID_VALUE = "1";

    public static final String ROOT_CODE_VALUE = "JGBM";

    public static final String ROOT_NAME_VALUE = "组织模板";

    /**
     * 组织类型ID
     */
    @ManyToOne()
    @JoinColumn(name = "type_id")
    private OrgType orgType;

    @Column(name = "parent_id")
    private String parentId;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<OrgTemplate> children;

    /**
     * 排序号
     */
    private Integer sequence;

    public OrgType getOrgType() {
        return this.orgType;
    }

    public void setOrgType(OrgType orgType) {
        this.orgType = orgType;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public OrgTemplate() {

    }

    public OrgTemplate(String parentId, OrgType orgType, Integer sequence) {
        this.setParentId(parentId);
        this.setOrgType(orgType);
        this.setSequence(sequence);
    }

    public List<OrgTemplate> getChildren() {
        return children;
    }

    public void setChildren(List<OrgTemplate> children) {
        this.children = children;
    }
    
    public static OrgTemplate createRoot() {
        OrgTemplate result = new OrgTemplate();

        result.setId(OrgTemplate.ROOT_ID_VALUE);
        result.setParentId(OrgTemplate.ROOT_PARENT_ID_VALUE);

        result.setVersion(1L);

        return result;
    }

}
