package com.huigou.uasp.bmp.codingrule.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.codingrule.application.CodingRuleApplication;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRule;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("codingRule")
public class CodingRuleController extends CommonController {

    private static final String CODING_RULE_PAGE = "CodingRule";

    private static final String CODING_RULE_DETAIL_PAGE = "CodingRuleDetail";

    private static final String CODING_RULE_ID_KEY_NAME = "codingRuleId";

    @Autowired
    private CodingRuleApplication codingRuleApplication;

    protected String getPagePath() {
        return "/system/codingrule/";
    }

    @RequiresPermissions("CodingRule:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到编码规则列表页面")
    public String forwardCodingRule() {
        return forward(CODING_RULE_PAGE);
    }

    @RequiresPermissions("CodingRule:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加编码规则明细页面")
    public String showInsertCodingRule() {
        return forward(CODING_RULE_DETAIL_PAGE);
    }

    private void buildCodingRuleDetail(CodingRule codingRule, SDO params) {
        List<CodingRuleDetail> inputDetails = params.getList("detailData", CodingRuleDetail.class);
        codingRule.setInputDetails_(inputDetails);
        codingRule.addUpdateFields_(AbstractEntity.INPUT_DETAILS_FIELD_NAME);
    }

    @RequiresPermissions("CodingRule:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加编码规则")
    public String insertCodingRule() {
        SDO params = this.getSDO();
        CodingRule codingRule = params.toObject(CodingRule.class);
        buildCodingRuleDetail(codingRule, params);
        String id = codingRuleApplication.saveCodingRule(codingRule);
        return success(id);
    }

    @RequiresPermissions("CodingRule:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改编码规则明细页面")
    public String showUpdateCodingRule() {
        SDO sdo = this.getSDO();
        String id = sdo.getId();
        CodingRule codingRule = codingRuleApplication.loadCodingRule(id);
        return forward(CODING_RULE_DETAIL_PAGE, codingRule);
    }

    @RequiresPermissions("CodingRule:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改编码规则")
    public String updateCodingRule() {
        SDO params = this.getSDO();
        CodingRule codingRule = params.toObject(CodingRule.class);

        buildCodingRuleDetail(codingRule, params);

        codingRuleApplication.saveCodingRule(codingRule);
        return success();
    }

    @RequiresPermissions("CodingRule:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动编码规则")
    public String moveCodingRules() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getProperty(FOLDER_ID_KEY_NAME, String.class);
        codingRuleApplication.moveCodingRules(ids, folderId);
        return success();
    }

    @RequiresPermissions("CodingRule:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改编码规则")
    public String updateCodingRulesStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        Integer status = sdo.getInteger(STATUS_KEY_NAME);
        codingRuleApplication.updateCodingRulesStatus(ids, status);
        return success();
    }

    @RequiresPermissions("CodingRule:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除编码规则")
    public String deleteCodingRules() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getIds();
        codingRuleApplication.deleteCodingRules(ids);
        return success();
    }

    @RequiresPermissions("CodingRule:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询编码规则")
    public String slicedQueryCodingRules() {
        SDO sdo = this.getSDO();
        CodingRuleQueryRequest queryRequest = sdo.toQueryRequest(CodingRuleQueryRequest.class);
        Map<String, Object> data = codingRuleApplication.slicedQueryCodingRules(queryRequest);
        return toResult(data);
    }

    @RequiresPermissions("CodingRule:query:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除系编码规则")
    public String deleteCodingRuleDetails() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList(IDS_KEY_NAME);
        String codingRuleId = sdo.getString(CODING_RULE_ID_KEY_NAME);
        codingRuleApplication.deleteCodingRuleDetails(codingRuleId, ids);
        return success();
    }

    @RequiresPermissions("CodingRule:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询编码规则明细")
    public String slicedQueryCodingRuleDetails() {
        SDO params = this.getSDO();
        ParentIdQueryRequest queryRequest = params.toQueryRequest(ParentIdQueryRequest.class);
        queryRequest.setParentId(params.getString(CODING_RULE_ID_KEY_NAME));
        Map<String, Object> data = codingRuleApplication.slicedQueryCodingRuleDetails(queryRequest);
        return toResult(data);
    }

}
