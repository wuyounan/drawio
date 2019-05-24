package com.huigou.util.extend.beanutils;

import org.apache.commons.beanutils.converters.AbstractConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huigou.util.StringUtil;

public class EnumConverter extends AbstractConverter {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumConverter.class);

    @SuppressWarnings("rawtypes")
    @Override
    protected String convertToString(final Object value) throws Throwable {
        return ((Enum) value).name();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Object convertToType(final Class type, final Object value) throws Throwable {
        if (value == null || StringUtil.isBlank(value.toString())) {
            return null;
        }
        return Enum.valueOf(type, value.toString());
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class getDefaultType() {
        return Enum.class;
    }

}