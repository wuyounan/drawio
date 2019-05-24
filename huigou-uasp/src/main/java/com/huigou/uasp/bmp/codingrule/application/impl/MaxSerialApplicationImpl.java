package com.huigou.uasp.bmp.codingrule.application.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.listener.VersionListener;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.codingrule.application.MaxSerialApplication;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleDetailDesc;
import com.huigou.uasp.bmp.codingrule.repository.MaxSerialRepository;
import com.huigou.uasp.bmp.configuration.domain.query.IntermilCodeDesc;
import com.huigou.uasp.bmp.configuration.domain.query.MaxSerialDesc;
import com.huigou.util.CommonUtil;
import com.huigou.util.StringUtil;

@Service("maxSerialApplication")
public class MaxSerialApplicationImpl implements MaxSerialApplication {

    private static final Log logger = LogFactory.getLog(MaxSerialApplicationImpl.class);

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private MaxSerialRepository maxSerialRepository;

    /**
     * 获取断号
     * 
     * @param codingRuleDetailId
     *            编码规则明细ID
     * @param sortItemValue
     *            分类排序值
     * @param initial
     *            初始值
     * @param step
     *            步长
     * @return
     */
    private IntermilCodeDesc getIntermilNOInfoValue(String codingRuleDetailId, String sortItemValue, Integer initial, Integer step) {
        String sql;
        if (step >= 0) {
            sql = "select ID, Serial_Number from SA_IntermitNO where CodingRuleDetail_ID = ? and Sort_Item_Value = ? and Serial_Number >= ? order by Serial_Number ";
        } else {
            sql = "select ID, Serial_Number from SA_IntermitNO where CodingRuleDetial_ID = ? and Sort_Item_Value = ? and  Serial_Number < ? order by Serial_Number ";
        }
        return this.sqlExecutorDao.queryOneToObject(sql, IntermilCodeDesc.class, codingRuleDetailId, sortItemValue, initial);
    }

    /**
     * 获取最大流水号
     * 
     * @param codingRuleDetailId
     *            编码规则明细ID
     * @param sortItemValue
     *            分类排序值
     * @return
     */
    private MaxSerialDesc getMaxSerialDesc(String codingRuleDetailId, String sortItemValue) {
        return getMaxSerialDesc(codingRuleDetailId, sortItemValue, null);
    }

    /**
     * 获取最大流水号定义
     * 
     * @param codingRuleDetailId
     *            编码规则明细ID
     * @param sortItemValue
     *            分类排序值
     * @param segmentAttributeValue
     *            段属性定义
     * @return
     */
    private MaxSerialDesc getMaxSerialDesc(String codingRuleDetailId, String sortItemValue, String segmentAttributeValue) {
        if (logger.isDebugEnabled()) {
            logger.debug("查询最大流水号操作，分类排序值为：" + sortItemValue + ";编码规则明细ID为：" + codingRuleDetailId);
        }
        String sql = "select id, Sort_Item_Value,Serial_Number,CodingRuleDetail_Id as Coding_Rule_Detail_Id, Initial_Value from SA_MaxSerial where CodingRuleDetail_Id = ? and Sort_Item_Value = ?";

        if (segmentAttributeValue != null) {
            // TODO
        }
        return this.sqlExecutorDao.queryToObject(sql, MaxSerialDesc.class, codingRuleDetailId, sortItemValue);
    }

