package com.huigou.uasp.bmp.portal.mainpage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 人员桌面分屏设置
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_PDScreen")
public class PersonDesktopScreen extends AbstractEntity {

    private static final long serialVersionUID = 7618129141376383765L;

    @Column(name = "person_id")
    private String personId;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

}
