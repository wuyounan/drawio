package com.huigou.uasp.bmp.bizconfig.function.application;

import java.util.List;

import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetailsDto;

public interface FunctionGroupPermissionsApplication {

    List<BpmFunctionsDetailsDto> checkFunctions(List<BpmFunctionsDetailsDto> datas);

}