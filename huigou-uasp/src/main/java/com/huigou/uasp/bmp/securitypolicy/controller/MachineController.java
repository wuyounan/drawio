package com.huigou.uasp.bmp.securitypolicy.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.context.SecurityGrade;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.bmp.securitypolicy.application.MachineApplication;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.bmp.securitypolicy.domain.query.MachinesQueryRequest;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

/**
 * MachineController
 * 
 * @author yuanwf
 */
@Controller
@ControllerMapping("machine")
public class MachineController extends CommonController {

    @Autowired
    private MachineApplication application;

    @Override
    protected String getPagePath() {
        return "/system/securitypolicy/";
    }

    @RequiresPermissions("Machine:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到机器维护列表页面")
    public String forwardMachine() {
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("machine");
    }

    @RequiresPermissions("Machine:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到添加机器明细页面")
    public String showInsertMachine() {
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("machineDetail");
    }

    @RequiresPermissions("Machine:create")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.ADD, description = "添加机器")
    public String insertMachine() {
        SDO params = this.getSDO();

        Machine machine = params.toObject(Machine.class);
        machine = this.application.saveMachine(machine);
        return success(machine.getId());
    }

    @RequiresPermissions("Machine:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改机器")
    public String updateMachine() {
        SDO params = this.getSDO();

        Machine machine = params.toObject(Machine.class);
        machine = this.application.saveMachine(machine);
        return success(machine.getId());
    }

    @RequiresPermissions("Machine:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DETALL, description = "跳转到修改机器页面")
    public String loadMachine() {
        SDO params = this.getSDO();

        String id = params.getString("id");
        Machine machine = this.application.loadMachine(id);
        this.putAttribute("securityGradeList", SecurityGrade.getData());
        return forward("machineDetail", machine);
    }

    @RequiresPermissions("Machine:update")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.UPDATE, description = "修改机器状态")
    public String updateMachinesStatus() {
        SDO sdo = this.getSDO();
        List<String> ids = sdo.getStringList("ids");
        int status = sdo.getInteger("status");
        this.application.updateMachinesStatus(ids, status);
        return success();
    }

    @RequiresPermissions("Machine:delete")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.DELETE, description = "删除机器")
    public String deleteMachine() {
        SDO params = this.getSDO();
        List<String> ids = params.getStringList("ids");
        this.application.deleteMachines(ids);
        return success();
    }

    @RequiresPermissions("Machine:query")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.QUERY, description = "查询机器")
    public String sliceQueryMachines() {
        SDO params = this.getSDO();
        MachinesQueryRequest queryRequest = params.toQueryRequest(MachinesQueryRequest.class);
        Map<String, Object> data = this.application.sliceQueryMachines(queryRequest);
        return toResult(data);
    }

}