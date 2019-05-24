package com.huigou.uasp.bmp.portal.mainpage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.annotation.ControllerMethodMapping;
import com.huigou.uasp.bpm.ViewTaskKind;
import com.huigou.uasp.bpm.engine.application.WorkflowApplication;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.Constants;
import com.huigou.util.SDO;

/**
 * 首页
 * 
 * @author gongmm
 */
@Controller
@ControllerMapping("homePage")
public class HomePageController extends CommonController {

    @Autowired
    private WorkflowApplication workflowService;

    /**
     * 进入首页
     * 
     * @return
     * @throws Exception
     * @author
     */
    @ControllerMethodMapping("/homePage")
    @LogInfo(logType = LogType.SYS, operaionType = OperationType.LIST, description = "跳转到首页")
    public String execute() throws Exception {
        SDO sdo = this.getSDO();
        // boolean infoPermissions = accessApplication.checkPersonFunPermissions(this.getOperator().getPersonId(), "infoPromulgates");
        // this.putAttribute("infoPermissions", infoPermissions);
        // 查询计时任务
        {
            SDO queryTask = new SDO();
            queryTask.setProperties(sdo.getProperties());
            queryTask.setOperator(this.getOperator());
            queryTask.putProperty("viewTaskKindList", ViewTaskKind.WAITING.getId());
            try {
                Map<String, Object> data = queryTasks(queryTask);
                this.putAttribute("tasks", data.get(Constants.ROWS));
                this.putAttribute("taskCount", data.get(Constants.RECORD));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // 跟踪任务
        {
            Map<String, Object> data = workflowService.queryTrackingTasks();
            this.putAttribute("trackingTasks", data.get(Constants.ROWS));
            this.putAttribute("trackingTaskCount", data.get(Constants.RECORD));
        }
        return Constants.HOME_PAGE;
    }

    /**
     * 查询待办任务组合查询参数
     * 
     * @return String
     * @author
     */
    private Map<String, Object> queryTasks(SDO sdo) {
        sdo.putProperty("queryCategory", "myTransaction");
        String viewTaskKindList = sdo.getString("viewTaskKindList");
        Assert.hasText(viewTaskKindList, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "viewTaskKindList"));
        sdo.putProperty(Constants.PAGE_SIZE_PARAM_NAME, sdo.getProperty(Constants.PAGE_SIZE_PARAM_NAME, String.class, "10"));
        sdo.putProperty(Constants.PAGE_PARAM_NAME, "1");
        sdo.putProperty(Constants.SORT_NAME_PARAM_NAME, "startTime");
        sdo.putProperty(Constants.SORT_ORDER_PARAM_NAME, "desc");
        Map<String, Object> data = workflowService.queryTasks(sdo);
        return data;
    }

    /**
     * 查询待办任务
     * 
     * @return String
     * @author
     */
    public String queryAllTasks() {
        SDO sdo = this.getSDO();
        sdo.putProperty("viewTaskKindList", ViewTaskKind.WAITING.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = queryTasks(sdo);
        map.put("tasks", data.get(Constants.ROWS));
        map.put("tasksCount", data.get(Constants.RECORD));
        Map<String, Object> trackingData = workflowService.queryTrackingTasks();
        map.put("trackingTasks", trackingData.get(Constants.ROWS));
        map.put("trackingTaskCount", trackingData.get(Constants.RECORD));
        return this.toResult(map);
    }

    /**
     * 按类型查询任务
     * 
     * @return
     */
    public String queryTasksByViewKind() {
        SDO sdo = this.getSDO();
        Map<String, Object> map = new HashMap<String, Object>();
        String viewTaskKindList = sdo.getString("viewTaskKind");
        Assert.hasText(viewTaskKindList, "查询类别不能为空!");
        ViewTaskKind kind = null;
        for (String viewTaskKind : viewTaskKindList.split(",")) {
            kind = ViewTaskKind.fromId(viewTaskKind);
            if (kind == ViewTaskKind.TRACKING) {
                Map<String, Object> trackingData = workflowService.queryTrackingTasks();
                map.put("tasks" + kind.getId(), trackingData.get(Constants.ROWS));
                map.put("taskCount" + kind.getId(), trackingData.get(Constants.RECORD));
            } else {
                SDO queryTask = new SDO();
                queryTask.setProperties(sdo.getProperties());
                queryTask.setOperator(this.getOperator());
                queryTask.putProperty("viewTaskKindList", kind.getId());
                Map<String, Object> data = queryTasks(queryTask);
                map.put("tasks" + kind.getId(), data.get(Constants.ROWS));
                map.put("taskCount" + kind.getId(), data.get(Constants.RECORD));
            }
        }
        return this.toResult(map);
    }

}
