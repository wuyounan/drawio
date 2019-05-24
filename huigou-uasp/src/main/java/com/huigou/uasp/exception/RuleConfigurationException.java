package com.huigou.uasp.exception;

/**
 * @ClassName: RuleConfigurationException
 * @Description: TODO 解析规则配置异常
 * @author BambooShang
 * @date 2014-2-19 下午12:13:39
 */
public class RuleConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -4460862939166967653L;

    public RuleConfigurationException() {
    }

    public RuleConfigurationException(String s) {
        super(s);
    }

    public RuleConfigurationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RuleConfigurationException(Throwable throwable) {
        super(throwable);
    }
}
