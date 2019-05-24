package com.huigou.express;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import com.ql.util.express.IExpressContext;

/**
 * 表达式环境
 * 
 * @author gongmm
 */
public class ExpressContext extends HashMap<String, Object> implements IExpressContext<String, Object> {

    private static final long serialVersionUID = -7472650796598011875L;

    private ApplicationContext context;

    public ExpressContext(ApplicationContext aContext) {
        this.context = aContext;
    }

    public ExpressContext(Map<String, Object> aProperties, ApplicationContext aContext) {
        super(aProperties);
        this.context = aContext;
    }

    /**
     * 抽象方法：根据名称从属性列表中提取属性值
     */
    public Object get(Object name) {
        Object result = null;
        result = super.get(name);
        try {
            if (result == null && this.context != null && this.context.containsBean((String) name)) {
                // 如果在Spring容器中包含bean，则返回String的Bean
                result = this.context.getBean((String) name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Object put(String name, Object object) {
        return super.put(name, object);
    }

}