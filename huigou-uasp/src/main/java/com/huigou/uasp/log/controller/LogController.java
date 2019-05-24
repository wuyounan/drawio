package com.huigou.uasp.log.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.RoleKind;
import com.huigou.context.TmspmConifg;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.application.LogApplication;
import com.huigou.uasp.log.application.LoginLogApplication;
import com.huigou.uasp.log.domain.model.LogStatus;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.uasp.log.domain.query.LoginQueryRequest;
import com.huigou.uasp.log.domain.query.LoginQueryRequest.QueryKind;
import com.huigou.uasp.log.domain.query.OperationLogQueryRequest;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Controller
@ControllerMapping("log")
public class LogController extends CommonController {

    @Resource(name = "logApplication")
    private LogApplication application;

    @Autowired
    private LoginLogApplication loginLogApplication;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Override
    protected String getPagePath() {
        return "/system/log/";
    }

    @RequiresPermissions(value = { "ErrorLoginLog:query" })
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到查询错误登录日志列表页面")
    public String forwardErrorLoginLog() {
        return forward("errorLoginLog");
    }

    @RequiresPermissions("LoginLog:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到查询 登录日志列表页面")
    public String forwardLoginLog() {
        return forward("loginLog");
    }

    @RequiresPermissions("ErrorLog:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到查询错误日志列表页面")
    public String forwardErrorLog() {
        // SDO sdo = this.getSDO();
        // String statusId = sdo.getString("statusId");
        this.putAttribute("roleKinds", RoleKind.getDataForErrorLog(tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm()));
        this.putAttribute("logType", LogType.getData());
        this.putAttribute("operationType", OperationType.getData());
        this.putAttribute("logStatus", LogStatus.getData());
        this.putAttribute("statusId", 0);
        return forward("log");
    }

    @RequiresPermissions("OperationLog:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到查询操作日志列表页面")
    public String forwardOperationLog() {
        // SDO sdo = this.getSDO();
        // String logKind = sdo.getString("kind");
        // String statusId = sdo.getString("statusId");
        this.putAttribute("roleKinds", RoleKind.getDataForOperationLog(tmspmConifg.isUseTspm() && tmspmConifg.isEnableTspm()));
        this.putAttribute("logType", LogType.getData());
        this.putAttribute("operationType", OperationType.getData());
        this.putAttribute("logStatus", LogStatus.getData());
        // this.putAttribute("statusId", statusId);
        return forward("log");
    }

    @RequiresPermissions("OperationLog:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到日志明细页面")
    public String loadOperationLog() {
        SDO sdo = this.getSDO();
        String id = sdo.getString("id");
        Map<String, Object> data = application.loadOperationLog(id);
        data.put("logTypeList", LogType.getData());
        data.put("operationNameList", OperationType.getData());
        return forward("logDetail", data);
    }

    @RequiresPermissions(value = { "OperationLog:query", "ErrorLog:query" }, logical = Logical.OR)
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询日志")
    public String slicedQueryOperationLogs() {
        /*
         * SDO sdo = this.getSDO();
         * String appName = sdo.getString("appName");
         * String operatorRoleKindId = sdo.getOperator().getRoleKind().getId();
         * String sortName = sdo.getString("sortname");
         * String sortOrder = sdo.getString("sortorder");
         * String fullId = sdo.getString("fullId");
         * String personMemberId = sdo.getOperator().getPersonMemberId();
         * String personMemberName = sdo.getString("personMemberName");
         * Date beginDate = sdo.getProperty("beginDate", Date.class);
         * Date endDate = sdo.getProperty("endDate", Date.class);
         * String logType = sdo.getString("logType");
         * String operationType = sdo.getString("operationType");
         * String ip = sdo.getString("ip");
         * String statusId = sdo.getString("statusId");
         * String roleKindId = sdo.getString("roleKindId");
         * String exportHead = sdo.getString("exportHead");
         * String exportType = sdo.getString("exportType");
         * int page = sdo.getInteger("page");
         * int pageSize = sdo.getInteger("pagesize");
         * Map<String, Object> data = application.slicedQueryOperationLogs(operatorRoleKindId, roleKindId, appName, fullId, statusId, logType, operationType, ip,
         * personMemberId, personMemberName, beginDate, endDate, page, pageSize, sortName,
         * sortOrder, exportHead, exportType);
         */

        SDO params = this.getSDO();
        OperationLogQueryRequest queryRequest = params.toQueryRequest(OperationLogQueryRequest.class);
        queryRequest.setOperatorRoleKindId(params.getOperator().getRoleKind().getId());
        queryRequest.setPersonMemberId(params.getOperator().getPersonMemberId());
        Map<String, Object> data = application.slicedQueryOperationLogs(queryRequest);
        return toResult(data);

    }

    @RequiresPermissions("LoginLog:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询登录日志")
    public String sliceQueryHistoricSessions() {
        SDO sdo = this.getSDO();
        String personMemberId = sdo.getOperator().getPersonMemberId();
        String operatorRoleKindId = sdo.getOperator().getRoleKind().getId();
        LoginQueryRequest queryRequest = sdo.toQueryRequest(LoginQueryRequest.class);

        queryRequest.setOperatorRoleKindId(operatorRoleKindId);
        queryRequest.setPersonMemberId(personMemberId);

        String queryKindId = sdo.getString("queryKindId");
        if (StringUtil.isNotBlank(queryKindId)) {
            QueryKind queryKind = QueryKind.valueOf(queryKindId);
            queryRequest.setQueryKind(queryKind);
        }

        Map<String, Object> data = loginLogApplication.sliceQueryHistoricSessions(queryRequest);
        return toResult(data);
    }
}
