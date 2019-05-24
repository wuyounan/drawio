package com.huigou.data.i18n.model;

import java.util.ArrayList;
import java.util.List;

import com.huigou.data.i18n.annotation.I18n;

/**
 * 国际化时资源替换模型
 * 
 * @author xx
 */
public class I18nModel {
    private String name;

    private String code;

    private String defaultName;

    public I18nModel() {

    }

    public I18nModel(String name, String code, String defaultName) {
        super();
        this.name = name;
        this.code = code;
        this.defaultName = defaultName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public static List<I18nModel> initByAnnotation(I18n[] i18ns) {
        if (i18ns == null || i18ns.length == 0) {
            return null;
        }
        List<I18nModel> list = new ArrayList<I18nModel>(i18ns.length);
        for (I18n i18n : i18ns) {
            list.add(new I18nModel(i18n.name(), i18n.code(), i18n.defaultName()));
        }
        return list;
    }
}