    @Override
    public String readMaxSerial(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, String segmentAttributeValue) {
        String maxVaule = "";
        Integer value = 0;
        sortItemValue = (sortItemValue.length() > 0) ? sortItemValue : CODING_RULE_MAX_SERIAL;

        IntermilCodeDesc intermilCodeDesc = getIntermilNOInfoValue(codingRuleDetailDesc.getId(), sortItemValue, codingRuleDetailDesc.getInitialValue(),
                                                                   codingRuleDetailDesc.getStep());

        if (intermilCodeDesc != null) {
            maxVaule = intermilCodeDesc.getSerialNumber() + "";
        } else {
            MaxSerialDesc maxSerialDesc = getMaxSerialDesc(codingRuleDetailDesc.getId(), sortItemValue, segmentAttributeValue);
            if (maxSerialDesc == null && (sortItemValue.indexOf(SPLIT_DELIMITER) != -1) && (StringUtil.isBlank(segmentAttributeValue))) {
                maxSerialDesc = getMaxSerialDesc(codingRuleDetailDesc.getId(), sortItemValue.replaceAll(SPLIT_DELIMITER, ""));
            }

            if (maxSerialDesc != null) {
                value = maxSerialDesc.getSerialNumber();
            }
            if (value == 0) {
                maxVaule = codingRuleDetailDesc.getInitialValue() + "";
            } else {
                maxVaule = (value + codingRuleDetailDesc.getStep()) + "";
            }
        }

        return maxVaule;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getMaxValueInNewTransaction(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNonBreak) {
        return getMaxValueForNonBreak(codingRuleDetailDesc, sortItemValue, isNonBreak);
    }

    @Override
    @Transactional
    public String getMaxValue(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNonBreak) {
        return getMaxValueForNonBreak(codingRuleDetailDesc, sortItemValue, isNonBreak);
    }

    private String getMaxValueForNonBreak(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNonBreak) {
        sortItemValue = (sortItemValue.length() > 0) ? sortItemValue : CODING_RULE_MAX_SERIAL;

        String guid = CommonUtil.createGUID();

        if (logger.isInfoEnabled()) {
            logger.info("开始获取最大流水号：" + guid);
        }

        Integer value = updateMaxSerial(codingRuleDetailDesc, sortItemValue, isNonBreak);

        if (logger.isInfoEnabled()) {
            logger.info("获取最大流水号结束：" + guid);
        }

        return value + "";
    }

    private void updateLockTable(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue) {
        String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
        if (logger.isInfoEnabled()) {
            logger.info("开始获取表锁 , 会话ID：" + sessionId);
        }

        String id = codingRuleDetailDesc.getId() + sortItemValue;

        String sql = "select count(*) fcount from SA_CodingRuleLock where id=?";
        Integer count = this.sqlExecutorDao.queryToInt(sql, id);

        if (count == 0) {
            sql = "insert into SA_CodingRuleLock values(?, 1)";

        } else {
            sql = "update SA_CodingRuleLock set Lock_Kind = 2 where id = ?";
        }
        this.sqlExecutorDao.executeUpdate(sql, id);

        if (logger.isInfoEnabled()) {
            logger.info("获取表锁结束，会话ID：" + sessionId);
        }
    }

    /**
     * 获取最大流水号
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param sortItemValue
     *            分类排序值
     * @return
     */
    private Integer getMaxSerialNoBreak(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue) {
        Integer value;
        String maxSerialId;
        MaxSerialDesc maxSerialDesc = this.getMaxSerialDesc(codingRuleDetailDesc.getId(), sortItemValue);
        if (maxSerialDesc == null && sortItemValue.indexOf(SPLIT_DELIMITER) != -1) {
            maxSerialDesc = this.getMaxSerialDesc(codingRuleDetailDesc.getId(), sortItemValue.replaceAll("_split_", ""));
        }
        if (maxSerialDesc == null) {
            StringBuilder sb = new StringBuilder();

            sb.append("insert into sa_maxserial");
            sb.append("  (id, codingruledetail_id, sort_item_value, serial_number, segment_attribute_value, initial_value, version)");
            sb.append("values");
            sb.append("  (?, ?, ?, ?, ?, ?, ?)");

            maxSerialId = CommonUtil.createGUID();

            Long version = this.sqlExecutorDao.queryToLong(VersionListener.GET_NEXT_SEQ_SQL);

            this.sqlExecutorDao.executeUpdate(sb.toString(), maxSerialId, codingRuleDetailDesc.getId(), sortItemValue, codingRuleDetailDesc.getInitialValue(),
                                              "", codingRuleDetailDesc.getInitialValue(), version);

            value = codingRuleDetailDesc.getInitialValue();
        } else {
            value = maxSerialDesc.getSerialNumber();
            value += codingRuleDetailDesc.getStep();
            Integer initialValue = maxSerialDesc.getInitialValue();
            if (codingRuleDetailDesc.getStep() > 0 && initialValue > value) {
                value = initialValue;
            } else if (codingRuleDetailDesc.getStep() < 0 && initialValue < value && initialValue > 0) {
                value = initialValue;
            }
            maxSerialId = maxSerialDesc.getId();
        }

        updateMaxSerial(maxSerialId, sortItemValue, value);

        return value;

    }

    /**
     * 更新最大流水号
     * 
     * @param id
     *            主键ID
     * @param sortItemValue
     *            分类排序值
     * @param serialNumber
     *            最大流水号
     */
    private void updateMaxSerial(String id, String sortItemValue, Integer serialNumber) {
        String sql = "update sa_maxserial set sort_item_value = ?, serial_number = ? where id = ?";
        this.sqlExecutorDao.executeUpdate(sql, sortItemValue, serialNumber, id);
    }

    /**
     * 在新事务中获取获取流水号
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param sortItemValue
     *            分类项目值
     * @param isNoBreak
     *            是否从断号表中获取
     * @return
     */
    private Integer updateMaxSerial(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean isNoBreak) {
        updateLockTable(codingRuleDetailDesc, sortItemValue);
        // TODO 从断号中获取
        Integer result = getMaxSerialNoBreak(codingRuleDetailDesc, sortItemValue);
        return result;
    }

}
