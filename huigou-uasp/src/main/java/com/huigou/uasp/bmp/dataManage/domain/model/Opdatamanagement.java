package com.huigou.uasp.bmp.dataManage.domain.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;

/**
 * 数据管理权限授权
 * 
 * @author xx
 *         SA_OPDATAMANAGEMENT
 * @date 2018-09-05 17:15
 */
@Entity
@Table(name = "SA_OPDATAMANAGEMENT")
@EntityListeners({ CreatorAndModifierListener.class })
public class Opdatamanagement extends AbstractEntity {

    private static final long serialVersionUID = -6484320213334510754L;

    /**
     * 数据管理权限类别
     **/
    @Column(name = "data_manage_id", length = 32)
    private String dataManageId;

    /**
     * 数据取值定义ID
     **/
    @Column(name = "data_managedetal_id", length = 32)
    private String dataManagedetalId;

    /**
     * 管理者ID
     **/
    @Column(name = "manager_id", length = 65)
    private String managerId;

    /**
     * 创建人信息
     */
    @Embedded
    private Creator creator;

    public String getDataManageId() {
        return this.dataManageId;
    }

    public void setDataManageId(String dataManageId) {
        this.dataManageId = dataManageId;
    }

    public String getDataManagedetalId() {
        return this.dataManagedetalId;
    }

    public void setDataManagedetalId(String dataManagedetalId) {
        this.dataManagedetalId = dataManagedetalId;
    }

    public String getManagerId() {
        return this.managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public void checkConstraints() {
        Assert.hasText(dataManageId, "管理权限类别ID不能为空!");
        Assert.hasText(dataManagedetalId, "管理权限数据取值定义ID不能为空!");
    }
}
