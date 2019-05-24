package com.huigou.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.huigou.exception.ApplicationException;

/**
 * List 工具类
 * 
 * @ClassName: ListUtil
 * @author
 * @date 2014-6-20 下午12:13:38
 * @version V1.0
 */

public class ListUtil {

    /**
     * 将List中实体中某个属性的值读取出来并组成一个指定分隔符分隔的字符串
     * 
     * @param list
     *            List 读取值的List对象
     * @param prop
     *            String 属性名
     * @param separator
     *            String 分隔符
     * @throws ApplicationException
     * @return String
     */
    public static String join(List<?> list, String prop, String separator) throws ApplicationException {
        if (list == null || list.size() == 0) {
            return "";
        }
        return list.stream().map((o) -> {
            if (o instanceof Map) {
                return ClassHelper.convert(((Map<?, ?>) o).get(prop), String.class, "");
            } else {
                return ClassHelper.getProperty(o, prop);
            }
        }).filter(string -> !string.isEmpty()).collect(Collectors.joining(separator));
    }

    /**
     * 将List中属性的值读取出来并组成一个指定分隔符分隔的字符串
     * 
     * @author
     * @param list
     * @param separator
     * @return
     * @throws ApplicationException
     *             String
     */
    public static String join(Collection<?> list, String separator) throws ApplicationException {
        if (list == null || list.size() == 0) {
            return "";
        }
        return list.stream().map((o) -> {
            return ClassHelper.convert(o, String.class, "");
        }).filter(string -> !string.isEmpty()).collect(Collectors.joining(separator));
    }

    /**
     * 将array中属性的值读取出来并组成一个指定分隔符分隔的字符串
     * 
     * @param array
     * @param separator
     * @return
     * @throws ApplicationException
     */
    public static String join(Object[] array, String separator) throws ApplicationException {
        if (array == null || array.length == 0) {
            return "";
        }
        return Stream.of(array).map((o) -> {
            return ClassHelper.convert(o, String.class, "");
        }).filter(string -> !string.isEmpty()).collect(Collectors.joining(separator));
    }

    /**
     * 判断List中是否存在指定属性
     * 
     * @author xiexin
     * @param array
     * @param v
     * @return boolean
     */
    public static <T> boolean contains(Collection<T> list, final T v) {
        for (final T e : list) {
            if (e == v || v != null && v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数组中是否存在指定属性
     * 
     * @author
     * @param array
     * @param v
     * @return boolean
     */
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array) {
            if (e == v || v != null && v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断List<Map>中是否存在指定属性
     * 
     * @author xiexin
     * @param array
     * @param v
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean contains(List<Map<String, Object>> list, String key, final T v) {
        for (Map<String, Object> map : list) {
            T e = (T) ClassHelper.convert(map.get(key), v.getClass());
            if (e == v || v != null && v.equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除重复元素
     * 
     * @param list
     * @return
     */
    public static <T> List<T> distinct(List<T> list) {
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }
        List<T> seen = new ArrayList<>();
        list.forEach((o) -> {
            if (!seen.contains(o)) {
                seen.add(o);
            }
        });
        return seen;
    }

    /**
     * 去除重复元素
     * 
     * @param list
     * @return
     */
    public static <T> List<T> distinct(List<T> list, String prop) {
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }
        return list.stream().filter(distinctByKey((o) -> {
            if (o instanceof Map) {
                return ClassHelper.convert(((Map<?, ?>) o).get(prop), String.class, "");
            } else {
                return ClassHelper.getProperty(o, prop);
            }
        })).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * * 将一个List均分成n个list,主要通过偏移量来实现的 *
     * 
     * @param source
     *            源集合
     * @param limit
     *            最大值
     * @return
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int limit) {
        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int listCount = (source.size() - 1) / limit + 1;
        int remaider = source.size() % listCount;
        // (先计算出余数)
        int number = source.size() / listCount;
        // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < listCount; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

}
