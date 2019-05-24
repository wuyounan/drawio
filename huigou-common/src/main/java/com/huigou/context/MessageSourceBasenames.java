package com.huigou.context;

import org.springframework.util.Assert;

/**
 * 国际换资源路径定义
 * 
 * @author xx
 */
public class MessageSourceBasenames {
    private String[] basenames = new String[0];

    public void setBasenames(String... basenames) {
        if (basenames != null) {
            this.basenames = new String[basenames.length];
            for (int i = 0; i < basenames.length; i++) {
                String basename = basenames[i];
                Assert.hasText(basename, "Basename must not be empty");
                this.basenames[i] = basename.trim();
            }
        } else {
            this.basenames = new String[0];
        }
    }

    public String[] getBasenames() {
        return basenames;
    }

}
