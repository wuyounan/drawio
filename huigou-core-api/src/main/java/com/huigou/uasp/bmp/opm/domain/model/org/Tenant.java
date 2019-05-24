package com.huigou.uasp.bmp.opm.domain.model.org;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;
import com.huigou.data.domain.model.BaseInfoAbstractEntity;

import org.springframework.util.Assert;

/**
 * 租户信息
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPTenant")
public class Tenant extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = 4670377375887900779L;

    /**
     * 描述
     **/
    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    /**
     * 联系人
     **/
    @Column(name = "CONTACTS", length = 128)
    private String contacts;

    /**
     * 联系电话
     **/
    @Column(name = "CONTACT_NUMBER", length = 256)
    private String contactNumber;

    /**
     * root_full_id
     **/
    @Column(name = "ROOT_FULL_ID", length = 1024)
    private String rootFullId;

    @Column(name = "ORG_ID")
    private String orgId;

    /**
     * 是否行业
     */
    @Column(name = "IS_INDUSTRY")
    private Integer isIndustry;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContacts() {
        return this.contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRootFullId() {
        return this.rootFullId;
    }

    public void setRootFullId(String rootFullId) {
        this.rootFullId = rootFullId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Integer getIsIndustry() {
        return isIndustry;
    }

    public void setIsIndustry(Integer isIndustry) {
        this.isIndustry = isIndustry;
    }

    public void checkConstraints(BaseInfoAbstractEntity other) {
        this.checkConstraints();
        if (other != null) {
            Assert.isTrue(!this.getCode().equalsIgnoreCase(other.getCode()), "编码不能重复。");
        }
    }

}
