package com.huigou.uasp.bmp.codingrule.application.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.codingrule.application.CodingGenerator;
import com.huigou.uasp.bmp.codingrule.application.DateTimeUtil;
import com.huigou.uasp.bmp.codingrule.application.MaxSerialApplication;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail.AttributeKind;
import com.huigou.uasp.bmp.codingrule.domain.model.CodingRuleDetail.DirectionKind;
import com.huigou.uasp.bmp.codingrule.domain.model.ReferenceDefinition;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleDesc;
import com.huigou.uasp.bmp.codingrule.domain.query.CodingRuleDetailDesc;
import com.huigou.uasp.bmp.codingrule.repository.CodingRuleRepository;
import com.huigou.util.CommonUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

@Service("codingGenerator")
public class CodingGeneratorImpl implements CodingGenerator {

    private static final Log logger = LogFactory.getLog(CodingGeneratorImpl.class);

    @Autowired
    private CodingRuleRepository codingRuleRepository;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private MaxSerialApplication maxSerialApplication;

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "codingRule");
        return queryDescriptor.getSqlByName(name);
    }

    private CodingRuleDesc getCodingRuleDesc(String bizKindId) {
        String sql = this.getQuerySqlByName("selectByCode");
        CodingRuleDesc codingRuleDesc = this.sqlExecutorDao.queryToObject(sql, CodingRuleDesc.class, bizKindId);
        Assert.state(codingRuleDesc != null, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_CODE, bizKindId, "编码规则"));

        sql = this.getQuerySqlByName("selectDetail");
        List<CodingRuleDetailDesc> codingRuleDetailDescs = this.sqlExecutorDao.queryToList(sql, CodingRuleDetailDesc.class, codingRuleDesc.getId());
        Assert.state(codingRuleDetailDescs.size() > 0, String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_BIZ_ID, codingRuleDesc.getId(), "编码规则明细"));
        codingRuleDesc.setDetails(codingRuleDetailDescs);

        return codingRuleDesc;
    }

    public String getPropertyValue(SDO params, String propertyName) {
        Object propertyValue = params.getProperty(propertyName);

        if (propertyValue != null && propertyValue instanceof Date) {
            propertyValue = new SimpleDateFormat("yyyy-MM-dd").format((Date) propertyValue).toString();
        }

        if (propertyValue == null) {
            propertyValue = "";
        }

        return propertyValue.toString();
    }

    /**
     * 获取属性值
     * 
     * @param params
     *            SDO 参数
     * @param propertyName
     *            属性值
     * @return
     */
    private String getFullPropertyValue(SDO params, String propertyName) {
        return getPropertyValue(params, propertyName);
    }

    /**
     * 截取字符串
     * 
     * @param value
     *            字符串
     * @param position
     *            开始位置
     * @param len
     *            长度
     * @param direction
     *            方向
     *            <p>
     *            FORWARD：从开始位置计算截取开始位置；
     *            <p>
     *            BACKWARD：从结束位置计算截取开始位置。
     * @return
     */
    private String interceptString(String value, int position, int len, String direction) {
        if (logger.isDebugEnabled()) {
            logger.info("截取字符串:" + value + " ,位置 = " + position + " ,长度 = " + len + " , 方向 = " + direction);
        }

        int beginIndex = 0;

        DirectionKind directionKind = DirectionKind.fromName(direction);

        int endIndex = value.length();

        if ((len > 0) && (position > 0) && (!DirectionKind.NONE.equals(directionKind)) && (position <= value.length())) {
            if (directionKind.isBackward()) {
                if (position <= value.length()) {
                    beginIndex = value.length() - position;
                } else {
                    beginIndex = 0;
                }
            } else if (directionKind.isForward()) {
                if (position <= value.length()) {
                    beginIndex = position - 1;
                } else {
                    beginIndex = value.length() - 1;
                }
            }
        }

        beginIndex = (beginIndex >= 0) ? beginIndex : 0;
        endIndex = beginIndex + len;
        endIndex = (endIndex < value.length()) ? endIndex : value.length();

        logger.info("开始位置：" + beginIndex + ",结束位置：" + endIndex);

        return value.substring(beginIndex, endIndex);
    }

    /**
     * 获取部分属性值
     * 
     * @param params
     *            SDO 参数
     * @param propertyName
     *            属性名称
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @return
     */
    private String getPartPropertyValue(SDO params, String propertyName, CodingRuleDetailDesc codingRuleDetailDesc) {
        String propertyValue = getPropertyValue(params, propertyName);
        return interceptString(propertyValue, codingRuleDetailDesc.getInterceptPos(), codingRuleDetailDesc.getInterceptLength(),
                               codingRuleDetailDesc.getInterceptDirection());
    }

    /**
     * 获取系统属性值
     * 
     * @param params
     *            SDO 参数
     * @param propertyName
     *            属性名称
     * @return
     */
    private String getFullPropertyValueForWhole(SDO params, String propertyName) {
        return getPropertyValueForWhole(params, propertyName);
    }

    /**
     * TODO 后期支持 获取系统属性值
     * 
     * @param params
     *            SDO 参数
     * @param propertyName
     *            属性名称
     * @return
     */
    private String getPropertyValueForWhole(SDO params, String propertyName) {
        String[] properties = propertyName.split("\\.");
        if (((properties.length > 2) && (properties[0] != null) && (!(properties[0].equalsIgnoreCase("period"))))
            || ((properties.length > 3) && (properties[0] != null) && (properties[0].equalsIgnoreCase("period")))) {
            // TODO 异常
        }
        if (properties.length == 0) {
            // TODO 异常
        }
        if (properties.length == 1) {
            logger.debug("属性值 : " + params.getString(properties[0]));
            return params.getString(properties[0]);
        }

        //Object obj = null;
        String wholeKind = properties[0];
        if (wholeKind.equals("Period")) { // 系统类别：如期间
            // obj = getPeriodInfo(properties);
        } else if (wholeKind.equals("City")) {// 系统类别：如区域
            // obj = getCityInfo(ctx);
        } else {// 系统类别：如用户
            // obj = ContextUtil.getCurrentUserInfo(ctx);
        }

        logger.debug("属性类别: " + wholeKind);
        //if (obj == null) {
        //    return "";
        //}
        return "";
    }

    /**
     * 获取部分系统属性值
     * 
     * @param params
     *            SDO参数
     * @param propertyName
     *            属性名称
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @return
     */
    private String getPartPropertyValueForWhole(SDO params, String propertyName, CodingRuleDetailDesc codingRuleDetailDesc) {
        return interceptString(getPropertyValueForWhole(params, propertyName), codingRuleDetailDesc.getInterceptPos(),
                               codingRuleDetailDesc.getInterceptLength(), codingRuleDetailDesc.getInterceptDirection());
    }

    /**
     * 获取引用值
     * 
     * @param referenceDefinition
     *            引用定义
     * @param params
     *            SDO参数
     * @return
     */
    private String getReferenceValue(SDO params, ReferenceDefinition referenceDefinition) {
        String sql = String.format("select %s from %s where %s = ?", referenceDefinition.getReferenceFieldName(), referenceDefinition.getReferenceTableName(),
                                   referenceDefinition.getReferencePrimaryKeyFieldName());
        String attributeValue = params.getString(referenceDefinition.getAttributeName());
        return this.sqlExecutorDao.queryToString(sql, attributeValue);
    }

    /**
     * 解析规则明细
     * 
     * @param params
     *            SDO参数
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @return
     */
    private String parseCodingRuleDetail(SDO params, CodingRuleDetailDesc codingRuleDetailDesc) {
        AttributeKind attributeKind = codingRuleDetailDesc.getAttributeKindEnum();
        String result = "";
        switch (attributeKind) {
        case SERIAL_NUMBER:
            // 流水号：通过规则明细的分类排序组合获取
            break;
        case SYSTEM_TIME:
            // 系统日期：通过定义的日期显示格式转换当前日期
            result = DateTimeUtil.getSystemDate(codingRuleDetailDesc.getFormat());
            break;
        case FIXED_VALUE:
            // 固定值：取定义的显示格式
            result = (codingRuleDetailDesc.getFormat() == null) ? "" : codingRuleDetailDesc.getFormat();
            break;
        case CUSTOM:
            result = "isCustom == true";
            break;
        case ATTRIBUTE:
            String format = codingRuleDetailDesc.getFormat();
            // 属性值，日期格式
            if (StringUtil.isNotBlank(format)) {
                format = format.trim().toLowerCase();
                int dateIndex = 0;
                try {
                    dateIndex = Integer.parseInt(format);
                } catch (NumberFormatException e) {
                    logger.info("CodingRule timeformat error(now use defaultformat): " + e.getMessage());
                    dateIndex = Integer.parseInt("17");
                }

                result = getFullPropertyValue(params, codingRuleDetailDesc.getAttributeValue().trim());

                String[] sysTimeFormat = null;
                try {
                    sysTimeFormat = DateTimeUtil.getTimeFormat(CommonUtil.parseDateTime(result));
                } catch (Exception e) {
                    logger.info("error", e);
                    return result;
                }
                switch (codingRuleDetailDesc.getAttributeUseKindEnum()) {
                case FULL:
                    result = sysTimeFormat[dateIndex];
                    break;
                case PART:
                    result = interceptString(sysTimeFormat[dateIndex], codingRuleDetailDesc.getInterceptPos(), codingRuleDetailDesc.getInterceptLength(),
                                             codingRuleDetailDesc.getInterceptDirection());
                    break;
                default:
                    break;
                }
            }
            // 其他情况
            switch (codingRuleDetailDesc.getAttributeUseKindEnum()) {
            case FULL:
                result = getFullPropertyValue(params, codingRuleDetailDesc.getAttributeValue().trim());
                break;
            case PART:
                result = getPartPropertyValue(params, codingRuleDetailDesc.getAttributeValue(), codingRuleDetailDesc);
                break;
            case REFERENCE:
                ReferenceDefinition referenceDefinition = ReferenceDefinition.fromSetting(codingRuleDetailDesc.getAttributeValue());
                result = this.getReferenceValue(params, referenceDefinition);
                break;
            }
            break;
        case WHOLE:
            switch (codingRuleDetailDesc.getAttributeUseKindEnum()) {
            case FULL:
                result = getFullPropertyValueForWhole(params, codingRuleDetailDesc.getAttributeValue().trim());
                break;
            case PART:
                result = getPartPropertyValueForWhole(params, codingRuleDetailDesc.getAttributeValue(), codingRuleDetailDesc);
                break;
            case REFERENCE:
                ReferenceDefinition referenceDefinition = ReferenceDefinition.fromSetting(codingRuleDetailDesc.getAttributeValue());
                result = this.getReferenceValue(params, referenceDefinition);
            }
            break;
        default:
            break;
        }
        return result;
    }

    /**
     * 解析编码规则明细
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param sortItemValue
     *            分类排序值
     * @param readonly
     *            只读
     * @param isNonBreak
     *            不短号
     * @return
     */
    private String parseCodingRuleDetail(CodingRuleDetailDesc codingRuleDetailDesc, String sortItemValue, boolean readonly, boolean isNonBreak) {
        if (sortItemValue == null) {
            sortItemValue = "";
        }

        if (readonly) {
            return maxSerialApplication.readMaxSerial(codingRuleDetailDesc, sortItemValue, null);
        }

        return maxSerialApplication.getMaxValueInNewTransaction(codingRuleDetailDesc, sortItemValue, isNonBreak);
    }

    /**
     * TODO 检查流水号有效性
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param initStr
     */
    @SuppressWarnings("unused")
    private void checkSerialNumberValid(CodingRuleDetailDesc codingRuleDetailDesc, String initStr) {
        try {
            if ((codingRuleDetailDesc.getAttributeKind() != null)
                && (codingRuleDetailDesc.getAttributeKind().equals("sn"))
                && (((codingRuleDetailDesc.getStep() > 0) && (codingRuleDetailDesc.getLength() > 0) && (Long.parseLong(initStr.trim()) >= Math.pow(10.0D,
                                                                                                                                                   codingRuleDetailDesc.getLength()))) || ((codingRuleDetailDesc.getStep() < 0) && (Long.parseLong(initStr.trim()) < 0L)))) {
                String crName;
                try {
                    // TODO
                    crName = "";// codingRuleDetailDesc.getCodingRule().getName();
                } catch (Exception ex) {
                    crName = "";
                }
                if (crName == null) {
                    crName = "";
                } else {
                    crName = "\"" + crName + "\"";
                }
                // throw new CodingRuleException(CodingRuleException.INFO_NUMBERISUSEUP, new Object[] { crName });
            }
        } catch (NumberFormatException ex) {
            logger.error(ex);
        }
    }

    /**
     * 格式字符串
     * 
     * @param codingRuleDetailDesc
     *            编码规则明细
     * @param value
     *            字符串
     * @return
     */
    private String formatString(CodingRuleDetailDesc codingRuleDetailDesc, String value) {
        if (logger.isDebugEnabled()) {
            logger.debug("格式化字符:" + value);
        }

        AttributeKind attributeKind = codingRuleDetailDesc.getAttributeKindEnum();

        if ((AttributeKind.SYSTEM_TIME.equals(attributeKind))
            || ((AttributeKind.ATTRIBUTE.equals(attributeKind)) && ((codingRuleDetailDesc.getAttributeUseKind().equals("part")) || (codingRuleDetailDesc.getAttributeUseKind().equals("reference"))))
            || ((AttributeKind.WHOLE.equals(attributeKind)) && ((codingRuleDetailDesc.getAttributeUseKind().equals("part")) || (codingRuleDetailDesc.getAttributeUseKind().equals("reference"))))) {
            return value;
        }

        // if ((initStr == null) || (initStr.trim().length() == 0)) {
        // return "";
        // }

        if ((value == null)) {
            value = "";
        }

        // checkSerialNumberValid(codingRuleDetailDesc, value);

        if ((codingRuleDetailDesc.getLength() > 0) && (codingRuleDetailDesc.getLength() > value.length())) {
            if (logger.isDebugEnabled()) {
                logger.debug("需补位。");
            }

            if (codingRuleDetailDesc.needFillSign(value)) {
                int fillNumber = codingRuleDetailDesc.getLength() - value.length();
                if (codingRuleDetailDesc.getFillSignDirectionEnum().equals(DirectionKind.FORWARD)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("补位\\:前补");
                    }

                    for (int i = 0; i < fillNumber; i++) {
                        value = codingRuleDetailDesc.getFillSign() + value;
                    }
                } else if (codingRuleDetailDesc.getFillSignDirectionEnum().equals(DirectionKind.BACKWARD)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("补位\\:后补");
                    }

                    for (int i = 0; i < fillNumber; i++) {
                        value = value + codingRuleDetailDesc.getFillSign();
                    }
                }
            }
        } else if ((codingRuleDetailDesc.getLength() > 0) && (codingRuleDetailDesc.getLength() < value.length())) {
            if (AttributeKind.SERIAL_NUMBER.equals(attributeKind)) {
                value = value.substring(value.length() - codingRuleDetailDesc.getLength(), value.length());
            } else {
                value = value.substring(0, codingRuleDetailDesc.getLength());
            }
        }

        return value;
    }

    @Override
    @Transactional
    public String getNextCode(String bizKindId, Map<String, Object> inputParams) {
        Assert.hasText(bizKindId, "参数bizKindId不能为空。");
        SDO params = new SDO(inputParams);

        CodingRuleDesc codingRuleDesc = this.getCodingRuleDesc(bizKindId);

        StringBuilder sortItemValue = new StringBuilder();
        StringBuilder result = new StringBuilder();

        boolean snBefore = true;

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("“%s”取号开始，共有%d个规则明细。", codingRuleDesc.getCode(), codingRuleDesc.getDetails().size()));
        }

        CodingRuleDetailDesc codingRuleDetailDesc, codingRuleDetailDesc2;
        for (int i = 0, detailSize = codingRuleDesc.getDetails().size(); i < detailSize; i++) {
            codingRuleDetailDesc = codingRuleDesc.getDetails().get(i);

            if (codingRuleDesc.useDelimiter() && codingRuleDetailDesc.useDelimiter() && result.length() > 0) {
                result.append(codingRuleDesc.getDelimiter());
            }

            String parsedEntryValue;

            AttributeKind codingRuleAttributeKind = AttributeKind.fromName(codingRuleDetailDesc.getAttributeKind());

            if (AttributeKind.SERIAL_NUMBER.equals(codingRuleAttributeKind)) {
                for (int j = i + 1; j < detailSize; j++) {
                    codingRuleDetailDesc2 = codingRuleDesc.getDetails().get(j);
                    if (codingRuleDetailDesc2.getIsSortByItem()) {
                        if (sortItemValue.length() > 0) {
                            sortItemValue.append(MaxSerialApplication.SPLIT_DELIMITER);
                        }
                        sortItemValue.append(this.parseCodingRuleDetail(params, codingRuleDetailDesc2));
                    }
                }

                parsedEntryValue = this.parseCodingRuleDetail(codingRuleDetailDesc, sortItemValue.toString(), false, true);

                snBefore = false;
            } else {
                parsedEntryValue = this.parseCodingRuleDetail(params, codingRuleDetailDesc);
            }

            if ("isCustom == true".equals(parsedEntryValue)) {
                String customValue = params.getString(CUSTOM_KEY);
                if (customValue.trim().length() > 0) {
                    parsedEntryValue = customValue.trim();
                } else {
                    parsedEntryValue = "";
                }
            }

            parsedEntryValue = formatString(codingRuleDetailDesc, parsedEntryValue);
            if (logger.isDebugEnabled()) {
                logger.debug("格式化后的条目值：" + parsedEntryValue);
            }

            if (snBefore && !AttributeKind.SERIAL_NUMBER.equals(codingRuleDetailDesc.getAttributeKindEnum()) && (codingRuleDetailDesc.getIsSortByItem())) {
                if (sortItemValue.length() > 0) {
                    sortItemValue.append(MaxSerialApplication.SPLIT_DELIMITER);
                }
                sortItemValue.append(parsedEntryValue);
            }

            if (codingRuleDetailDesc.getIsDisplay()) {
                result.append(parsedEntryValue);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("“%s”取号结束， 编码为%s。", codingRuleDesc.getName(), result.toString()));
        }
        if (logger.isInfoEnabled()){
            logger.info(String.format("“%s”取号结束， 编码为%s。", codingRuleDesc.getName(), result.toString()));
        }
        return result.toString();
    }

}
