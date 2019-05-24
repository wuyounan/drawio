package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 数据管理权限包含维度明细
 * 
 * @author xx
 *         SA_OPDATAMANAGETYPEKIND
 * @date 2018-09-04 11:58
 */
@Entity
@Table(name = "SA_OPDATAMANAGETYPEKIND")
public class Opdatamanagetypekind extends AbstractEntity {

    private static final long serialVersionUID = 6868315319355988302L;

    /**
     * 数据管理权限类别
     **/
    @Column(name = "data_manage_id", length = 32)
    private String dataManageId;

    /**
     * 资源维度ID
     **/
    @Column(name = "data_kind_id", length = 32)
    private String dataKindId;

    public String getDataManageId() {
        return this.dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getDataKindId() {
        return this.dataKindId;
    }

    public void setDataKindId(String dataKindId) {
        this.dataKindId = dataKindId;
    }

}
