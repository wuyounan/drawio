package com.huigou.data.i18n.model;

public class I18nDataModel {
    private String value;

    private String code;

    public I18nDataModel(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
