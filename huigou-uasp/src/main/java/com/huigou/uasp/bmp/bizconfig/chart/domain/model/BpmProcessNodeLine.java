package com.huigou.uasp.bmp.bizconfig.chart.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 节点关联关系
 * 
 * @author xx
 *         BPM_PROCESS_NODE_LINE
 * @date 2017-04-17 09:23
 */
@Entity
@Table(name = "BPM_PROCESS_NODE_LINE")
public class BpmProcessNodeLine extends AbstractEntity {

    private static final long serialVersionUID = -5097549089130525902L;

    /**
     * business_process_id
     **/
    @Column(name = "business_process_id", length = 32)
    private String businessProcessId;

    /**
     * line_type
     **/
    @Column(name = "line_type", length = 32)
    private String lineType;

    /**
     * name
     **/
    @Column(name = "name", length = 64)
    private String name;

    /**
     * from_node
     **/
    @Column(name = "from_node", length = 32)
    private String fromNode;

    /**
     * to_node
     **/
    @Column(name = "to_node", length = 32)
    private String toNode;

    public String getBusinessProcessId() {
        return this.businessProcessId;
    }

    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    public String getLineType() {
        return this.lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromNode() {
        return this.fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return this.toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

}
