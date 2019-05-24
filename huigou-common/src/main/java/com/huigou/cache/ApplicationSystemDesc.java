package com.huigou.cache;

import java.io.Serializable;

/**
 * 应用系统描述
 * 
 * @author gongmm
 */
public class ApplicationSystemDesc implements Serializable {

    private static final long serialVersionUID = 4054772710808977007L;

    private String id;

    private String code;

    private String name;

    private String classPrefix;

    public ApplicationSystemDesc() {

    }

    public ApplicationSystemDesc(String id, String code, String name, String classPrefix) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.classPrefix = classPrefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassPrefix() {
        return classPrefix;
    }

    public void setClassPrefix(String classPrefix) {
        this.classPrefix = classPrefix;
    }

}
