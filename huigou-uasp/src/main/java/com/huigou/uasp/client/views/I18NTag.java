package com.huigou.uasp.client.views;

import com.huigou.util.Constants;

/**
 * 国际化资源读取
 * 
 * @author xx
 */
public class I18NTag extends AbstractTag {

    private static final long serialVersionUID = -1584163120230030634L;

    private String dictionary;

    public I18NTag() {
        super();
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    protected String getDefaultTemplate() {
        return "i18n";
    }

    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        addParameter("webApp", Constants.WEB_APP);
        if (null != dictionary) {
            addParameter("dictionary", dictionary);
        }
    }

}
