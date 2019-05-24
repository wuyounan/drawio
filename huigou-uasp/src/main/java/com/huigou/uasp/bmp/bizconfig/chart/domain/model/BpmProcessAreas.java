package com.huigou.uasp.bmp.bizconfig.chart.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 流程图区域组织划分框
 * 
 * @author
 *         BPM_PROCESS_AREAS
 * @date 2018-03-07 11:15
 */
@Entity
@Table(name = "BPM_PROCESS_AREAS")
public class BpmProcessAreas extends AbstractEntity {


    private static final long serialVersionUID = -5317015924359423452L;

    /**
     * business_process_id
     **/
    @Column(name = "business_process_id", length = 32)
    private String businessProcessId;

    /**
     * NAME
     **/
    @Column(name = "name", length = 64)
    private String name;

    /**
     * LEFT
     **/
    @Column(name = "left", length = 22)
    private BigDecimal left;

    /**
     * TOP
     **/
    @Column(name = "top", length = 22)
    private BigDecimal top;

    /**
     * COLOR
     **/
    @Column(name = "color", length = 32)
    private String color;

    /**
     * WIDTH
     **/
    @Column(name = "width", length = 22)
    private BigDecimal width;

    /**
     * HEIGHT
     **/
    @Column(name = "height", length = 22)
    private BigDecimal height;

    public String getBusinessProcessId() {
        return this.businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLeft() {
        return this.left;
    }

    public void setLeft(BigDecimal left) {
        this.left = left;
    }

    public BigDecimal getTop() {
        return this.top;
    }

    public void setTop(BigDecimal top) {
        this.top = top;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getWidth() {
        return this.width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return this.height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

}
