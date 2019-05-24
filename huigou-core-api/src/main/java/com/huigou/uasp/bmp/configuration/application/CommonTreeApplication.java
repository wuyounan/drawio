package com.huigou.uasp.bmp.configuration.application;

import java.util.List;
import java.util.Map;

import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;
import com.huigou.uasp.bmp.configuration.domain.query.CommonTreeQueryRequest;

/**
 * 通用树接口
 * 
 * @author Gerald
 */
public interface CommonTreeApplication {
    /**
     * 查询文件配置地址
     */
    String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/configuration.xml";

    /**
     * 插入通用树
     * 
     * @param commonTree
     * @return
     */
    String insert(CommonTree commonTree);

    /**
     * 修改
     * 
     * @param commonTree
     * @return
     */
    String update(CommonTree commonTree);

    /**
     * 加载通用树
     * 
     * @param id
     *            主键Id
     * @return
     */
    CommonTree load(String id);

    /**
     * 删除通用树
     * 
     * @param id
     */
    void delete(List<String> ids);

    /**
     * 更新排序号
     * 
     * @param params
     *            通用树ID和排序号的Map
     */
    void updateSequence(Map<String, Integer> params);

    /**
     * 根据类别ID和编码查找通用树
     * 
     * @param kindId
     *            类别ID
     * @param code
     *            编码
     */
    CommonTree findByKindIdAndCode(Integer kindId, String code);

    /**
     * 查询通用树
     * 
     * @param kindId
     *            类别ID
     * @param parentId
     *            父ID
     * @param queryModel
     *            查询模型
     * @return
     */
    Map<String, Object> query(CommonTreeQueryRequest queryRequest);

}
