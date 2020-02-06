
package com.huigou.index.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.CodeAndNameQueryRequest;
import com.huigou.index.domain.model.IndexClassificationDim;

/**
 * 指标分类维度应用接口
 * 
 * @author
 *         gongmm
 * @date 2017-09-25 14:58
 */
public interface IndexClassificationDimApplication {

    String QUERY_XML_FILE_PATH = "/config/index/index.xml";
    
    String ENTITY_NAME = "indexClassificationDim";
    

    /**
     * 保存指标分类维度
     * 
     * @param indexClassificationDim
     *            指标分类维度实体
     */
    String saveIndexClassificationDim(IndexClassificationDim indexClassificationDim);

    /**
     * 加载指标分类维度
     * 
     * @param id
     *            指标分类维度ID
     */
    IndexClassificationDim loadIndexClassificationDim(String id);

    /**
     * 更新指标分类维度状态
     * 
     * @param ids
     *            指标分类维度ID列表
     * @param status
     *            状态
     */
    void updateIndexClassificationDimsStatus(List<String> ids, Integer status);

    /**
     * 获取指标分类维度下一个排序号
     * 
     * @return 下一个排序号
     */
    Integer getIndexClassificationDimNextSequence();

    /**
     * 更新指标分类维度排序号
     * 
     * @param params
     *            业务ID和排序号组成的Map
     */
    void updateIndexClassificationDimsSequence(Map<String, Integer> params);

    /**
     * 删除指标分类维度
     * 
     * @param ids
     *            指标分类维度ID列表
     */
    void deleteIndexClassificationDims(List<String> ids);

    /**
     * 查询启用指标分类维度
     * @return
     */
    List<Map<String, Object>> queryEnabledAll();
    
    /**
     * 查询指标分类维度
     * 
     * @return queryRequest
     *         查询模型
     */
    Map<String, Object> slicedQueryIndexClassificationDims(CodeAndNameQueryRequest queryRequest);

}
