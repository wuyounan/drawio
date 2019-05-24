package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.configuration.domain.model.SysDictionary;
import com.huigou.uasp.bmp.configuration.domain.query.SysDictionariesQueryRequest;

/**
 * 数据字典
 * 
 * @author gongmm
 */
public interface DictionaryApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 保存数据字典
     * 
     * @param sysDictionary
     *            数据字典实体
     * @return
     *         数据字典ID
     */
    String saveSysDictionary(SysDictionary sysDictionary);

    /**
     * 加载数据字典
     * 
     * @param id
     *            数据字典ID
     * @return
     *         数据字典实体
     */
    SysDictionary loadSysDictionary(String id);

    /**
     * 删除数据字典
     * 
     * @param ids
     *            数据字典ID列表
     */
    void deleteSysDictionaries(List<String> ids);

    /**
     * 移动数据字典
     * 
     * @param ids
     *            数据字典ID列表
     * @param folderId
     *            文件夹ID
     */
    void moveSysDictionaries(List<String> ids, String folderId);

    /**
     * 更新数据字典状态
     * 
     * @param ids
     *            数据字典ID列表
     * @param status
     *            状态
     */
    void updateSysDictionariesStatus(List<String> ids, Integer status);

    /**
     * 分页查询数据字典
     * 
     * @param folderId
     *            文件ID
     * @param code
     *            编码
     * @param name
     *            名称
     * @param kindId
     *            类别
     * @param status
     *            状态
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> slicedQuerySysDictionaries(SysDictionariesQueryRequest queryRequest);

    /**
     * 删除数据字典明细
     * 
     * @param dictionaryId
     *            数据字典ID
     * @param ids
     *            明细ID列表
     */
    void deleteSysDictionaryDetails(String dictionaryId, List<String> ids);

    /**
     * 根据字典编码查询明细
     * 
     * @param code
     *            字典编码
     * @return
     */
    Map<String, Object> querySysDictionaryDetailsByCode(String code);

    /**
     * 分页查询数据字典明细
     * 
     * @param dictionaryId
     * @param queryModel
     * @return
     */
    Map<String, Object> slicedQuerySysDictionaryDetails(ParentIdQueryRequest queryRequest);

    /**
     * 更新数据字典明细状态
     * 
     * @param ids
     *            数据字典明细ID列表
     * @param status
     *            状态
     */
    void updateSysDictionaryDetailsStatus(List<String> ids, Integer status);

    /**
     * 同步缓存
     */
    void syncCache();

}
