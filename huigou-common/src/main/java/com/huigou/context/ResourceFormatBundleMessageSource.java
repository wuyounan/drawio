package com.huigou.context;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.Assert;

/**
 * 该实现类允许用户通过beanName指定一个资源名（包括类路径的全限定资源名），或通过beanNames指定一组资源名。不同的区域获取加载资源文件，达到国际化信息的目的
 * 
 * @see 重写 getStringOrNull 方法 单引号转义
 * @author xx
 */
public class ResourceFormatBundleMessageSource extends ResourceBundleMessageSource implements InitializingBean {
    /**
     * 扩展资源路径
     */
    private MessageSourceBasenames messageSourceBasenames;

    public void setMessageSourceBasenames(MessageSourceBasenames messageSourceBasenames) {
        this.messageSourceBasenames = messageSourceBasenames;
    }

    /**
     * 默认资源路径
     */
    private String[] defbasenames = new String[0];

    public void setDefbasenames(String... basenames) {
        if (basenames != null) {
            this.defbasenames = new String[basenames.length];
            for (int i = 0; i < basenames.length; i++) {
                String basename = basenames[i];
                Assert.hasText(basename, "Basename must not be empty");
                this.defbasenames[i] = basename.trim();
            }
        } else {
            this.defbasenames = new String[0];
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 设置是否始终应用’消息格式组件‘，解析没有参数的消息。
         * 例如：,MessageFormat希望单引号被转义为"''"。 如果您的消息文本全部用这样的转义编写， 即使没有定义参数占位符，您需要将此标志设置为“true”。 否则，只有具有实际参数的消息文本应该用MessageFormat转义来编写。
         */
        this.setAlwaysUseMessageFormat(true);
        String[] basenames = ArrayUtils.addAll(new String[0], this.defbasenames);
        if (this.messageSourceBasenames != null) {
            basenames = ArrayUtils.addAll(basenames, messageSourceBasenames.getBasenames());
        }
        this.setBasenames(basenames);
    }

    /**
     * 将所有的语言包获取都走传参路线，即都会经过MessageFormat处理，即单引号都要转义 重写getStringOrNull
     */
    protected String getStringOrNull(ResourceBundle bundle, String key) {
        if (bundle.containsKey(key)) {
            try {
                String val = bundle.getString(key);
                return val.replaceAll("'", "''");
            } catch (MissingResourceException var4) {
            }
        }
        return null;
    }

}
