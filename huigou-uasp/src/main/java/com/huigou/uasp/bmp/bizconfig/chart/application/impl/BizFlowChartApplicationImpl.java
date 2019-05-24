package com.huigou.uasp.bmp.bizconfig.chart.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.bizconfig.chart.application.BizFlowChartApplication;
import com.huigou.uasp.bmp.bizconfig.chart.application.FlowChartPermissionsApplication;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessAreas;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNode;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeLine;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeTemp;
import com.huigou.uasp.bmp.bizconfig.chart.domain.query.ProcessNodeFunctionQueryRequest;
import com.huigou.uasp.bmp.bizconfig.chart.repository.BpmProcessAreasRepository;
import com.huigou.uasp.bmp.bizconfig.chart.repository.BpmProcessNodeFunctionRepository;
import com.huigou.uasp.bmp.bizconfig.chart.repository.BpmProcessNodeLineRepository;
import com.huigou.uasp.bmp.bizconfig.chart.repository.BpmProcessNodeRepository;
import com.huigou.uasp.bmp.bizconfig.chart.repository.BpmProcessNodeTempRepository;
import com.huigou.uasp.bmp.bizconfig.process.application.BizBusinessProcessApplication;
import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;
import com.huigou.uasp.bmp.bizconfig.process.repository.BpmBusinessProcessRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ListUtil;
import com.huigou.util.StringUtil;

/**
 * 流程图绘制
 *
 * @author xx
 * @version V1.0
 * @ClassName: FlowNodeApplicationImpl
 * @date 2017-03-06 14:49
 */
@Service("bizFlowChartApplication")
public class BizFlowChartApplicationImpl extends BaseApplication implements BizFlowChartApplication {
    @Autowired
    private BpmProcessNodeRepository bpmProcessNodeRepository;

    @Autowired
    private BpmProcessNodeTempRepository bpmProcessNodeTempRepository;

    @Autowired
    private BpmProcessNodeLineRepository bpmProcessNodeLineRepository;

    @Autowired
    private BpmProcessNodeFunctionRepository bpmProcessNodeFunctionRepository;

    @Autowired
    private BpmProcessAreasRepository bpmProcessAreasRepository;

    @Autowired
    private BpmBusinessProcessRepository bpmBusinessProcessRepository;

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Autowired
    private BizBusinessProcessApplication bpmBusinessProcessApplication;

