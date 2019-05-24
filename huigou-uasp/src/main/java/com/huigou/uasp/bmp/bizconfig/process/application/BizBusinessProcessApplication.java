package com.huigou.uasp.bmp.bizconfig.process.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;
import com.huigou.uasp.bmp.bizconfig.process.domain.query.BusinessProcessQueryRequest;

/**
 * 流程定义应用接口
 *
 * @author
 * @date 2017-04-07 14:00
 */
public interface BizBusinessProcessApplication {

    /**
     * 查询文件
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/businessProcess.xml";

    /**
     * 保存流程定义
     *
     * @param businessProcess
     *            流程定义对象
     */
    String insertBusinessProcess(BpmBusinessProcess businessProcess);

    /**
     * 修改流程定义
     *
     * @param businessProcess
     *            流程定义对象
     */
    BpmBusinessProcess updateBusinessProcess(BpmBusinessProcess businessProcess);

    /**
     * 修改流程定义排序号
     *
     * @param params
     *            流程定义对象ID和排序号sequence组的map
     */
    void updateBusinessProcessesSequence(String parentId, Map<String, Integer> params);

    /**
     * 删除流程定义
     *
     * @param ids
     *            流程定义对象ID列表
     */
    void deleteBusinessProcesses(List<String> ids);

    /**
     * 加载流程定义
     *
     * @param id
     *            流程定义对象ID
     * @return 流程定义对象
     */
    BpmBusinessProcess loadBusinessProcess(String id);

    /**
     * 分页查询流程定义
     *
     * @param queryRequest
     *            流程定义对象查询请求对象
     * @return 流程定义对象map
     */
    Map<String, Object> slicedQueryBusinessProcesses(BusinessProcessQueryRequest queryRequest);

    /**
     * 查询流程定义
     *
     * @param parentId
     *            流程定义对象父ID
     * @return 流程定义对象列表
     */
    List<Map<String, Object>> queryBusinessProcesses(String parentId);

    /**
     * 获取下一个排序号
     *
     * @param parentId
     *            流程定义对象父ID
     * @return 下一个排序号
     */
    Integer getBusinessProcessNextSequence(String parentId);

    /**
     * 保存属性
     *
     * @param businessProcess
     */
    void saveBusinessProcessAndAttribute(BpmBusinessProcess businessProcess);

    /**
     * 移动时查询权限树
     * 
     * @param parentId
     * @param excludeIds
     * @return
     */
    List<Map<String, Object>> queryBusinessProcessesOnMove(String parentId, String excludeIds);

    /**
     * 移动流程定义
     *
     * @param parentId
     *            流程定义对象父ID
     * @param ids
     *            流程定义对象ID列表
     */
    void moveBusinessProcesses(String parentId, List<String> ids);

    /**
     * 根据编码查询流程记录
     * 
     * @param code
     * @return
     */
    BpmBusinessProcess loadBusinessProcessByCode(String code);

}
