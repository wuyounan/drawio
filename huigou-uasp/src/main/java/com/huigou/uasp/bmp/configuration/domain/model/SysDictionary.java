package com.huigou.uasp.bmp.configuration.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.huigou.annotation.JsonIgnore;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

/**
 * 系统字典
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_Dictionary")
public class SysDictionary extends BaseInfoWithFolderAbstractEntity {

    private static final long serialVersionUID = -3306053232517324053L;

    @Column(name = "kind_id")
    private Integer kindId;

    private String remark;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dictionary_id")
    private List<SysDictionaryDetail> details;

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    @JsonIgnore()
    public List<SysDictionaryDetail> getDetails() {
        return details;
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void setDetails(List<? extends AbstractEntity> details) {
         this.details = (List<SysDictionaryDetail>) details;
    }
    
}
