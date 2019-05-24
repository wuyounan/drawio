package com.huigou.data.query.parser.model;

import java.io.Serializable;

import com.huigou.util.ClassHelper;

/**
 * 查询包含字段模型
 * 
 * @author xx
 */
public class FieldModel implements Serializable {

    private static final long serialVersionUID = -2813844281153583451L;

    private String name;

    private String alias;

    private String code;

    private String type;

    private String mask;

    private int sequence = 1;

    private String align;

    private Long width;

    private String dictionary;

    private String autoCondition;

    public FieldModel() {
    }

    public FieldModel(String name, String code, String type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getAutoCondition() {
        return autoCondition;
    }

    public void setAutoCondition(String autoCondition) {
        this.autoCondition = autoCondition;
    }

    public static FieldModel newInstance(Object obj) {
        FieldModel model = new FieldModel();
        ClassHelper.copyProperties(obj, model);
        return model;
    }

}
