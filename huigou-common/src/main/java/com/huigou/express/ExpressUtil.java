package com.huigou.express;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.huigou.exception.ExpressExecuteException;
import com.huigou.util.LogHome;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;

/**
 * 执行的工具类
 * 
 * @author xx
 */
public class ExpressUtil implements ApplicationContextAware {

    private static ExpressRunner runner;
    static {
        runner = new ExpressRunner();
    }

    private List<Object> beanNames;

    private ApplicationContext applicationContext;// spring上下文

    public List<Object> getBeanNames() {
        return beanNames;
    }

    public void setBeanNames(List<Object> beanNames) {
        this.beanNames = beanNames;
    }

    public void setApplicationContext(ApplicationContext aContext) throws BeansException {
        applicationContext = aContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param statement
     *            执行语句
     * @param context
     *            上下文
     * @throws Exception
     */
    public Object execute(String statement, Map<String, Object> context) throws Exception {
        try {
            IExpressContext<String, Object> expressContext = new ExpressContext(context, applicationContext);
            statement = initStatement(statement);
            return runner.execute(statement, expressContext, null, true, false);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LogHome.getLog(this).error(e.getTargetException());
            throw new ExpressExecuteException("表达式：\"" + statement + "\" 执行异常:" + e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            e.printStackTrace();
            LogHome.getLog(this).error(e);
            throw new ExpressExecuteException("表达式：\"" + statement + "\" 执行异常", e);
        } finally {
            // 释放脚本变量容器
            VariableContainer.removeVariableMap();
        }

    }

    public Object execute(String statement) throws Exception {
        try {
            IExpressContext<String, Object> expressContext = new ExpressContext(applicationContext);
            statement = initStatement(statement);
            return runner.execute(statement, expressContext, null, true, false);
        } catch (InvocationTargetException e) {
            LogHome.getLog(this).error(e.getTargetException());
            throw new ExpressExecuteException("表达式：\"" + statement + "\" 执行异常:" + e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            LogHome.getLog(this).error(e);
            throw new ExpressExecuteException("表达式：\"" + statement + "\" 执行异常", e);
        } finally {
            // 释放脚本变量容器
            VariableContainer.removeVariableMap();
        }
    }

    /**
     * 在此处把一些中文符号替换成英文符号
     * 
     * @param statement
     * @return
     */
    private String initStatement(String statement) {
        return statement.replace("（", "(").replace("）", ")").replace("；", ";").replace("，", ",").replace("“", "\"").replace("”", "\"");
    }

    protected void addFunction(String name, Object object, String functionName, Class<?>[] parameterClassTypes) {
        synchronized (runner) {
            try {
                runner.addFunctionOfServiceMethod(name, object, functionName, parameterClassTypes, null);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("初始化失败表达式", e);
            }
        }
    }

}