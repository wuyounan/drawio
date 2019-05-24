package com.huigou.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.exception.ApplicationException;
import com.huigou.util.extend.beanutils.EnumAwareConvertUtilsBean;

/**
 * BeanUtils 功能加强
 * 
 * @author gongmm
 */
public class ClassHelper extends BeanUtils {

    private static Logger log = LogHome.getLog(ClassHelper.class);

    private static final String[] BASE_TYPES = { "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float",
                                                "java.lang.Double", "java.lang.Character", "java.lang.Boolean", "java.lang.String", "java.math.BigDecimal" };

    private ClassHelper() {
    }

    static {
        BeanUtilsBean.setInstance(new BeanUtilsBean(new EnumAwareConvertUtilsBean()));
        // 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
        // BeanUtils对时间转换的初始化设置
        ConvertUtils.register(new DateConverter(), java.sql.Date.class);
        ConvertUtils.register(new DateConverter(), java.util.Date.class);
        ConvertUtils.register(new DateConverter(), java.sql.Timestamp.class);
        // BeanUtils里注册 数值类型 的方法
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new BigDecimalMoneyConverter(null), BigDecimal.class);
        ConvertUtils.register(new Converter() {
            public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) {
                if (value == null) {
                    return null;
                }
                if (value instanceof Number) {
                    return (value);
                }
                try {
                    String stringValue = value.toString().trim();
                    if (StringUtil.isNotBlank(stringValue)) {
                        stringValue = stringValue.replaceAll(",", "");
                    }
                    return (new BigDecimal(stringValue));
                } catch (Exception e) {
                    return null;
                }
            }

        }, Number.class);

    }

    public static void copyProperties(Object from, Object to) {
        try {
            BeanUtils.copyProperties(to, from);
        } catch (IllegalAccessException illegalaccessexception) {
            log.error(illegalaccessexception.toString());
        } catch (InvocationTargetException invocationtargetexception) {
            log.error(invocationtargetexception.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public static Object clone(Object form) {
        Object to = null;
        try {
            to = form.getClass().newInstance();
            BeanUtils.copyProperties(to, form);
        } catch (IllegalAccessException illegalaccessexception) {
            log.error(illegalaccessexception.toString());
        } catch (InvocationTargetException invocationtargetexception) {
            log.error(invocationtargetexception.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
        return to;
    }

    /**
     * 为对像属性赋值
     * 
     * @param bean
     * @param name
     * @param value
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InvocationTargetException e) {
            log.error(e);
        }
    }

    public static String getProperty(Object bean, String name) {
        try {
            return BeanUtils.getProperty(bean, name);
        } catch (IllegalAccessException e) {
            log.error(e);
        } catch (InvocationTargetException e) {
            log.error(e);
        } catch (NoSuchMethodException e) {
            log.error(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromMap(Class<T> cls, Map<String, Object> params) {
        Object o = null;
        try {
            o = cls.newInstance();
            BeanUtils.populate(o, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) o;
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        final Field field = getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object getFieldValue(Object obj, Field field) {
        Assert.notNull(obj, "参数obj不能为空。");
        Assert.notNull(field, "参数field不能为空。");
        Object result = null;
        field.setAccessible(true);
        try {
            result = field.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                field = null;
                // e.printStackTrace();
            }
        }
        return field;
    }

    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception {
        Field field = getField(obj, fieldName);// obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    public static Field getField(Class<?> cls, String name) {
        Field field = null;
        for (Class<?> clazz = cls; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                field = null;
            }
        }
        return field;
    }

    /**
     * 根据类名转化对象
     * 
     * @Title: convert
     * @author
     * @param @param obj
     * @param @param className
     * @param @return
     * @return Object
     * @throws
     */
    public static Object convert(Object obj, String className) {
        if (obj == null || obj.toString().equals("")) {
            return null;
        }
        try {
            Object value = checkNumberValue(obj, className);
            Class<?> cls = Class.forName(className);
            Object o = ConvertUtils.convert(value, cls);
            if (o == null) {
                throw new ApplicationException("强制类型转换错误:[" + obj + "][" + className + "]");
            }
            return o;
        } catch (ClassNotFoundException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 根据类名转化对象
     * 
     * @Title: convert
     * @author
     * @param @param obj
     * @param @param cls
     * @param @return
     * @return Object
     * @throws
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object obj, Class<T> cls) {
        if (obj == null || obj.toString().equals("")) {
            return null;
        }
        Object value = checkNumberValue(obj, cls);
        Object o = ConvertUtils.convert(value, cls);
        if (o == null) {
            throw new ApplicationException("强制类型转换错误:[" + obj + "][" + cls.getName() + "]");
        }
        return (T) o;
    }

    /**
     * 根据类名转化对象 转换失败返回默认值
     * 
     * @Title: convert
     * @author
     * @param <T>
     * @param obj
     * @param cls
     * @param defaultValue
     * @return
     * @throws ApplicationException
     *             T
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object obj, Class<T> cls, Object defaultValue) {
        if (obj == null || obj.toString().equals("")) {
            return (T) defaultValue;
        }
        Object value = checkNumberValue(obj, cls);
        Object o = ConvertUtils.convert(value, cls);
        if (o == null) {
            return (T) defaultValue;
        }
        return (T) o;
    }

    /**
     * 判断是否为基本数据类型
     * 
     * @Title: isBaseType
     * @author
     * @Description: 判断是否为基本数据类型
     * @param typeName
     * @return boolean
     */
    public static boolean isBaseType(Class<?> cls) {
        if (java.util.Date.class.isAssignableFrom(cls)) {
            return true;
        }
        for (int i = 0; i < BASE_TYPES.length; i++) {
            if (cls.getName().equals(BASE_TYPES[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个子类的父类是不是所指定的类型.
     * 
     * @Title: isSubClass
     * @author
     * @param @param subClass
     * @param @param parent
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isSubClass(Class<?> subClass, Class<?> parent) {
        for (Class<?> c = subClass; c != null; c = c.getSuperclass()) {
            if (c == parent) return true;
        }
        return false;
    }

    /**
     * 判断对象实现的所有接口中是否有type接口
     * 
     * @Title: isInterface
     * @author
     * @param @param subClass
     * @param @param type
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isInterface(Class<?> subClass, Class<?> type) {
        Class<?>[] face = subClass.getInterfaces();
        for (int i = 0, j = face.length; i < j; i++) {
            if (face[i] == type) {
                return true;
            } else {
                Class<?>[] face1 = face[i].getInterfaces();
                for (int x = 0; x < face1.length; x++) {
                    if (face1[x] == type) {
                        return true;
                    } else if (isInterface(face1[x], type)) {
                        return true;
                    }
                }
            }
        }
        if (null != subClass.getSuperclass()) {
            return isInterface(subClass.getSuperclass(), type);
        }
        return false;
    }

    /**
     * 前台页面传入的BigDecimal类型可能含有","(money类别时)这里做特殊处理
     * 
     * @author
     * @return
     * @throws ClassNotFoundException
     */
    private static Object checkNumberValue(Object obj, String className) throws ClassNotFoundException {
        if (null == obj) {
            return null;
        }
        Class<?> cls = Class.forName(className);
        return checkNumberValue(obj, cls);
    }

    /**
     * 前台页面传入的BigDecimal类型可能含有","(money类别时)这里做特殊处理
     * 
     * @author
     * @return
     * @throws
     */
    private static Object checkNumberValue(Object obj, Class<?> cls) {
        if (null == obj) {
            return null;
        }
        if (isSubClass(cls, java.lang.Number.class)) {// 判断是否是Number
            return obj.toString().replace(",", "");// 清除逗号;
        }
        return obj;
    }

    /**
     * 得到定义和继承的字段
     * 
     * @param obj
     *            对象
     */
    public static List<Field> getDeclaredAndInheritedFields(Object obj) {
        List<Field> fields = new ArrayList<Field>();
        Class<?> cls = obj.getClass();
        while (cls != null) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass();
        }

        Iterator<Field> iterator = fields.iterator();

        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (Modifier.isStatic(field.getModifiers())) {
                iterator.remove();
                continue;
            }

            JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
            if (jsonIgnore != null) {
                iterator.remove();
                continue;
            }
        }

        return fields;
    }

    public static List<Method> getDeclaredAndInheritedGetters(Object obj) {
        List<Method> methods = new ArrayList<Method>();

        Class<?> cls = obj.getClass();
        while (cls != null) {
            methods.addAll(Arrays.asList(cls.getDeclaredMethods()));
            cls = cls.getSuperclass();
        }

        Iterator<Method> iterator = methods.iterator();

        while (iterator.hasNext()) {
            Method method = iterator.next();
            if (!method.getName().startsWith("get")) {
                iterator.remove();
            }
        }

        return methods;
    }

    /**
     * 对象转为map
     * 
     * @param bean
     * @return
     */
    public static final Map<String, Object> toMap(Object bean) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean.getClass());
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod != null) {
                        Object result = readMethod.invoke(bean, new Object[0]);
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        } else {
                            returnMap.put(propertyName, "");
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ApplicationException("对象转换Map错误：" + e.getMessage());
        }
        return returnMap;
    }

    /**
     * 对象转为map 空属性不转换为 空字符串
     * 
     * @param bean
     * @return
     */
    public static final Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            PropertyDescriptor propertyDescriptors[] = PropertyUtils.getPropertyDescriptors(bean.getClass());
            PropertyDescriptor apropertydescriptor[];
            int j = (apropertydescriptor = propertyDescriptors).length;
            for (int i = 0; i < j; i++) {
                PropertyDescriptor descriptor = apropertydescriptor[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    if (readMethod != null) {
                        Object result = readMethod.invoke(bean, new Object[0]);
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        } else {
                            // 空属性不转换为 空字符串
                            returnMap.put(propertyName, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return returnMap;
    }

    /**
     * 判断是否包含属性
     * 
     * @param cls
     * @param propertyName
     * @return
     */
    public static final boolean hasProperty(Class<?> cls, String propertyName) {
        try {
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(cls);
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String name = descriptor.getName();
                if (name.equals(propertyName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return false;
    }

    /**
     * 获取全部属性名
     * 
     * @param cls
     * @return
     */
    public static final List<String> getPropertyNames(Class<?> cls) {
        List<String> names = new ArrayList<String>();
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(cls);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                names.add(propertyName);
            }
        }
        return names;
    }

}
