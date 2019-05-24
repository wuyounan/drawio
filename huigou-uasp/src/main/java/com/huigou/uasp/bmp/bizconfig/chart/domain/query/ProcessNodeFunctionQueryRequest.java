package com.huigou.uasp.bmp.bizconfig.chart.domain.query;

import org.springframework.util.Assert;

import com.huigou.data.domain.query.QueryAbstractRequest;

/**
 * 活动节点功能
 * 
 * @author
 *         BPM_PROCESS_NODE_FUNCTION
 * @date 2018-01-29 09:50
 */
public class ProcessNodeFunctionQueryRequest extends QueryAbstractRequest {

    protected String viewId;

    protected String businessProcessId;

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

    public void checkConstraints() {
        Assert.hasText(viewId, "节点ID不能为空!");
        Assert.hasText(businessProcessId, "流程ID不能为空!");
    }
}
