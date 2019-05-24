package com.huigou.uasp.bmp.codingrule.application;

import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleDetailDesc;

/**
 * 流水号应用
 * 
 * @author gongmm
 */
public interface MaxSerialApplication {

    String SPLIT_DELIMITER = "_split_";
    
    String CODING_RULE_MAX_SERIAL = "codingrule_maxserial";

    /**
     * 读取最大流水号
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param sortItemValue
     *            分类排序值
     * @param segmentAttributeValue
     *            分段属性值
     * @return
     */
    String readMaxSerial(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, String segmentAttributeValue);

    /**
     * 获取最大流水号
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param sortItemValue
     *            分类排序值
     * @param isNonBreak
     *            是否从断号中取
     * @return
     */
    String getMaxValue(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNonBreak);
    
    /**
     * 在新事务中获取最大流水号
     * @param codingRuleDetailDesc
     * 编码规则明细
     * @param sortItemValue
     * 
     * @param isNonBreak
     * @return
     */
    String getMaxValueInNewTransaction(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNonBreak);
}
