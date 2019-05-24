package com.huigou.uasp.bmp.fn.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.configuration.application.CodeBuildRuleApplication;
import com.huigou.uasp.bmp.configuration.domain.model.CodeBuildRule;
import com.huigou.uasp.bmp.fn.AbstractDaoFunction;
import com.huigou.uasp.bmp.fn.CodeGenerator;
import com.huigou.util.DateUtil;

/**
 * 单据编码规则生成器
 * 
 * @author xx
 */
@Service("codeGenerator")
public class CodeGeneratorImpl extends AbstractDaoFunction implements CodeGenerator {

    @Autowired
    private CodeBuildRuleApplication codeBuildRuleApplication;

    /**
     * 获取单据编号
     * 
     * @return
     */
    public String getNextCode(String bizKindId) {
        StringBuffer sb = new StringBuffer();
        try {
            CodeBuildRule codeBuildRule = codeBuildRuleApplication.getRuleValueAsStep(bizKindId, 1);
            String rule = codeBuildRule.getRule();
            Integer nextValue = codeBuildRule.getCurrentValue();
            Pattern p = Pattern.compile(CodeBuildRuleApplication.RULE_REG, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(rule);
            if (m.find()) {
                String dateFormat = m.group(1);
                int serialLength = Integer.parseInt(m.group(3));
                m.appendReplacement(sb, formatDate(dateFormat, DateUtil.getDateTime()) + m.group(2) + formatSerialNumber(nextValue, serialLength));
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return sb.toString();
    }

    /**
     * 按步长批量取号
     * 
     * @param bizKindId
     * @param step
     */
    public List<String> getNextCodesAsStep(String bizKindId, Integer step) {
        // 步长最小为1
        if (step == null || step < 1) {
            step = 1;
        }
        List<String> list = new ArrayList<>(step);
        try {
            CodeBuildRule codeBuildRule = codeBuildRuleApplication.getRuleValueAsStep(bizKindId, step);
            String rule = codeBuildRule.getRule();
            Integer lastValue = codeBuildRule.getLastValue();
            Pattern p = Pattern.compile(CodeBuildRuleApplication.RULE_REG, Pattern.CASE_INSENSITIVE);
            int serialLength = 1;
            StringBuffer sb = new StringBuffer();
            Matcher m = p.matcher(rule);
            if (m.find()) {
                String dateFormat = m.group(1);
                serialLength = Integer.parseInt(m.group(3));
                m.appendReplacement(sb, formatDate(dateFormat, DateUtil.getDateTime()) + m.group(2) + "%s");
            }
            for (int i = 1; i <= step; i++) {
                list.add(String.format(sb.toString(), formatSerialNumber(lastValue + i, serialLength)));
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return list;
    }

    /**
     * 格式化序列号
     * 
     * @param serialNumber
     *            序列号
     * @param length
     *            长度
     * @return
     */
    private String formatSerialNumber(Integer serialNumber, int length) {
        String result = String.valueOf(serialNumber);
        Assert.isTrue(result.length() <= length, String.format("流水号长度超出限制，设置长度：%s，实际长度：%s。", length, result.length()));
        while (result.length() < length) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * 更具单据编号规则格式化日期
     * 
     * @param dateFormat
     *            日期格式
     * @param date
     *            日期
     * @return
     */
    private String formatDate(String dateFormat, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }

}
