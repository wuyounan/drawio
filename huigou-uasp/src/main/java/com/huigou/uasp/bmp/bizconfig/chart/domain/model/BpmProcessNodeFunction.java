package com.huigou.uasp.bmp.bizconfig.chart.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 活动节点功能
 * 
 * @author
 *         BPM_PROCESS_NODE_FUNCTION
 * @date 2018-01-29 09:47
 */
@Entity
@Table(name = "BPM_PROCESS_NODE_FUNCTION")
public class BpmProcessNodeFunction extends AbstractEntity {

    private static final long serialVersionUID = -1646069363896692589L;

    /**
	*
	**/
    @Column(name = "view_id", length = 32)
    private String viewId;

    /**
     * 流程ID
     **/
    @Column(name = "business_process_id", length = 32)
    private String businessProcessId;

    /**
     * 编码
     **/
    @Column(name = "code", length = 32)
    private String code;

    /**
     * 名称
     **/
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 图标
     **/
    @Column(name = "icon", length = 32)
    private String icon;

    /**
     * 连接
     **/
    @Column(name = "url", length = 256)
    private String url;

    /**
     * 参数
     **/
    @Column(name = "param", length = 256)
    private String param;

    /**
     * 序号
     **/
    @Column(name = "sequence", length = 22)
    private Integer sequence;

    @Transient
    private Integer isFunction;

    public String getViewId() {
        return this.viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getBusinessProcessId() {
        return this.businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getIsFunction() {
        return isFunction;
    }

    public void setIsFunction(Integer isFunction) {
        this.isFunction = isFunction;
    }

    public void checkConstraints() {
        Assert.hasText(viewId, "节点ID不能为空!");
        Assert.hasText(businessProcessId, "流程ID不能为空!");
    }
}
