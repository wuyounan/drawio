package com.huigou.uasp.bmp.bizClassification.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassification;
import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassifyAbstractEntity;
import com.huigou.uasp.bmp.bizClassification.domain.query.BizClassificationQueryRequest;

/**
 * 业务分类配置接口定义
 */
public interface BizClassificationApplication {

    static String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/bizClassification.xml";

    /**
     * 查询根节点
     * 
     * @return
     */
    void insertBizClassificationRoot();

    /**
     * 添加业务分类配置
     *
     * @param bizClassification
     *            业务分类配置实体
     * @return 业务分类配置ID
     */
    String insertBizClassification(BizClassification bizClassification);

    /**
     * 修改业务分类配置
     *
     * @param bizClassification
     *            业务分类配置实体
     * @param OldName
     *            原名称
     * @return 业务分类配置ID
     */
    String updateBizClassification(BizClassification bizClassification);

    /**
     * 加载业务分类配置
     *
     * @param id
     *            业务分类配置ID
     * @return 业务分类配置实体
     */
    BizClassification loadBizClassification(String id);

    /**
     * 删除业务分类配置
     *
     * @param ids
     *            业务分类配置Id集合
     */
    void deleteBizClassifications(List<String> ids);

    /**
     * 移动业务分类配置
     * 
     * @param parentId
     * @param ids
     */
    void moveBizClassifications(String parentId, List<String> ids);

    /**
     * 获取业务分类配置下一排序号
     *
     * @param parentId
     *            父ID
     * @return 下一排序号值
     */
    Integer getBizClassificationNextSequence(String parentId);

    /**
     * 保存业务分类配置排序号
     *
     * @param parentId
     *            业务分类配置ID和排序号组成的Map
     */
    void updateBizClassificationsSequence(Map<String, Integer> params);

    /**
     * 保存计划任务状态
     *
     * @param ids
     *            计划任务ID集合
     * @param status
     *            状态
     */
    void updateBizClassificationsStatus(List<String> ids, Integer status);

    /**
     * 查询业务分类配置
     *
     * @param bizClassificationQueryRequest
     * @return 业务分类配置Map集合
     */
    Map<String, Object> queryBizClassifications(BizClassificationQueryRequest bizClassificationQueryRequest);

    /**
     * 分页查询业务分类配置
     *
     * @param bizClassificationQueryRequest
     * @return 业务分类配置Map集合
     */
    Map<String, Object> sliceQueryBizClassifications(BizClassificationQueryRequest bizClassificationQueryRequest);

    /**
     * 删除业务分类配置明细
     *
     * @param ids
     *            业务分类配置明细Id集合
     */
    void deleteBizclassificationdetails(List<String> ids);

    /**
     * 创建权限资源
     * 
     * @param fullId
     */
    void buildPermission(String fullId);

    /**
     * 更新业务分类配置明细排序号
     *
     * @param parentId
     *            业务分类配置明细ID和排序号组成的Map
     */
    void updateBizClassificationDetailsSequence(Map<String, Integer> params);

    /**
     * 保存业务分类配置明细状态
     *
     * @param ids
     *            业务分类配置明细ID集合
     * @param status
     *            状态
     */
    void updateBizclassificationdetailsStatus(List<String> ids, Integer status);

    /**
     * 批量插入业务字段系统分组
     * 
     * @param bizClassificationId
     * @param bizPropertyIds
     */
    void batchInsertBizFieldSysGroup(String bizClassificationId, String[] bizPropertyIds);

    /**
     * 按权限查询业务分类配置明细
     *
     * @param bizClassificationQueryRequest
     * @return 业务分类配置明细Map集合
     */
    List<Map<String, Object>> queryBizClassificationsByPermission(BizClassificationQueryRequest bizClassificationQueryRequest);

    /**
     * 查询业务分类配置明细
     *
     * @param bizClassificationQueryRequest
     * @return 业务分类配置明细Map集合
     */
    Map<String, Object> queryBizClassificationDetails(BizClassificationQueryRequest bizClassificationQueryRequest);

    /**
     * 根据配置ID查询配置明细
     * 
     * @param bizClassificationId
     * @return
     */
    List<Map<String, Object>> queryByClassificationId(String bizClassificationId);

    /**
     * 查询业务表可视字段
     * 
     * @param bizPropertyId
     * @return
     */
    List<Map<String, Object>> queryClassificationVisibleFields(String bizPropertyId);

    /**
     * 查询业务表实体
     * 
     * @param bizclassificationdetailId
     * @return
     */
    Class<? extends BizClassifyAbstractEntity> getBizClassifyEntityClass(String bizclassificationdetailId);

}
