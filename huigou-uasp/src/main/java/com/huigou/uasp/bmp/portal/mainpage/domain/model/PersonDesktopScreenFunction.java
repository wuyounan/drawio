package com.huigou.uasp.bmp.portal.mainpage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 用户桌面分屏功能
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_PDScreenFunction")
public class PersonDesktopScreenFunction extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "screen_id")
    private String screenId;

    @Column(name = "function_id")
    private String functionId;

    @Column(name = "sequence")
    private Integer sequence;

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
