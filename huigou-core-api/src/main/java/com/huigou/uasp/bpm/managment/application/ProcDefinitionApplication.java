package com.huigou.uasp.bpm.managment.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;

/**
 * 流程定义
 *
 * @author gongmm
 */
public interface ProcDefinitionApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/bpm.xml";

    /**
     * 保存流程定义
     *
     * @param procDefinition 流程定义
     * @return
     */
    String insertProcDefinition(ProcDefinition procDefinition);

    /**
     * 修改流程定义
     *
     * @param procDefinition 流程定义
     */
    void updateProcDefinition(ProcDefinition procDefinition);

    /**
     * 加载流程定义
     *
     * @param id 流程定义ID
     * @return
     */
    ProcDefinition loadProcDefinition(String id);

    /**
     * 通过流程ID加载流程定义
     *
     * @param procId 流程定义ID
     * @return
     */
    ProcDefinition loadProcDefinitionByProcId(String procId);

    /**
     * 加载流程定义
     *
     * @param procId     流程定义ID
     * @param procUnitId 流程环节ID
     * @return
     */
    ProcDefinition loadProcDefinitionByProcAndProcUnitId(String procId, String procUnitId);

    /**
     * 删除流程定义
     *
     * @param ids 流程定义ID列表
     */
    void deleteProcDefinitions(List<String> ids);

    /**
     * 查询流程定义
     *
     * @param parentId        父ID
     * @param code            编码
     * @param name            名称
     * @param includeProcUnit 包括流程环节
     * @param pageable        分页信息
     * @deprecated 请使用 {@link #findProcDefinitions(boolean, ParentAndCodeAndNameQueryRequest)} 替代。
     */
    @Deprecated
    Map<String, Object> queryProcDefinitions(boolean includeProcUnit, ParentAndCodeAndNameQueryRequest queryRequest);

    /**
     * @since 1.1.3
     */
    Map<String, Object> findProcDefinitions(boolean includeProcUnit, ParentAndCodeAndNameQueryRequest queryRequest);

    /**
     * 分页查询流程定义
     */
    Map<String, Object> slicedQueryProcDefinitions(ParentIdQueryRequest queryRequest);


    /**
     * 得到下一个排序号
     *
     * @param parentId
     * @return
     */
    Integer getProcDefinitionNextSequence(String parentId);

    /**
     * 更改流程定义排序号
     *
     * @param params
     */
    void updateProcDefinitionSequence(Map<String, Integer> params);

    /**
     * 移动流程定义
     *
     * @param ids
     * @param parentId
     */
    void moveProcDefinitions(List<String> ids, String parentId);

    /**
     * 绑定Activiti流程定义
     *
     * @param id     流程定义ID
     * @param procId Activiti流程ID
     */
    void bindActivitiProcDefinition(String id, String procId);

    /**
     * 导入流程环节
     *
     * @param parentId 父ID
     */
    void importProcUnits(String parentId);

    /**
     * 查询一级流程定义
     *
     * @return
     */
    List<Map<String, Object>> queryOneLevelProcDefinitions();

    /**
     * 从Activiti中读取流程环节
     *
     * @param procId
     */
    List<Map<String, Object>> queryProcUnitsFromActiviti(String processDefinitionKey);

    /**
     * 查询流程环节，用于排序
     */
    /**
     * @param processDefinitionKey 流程定义Key
     * @return
     */
    List<ProcDefinition> queryProcUnitsForSequence(String processDefinitionKey);

}
