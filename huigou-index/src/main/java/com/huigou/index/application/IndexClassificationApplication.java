
package com.huigou.index.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentAndCodeAndNameQueryRequest;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.query.IndexClassificationQueryRequest;

/**
 * 指标分类接口
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
public interface IndexClassificationApplication {

    String QUERY_XML_FILE_PATH = "/config/index/index.xml";

    String ENTITY_NAME = "indexClassification";
    /**
     * 保存指标分类
     * 
     * @param indexClassification
     *            指标分类实体
     */
    String saveIndexClassification(IndexClassification indexClassification);

    /**
     * 加载指标分类
     * 
     * @param id
     *            指标分类ID
     * @return
     */
    IndexClassification loadIndexClassification(String id);

    /**
     * 更新指标维度状态
     * 
     * @param ids
     *            指标分类ID列表
     * @param status
     *            状态
     */
    void updateIndexClassificationsStatus(List<String> ids, Integer status);

    /**
     * 获取指标分类下一个排序号
     * 
     * @param parentId
     *            父ID
     * @return 下一个排序号
     */
    Integer getIndexClassificationNextSequence(String parentId);

    /**
     * 更新指标分类排序号
     * 
     * @param params
     *            业务ID和排序号组成的Map
     */
    void updateIndexClassificationsSequence(Map<String, Integer> params);

    /**
     * 删除指标分类
     * 
     * @param ids
     *            指标分类ID列表
     */
    void deleteIndexClassifications(List<String> ids);

    /**
     * 查询指标分类
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryIndexClassifications(IndexClassificationQueryRequest queryRequest);

    /**
     * 分页查询指标分类
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQueryIndexClassifications(ParentAndCodeAndNameQueryRequest queryRequest);

}
