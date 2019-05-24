package com.huigou.uasp.bmp.configuration.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.cache.SystemCache;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bmp.configuration.application.ParameterApplication;
import com.huigou.uasp.bmp.configuration.domain.model.SysParameter;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.DateUtil;
import com.huigou.util.SDO;

/**
 * 系统参数
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("parameter")
public class ParameterController extends CommonController {

    private final static String PARAMETER_PAGE = "Parameter";

    private final static String PARAMETER_DETAIL_PAGE = "ParameterDetal";

    @Autowired
    private ParameterApplication application;

    protected String getPagePath() {
        return "/system/configuration/";
    }

    @RequiresPermissions("SysParameter:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.LIST, description = "跳转到系统参数列表列表页面")
    public String forward() {
        return forward(PARAMETER_PAGE);
    }

    @RequiresPermissions("SysParameter:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到添加系统参数明细页面")
    public String showInsertSysParameter() {
        return forward(PARAMETER_DETAIL_PAGE);
    }

    @RequiresPermissions("SysParameter:create")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.ADD, description = "添加系统参数")
    public String insertSysParameter() {
        SDO params = this.getSDO();
        SysParameter sysParameter = params.toObject(SysParameter.class);
        String id = application.saveSysParameter(sysParameter);
        return success(id);
    }

    @RequiresPermissions("SysParameter:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DETALL, description = "跳转到修改系统参数明细页面")
    public String showUpdateSysParameter() {
        SDO params = this.getSDO();
        String id = params.getString(ID_KEY_NAME);
        SysParameter sysParameter = application.loadSysParameter(id);
        return forward(PARAMETER_DETAIL_PAGE, sysParameter);
    }

    @RequiresPermissions("SysParameter:update")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.UPDATE, description = "修改系统参数")
    public String updateSysParameter() {
        SDO params = this.getSDO();
        SysParameter sysParameter = params.toObject(SysParameter.class);
        application.saveSysParameter(sysParameter);
        return success();
    }

    @RequiresPermissions("SysParameter:delete")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.DELETE, description = "删除系统参数")
    public String deleteSysParameters() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList(IDS_KEY_NAME);
        application.deleteSysParameters(ids);
        return success();
    }

    @RequiresPermissions("SysParameter:move")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.MOVE, description = "移动系统参数")
    public String moveSysParameters() {
        SDO params = this.getSDO();

        List<String> ids = params.getStringList(IDS_KEY_NAME);
        String folderId = params.getProperty(FOLDER_ID_KEY_NAME, String.class);
        application.moveSysParameters(ids, folderId);
        return success();
    }

    @RequiresPermissions("SysParameter:query")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.QUERY, description = "查询系统参数")
    public String slicedQuerySysParameters() {
        SDO params = this.getSDO();
        FolderAndCodeAndNameQueryRequest queryRequest = params.toQueryRequest(FolderAndCodeAndNameQueryRequest.class);
        Map<String, Object> data = application.slicedQuerySysParameters(queryRequest);
        return this.toResult(data);
    }

    @RequiresPermissions("SysParameter:syn")
    @LogInfo(logType = LogType.SYS, subType = "", operaionType = OperationType.CACHE, description = "同步系统参数")
    public String syncCacheSysParameter() {
        application.syncCache();
        return success(MessageSourceContext.getMessage(MessageConstants.DATA_OPERATION_SUCCESS));
    }

    @ControllerMethodMapping("/updateSysStartTime")
    public String logout() {
        SystemCache.setStartTime(DateUtil.getDateFormat("yyyyMMddHHmm", DateUtil.getTimestamp()));
        return success();
    }

}
