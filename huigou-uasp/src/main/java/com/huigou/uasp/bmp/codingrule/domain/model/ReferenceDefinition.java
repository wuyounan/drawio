package com.huigou.uasp.bmp.codingrule.domain.model;

import org.springframework.util.Assert;

public class ReferenceDefinition {
   // private ReferenceKind referenceKind;

    private String attributeName;

    private String referenceTableName;

    private String referenceFieldName;

    private String referencePrimaryKeyFieldName;

    // public ReferenceKind getReferenceKind() {
    // return referenceKind;
    // }
    //
    // public void setReferenceKind(ReferenceKind referenceKind) {
    // this.referenceKind = referenceKind;
    // }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getReferenceTableName() {
        return referenceTableName;
    }

    public void setReferenceTableName(String referenceTableName) {
        this.referenceTableName = referenceTableName;
    }

    public String getReferenceFieldName() {
        return referenceFieldName;
    }

    public void setReferenceFieldName(String referenceFieldName) {
        this.referenceFieldName = referenceFieldName;
    }
    
    public String getReferencePrimaryKeyFieldName() {
        return referencePrimaryKeyFieldName;
    }

    public void setReferencePrimaryKeyFieldName(String referencePrimaryKeyFieldName) {
        this.referencePrimaryKeyFieldName = referencePrimaryKeyFieldName;
    }
    
    public static ReferenceDefinition fromSetting(String settingValue){
        Assert.hasText(settingValue, "未设置引用定义。");
        
        String[] values = settingValue.split("\\.");
        Assert.state(values.length == 4, "引用定义格式错误。");
        
        ReferenceDefinition result = new ReferenceDefinition();
        
        //result.setReferenceKind(ReferenceKind.REFERENCE);
        result.setReferenceTableName(values[0]);
        result.setReferenceFieldName(values[1]);
        result.setReferencePrimaryKeyFieldName(values[2]);
        result.setAttributeName(values[3]);
        
        return result;
    } 

    /*
    public enum ReferenceKind {
        THIS, REFERENCE;
        
        public static ReferenceKind fromName(String name) {
            // 1、转换
            for (ReferenceKind item : ReferenceKind.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
            // 2、不能转换
            throw new IllegalArgumentException("无效的引用类别 : " + name);
        }
    }*/
}