    @Override
    public BpmBusinessProcess loadBusinessProcess(String businessProcessId) {
        BpmBusinessProcess process = bpmBusinessProcessRepository.findOne(businessProcessId);
        Assert.notNull(process, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, businessProcessId, "流程"));
        return process;
    }

    @Override
    public BpmBusinessProcess loadBusinessProcessByCode(String code) {
        Assert.hasText(code, CommonDomainConstants.ID_NOT_BLANK);
        List<BpmBusinessProcess> datas = bpmBusinessProcessRepository.findByCode(code);
        Assert.notEmpty(datas, String.format("未找到[%s]对应的流程记录!", code));
        return datas.get(0);
    }

    @Override
    public Map<String, Object> queryFlowNodesAndLines(String businessProcessId) {
        Assert.hasText(businessProcessId, CommonDomainConstants.ID_NOT_BLANK);
        Map<String, Object> map = new HashMap<String, Object>();
        List<BpmProcessNode> nodes = bpmProcessNodeRepository.findByBusinessProcessIdOrderByObjectKindCodeAsc(businessProcessId);
        List<BpmProcessNodeLine> lines = bpmProcessNodeLineRepository.findByBusinessProcessId(businessProcessId);
        List<BpmProcessAreas> areas = bpmProcessAreasRepository.findByBusinessProcessId(businessProcessId);
        // 处理ID 页面图像显示的ID与数据库存储的ID不同
        for (BpmProcessNode node : nodes) {
            node.setId(node.getViewId());
        }
        map.put("nodes", nodes);
        map.put("lines", lines);
        map.put("areas", areas);
        return map;
    }

    @Override
    public BpmProcessNode loadFlowNodeByViewId(String businessProcessId, String viewId) {
        List<BpmProcessNode> nodes = this.bpmProcessNodeRepository.findOneByBusinessProcessIdAndViewId(businessProcessId, viewId);
        if (nodes != null && nodes.size() > 0) {
            return nodes.get(0);
        }
        return null;
    }

    /**
     * 删除节点
     *
     * @param nodes
     *            节点列表
     * @return
     */
    private void deleteNode(List<BpmProcessNode> nodes) {
        if (nodes != null && nodes.size() > 0) {
            for (BpmProcessNode node : nodes) {
                bpmProcessNodeRepository.delete(node);
            }
        }
    }

    /**
     * 删除临时节点
     * 
     * @param businessProcessId
     * @param nodes
     */
    private void deleteNodeTemp(String businessProcessId, List<BpmProcessNodeTemp> nodes) {
        if (nodes != null && nodes.size() > 0) {
            for (BpmProcessNodeTemp node : nodes) {
                this.bpmProcessNodeFunctionRepository.deleteByViewId(node.getViewId());
            }
        }
        this.bpmProcessNodeTempRepository.deleteByProcessId(businessProcessId);
    }

    @Override
    @Transactional
    public void deleteFlowChart(String businessProcessId) {
        Assert.hasText(businessProcessId, CommonDomainConstants.ID_NOT_BLANK);
        List<BpmProcessNode> dbNodes = bpmProcessNodeRepository.findByBusinessProcessId(businessProcessId);
        this.deleteNode(dbNodes);
        bpmProcessNodeLineRepository.deleteLineByProcessId(businessProcessId);
        List<BpmProcessNodeTemp> dbNodeTemps = bpmProcessNodeTempRepository.findByBusinessProcessId(businessProcessId);
        this.deleteNodeTemp(businessProcessId, dbNodeTemps);
        bpmProcessNodeFunctionRepository.deleteByProcessId(businessProcessId);
        bpmProcessAreasRepository.deleteByProcessId(businessProcessId);
        BpmBusinessProcess businessProcess = bpmBusinessProcessRepository.findOne(businessProcessId);
        businessProcess.setIsFlowChart(0);
        bpmBusinessProcessRepository.save(businessProcess);
    }

    /**
     * 按viewId 组合节点MAP
     * 
     * @param tenantId
     * @param businessProcessId
     * @return
     */
    private Map<String, BpmProcessNode> getProcessNodeMap(String businessProcessId) {
        List<BpmProcessNode> dbNodes = bpmProcessNodeRepository.findByBusinessProcessId(businessProcessId);
        Map<String, BpmProcessNode> map = new HashMap<String, BpmProcessNode>(dbNodes.size());
        for (BpmProcessNode node : dbNodes) {
            map.put(node.getViewId(), node);
        }
        return map;
    }

    /**
     * 按viewId 组合临时节点MAP
     * 
     * @param tenantId
     * @param businessProcessId
     * @return
     */
    private Map<String, BpmProcessNodeTemp> getProcessNodeTempMap(String businessProcessId) {
        List<BpmProcessNodeTemp> dbNodes = this.bpmProcessNodeTempRepository.findByBusinessProcessId(businessProcessId);
        Map<String, BpmProcessNodeTemp> map = new HashMap<String, BpmProcessNodeTemp>(dbNodes.size());
        for (BpmProcessNodeTemp node : dbNodes) {
            map.put(node.getViewId(), node);
        }
        return map;
    }

    @Override
    @Transactional
    public void saveFlowNodesAndLines(String businessProcessId, String chartDirection, List<BpmProcessNode> nodes, List<BpmProcessNodeLine> lines,
                                      List<BpmProcessAreas> areas) {
        Assert.hasText(businessProcessId, CommonDomainConstants.ID_NOT_BLANK);
        BpmBusinessProcess businessProcess = bpmBusinessProcessRepository.findOne(businessProcessId);
        Assert.notNull(businessProcess, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, businessProcessId, "流程"));
        // 数据库中已存在的节点
        Map<String, BpmProcessNode> dbNodes = this.getProcessNodeMap(businessProcessId);
        // 临时节点
        Map<String, BpmProcessNodeTemp> dbNodeTemps = this.getProcessNodeTempMap(businessProcessId);
        if (nodes != null && nodes.size() > 0) {
            List<BpmProcessNode> saveNodes = new ArrayList<BpmProcessNode>(nodes.size());
            String viewId = null;
            for (BpmProcessNode node : nodes) {
                viewId = node.getViewId();
                BpmProcessNode oldNode = dbNodes.get(viewId);
                if (oldNode == null) {
                    oldNode = node;
                    oldNode.setBusinessProcessId(businessProcessId);
                } else {
                    oldNode.setCode(node.getCode());
                    oldNode.setName(node.getName());
                    oldNode.setObjectKindCode(node.getObjectKindCode());
                    oldNode.setXaxis(node.getXaxis());
                    oldNode.setYaxis(node.getYaxis());
                    oldNode.setRuleKind(node.getRuleKind());
                    oldNode.setInterfaceKind(node.getInterfaceKind());
                    oldNode.setQuoteId(node.getQuoteId());
                    dbNodes.put(viewId, null);
                }
                // 处理临时节点中存储的数据
                BpmProcessNodeTemp processNodeTemp = dbNodeTemps.get(viewId);
                if (processNodeTemp != null) {
                    oldNode.setLinkKindCodes(processNodeTemp.getLinkKindCodes());
                    oldNode.setOwnerId(processNodeTemp.getOwnerId());
                    oldNode.setOwnerName(processNodeTemp.getOwnerName());
                    oldNode.setRemark(processNodeTemp.getRemark());
                    oldNode.setFunctionCode(processNodeTemp.getFunctionCode());
                    oldNode.setEnName(processNodeTemp.getEnName());
                    oldNode.setNodeCode(processNodeTemp.getNodeCode());
                    dbNodeTemps.put(viewId, null);
                }
                Assert.hasText(oldNode.getViewId(), CommonDomainConstants.ID_NOT_BLANK);
                saveNodes.add(oldNode);
            }
            bpmProcessNodeRepository.save(saveNodes);
            // 删除原有连线
            bpmProcessNodeLineRepository.deleteLineByProcessId(businessProcessId);
            for (BpmProcessNodeLine line : lines) {
                line.setBusinessProcessId(businessProcessId);
            }
            bpmProcessNodeLineRepository.save(lines);
            // 设置流程存在流程图
            businessProcess.setIsFlowChart(1);
        } else {
            businessProcess.setIsFlowChart(0);
        }
        // 修改流程图定义
        businessProcess.setChartDirection(chartDirection);
        bpmBusinessProcessRepository.save(businessProcess);
        // 不存在的节点
        List<BpmProcessNode> delNodes = new ArrayList<BpmProcessNode>(dbNodes.size());
        for (String key : dbNodes.keySet()) {
            BpmProcessNode node = dbNodes.get(key);
            if (node != null) {
                delNodes.add(node);
            }
        }
        // 删除不存在的节点
        this.deleteNode(delNodes);
        // 不存在的临时节点
        List<BpmProcessNodeTemp> delNodeTemps = new ArrayList<BpmProcessNodeTemp>(dbNodeTemps.size());
        for (String key : dbNodeTemps.keySet()) {
            BpmProcessNodeTemp node = dbNodeTemps.get(key);
            if (node != null) {
                delNodeTemps.add(node);
            }
        }
        // 删除全部临时节点
        this.deleteNodeTemp(businessProcessId, delNodeTemps);
        // 处理区域信息保存
        bpmProcessAreasRepository.deleteByProcessId(businessProcessId);
        if (areas != null && areas.size() > 0) {
            for (BpmProcessAreas area : areas) {
                area.setId(null);
                area.setBusinessProcessId(businessProcessId);
            }
            bpmProcessAreasRepository.save(areas);
        }
    }

    @Override
    @Transactional
    public BpmProcessNodeTemp saveFlowNode(String businessProcessId, BpmProcessNodeTemp node) {
        Assert.hasText(businessProcessId, CommonDomainConstants.ID_NOT_BLANK);
        BpmBusinessProcess businessProcess = bpmBusinessProcessRepository.findOne(businessProcessId);
        Assert.notNull(businessProcess, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, businessProcessId, "流程"));
        String viewId = node.getViewId();
        // 保存到临时记录 不影响正常节点保存
        List<BpmProcessNodeTemp> oldNodes = this.bpmProcessNodeTempRepository.findOneByBusinessProcessIdAndViewId(businessProcessId, viewId);
        BpmProcessNodeTemp oldNode = null;
        if (oldNodes != null && oldNodes.size() > 0) {
            oldNode = oldNodes.get(0);
            node.setId(oldNode.getId());
            oldNode.fromEntity(node);
        } else {
            oldNode = node;
            oldNode.setId(null);
            oldNode.setBusinessProcessId(businessProcessId);
        }
        List<BpmProcessNodeFunction> funs = bpmProcessNodeFunctionRepository.findByViewIdOrderBySequenceAsc(viewId);
        if (funs != null && funs.size() > 0) {
            Map<String, String> kindCodeMap = new LinkedHashMap<>(funs.size());
            for (BpmProcessNodeFunction fun : funs) {
                kindCodeMap.put(fun.getIcon(), "1");
            }
            oldNode.setLinkKindCodes(ListUtil.join(kindCodeMap.keySet(), ","));
        } else {
            oldNode.setLinkKindCodes("");
        }
        oldNode = bpmProcessNodeTempRepository.save(oldNode);
        return oldNode;
    }

    @Override
    @Transactional
    public String saveProcessNodeFunction(BpmProcessNodeFunction processNodeFunction) {
        Assert.notNull(processNodeFunction, CommonDomainConstants.OBJECT_NOT_NULL);
        processNodeFunction.checkConstraints();
        if (processNodeFunction.isNew()) {
            processNodeFunction.setSequence(this.commonDomainService.getNextSequence(BpmProcessNodeFunction.class, "viewId", processNodeFunction.getViewId()));
        }
        processNodeFunction = (BpmProcessNodeFunction) this.commonDomainService.loadAndFillinProperties(processNodeFunction);
        processNodeFunction = bpmProcessNodeFunctionRepository.save(processNodeFunction);
        return processNodeFunction.getId();
    }

    @Override
    public BpmProcessNodeFunction loadProcessNodeFunction(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return bpmProcessNodeFunctionRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteProcessNodeFunction(List<String> ids) {
        List<BpmProcessNodeFunction> objs = bpmProcessNodeFunctionRepository.findAll(ids);
        bpmProcessNodeFunctionRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryProcessNodeFunction(ProcessNodeFunctionQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "processNodeFunction");
        return this.sqlExecutorDao.executeQuery(queryDescriptor, queryRequest);
    }

    @Override
    @Transactional
    public void updateProcessNodeFunctionSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(BpmProcessNodeFunction.class, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryFlowNodesAndLinesAndFunction(String businessProcessId) {
        BpmBusinessProcess businessProcess = bpmBusinessProcessApplication.loadBusinessProcess(businessProcessId);
        Assert.notNull(businessProcess);
        Map<String, Object> map = this.queryFlowNodesAndLines(businessProcessId);
        Operator operator = ThreadLocalUtil.getOperator();
        // 查询定义的功能
        List<BpmProcessNodeFunction> tmps = bpmProcessNodeFunctionRepository.findByBusinessProcessIdOrderBySequenceAsc(businessProcessId);
        FlowChartPermissionsApplication application = null;
        List<BpmProcessNodeFunction> objs = null;
        String userCode = businessProcess.getUserCode();
        if (StringUtil.isNotBlank(userCode)) {
            application = ApplicationContextWrapper.getBean(userCode, FlowChartPermissionsApplication.class);
        }
        // 权限校验
        if (application != null) {
            objs = application.checkProcessNodeFunction(tmps);
        } else {// 默认使用系统中的角色权限校验
            objs = this.checkProcessNodeFunction(tmps);
        }
        Map<String, List<BpmProcessNodeFunction>> funs = new HashMap<>();
        if (objs != null && objs.size() > 0) {
            List<BpmProcessNodeFunction> temp = null;
            for (BpmProcessNodeFunction obj : objs) {
                if (funs.containsKey(obj.getViewId())) {
                    temp = funs.get(obj.getViewId());
                } else {
                    temp = new ArrayList<BpmProcessNodeFunction>();
                }
                temp.add(obj);
                funs.put(obj.getViewId(), temp);
            }
        }
        map.put("functions", funs);
        // 节点权限验证
        List<BpmProcessNode> nodes = (List<BpmProcessNode>) map.get("nodes");
        if (nodes != null && nodes.size() > 0) {
            boolean flag = true;
            String functionCode = "";
            List<String> noPermissions = new ArrayList<>();
            for (BpmProcessNode node : nodes) {
                functionCode = node.getFunctionCode();
                if (StringUtil.isNotBlank(functionCode)) {
                    // 校验当前登录用户是否有功能权限
                    flag = accessApplication.checkPersonFunPermissions(operator.getUserId(), functionCode);
                    if (!flag) {
                        // 没有权限
                        noPermissions.add(node.getId());
                    }
                }
            }
            map.put("noPermissions", ListUtil.join(noPermissions, ","));
        }
        return map;
    }

    /**
     * 默认系统权限校验
     * 
     * @param nodeFunctions
     * @return
     */
    private List<BpmProcessNodeFunction> checkProcessNodeFunction(List<BpmProcessNodeFunction> nodeFunctions) {
        Operator operator = ThreadLocalUtil.getOperator();
        List<BpmProcessNodeFunction> objs = new ArrayList<>(nodeFunctions.size());
        if (nodeFunctions == null || nodeFunctions.size() == 0) {
            return objs;
        }
        // 判断功能是否有权限
        String sql = "select count(0) from sa_opfunction t where t.code=?";
        boolean flag = true;
        for (BpmProcessNodeFunction fun : nodeFunctions) {
            int count = this.sqlExecutorDao.queryToInt(sql, fun.getCode());
            if (count == 0) {
                fun.setIsFunction(0);
                objs.add(fun);
            } else {
                flag = accessApplication.checkPersonFunPermissions(operator.getUserId(), fun.getCode());
                if (flag) {
                    fun.setIsFunction(1);
                    objs.add(fun);
                }
            }
        }
        return objs;
    }

}
