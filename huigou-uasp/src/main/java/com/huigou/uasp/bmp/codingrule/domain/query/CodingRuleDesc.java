package com.huigou.uasp.bmp.codingrule.domain.query;

import java.util.List;

import com.huigou.util.StringUtil;

public class CodingRuleDesc {

    private String id;

    private String code;

    private String name;

    private String delimiter;

    private boolean isAddNoBreak;

    private boolean isAddShow;

    private boolean isModifiable;

    private String ruleType;

    private boolean isBreakCode;

    private boolean isSelectBreakCode;

    private Integer status;
    
    private List<CodingRuleDetailDesc> details;

    public List<CodingRuleDetailDesc> getDetails() {
        return details;
    }

    public void setDetails(List<CodingRuleDetailDesc> details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean getIsAddNoBreak() {
        return this.isAddNoBreak;
    }

    public void setIsAddNoBreak(boolean isAddNoBreak) {
        this.isAddNoBreak = isAddNoBreak;
    }

    public boolean getIsAddShow() {
        return this.isAddShow;
    }

    public void setIsAddShow(boolean isAddShow) {
        this.isAddShow = isAddShow;
    }

    public boolean getIsModifiable() {
        return this.isModifiable;
    }

    public void setIsModifiable(boolean isModifiable) {
        this.isModifiable = isModifiable;
    }

    public String getRuleType() {
        return this.ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public boolean getIsBreakCode() {
        return this.isBreakCode;
    }

    public void setIsBreakCode(boolean isBreakCode) {
        this.isBreakCode = isBreakCode;
    }

    public boolean getIsSelectBreakCode() {
        return this.isSelectBreakCode;
    }

    public void setIsSelectBreakCode(boolean isSelectBreakCode) {
        this.isSelectBreakCode = isSelectBreakCode;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public boolean useDelimiter(){
       return StringUtil.isNotBlank(this.getDelimiter());
    }
    
    
}
