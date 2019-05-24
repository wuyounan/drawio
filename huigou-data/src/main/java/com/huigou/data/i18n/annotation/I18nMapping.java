package com.huigou.data.i18n.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 例子
 * I18nMapping(i18n = @I18n(name = "name", code = "{code}"))
 * public List<Map<String, Object>> querySettlementKinds(String id)
 * I18nMapping(keyName = "Rows", i18n = @I18n(name = "name", code = "tech.enum.reimbursement.{code}"))
 * public Map<String, Object> queryRbKinds(RbKindQueryRequest queryRequest)
 * 
 * @author zz
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nMapping {
    String keyName() default "";

    I18n[] i18n();
}
