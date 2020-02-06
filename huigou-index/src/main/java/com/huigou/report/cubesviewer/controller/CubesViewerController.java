package com.huigou.report.cubesviewer.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huigou.uasp.annotation.ControllerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.huigou.report.cubesviewer.application.CubesViewerApplication;
import com.huigou.report.cubesviewer.domain.model.CubesViewerOperatorQueryScheme;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.SDO;

@Controller
@ControllerMapping("/cubesViewer")
public class CubesViewerController extends CommonController {

    private static String CUBES_VIEW_PAGE = "view";

    private static String LOCAL_CUBES_SERVER = "/cubesViewer/server";

    @Autowired
    private CubesViewerApplication cubesViewerApplication;

    protected String getPagePath() {
        return "/report/cubesviewer/";
    }

    @RequestMapping(value = "/forwardView")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardView() {
        this.putAttribute("cubesUrl", cubesViewerApplication.getCubesServerUrl());
        return forward(CUBES_VIEW_PAGE);
    }

    @RequestMapping(value = "/getCubesViewerDefinition", method = RequestMethod.POST)
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String getCubesViewerDefinition(@RequestParam(value = "cubeCodes[]") String[] cubeCodes) {
        List<String> cubeCodeList = Arrays.asList(cubeCodes);
        Map<String, Object> result = cubesViewerApplication.getCubesViewerDefinition(cubeCodeList);
        return this.toResult(result);
    }

    @RequestMapping(value = "/server/**", produces = { "application/json" })
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public @ResponseBody String server() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String queryString = request.getQueryString();
        String cubesViewerAction = uri.replaceFirst(contextPath, "").replaceFirst(LOCAL_CUBES_SERVER, "");

        String result = this.cubesViewerApplication.callCubesViewerService(cubesViewerAction, queryString);
        return result;
    }

    @RequestMapping(value = "/saveQueryScheme", method = RequestMethod.POST)
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.SAVE, description = "")
    public String saveCubesViewerOperatorQueryScheme() {
        SDO params = this.getSDO();
        CubesViewerOperatorQueryScheme cubesViewerOperatorQueryScheme = params.toObject(CubesViewerOperatorQueryScheme.class);
        String id = this.cubesViewerApplication.saveCubesViewerOperatorQueryScheme(cubesViewerOperatorQueryScheme);
        return this.success(id);
    }

    @RequestMapping(value = "/renameQueryScheme")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.SAVE, description = "")
    public String renameCubesViewerOperatorQueryScheme() {
        SDO params = this.getSDO();
        String id = params.getId();
        String newName = params.getString("newName");
        this.cubesViewerApplication.renameCubesViewerOperatorQueryScheme(id, newName);
        return this.success();
    }

    @RequestMapping(value = "/deleteQueryScheme")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DELETE, description = "")
    public String deleteCubesViewerOperatorQueryScheme() {
        SDO params = this.getSDO();
        this.cubesViewerApplication.deleteCubesViewerOperatorQueryScheme(params.getId());
        return this.success();
    }

    @RequestMapping(value = "/queryQuerySchemes")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryCubesViewerOperatorQuerySchemesForCurrentOperator() {
        SDO params = this.getSDO();
        String functionCode = params.getString("functionCode");
        List<Map<String, Object>> data = this.cubesViewerApplication.queryCubesViewerOperatorQuerySchemesForCurrentOperator(functionCode);
        return this.toResult(data);
    }

}
