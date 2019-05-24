package com.huigou.uasp.bmp.bizconfig.chart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.bizconfig.chart.application.BizFlowChartApplication;
import com.huigou.uasp.bmp.bizconfig.chart.domain.FlowInterfaceKind;
import com.huigou.uasp.bmp.bizconfig.chart.domain.FlowRuleKind;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessAreas;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNode;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeLine;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeTemp;
import com.huigou.uasp.bmp.bizconfig.chart.domain.query.ProcessNodeFunctionQueryRequest;
import com.huigou.uasp.bmp.bizconfig.model.ObjectKindEnum;
import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;
import com.huigou.uasp.client.CommonController;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 流程图绘制
 *
 * @author xx
 * @version V1.0
 * @ClassName: FlowNodeController
 * @date 2017-03-06 14:49
 */
@Controller
@ControllerMapping("bizFlowChart")
public class BizFlowChartController extends CommonController {

    @Autowired
    private BizFlowChartApplication bizFlowChartApplication;

    protected String getPagePath() {
        return "/system/bizconfig/flowchart/";
    }

    public String forwardFlowchart() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        BpmBusinessProcess process = bizFlowChartApplication.loadBusinessProcess(businessProcessId);
        this.putAttribute("flowNodeKind", ObjectKindEnum.getFlowNodeKind());
        this.putAttribute("flowRuleKind", FlowRuleKind.getData());
        this.putAttribute("flowInterfaceKind", FlowInterfaceKind.getData());
        return this.forward("flowchart", process);
    }

    public String queryFlowNodesAndLines() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        Map<String, Object> data = bizFlowChartApplication.queryFlowNodesAndLines(businessProcessId);
        return this.toResult(data);
    }

    public String showLoadFlowNode() {
        SDO sdo = this.getSDO();
        String viewId = sdo.getString("viewId");
        String businessProcessId = sdo.getString("businessProcessId");
        String objectKindCode = sdo.getString("objectKindCode");
        BpmProcessNode node = bizFlowChartApplication.loadFlowNodeByViewId(businessProcessId, viewId);
        if (node != null) {
            objectKindCode = node.getObjectKindCode();
        }
        this.putAttribute("flowNodeKind", ObjectKindEnum.getFlowNodeKind());
        this.putAttribute("flowRuleKind", FlowRuleKind.getData());
        this.putAttribute("flowInterfaceKind", FlowInterfaceKind.getData());
        this.putAttribute("viewId", viewId);
        this.putAttribute("objectKindCode", objectKindCode);
        return forward(String.format("node%sDetail", objectKindCode), node);
    }

    public String saveFlowNodesAndLines() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        String chartDirection = sdo.getString("chartDirection");
        List<BpmProcessNode> nodes = sdo.getList("nodes", BpmProcessNode.class);
        List<BpmProcessNodeLine> lines = sdo.getList("lines", BpmProcessNodeLine.class);
        List<BpmProcessAreas> areas = sdo.getList("areas", BpmProcessAreas.class);
        bizFlowChartApplication.saveFlowNodesAndLines(businessProcessId, chartDirection, nodes, lines, areas);
        return success();
    }

    public String saveFlowNode() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        BpmProcessNodeTemp node = sdo.toObject(BpmProcessNodeTemp.class);
        node = bizFlowChartApplication.saveFlowNode(businessProcessId, node);
        return success(node);
    }

    public String deleteFlowChart() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        bizFlowChartApplication.deleteFlowChart(businessProcessId);
        return success();
    }

    public String queryProcessNodeFunction() {
        SDO sdo = this.getSDO();
        ProcessNodeFunctionQueryRequest queryRequest = sdo.toQueryRequest(ProcessNodeFunctionQueryRequest.class);
        Map<String, Object> data = bizFlowChartApplication.queryProcessNodeFunction(queryRequest);
        return toResult(data);
    }

    public String showInsertProcessNodeFunction() {
        SDO sdo = this.getSDO();
        return forward("processNodeFunctionDetail", sdo);
    }

    public String saveProcessNodeFunction() {
        SDO sdo = this.getSDO();
        BpmProcessNodeFunction processNodeFunction = sdo.toObject(BpmProcessNodeFunction.class);
        String id = bizFlowChartApplication.saveProcessNodeFunction(processNodeFunction);
        return success(id);
    }

    public String showLoadProcessNodeFunction() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        BpmProcessNodeFunction processNodeFunction = bizFlowChartApplication.loadProcessNodeFunction(id);
        return forward("processNodeFunctionDetail", processNodeFunction);
    }

    public String deleteProcessNodeFunction() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        bizFlowChartApplication.deleteProcessNodeFunction(ids);
        return success();
    }

    public String updateProcessNodeFunctionSequence() {
        SDO sdo = this.getSDO();
        Map<String, Integer> map = sdo.getStringMap("data");
        bizFlowChartApplication.updateProcessNodeFunctionSequence(map);
        return success();
    }

    public String showViewFlowchart() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        BpmBusinessProcess process = null;
        if (StringUtil.isNotBlank(businessProcessId)) {
            process = bizFlowChartApplication.loadBusinessProcess(businessProcessId);
        } else {
            String code = sdo.getString("code");
            process = bizFlowChartApplication.loadBusinessProcessByCode(code);
        }
        this.putAttribute("businessProcessId", process.getId());
        this.putAttribute("processRemark", process.getFlowAim());
        return this.forward("flowchartUsed", process);
    }

    public String queryFlowNodesAndLinesAndFunction() {
        SDO sdo = this.getSDO();
        String businessProcessId = sdo.getString("businessProcessId");
        Map<String, Object> data = bizFlowChartApplication.queryFlowNodesAndLinesAndFunction(businessProcessId);
        return this.toResult(data);
    }
}
