package com.huigou.report.cboard.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 指标展示界面参数
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
@Entity
@Table(name = "RP_CBoard_Definition_UI_Param")
public class CboardDefinitionUIParam extends AbstractEntity {

    private static final long serialVersionUID = 7980888827826604463L;

    private String code;

    private String name;

    @Column(name = "ui_param_kind", length = 32)
    private String uiParamKind;

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

    public String getUiParamKind() {
        return uiParamKind;
    }

    public void setUiParamKind(String uiParamKind) {
        this.uiParamKind = uiParamKind;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
