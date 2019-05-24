package com.huigou.util;

import java.math.BigDecimal;

import org.apache.commons.beanutils.converters.NumberConverter;

/**
 * BigDecimal转换器
 * <p>
 * 输入的BigDecimal 可能带有money格式,号
 * 
 * @author xx
 */
public class BigDecimalMoneyConverter extends NumberConverter {

    /**
     * Construct a <b>java.math.BigDecimal</b> <i>Converter</i> that throws
     * a <code>ConversionException</code> if an error occurs.
     */
    public BigDecimalMoneyConverter() {
        super(true);
    }

    /**
     * Construct a <b>java.math.BigDecimal</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue
     *            The default value to be returned
     *            if the value to be converted is missing or an error
     *            occurs converting the value.
     */
    public BigDecimalMoneyConverter(Object defaultValue) {
        super(true, defaultValue);
    }

    /**
     * Return the default type this <code>Converter</code> handles.
     *
     * @return The default type this <code>Converter</code> handles.
     */
    protected Class<?> getDefaultType() {
        return BigDecimal.class;
    }

    @SuppressWarnings("rawtypes")
    public Object convert(Class type, Object value) {
        if (value instanceof String) {
            String stringValue = value.toString().trim();
            if (StringUtil.isNotBlank(stringValue)) {
                stringValue = stringValue.replaceAll(",", "");
            }
            return super.convert(type, stringValue);
        }
        return super.convert(type, value);
    }

}
