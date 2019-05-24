package com.huigou.util.extend.beanutils;

import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.apache.commons.beanutils.Converter;

public class EnumAwareConvertUtilsBean extends ConvertUtilsBean2 {

    private static final EnumConverter ENUM_CONVERTER = new EnumConverter();

    @SuppressWarnings("rawtypes")
    @Override
    public Converter lookup(Class clazz) {
        final Converter converter = super.lookup(clazz);
        if (converter == null && clazz.isEnum()) {
            return ENUM_CONVERTER;
        } else {
            return converter;
        }
    }

}