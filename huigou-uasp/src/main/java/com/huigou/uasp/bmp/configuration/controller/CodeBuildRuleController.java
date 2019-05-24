package com.huigou.uasp.bmp.configuration.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.CodeBuildRuleApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CodeBuildRule;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * 编码生成规则
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("codeBuildRule")
public class CodeBuildRuleController extends CommonController {

    private static String LIST_PAGE = "CodeBuildRule";

    private static String DETAIL_PAGE = "CodeBuildRuleDetail";

    @Autowired
    private CodeBuildRuleApplication codeBuildRuleApplication;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    @RequiresPermissions("CodeBuildRule:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到单据编码生成规则列表页面")
    public String forward() {
        return forward(LIST_PAGE);
    }

    @RequiresPermissions("CodeBuildRule:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加单据编码生成规则明细页面")
    public String showInsertCodeBuildRule() {
        return forward(DETAIL_PAGE);
    }

    @RequiresPermissions("CodeBuildRule:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加单据编码生成规则")
    public String insertCodeBuildRule() {
        SDO params = this.getSDO();
        CodeBuildRule codeBuildRule = params.toObject(CodeBuildRule.class);
        String id = codeBuildRuleApplication.saveCodeBuildRule(codeBuildRule);
        return success(id);
    }

    @RequiresPermissions("CodeBuildRule:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改单据编码生成规则明细页面")
    public String showUpdateCodeBuildRule() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        CodeBuildRule codeBuildRule = codeBuildRuleApplication.loadCodeBuildRule(id);
        return forward(DETAIL_PAGE, codeBuildRule);
    }

    @RequiresPermissions("CodeBuildRule:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改单据编码生成规则")
    public String updateCodeBuildRule() {
        SDO params = this.getSDO();
        CodeBuildRule codeBuildRule = params.toObject(CodeBuildRule.class);
        codeBuildRuleApplication.saveCodeBuildRule(codeBuildRule);
        return success();
    }

    @RequiresPermissions("CodeBuildRule:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除单据编码生成规则")
    public String deleteCodeBuildRules() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        codeBuildRuleApplication.deleteCodeBuildRules(ids);
        return success();
    }

    @RequiresPermissions("CodeBuildRule:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动单据编码生成规则")
    public String moveCodeBuildRules() {
        SDO params = this.getSDO();
        String folderId = params.getString(FOLDER_ID_KEY_NAME);
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        codeBuildRuleApplication.moveCodeBuildRules(ids, folderId);
        return success();
    }

    @RequiresPermissions("CodeBuildRule:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询单据编码生成规则")
    public String slicedQueryCodeBuildRules() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> codeBuildRuleEntities = codeBuildRuleApplication.slicedQueryCodeBuildRules(queryRequest);
        return toResult(codeBuildRuleEntities);
    }

}
