package com.huigou.index.application;

import java.util.List;
import java.util.Map;

import com.huigou.index.domain.model.Index;
import com.huigou.index.domain.model.IndexAttachClassification;
import com.huigou.index.domain.model.IndexClassification;
import com.huigou.index.domain.model.IndexEntry;
import com.huigou.index.domain.query.IndexEntryEntryQueryRequest;
import com.huigou.index.domain.query.IndexQueryRequest;

/**
 * 指标接口
 * 
 * @author
 * @date 2017-09-25 14:58
 */
public interface IndexApplication {

    String QUERY_XML_FILE_PATH = "/config/index/index.xml";

    String INDEX_ENTITY_NAME = "index";

    String INDEX_ENTRY_ENTITY_NAME = "indexEntry";

    String INDEX_ENTRY_FORMULA_PARAM_ENTITY_NAME = "indexEntryFormulaParam";

    String INDEX_ENTRY_UI_PARAM_ENTITY_NAME = "indexEntryUIParam";

    String INDEX_ENTRY_TAB_ENTITY_NAME = "indexEntryTab";

    /**
     * 保存 指标
     * 
     * @param index
     *            指标实体
     * @return
     *         指标实体ID
     */
    String saveIndex(Index index);

    /**
     * 加载 指标
     * 
     * @param id
     *            指标实体ID
     * @return
     *         指标实体
     */
    Index loadIndex(String id);

    /**
     * 删除 指标集合
     * 
     * @param ids
     *            指标ID集合
     */
    void deleteIndexes(List<String> ids);

    /**
     * 查询 指标
     * 
     * @param queryRequest
     *            指标查询实体
     * @return
     *         指标集合
     */
    Map<String, Object> slicedQueryIndexes(IndexQueryRequest queryRequest);

    /**
     * 保存排序号
     * 
     * @param params
     */
    void updateIndexsSequence(Map<String, Integer> params);

    /**
     * 修改状态
     * 
     * @param ids
     *            指标定义ID集合
     * @param status
     *            状态
     */
    void updateIndexsStatus(List<String> ids, Integer status);

    /**
     * 获取指标定义下一排序号
     * 
     * @return 排序号
     */
    Integer getIndexNextSequence();

    /**
     * 保存 指标归属分配
     * 
     * @param indexAttachClassification
     *            指标归属分配实体
     * @return
     *         指标归属分配ID
     */
    String saveIndexAttachClassification(IndexAttachClassification indexAttachClassification);

    /**
     * 移动 指标
     * 
     * @param ids
     *            指标ID集合
     * @param classificationId
     *            指标分类ID
     */
    void moveIndexs(List<String> ids, String classificationId);

    /**
     * 保存指标明细
     * 
     * @param indexEntry
     *            指标明细实体
     * @return
     *         指标明细实体ID
     */
    String saveIndexEntry(IndexEntry indexEntry);

    /**
     * 加载指标明细
     * 
     * @param id
     *            指标明细实体ID
     * @return
     *         指标明细实体
     */
    IndexEntry loadIndexEntry(String id);
    
    /**
     * 加载指标类型
     * 
     * @param id
     */
    IndexClassification loadIndexClassification(String id);

    /**
     * 删除 指标明细
     * 
     * @param ids
     *            指标明细实体ID集合
     */
    void deleteIndexEntries(List<String> ids);

    /**
     * 删除指标明细公式
     * 
     * @param entryId
     *            指标明细ID
     * @param ids
     *            公式实体ID集合
     */
    void deleteIndexEntryFormulaParams(String entryId, List<String> ids);

    /**
     * 删除指标界面参数
     * 
     * @param entryId
     *            指标明细ID
     * @param ids
     *            界面参数实体ID集合
     */
    void deleteIndexEntryUIParams(String entryId, List<String> ids);

    /**
     * 删除指标表格
     * 
     * @param entryId
     *            指标明细ID
     * @param ids
     *            表格实体ID集合
     */
    void deleteIndexEntryTabs(String entryId, List<String> ids);

    /**
     * 获取下一排序号
     * 
     * @param indexId
     *            指标ID
     * @return
     */
    Integer getIndexEntryNextSequence(String indexId);

    /**
     * 保存排序号
     * 
     * @param params
     */
    void updateIndexEntriesSequence(Map<String, Integer> params);

    /**
     * 查询指标明细
     * 
     * @param queryRequest
     *            指标查询条件实体
     * @return
     *         指标明细实体集合
     */
    Map<String, Object> slicedQueryIndexEntries(IndexQueryRequest queryRequest);

    /**
     * 查询指标界面参数
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryIndexEntryUIParams(IndexEntryEntryQueryRequest queryRequest);

    /**
     * 查询指标表格
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryIndexEntryTabs(IndexEntryEntryQueryRequest queryRequest);

    /**
     * 查询公式参数
     * 
     * @param queryRequest
     *            查询模型
     * @return
     */
    Map<String, Object> queryIndexEntryFormulaParams(IndexEntryEntryQueryRequest queryRequest);

    /**
     * 查询指标实例
     * 
     * @param indexId
     * @return
     */
    Map<String, Object> queryIndexEntriesByIndexId(String indexId);

}
