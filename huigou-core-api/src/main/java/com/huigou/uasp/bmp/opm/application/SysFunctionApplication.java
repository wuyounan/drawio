package com.huigou.uasp.bmp.opm.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;

/**
 * 系统功能
 * 
 * @author gongmm
 */
public interface SysFunctionApplication {

    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/resource.xml";

    /**
     * 添加系统功能
     * 
     * @param sysFunction
     *            系统功能对象
     * @return
     *         系统功能ID
     */
    String insertSysFunction(SysFunction sysFunction);

    /**
     * 修改系统功能
     * 
     * @param sysFunction
     *            系统功能对象
     * @return 系统功能ID
     */
    String updateSysFunction(SysFunction sysFunction);

    /**
     * 从数据库中读取一条功能
     * 
     * @param id
     *            功能ID
     * @return
     */
    SysFunction loadSysFunction(String id);

    /**
     * 删除系统功能
     * 
     * @param ids
     *            系统功能ID列表
     */
    void deleteSysFunctions(List<String> ids);

    /**
     * 更新系统功能状态
     * 
     * @param ids
     *            系统功能ID列表
     * @param status
     *            状态
     */
    void updateSysFunctionsStatus(List<String> ids, Integer status);

    /**
     * 根据条件从数据库中读取功能数据
     * 
     * @param params
     * @return
     */
    /**
     * 查询系统功能
     * 
     * @param queryRequest
     *            查询请求
     * @return
     */
    Map<String, Object> querySysFunctions(ParentAndCodeAndNameQueryRequest queryRequest);

    /**
     * 获取系统功能下一个排序号
     * 
     * @param parentId
     *            父ID
     * @return 下一个排序号
     */
    Integer getSysFunctionNextSequence(String parentId);

    /**
     * 更新系统功能排序号
     * 
     * @param params
     *            系统功能ID和排序号的Map
     */
    void updateSysFunctionsSequence(Map<String, Integer> params);

    /**
     * 构建权限
     * 
     * @param fullId
     *            功能ID全路径
     */
    void buildPermission(String fullId);

    /**
     * 功能移动
     * 
     * @param parentId
     * @param ids
     */
    void moveFunctions(String parentId, List<String> ids);

}
