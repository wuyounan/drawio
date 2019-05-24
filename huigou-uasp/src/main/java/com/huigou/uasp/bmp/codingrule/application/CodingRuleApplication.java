package com.huigou.uasp.bmp.codingrule.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRule;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleQueryRequest;

/**
 * 编码规则应用
 * 
 * @author gongmm
 */
public interface CodingRuleApplication {

    static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/codingRule.xml";

    /**
     * 保存编码规则
     * 
     * @param codingRule
     *            编码规则对象
     * @return
     */
    String saveCodingRule(CodingRule codingRule);

    /**
     * 加载编码规则
     * 
     * @param id
     *            编码规则ID
     * @return
     */
    CodingRule loadCodingRule(String id);

    /**
     * 移动编码规则
     * 
     * @param ids
     *            编码规则ID集合
     * @param folderId
     *            文件夹ID
     */
    void moveCodingRules(List<String> ids, String folderId);

    /**
     * 更新编码规则状态
     * 
     * @param ids
     *            编码规则ID集合
     * @param status
     *            状态
     */
    void updateCodingRulesStatus(List<String> ids, Integer status);

    /**
     * 删除编码规则
     * 
     * @param ids
     *            编码规则ID集合
     */
    void deleteCodingRules(Collection<String> ids);

    /**
     * 删除编码规则明细
     * 
     * @param codingRuleId
     *            编码规则ID
     * @param ids
     *            编码规则明细ID集合
     */
    void deleteCodingRuleDetails(String codingRuleId, List<String> ids);

    /**
     * 分页查询编码规则
     * 
     * @param queryRequest
     *            查询请求
     * @return
     */
    Map<String, Object> slicedQueryCodingRules(CodingRuleQueryRequest queryRequest);

    /**
     * 分页查询编码规则明细
     * 
     * @param queryRequest
     * @return
     */
    Map<String, Object> slicedQueryCodingRuleDetails(ParentIdQueryRequest queryRequest);
}
