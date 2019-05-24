package com.huigou.uasp.bmp.common;

/**
 * 区间类型
 */
public enum IntervalKind {

    OPEN, // (, )
    LEF_OPEN, // (, ]
    RIGHT_OPEN, // [, )
    CLOSE;// []

    private Float operand1;

    private Float operand2;

    public Float getOperand1() {
        return operand1;
    }

    public Float getOperand2() {
        return operand2;
    }

    /**
     * 得到区间类型
     * 
     * @param value
     *            参数
     * @return 区间
     */
    public static IntervalKind getIntervalKind(String value) {
        IntervalKind result;
        String[] values;
        char firstChar, lastChar;
        value = value.trim();
        int len = value.length();
        firstChar = value.charAt(0);
        lastChar = value.charAt(len - 1);
        if (firstChar == '(') {
            if (lastChar == ')') result = OPEN;
            else
                result = LEF_OPEN;
        } else {
            if (lastChar == ')') result = RIGHT_OPEN;
            else
                result = CLOSE;
        }
        value = value.substring(1, len - 1);
        values = value.split(",");
        result.operand1 = Float.parseFloat(values[0]);

        if (values[1].equalsIgnoreCase("+")) {
            result.operand2 = Float.MAX_VALUE;
        } else
            result.operand2 = Float.parseFloat(values[1]);
        return result;
    }
}
