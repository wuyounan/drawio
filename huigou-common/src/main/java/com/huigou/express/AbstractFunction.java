package com.huigou.express;

import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;

/**
 * 自定义函数基类
 * 
 * @author gongmm
 */
public abstract class AbstractFunction {
    /**
     * 获取当前操作员
     * 
     * @author
     * @return Operator
     */
    public Operator getOperator() {
        return ThreadLocalUtil.getOperator();
    }

    /**
     * 获取变量值
     * 
     * @author
     * @param name
     * @return Object
     */
    public Object getVariable(String name) {
        return VariableContainer.getVariable(name);
    }

    /**
     * 获取变量值
     * 
     * @author
     * @param name
     * @return Object
     */
    public <T> T getVariable(String name, Class<T> cls) {
        return VariableContainer.getVariable(name, cls);
    }
}
