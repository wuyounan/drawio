package com.huigou.data.i18n.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.i18n.model.I18nModel;
import com.huigou.util.ClassHelper;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 国际化处理后台返回数据切面
 * 
 * @author xx
 */
public abstract class I18nAspect {
    protected static final String BEFORE_NAME = "%sBeforeI18n";

    /**
     * 循环执行切入点
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */

    abstract Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    /**
     * 国际化资源处理
     * 
     * @param result
     * @param keyName
     * @param i18ns
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    protected Object process(Object result, String keyName, List<I18nModel> i18ns) throws Throwable {
        if (result == null) {
            return result;
        }
        if (i18ns == null || i18ns.size() == 0) {
            return result;
        }
        Object i18nResult = null;
        // 根据不同的返回类型调用不同的处理方法
        if (result.getClass() == SDO.class) {
            if (StringUtil.isBlank(keyName)) {
                i18nResult = this.processSDO((SDO) result, i18ns);
            } else {
                i18nResult = this.process(((SDO) result).getProperty(keyName), i18ns);
                ((SDO) result).putProperty(keyName, i18nResult);
                return result;
            }
        } else if (ClassHelper.isInterface(result.getClass(), List.class)) {
            // 返回结果为列表
            i18nResult = this.processList((List<Object>) result, i18ns);
        } else if (ClassHelper.isInterface(result.getClass(), Map.class)) {
            // 返回结果为map
            if (StringUtil.isBlank(keyName)) {
                i18nResult = this.processMap((Map<String, Object>) result, i18ns);
            } else {
                i18nResult = this.process(((Map<String, Object>) result).get(keyName), i18ns);
                ((Map<String, Object>) result).put(keyName, i18nResult);
                return result;
            }
        } else {
            // 其他的为 pojo
            if (StringUtil.isBlank(keyName)) {
                i18nResult = this.processBean(result, i18ns);
            } else {
                i18nResult = this.process(ClassHelper.getFieldValue(result, keyName), i18ns);
                ClassHelper.setFieldValue(result, keyName, i18nResult);
                return result;
            }
        }
        return i18nResult;
    }

    /**
     * 国际化资源处理
     * 
     * @param result
     * @param i18ns
     * @return
     * @throws Throwable
     */
    protected Object process(Object result, List<I18nModel> i18ns) throws Throwable {
        return this.process(result, null, i18ns);
    }

    /**
     * SDO中数据国际化处理
     * 
     * @param sdo
     * @param i18ns
     * @return
     */
    protected Object processSDO(SDO sdo, List<I18nModel> i18ns) {
        String name = "", code = "", defaultName = "";
        String nameValue = "", codeValue = "", defaultValue = "", i18nValue = "";
        for (I18nModel i18n : i18ns) {
            name = i18n.getName();
            code = i18n.getCode();
            defaultName = i18n.getDefaultName();
            if (StringUtil.isBlank(name)) {
                continue;
            }
            // 原始数据值
            nameValue = codeValue = defaultValue = sdo.getProperty(name, String.class);
            if (StringUtil.isBlank(nameValue)) {
                continue;
            }
            // 国际化编码
            if (StringUtil.isNotBlank(code)) {
                try {
                    codeValue = StringUtil.patternParser(code, sdo.getProperties());
                } catch (Exception e) {
                }
            }
            if (StringUtil.isNotBlank(defaultName)) {
                // 默认数据值
                defaultValue = sdo.getProperty(defaultName, String.class);
            }
            i18nValue = MessageSourceContext.getMessageAsDefault(codeValue, defaultValue);
            sdo.putProperty(name, i18nValue);
            sdo.putProperty(String.format(BEFORE_NAME, name), nameValue);
        }
        return sdo;
    }

    /**
     * map中数据处理国际化
     * 
     * @param map
     * @param i18ns
     * @return
     */
    protected Object processMap(Map<String, Object> map, List<I18nModel> i18ns) {
        if (map == null || map.size() == 0) {
            return map;
        }
        String name = "", code = "", defaultName = "";
        String nameValue = "", codeValue = "", defaultValue = "", i18nValue = "";
        for (I18nModel i18n : i18ns) {
            name = i18n.getName();
            code = i18n.getCode();
            defaultName = i18n.getDefaultName();
            if (StringUtil.isBlank(name)) {
                continue;
            }
            // 原始数据值
            nameValue = codeValue = defaultValue = ClassHelper.convert(map.get(name), String.class);
            if (StringUtil.isBlank(nameValue)) {
                continue;
            }
            // 国际化编码
            if (StringUtil.isNotBlank(code)) {
                try {
                    codeValue = StringUtil.patternParser(code, map);
                } catch (Exception e) {
                }
            }
            if (StringUtil.isNotBlank(defaultName)) {
                // 默认数据值
                defaultValue = ClassHelper.convert(map.get(defaultName), String.class);
            }
            i18nValue = MessageSourceContext.getMessageAsDefault(codeValue, defaultValue);
            map.put(name, i18nValue);
            map.put(String.format(BEFORE_NAME, name), nameValue);
        }
        return map;
    }

    /**
     * BEAN 数据国际化
     * 
     * @param bean
     * @param i18ns
     * @return
     */
    protected Object processBean(Object bean, List<I18nModel> i18ns) {
        String name = "", code = "", defaultName = "";
        String nameValue = "", codeValue = "", defaultValue = "", i18nValue = "";
        for (I18nModel i18n : i18ns) {
            name = i18n.getName();
            code = i18n.getCode();
            if (StringUtil.isBlank(name)) {
                continue;
            }
            // 原始数据值
            nameValue = codeValue = defaultValue = ClassHelper.getProperty(bean, name);
            if (StringUtil.isBlank(nameValue)) {
                continue;
            }
            // 国际化编码
            if (StringUtil.isNotBlank(code)) {
                try {
                    codeValue = StringUtil.patternParser(code, ClassHelper.toMap(bean));
                } catch (Exception e) {
                }
            }
            if (StringUtil.isNotBlank(defaultName)) {
                // 默认数据值
                defaultValue = ClassHelper.getProperty(bean, defaultName);
            }
            i18nValue = MessageSourceContext.getMessageAsDefault(codeValue, defaultValue);
            ClassHelper.setProperty(bean, name, i18nValue);
        }
        return bean;
    }

    /**
     * list中数据国际化
     * 
     * @param list
     * @param i18ns
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Object processList(List<?> list, List<I18nModel> i18ns) {
        if (list == null || list.size() == 0) {
            return list;
        }
        List<Object> result = new ArrayList<Object>(list.size());
        for (Object obj : list) {
            if (ClassHelper.isBaseType(obj.getClass())) {
                result.add(obj);
            } else if (ClassHelper.isInterface(obj.getClass(), Map.class)) {
                result.add(this.processMap((Map<String, Object>) obj, i18ns));
            } else {
                result.add(this.processBean(obj, i18ns));
            }
        }
        return result;
    }
}
