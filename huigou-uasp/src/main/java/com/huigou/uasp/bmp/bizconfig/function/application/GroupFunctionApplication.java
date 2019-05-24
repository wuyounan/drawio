package com.huigou.uasp.bmp.bizconfig.function.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctions;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetails;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroup;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroupDto;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsDetailsQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsGroupQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsQueryRequest;

/**
 * 业务功能分组管理
 * 
 * @author
 * @date 2018-03-28 11:16
 */
public interface GroupFunctionApplication {
    /**
     * 查询文件
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/groupFunction.xml";

    Map<String, String> getGroupColorMap();

    Map<String, String> getFuncColorMap();

    /**
     * 保存 业务功能
     * 
     * @author
     * @param params
     */
    String saveFunctions(BpmFunctions functions);

    /**
     * 加载 业务功能
     * 
     * @author
     * @return SDO
     */
    BpmFunctions loadFunctions(String id);

    /**
     * 删除 业务功能
     * 
     * @author
     */
    void deleteFunctions(String id);

    /**
     * 查询 业务功能
     * 
     * @author
     * @return SDO
     */
    Map<String, Object> slicedQueryFunctions(FunctionsQueryRequest queryRequest);

    /**
     * 保存 业务功能分组
     * 
     * @author
     * @param params
     */
    String saveFunctionsGroup(BpmFunctionsGroup functionsGroup);

    /**
     * 加载 业务功能分组
     * 
     * @author
     * @return SDO
     */
    BpmFunctionsGroup loadFunctionsGroup(String id);

    /**
     * 删除 业务功能分组
     * 
     * @author
     */
    void deleteFunctionsGroup(List<String> ids);

    /**
     * 查询 业务功能分组
     * 
     * @author
     * @return SDO
     */
    Map<String, Object> queryFunctionsGroup(FunctionsGroupQueryRequest queryRequest);

    /**
     * 更新分组排序号
     * 
     * @param map
     */
    void updateFunctionsGroupSequence(Map<String, Integer> map);

    /**
     * 保存 业务功能对应连接
     * 
     * @author
     * @param params
     */
    String saveFunctionsDetails(BpmFunctionsDetails functionsDetails);

    /**
     * 加载 业务功能对应连接
     * 
     * @author
     * @return SDO
     */
    BpmFunctionsDetails loadFunctionsDetails(String id);

    /**
     * 删除 业务功能对应连接
     * 
     * @author
     */
    void deleteFunctionsDetails(List<String> ids);

    /**
     * 查询 业务功能对应连接
     * 
     * @author
     * @return SDO
     */
    Map<String, Object> queryFunctionsDetails(FunctionsDetailsQueryRequest queryRequest);

    /**
     * 修改方法排序号
     * 
     * @param map
     */
    void updateFunctionsDetailsSequence(Map<String, Integer> map);

    /**
     * 移动功能
     * 
     * @param ids
     * @param groupId
     */
    void updateFunctionsDetailsGroup(List<String> ids, String groupId);

    /**
     * 批量修改颜色
     * 
     * @param ids
     * @param color
     */
    void updateFunctionsDetailsColor(List<String> ids, String color);

    List<BpmFunctionsGroupDto> queryFunctionsGroup(String code);

}
