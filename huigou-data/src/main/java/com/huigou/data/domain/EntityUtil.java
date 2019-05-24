package com.huigou.data.domain;

import javax.persistence.Table;

import org.springframework.util.Assert;

import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.util.StringUtil;

public class EntityUtil {
    /**
     * 获取Entity 对应的表名
     * 
     * @param clazz
     * @return
     */
    public static String getTableName(Class<? extends AbstractEntity> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        Assert.notNull(table, clazz.getSimpleName() + "不是JPA类。");
        String tableName = table.name();
        if (StringUtil.isBlank(table.name())) {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    /**
     * Assert a boolean expression, throwing {@code DuplicateException} if the test result is {@code false}.
     * 
     * <pre class="code">
     * EntityUtil.isNotDuplicate(count == 0, &quot;code is duplicate&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param message
     *            message to use if the assertion fails
     * @throws PropertyDuplicateException
     *             if expression is {@code false}
     */
    public static void isNotDuplicate(boolean expression, String message) {
        if (!expression) {
            throw new PropertyDuplicateException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing {@code EntityIsReferencedException} if the test result is {@code false}.
     * 
     * <pre class="code">
     * EntityUtil.IsNotReferenced(count == 0, &quot;code is duplicate&quot;);
     * </pre>
     * 
     * @param expression
     *            a boolean expression
     * @param message
     *            message to use if the assertion fails
     * @throws EntityIsReferencedException
     *             if expression is {@code false}
     */
    public static void IsNotReferenced(boolean expression, String message) {
        if (!expression) {
            throw new EntityIsReferencedException(message);
        }
    }

}
