package com.huigou.uasp.bmp.flexfield.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroup;
import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldDefinition;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldBizGroupsQueryRequest;
import com.huigou.uasp.bmp.flexfield.domain.query.FlexFieldDefinitionsQueryRequest;

/**
 * 弹性域应用
 * 
 * @author gongmm
 */
public interface FlexFieldApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/fexField.xml";

    /**
     * 保存弹性域定义
     * 
     * @param flexFieldDefinition
     *            弹性域定义
     * @return 弹性域定义ID
     */
    String saveFlexFieldDefinition(FlexFieldDefinition flexFieldDefinition);

    /**
     * 更新弹性域定义排序号
     * 
     * @param params
     *            弹性域定义ID、排序号组合的Map
     */
    void updateFlexFieldDefinitionSequence(Map<String, Integer> params);

    /**
     * 移动弹性域定义
     * 
     * @param ids
     *            弹性域定义ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveFlexFieldDefinitions(List<String> ids, String folderId);

    /**
     * 加载弹性域定义
     * 
     * @param id
     *            弹性域定义ID
     * @return 弹性域定义对象
     */
    FlexFieldDefinition loadFlexFieldDefinition(String id);

    /**
     * 删除弹性域定义
     * 
     * @param ids
     *            弹性域定义ID列表
     */
    void deleteFlexFieldDefinitions(List<String> ids);

    /**
     * 分页查询弹性域定义
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryFlexFieldDefinitions(FlexFieldDefinitionsQueryRequest queryRequest);

    /**
     * 保存弹性域业务分组
     * 
     * @param flexFieldBizGroup
     *            弹性域业务分组
     * @return
     */
    String saveFlexFieldBizGroup(FlexFieldBizGroup flexFieldBizGroup);

    /**
     * 加载弹性域业务分组
     * 
     * @param id
     *            弹性域业务分组ID
     * @return
     */
    FlexFieldBizGroup loadFlexFieldBizGroup(String id);

    /**
     * 删除弹性域业务分组
     * 
     * @param ids
     *            弹性域业务分组ID列表
     */
    void deleteFlexFieldBizGroups(List<String> ids);

    /**
     * 分页查询弹性域业务分组
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryFlexFieldBizGroups(FlexFieldBizGroupsQueryRequest queryRequest);

    /**
     * 移动弹性域业务分组
     * 
     * @param ids
     *            弹性域业务分组ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveFlexFieldBizGroups(List<String> ids, String folderId);

    /**
     * 更新弹性域业务分组排序号
     * 
     * @param params
     *            弹性域业务分组ID、排序号组合的Map
     */
    void updateFlexFieldBizGroupSequence(Map<String, Integer> params);

    /**
     * 复制新增
     * 
     * @param id
     * @return
     */
    String copyFlexFieldBizGroups(String id);

    /**
     * 保存弹性域业务分组字段
     * 
     * @param flexFieldBizGroupId
     *            弹性域业务分组ID
     * @param flexFieldDefinitionIds
     *            弹性域定义ID列表
     */
    void saveFlexFieldBizGroupFields(String flexFieldBizGroupId, List<String> flexFieldDefinitionIds);

    /**
     * 删除弹性域业务分组字段
     * 
     * @param flexFieldBizGroupId
     *            弹性域业务分组ID
     * @param ids
     *            弹性域业务分组字段ID列表
     */
    void deleteFlexFieldBizGroupFields(String flexFieldBizGroupId, List<String> ids);

    /**
     * 分页查询弹性域业务分组字段
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryFlexFieldBizGroupFields(ParentIdQueryRequest queryRequest);

    /**
     * 查询可视字段
     * 
     * @param parentId
     * @return
     */
    List<Map<String, Object>> queryVisibleFields(String parentId);

    /**
     * 保存弹性域
     * 
     * @param bizKindId
     *            业务类别ID
     * @param bizId
     *            业务ID
     */
    void saveFlexFieldStorages(String bizKindId, String bizId);

    /**
     * 删除弹性域
     * 
     * @param bizKindId
     * @param bizId
     */
    void deleteFlexFieldStorages(String bizKindId, String bizId);

    /**
     * 删除弹性域
     * 
     * @param bizId
     */
    void deleteFlexFieldStorages(String bizId);

    /**
     * 查询弹性域业务分组字段
     * 
     * @param bizCode
     *            业务编码
     * @param bizId
     *            业务ID
     * @return
     */
    List<Map<String, Object>> queryFlexFieldBizGroupFieldStorage(String bizKindId, String bizId);
}