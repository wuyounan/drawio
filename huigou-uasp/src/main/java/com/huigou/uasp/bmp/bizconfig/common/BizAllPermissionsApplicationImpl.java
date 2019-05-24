package com.huigou.uasp.bmp.bizconfig.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.huigou.uasp.bmp.bizconfig.chart.application.FlowChartPermissionsApplication;
import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;
import com.huigou.uasp.bmp.bizconfig.function.application.FunctionGroupPermissionsApplication;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetailsDto;

/**
 * 不进行权限校验 所有输入都具有权限
 * 
 * @author xiexin
 */
@Service("bizAllPermissionsApplication")
public class BizAllPermissionsApplicationImpl implements FlowChartPermissionsApplication, FunctionGroupPermissionsApplication {

    @Override
    public List<BpmProcessNodeFunction> checkProcessNodeFunction(List<BpmProcessNodeFunction> nodeFunctions) {
        List<BpmProcessNodeFunction> objs = new ArrayList<>(nodeFunctions.size());
        if (nodeFunctions == null || nodeFunctions.size() == 0) {
            return objs;
        }
        for (BpmProcessNodeFunction fun : nodeFunctions) {
            fun.setIsFunction(1);
            objs.add(fun);
        }
        return objs;
    }

    @Override
    public List<BpmFunctionsDetailsDto> checkFunctions(List<BpmFunctionsDetailsDto> datas) {
        List<BpmFunctionsDetailsDto> objs = new ArrayList<>(datas.size());
        if (datas == null || datas.size() == 0) {
            return objs;
        }
        for (BpmFunctionsDetailsDto fun : datas) {
            fun.setHasPermission(true);
            objs.add(fun);
        }
        return objs;
    }

}
