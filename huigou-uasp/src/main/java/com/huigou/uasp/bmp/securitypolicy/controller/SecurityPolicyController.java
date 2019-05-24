package com.huigou.uasp.bmp.securitypolicy.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.SecurityGrade;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.securitypolicy.application.SecurityPolicyApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;
import com.huigou.uasp.bmp.securitypolicy.domain.query.SecurityPoliciesQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("securityPolicy")
public class SecurityPolicyController extends CommonController {

    @Autowired
    private SecurityPolicyApplication securityPolicyApplication;

    protected String getPagePath() {
        return "/system/securitypolicy/";
    }

    @RequiresPermissions("SecurityPolicy:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到安全策略列表页面")
    public String forwardSecurityPolicy() {
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("securityPolicy");
    }

    @RequiresPermissions("SecurityPolicy:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加安全策略明细页面")
    public String showInsertSecurityPolicy() {
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("securityPolicyDetail");
    }

    @RequiresPermissions("SecurityPolicy:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改安全策略明细页面")
    public String showUpdateSecurityPolicy() {
        SDO sdo = this.getSDO();
        String id = sdo.getString("id");
        SecurityPolicy result = this.securityPolicyApplication.loadSecurityPolicy(id);
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("securityPolicyDetail", result);
    }

    @RequiresPermissions("SecurityPolicy:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加安全策略")
    public String insertSecurityPolicy() {
        SDO params = this.getSDO();
        SecurityPolicy securityPolicy = params.toObject(SecurityPolicy.class);
        this.securityPolicyApplication.saveSecurityPolicy(securityPolicy);
        return success();
    }

    @RequiresPermissions("SecurityPolicy:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改安全策略")
    public String updateSecurityPolicy() {
        SDO params = this.getSDO();
        SecurityPolicy securityPolicy = params.toObject(SecurityPolicy.class);
        this.securityPolicyApplication.saveSecurityPolicy(securityPolicy);
        return success();
    }

    @RequiresPermissions("SecurityPolicy:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改安全策略状态")
    public String updateSecurityPoliciesStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        Integer status = sdo.getInteger("status");
        this.securityPolicyApplication.updateSecurityPoliciesStatus(ids, status);
        return success();
    }

    @RequiresPermissions("SecurityPolicy:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除安全策略")
    public String deleteSecurityPolicies() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        this.securityPolicyApplication.deleteSecurityPolicies(ids);
        return success();
    }

    @RequiresPermissions("SecurityPolicy:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "分页查询安全策略")
    public String sliceQuerySecurityPolicies() {
        SDO params = this.getSDO();
        SecurityPoliciesQueryRequest queryRequest = params.toQueryRequest(SecurityPoliciesQueryRequest.class);
        Map<String, Object> result = this.securityPolicyApplication.sliceQuerySecurityPolicies(queryRequest);
        return toResult(result);
    }

}
