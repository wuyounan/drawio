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
@Table(name = "SA_OPOrgProperty")
public class OrgProperty extends AbstractEntity {

    private static final long serialVersionUID = -1001609818194860044L;

    @Column(name="property_definition_id")
    private String propertyDefinitionId;
    
//    /**
//     * 属性名
//     */
//    @ManyToOne()
//    @JoinColumn(name = "property_definition_id")
//    private OrgPropertyDefinition orgPropertyDefinition;
    
    @Column(name = "property_value")
    private String propertyValue;

    @Column(name = "property_display")
    private String propertyDisplay;
    
    

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
    
    public String getPropertyDisplay() {
        return propertyDisplay;
    }

    public void setPropertyDisplay(String propertyDisplay) {
        this.propertyDisplay = propertyDisplay;
    }

//    public OrgPropertyDefinition getOrgPropertyDefinition() {
//        return orgPropertyDefinition;
//    }
//
//    public void setOrgPropertyDefinition(OrgPropertyDefinition orgPropertyDefinition) {
//        this.orgPropertyDefinition = orgPropertyDefinition;
//    }
    
    public String getPropertyDefinitionId() {
        return propertyDefinitionId;
    }
    
    public void setPropertyDefinitionId(String propertyDefinitionId) {
        this.propertyDefinitionId = propertyDefinitionId;
    }

}
