package com.huigou.uasp.bmp.configuration.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 个人查询方案
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_PersonQueryScheme")
public class PersonQueryScheme extends AbstractEntity {

    private static final long serialVersionUID = 1049964628143811505L;

    @Column(name = "kind_id")
    private String kindId;

    @Column(name = "person_id")
    private String personId;

    private String name;

    private String param;

    private Integer sequence;

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
