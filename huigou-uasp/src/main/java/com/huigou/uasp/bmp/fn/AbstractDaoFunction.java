package com.huigou.uasp.bmp.fn;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.express.AbstractFunction;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 运行访问数据库的函数基类
 * 
 * @author gongmm
 */
public abstract class AbstractDaoFunction extends AbstractFunction {
    @Autowired
    protected SQLExecutorDao sqlExecutorDao;

    @SuppressWarnings("unchecked")
    protected String getBizParamValue(String param) {
        String value = null;
        SDO bizData = ThreadLocalUtil.getVariable(Constants.SDO, SDO.class);
        if (bizData != null) {
            Map<String, Object> bizParams = bizData.getProperty("bizParams", Map.class);
            // （业务、系统）参数优先，单据数据次之
            if (bizParams != null && bizParams.get(param) != null) {
                value = ClassHelper.convert(bizParams.get(param), String.class);
            }
            if (StringUtil.isBlank(value)) {
                value = bizData.getProperty(param, String.class, "");
            }
        }
        return value;
    }

    protected String getBizId() {
        return this.getBizParamValue("bizId");
    }

}
