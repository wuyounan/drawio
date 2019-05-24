package com.huigou.uasp.bmp.bizconfig.chart.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessAreas;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNode;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeLine;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeTemp;
import com.huigou.uasp.bmp.bizconfig.chart.domain.query.ProcessNodeFunctionQueryRequest;
import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;

/**
 * 流程图绘制
 * 
 * @author xx
 * @date 2017-03-06 14:49
 */
public interface BizFlowChartApplication {
    /**
     * 查询文件
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/businessChart.xml";

    /**
     * 查询流程定义
     * 
     * @param businessProcessId
     * @return
     */
    BpmBusinessProcess loadBusinessProcess(String businessProcessId);

    /**
     * 根据编码查询流程定义
     * 
     * @param code
     * @return
     */
    BpmBusinessProcess loadBusinessProcessByCode(String code);

    /**
     * 查询 流程图节点级连线
     * 
     * @author xx
     * @return SDO
     */
    Map<String, Object> queryFlowNodesAndLines(String businessProcessId);

    /**
     * 加载 流程图节点
     * 
     * @author xx
     * @return SDO
     */
    BpmProcessNode loadFlowNodeByViewId(String businessProcessId, String viewId);

    /**
     * 统一保存数据
     * 
     * @param nodes
     * @param lines
     */
    void saveFlowNodesAndLines(String businessProcessId, String chartDirection, List<BpmProcessNode> nodes, List<BpmProcessNodeLine> lines, List<BpmProcessAreas> areas);

    /**
     * 保存节点属性
     * 
     * @param businessProcessId
     * @param node
     */
    BpmProcessNodeTemp saveFlowNode(String businessProcessId, BpmProcessNodeTemp node);

    /**
     * 删除流程图
     * 
     * @param businessProcessId
     *            流程ID
     */
    void deleteFlowChart(String businessProcessId);

    /**
     * 保存 活动节点功能
     * 
     * @author
     * @param params
     */
    String saveProcessNodeFunction(BpmProcessNodeFunction processNodeFunction);

    /**
     * 加载 活动节点功能
     * 
     * @author
     * @return SDO
     */
    BpmProcessNodeFunction loadProcessNodeFunction(String id);

    /**
     * 删除 活动节点功能
     * 
     * @author
     */
    void deleteProcessNodeFunction(List<String> ids);

    /**
     * 查询 活动节点功能
     * 
     * @author
     * @return SDO
     */
    Map<String, Object> queryProcessNodeFunction(ProcessNodeFunctionQueryRequest queryRequest);

    /**
     * 保存 活动节点功能排序号
     * 
     * @param map
     */
    void updateProcessNodeFunctionSequence(Map<String, Integer> map);

    /**
     * 查询 流程图节点，连线及功能
     * 
     * @param businessProcessId
     * @return
     */
    Map<String, Object> queryFlowNodesAndLinesAndFunction(String businessProcessId);

}
