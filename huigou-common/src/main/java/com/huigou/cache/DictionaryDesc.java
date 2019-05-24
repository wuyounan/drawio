package com.huigou.cache;

import java.io.Serializable;

import com.huigou.context.MessageSourceContext;

/**
 * 系统字典成员
 * 
 * @author gongmm
 */
public class DictionaryDesc implements Serializable {

    private static final long serialVersionUID = -8275067965997958489L;

    private String code;

    private String name;

    private String typeId;

    private String value;

    public DictionaryDesc() {

    }

    public DictionaryDesc(String code, String name, String typeId, String value) {
        this.code = code;
        this.name = name;
        this.typeId = typeId;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public java.lang.String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getI18NKey() {
        return String.format("dictionary.%s.%s", code, value);
    }

    public String getName() {
        /******** 加入获取国际化资源的方法 *********/
        // 组合出的key 如 dictionary.yesorno.0
        return MessageSourceContext.getMessageAsDefault(getI18NKey(), this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}
