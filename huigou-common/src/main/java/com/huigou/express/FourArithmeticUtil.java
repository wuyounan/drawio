package com.huigou.express;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huigou.exception.ExpressExecuteException;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 解析执行四则运算
 * 
 * @author xiex
 */
public class FourArithmeticUtil {
    private static Pattern pattern = Pattern.compile("\\{(.*?)\\}", Pattern.CASE_INSENSITIVE);

    /**
     * 解析公式
     * 
     * @param formula
     * @param param
     * @return
     */
    public static String parseFormula(String formula, Map<String, Object> param) {
        if (StringUtil.isBlank(formula)) {
            return "";
        }
        String name = "", value = "", varName;
        StringBuffer variable = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        boolean result = false;
        Matcher m = pattern.matcher(formula);
        result = m.find();
        int i = 0;
        while (result) {
            if (m.group(1) != null) {
                name = m.group(1).trim();
                value = ClassHelper.convert(param.get(name), String.class);
                if (StringUtil.isBlank(value)) {
                    throw new ExpressExecuteException(String.format("未找到[%s]对应参数", name));
                }
                varName = String.format("v%s", i);
                m.appendReplacement(sb, varName);
                variable.append("double ").append(varName).append("=").append(value).append(";");
                i++;
            }
            result = m.find();
        }
        m.appendTail(sb);
        variable.append("return ").append(sb).append(";");
        return variable.toString();
    }

    /**
     * 执行函数取numebr数据
     * 
     * @param formula
     *            公式
     * @param param
     *            参数
     * @param scale
     *            取值精度
     * @return
     */
    public static BigDecimal evaluateFormula(String formula, Map<String, Object> param, Integer scale) {
        String function = formula;
        try {
            function = parseFormula(function, param);
        } catch (Exception e) {
            throw new ExpressExecuteException(e);
        }
        int s = scale == null ? 2 : scale;
        Object value = null;
        try {
            value = ExpressManager.evaluate(function);
            if (value != null) {
                if (value instanceof Number) {
                    Number num = (Number) value;
                    BigDecimal bd = new BigDecimal(num.doubleValue());
                    // 判断是否存在小数
                    if (bd.compareTo(new BigDecimal(bd.longValue())) > 0) {
                        // 小数保留2位小数
                        return bd.setScale(s, BigDecimal.ROUND_HALF_UP);
                    } else {
                        return bd;
                    }
                } else {
                    throw new ExpressExecuteException("函数返回的数据不是数字!");
                }
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            throw new ExpressExecuteException(e);
        }
    }

}
