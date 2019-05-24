package com.huigou.uasp.bmp.configuration.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.configuration.application.PersonQuerySchemeApplication;
import com.huigou.uasp.bmp.configuration.domain.model.PersonQueryScheme;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("personQueryScheme")
public class PersonQuerySchemeController extends CommonController {

    @Autowired
    private PersonQuerySchemeApplication personQuerySchemeApplication;

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.SAVE, description = "保存个人查询方案")
    public String savePersonQueryScheme() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        PersonQueryScheme personQueryScheme = params.toObject(PersonQueryScheme.class);
        id = personQuerySchemeApplication.savePersonQueryScheme(personQueryScheme);
        return success(id);
    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询个人查询方案")
    public String queryPersonQuerySchemes() {
        SDO params = this.getSDO();
        String kindId = params.getString("kindId");
        Map<String, Object> codeBuildRuleEntities = personQuerySchemeApplication.queryPersonQuerySchemes(ThreadLocalUtil.getOperator().getUserId(), kindId);
        return toResult(codeBuildRuleEntities);

    }

    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除个人查询方案")
    public String deletePersonQueryScheme() {
        SDO params = this.getSDO();
        String id = params.getString("id");
        personQuerySchemeApplication.deletePersonQueryScheme(id);
        return success();
    }

}
