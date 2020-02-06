package com.huigou.report.cboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.huigou.report.cboard.application.CboardApplication;
import com.huigou.report.cboard.application.CboardDefinitionApplication;
import com.huigou.report.cboard.domain.model.CboardDefinition;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

import net.sf.json.JSONArray;

@Controller
@ControllerMapping("/cboard")
public class CboardController extends CommonController {

    private static final String CBOARD_VIEW_PAGE = "cboardView";
    
    private static final String CBOARDDASHBOARD_PAGE = "cboardDashboard";

    @Autowired
    private CboardApplication cboardApplication;
    
    @Autowired
    private CboardDefinitionApplication cboardDefinitionApplication;
    

    @Autowired
    protected String getPagePath() {
        return "/report/cboard/";
    }

    /**
     * CBoard代理接口
     * 
     * @return
     */
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String getAggregateData() {
        SDO sdo = this.getSDO();
        String definitionId = sdo.getString("definitionId");
        String tableId = sdo.getString("tableId");

        Map<String, Object> cascadeParams = sdo.getObjectMap("cascadeParams");

        JSONArray data = this.cboardApplication.getAggregateData(definitionId, tableId, cascadeParams);

        return this.toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardCboardView() {
        SDO sdo = this.getSDO();
        String code = sdo.getString("cboardCode");
        
        CboardDefinition cboardDefinition = cboardDefinitionApplication.loadCboardDefinitionByCode(code);
        
        this.putAttribute("cboardDefinition", cboardDefinition);
        
        return forward(CBOARD_VIEW_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardCboardDashboard() {
        this.putAttribute("cboardServerUrl", cboardApplication.getCboardServerUrl());
        return forward(CBOARDDASHBOARD_PAGE);
    }
}
