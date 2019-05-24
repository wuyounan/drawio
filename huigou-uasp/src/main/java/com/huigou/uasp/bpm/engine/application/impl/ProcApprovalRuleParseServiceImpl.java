package com.huigou.uasp.bpm.engine.application.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.IntervalKind;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.fn.impl.OrgFun;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.proxy.OrgApplicationProxy;
import com.huigou.uasp.bpm.configuration.application.ApprovalRuleApplication;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRule;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleElement;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandler;
import com.huigou.uasp.bpm.configuration.domain.model.ApprovalRuleHandlerAssist;
import com.huigou.uasp.bpm.configuration.domain.model.HandlerKind;
import com.huigou.uasp.bpm.engine.application.HandlerParseService;
import com.huigou.uasp.bpm.engine.application.ProcApprovalRuleParseService;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

@Service("procApprovalRuleParseService")
public class ProcApprovalRuleParseServiceImpl extends BaseApplication implements ProcApprovalRuleParseService {

    @Autowired
    private OrgApplicationProxy orgApplication;

    @Autowired
    private OrgFun orgFun;

    @Autowired
    private HandlerParseService handlerParseService;

    @Autowired
    private ApprovalRuleApplication approvalRuleApplication;

    public void setHandlerParseService(HandlerParseService handlerParseService) {
        this.handlerParseService = handlerParseService;
    }

