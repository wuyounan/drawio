package com.huigou.uasp.bmp.bizconfig.chart.application;

import java.util.List;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;

/**
 * 流程图中功能权限校验
 * 
 * @author xx
 * @date 2017-03-06 14:49
 */
public interface FlowChartPermissionsApplication {

    List<BpmProcessNodeFunction> checkProcessNodeFunction(List<BpmProcessNodeFunction> nodeFunctions);

}