    public void setOrgFun(OrgFun orgFun) {
        this.orgFun = orgFun;
    }

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/bpm.xml", "procApprovalRuleParse");
        return queryDescriptor.getSqlByName(name);
    }

    /**
     * 得到最近的规则配置组织ID
     * 
     * @param ownerOrgId
     *            业务组织ID
     * @param procId
     *            流程ID
     * @param procUnitId
     *            环节ID
     * @return
     */
    private String getNearestRuleOrgId(String ownerOrgId, String procId, String procUnitId) {
        String sql = this.getQuerySqlByName("selectNearestRuleOrgId");
        String result = this.sqlExecutorDao.queryOneToObject(sql, String.class, procId, procUnitId, ownerOrgId);
        if (StringUtil.isBlank(result)) {
            result = SystemCache.getParameter("HQOrganId", String.class);
        }
        return result;
    }

    /**
     * 内部解析流程规则 <br/>
     * <li>流程环节设置的审批规则,根据优先级排序 <li>得到一条规则的审批规则设置 <li>验证参数是否满足规则 <li>若满足规则，获取审批人 <li>若未满足返回到第2步
     * 
     * @param procId
     *            流程ID
     * @param procUnitId
     *            环节ID
     * @param params
     *            参数
     */
    private ApprovalRule internalParseProcApprovalRule(String procId, String procUnitId, Map<String, Object> params) {
        // 审批要素
        List<ApprovalRuleElement> ruleElements;
        String setStringValue, inputStringValue;
        float setFloatValue, inputFloatValue;
        // 区间类型
        IntervalKind intervalKind;
        boolean matchSuccess = true;

        ApprovalRule matchedRule = null;

        String ownerOrgId = ClassHelper.convert(params.get("ownerOrgId"), String.class, "");
        String ruleOrgId = this.getNearestRuleOrgId(ownerOrgId, procId, procUnitId);

        // Util.check(!StringUtil.isBlank(ruleOrgId), String.format("组织机构Id“%s”没有配置审批规则。", ownerOrgId));
        //
        // //当前机构的审批规则
        // String sql = this.getQuerySqlByName("queryRule");
        // Map<String, Object> queryParams = QueryParameter.buildParameters("orgId", ruleOrgId, "procId", procId, "procUnitId", procUnitId);
        // List<ApprovalRule> rules = this.generalRepository.query(sql, queryParams);
        // //别的机构分配给前机构的规则
        // sql = this.getQuerySqlByName("queryScopeRule");
        // List<ApprovalRule> scopeRules = this.generalRepository.query(sql, queryParams);
        //
        // rules.addAll(scopeRules);

        List<ApprovalRule> rules = this.queryScopeApprovalRules(procId, procUnitId, ruleOrgId, false);

        // end
        for (ApprovalRule rule : rules) {
            ruleElements = rule.getApprovalRuleElements();
            for (ApprovalRuleElement ruleElement : ruleElements) {
                inputStringValue = ClassHelper.convert(params.get(ruleElement.getElementCode()), String.class, "");
                Util.check(!StringUtil.isBlank(inputStringValue), String.format("审批要素%s没有赋值。", ruleElement.getElementCode()));
                switch (ruleElement.getOperatorKind()) {
                case EQ:
                    matchSuccess = inputStringValue.equals(ruleElement.getFvalueId());
                    break;
                case NOT_EQ:
                    matchSuccess = !inputStringValue.equals(ruleElement.getFvalueId());
                    break;
                case OIN:
                    setStringValue = String.format(",%s,", ruleElement.getFvalueId());
                    matchSuccess = setStringValue.contains(String.format(",%s,", inputStringValue));
                    break;
                case LT:
                case LE:
                case GT:
                case GE:
                    inputFloatValue = ClassHelper.convert(inputStringValue, Float.class);
                    setFloatValue = ClassHelper.convert(ruleElement.getFvalueId(), Float.class);
                    switch (ruleElement.getOperatorKind()) {
                    case LT:
                        matchSuccess = inputFloatValue < setFloatValue;
                        break;
                    case LE:
                        matchSuccess = inputFloatValue <= setFloatValue;
                        break;
                    case GT:
                        matchSuccess = inputFloatValue > setFloatValue;
                        break;
                    case GE:
                        matchSuccess = inputFloatValue >= setFloatValue;
                        break;
                    default:
                        matchSuccess = false;
                        break;
                    }
                    break;
                case INTERVAL:
                    intervalKind = IntervalKind.getIntervalKind(ruleElement.getFvalueId());

                    inputFloatValue = Float.parseFloat(inputStringValue);
                    switch (intervalKind) {
                    case OPEN:
                        matchSuccess = (intervalKind.getOperand1() < inputFloatValue) && (inputFloatValue < intervalKind.getOperand2());
                        break;
                    case LEF_OPEN:
                        matchSuccess = (intervalKind.getOperand1() < inputFloatValue) && (inputFloatValue <= intervalKind.getOperand2());
                        break;
                    case RIGHT_OPEN:
                        matchSuccess = (intervalKind.getOperand1() <= inputFloatValue) && (inputFloatValue < intervalKind.getOperand2());
                        break;
                    case CLOSE:
                        matchSuccess = (intervalKind.getOperand1() <= inputFloatValue) && (inputFloatValue <= intervalKind.getOperand2());
                        break;
                    }
                    break;
                default:
                    matchSuccess = false;
                    break;
                }
                // 一项未匹配成功，跳出循环，匹配下一个规则
                if (!matchSuccess) break;
            }
            if (matchSuccess) {
                matchedRule = rule;
                // ruleId = rule.getId();
                SDO bizData = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
                bizData.putProperty("procApprovalRuleFullName", rule.getFullName());
                break;
            }
        }
        if (rules.size() > 0 && matchSuccess) {
            List<ApprovalRuleHandler> handlers = matchedRule.getApprovalRuleHandlers();

            for (ApprovalRuleHandler ruleHandler : handlers) {
                buildProcHandlers(ruleHandler);
            }
            // organizeHandlers(handlers);
            return matchedRule;
        }
        return null;
    }

    private void internalBuildHandlers(List<OrgUnit> orgUnits, HandlerKind handlerKind, String handlerId) {
        this.handlerParseService.buildHandler(handlerKind, handlerId, orgUnits);
    }

    /**
     * 生成流程处理人
     */
    private void buildProcHandlers(ApprovalRuleHandler ruleHandler) {
        ruleHandler.getOrgUnits().clear();
        internalBuildHandlers(ruleHandler.getOrgUnits(), ruleHandler.getHandlerKind(), ruleHandler.getHandlerId());
        for (ApprovalRuleHandlerAssist item : ruleHandler.getAssists()) {
            this.internalBuildHandlers(item.getOrgUnits(), item.getHandlerKind(), item.getHandlerId());
        }
    }

    /**
     * 填充操作员环境参数
     * 
     * @param params
     * @param bizParams
     */
    private void fillOperatorContextParams(Map<String, Object> params, Map<String, Object> bizParams) {
        Operator operator = OpmUtil.getBizOperator();
        // 当前操作员系统参数
        params.put("orgAdminKind", operator.getOrgAdminKind()); // 机构类别
        params.put("deptKind", operator.getDeptKind()); // 部门类别
        params.put("orgId", operator.getOrgId()); // 机构ID
        params.put("deptId", operator.getDeptId()); // 部门ID
        params.put("posId", operator.getPositionId()); // 岗位ID
        params.put("psmId", operator.getPersonMemberId()); // 人员成员ID

        String ownerOrgId = null;
        if (bizParams != null) {
            ownerOrgId = ClassHelper.convert(bizParams.get("ownerOrgId"), String.class, "");
        }

        if (StringUtil.isBlank(ownerOrgId)) {
            ownerOrgId = operator.getOrgId();
        }

        params.put("ownerOrgId", ownerOrgId);
    }

    /**
     * 填充业务组织参数
     * 
     * @param params
     * @param bizParams
     */
    private void fillBizOrgParams(Map<String, Object> params, Map<String, Object> bizParams) {
        String manageOrgId = null;
        if (bizParams != null) {
            manageOrgId = ClassHelper.convert(bizParams.get(OpmUtil.BIZ_MANAGE_ORG_ID_FIELD_NAME), String.class);
        }

        if (Util.isEmptyString(manageOrgId)) {
            SDO bizData = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
            if (bizData != null) {
                manageOrgId = bizData.getProperty(OpmUtil.BIZ_MANAGE_ORG_ID_FIELD_NAME, String.class);
            }
        }
        boolean isIncludeBizOrg = !Util.isEmptyString(manageOrgId);

        if (isIncludeBizOrg) {
            Org org = orgApplication.loadOrg(manageOrgId);
            Assert.notNull(org, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, manageOrgId, "组织"));
            Util.check(org != null, String.format("没有找到“%s”对应的组织。", manageOrgId));

            params.put("bizOrgAdminKind", orgFun.getOrgAdminKindById(org.getOrgId())); // 业务机构类别
            params.put("bizOrgAreaKind", orgFun.getOrgProperty(org.getOrgId(), "orgAreaKind")); // 业务区域类别
            params.put("deptKind", orgFun.getOrgProperty(org.getOrgId(), "deptKind")); // 部门类别 TODO 是否加biz
            params.put("bizOrgFullId", org.getFullId());
            // ...
            params.put("bizOrgId", org.getOrgId()); // 业务组织（公司）ID
        }
    }

    /**
     * 调整参数
     * 
     * @param params
     * @param bizParams
     */
    private void adjustParams(Map<String, Object> params, Map<String, Object> bizParams) {
        if (bizParams != null) {
            params.putAll(bizParams);
        }
        SDO bizData = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        if (bizData != null) {
            bizData.putProperty("bizParams", params);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ApprovalRule> queryScopeApprovalRules(String procId, String procUnitId, String ownerOrgId, boolean includeClassification) {
        // 桥接
        String ruleOrgId = this.getNearestRuleOrgId(ownerOrgId, procId, procUnitId);

        Util.check(!StringUtil.isBlank(ruleOrgId), String.format("组织机构Id“%s”没有配置审批规则。", ownerOrgId));

        Integer nodeKindId = includeClassification ? ApprovalRule.NodeKind.CATEGORY.getId() : ApprovalRule.NodeKind.RULE.getId();

        // 当前机构的审批规则
        String sql = this.getQuerySqlByName("queryRule");
        Map<String, Object> queryParams = QueryParameter.buildParameters("orgId", ruleOrgId, "procId", procId, "procUnitId", procUnitId, "nodeKindId",
                                                                         nodeKindId);
        List<ApprovalRule> rules = this.generalRepository.query(sql, queryParams);
        // 别的机构分配给前机构的规则
        sql = this.getQuerySqlByName("queryScopeRule");
        List<ApprovalRule> scopeRules = this.generalRepository.query(sql, queryParams);

        rules.addAll(scopeRules);

        return rules;
    }

    @Override
    public ApprovalRule execute(String procId, String procUnitId, Map<String, Object> bizParams) {

        Map<String, Object> params = new HashMap<String, Object>();
        fillOperatorContextParams(params, bizParams);
        fillBizOrgParams(params, bizParams);
        adjustParams(params, bizParams);

        return internalParseProcApprovalRule(procId, procUnitId, params);
    }

}
