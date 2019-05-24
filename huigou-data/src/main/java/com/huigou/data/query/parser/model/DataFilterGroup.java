package com.huigou.data.query.parser.model;

import java.io.Serializable;
import java.util.List;

import com.huigou.util.StringUtil;

/**
 * 高级自定义查询规则分组
 * 
 * @author xx
 */
public class DataFilterGroup implements Serializable {

    private static final long serialVersionUID = 1691775486700363045L;

    private String op;

    private List<DataFilterRule> rules;

    private List<DataFilterGroup> groups;

    public String getOp() {
        if (StringUtil.isBlank(op)) {
            return " and ";
        }
        return String.format(" %s ", op);
    }

    public void setOp(String op) {
        this.op = op;
    }

    public List<DataFilterRule> getRules() {
        return rules;
    }

    public void setRules(List<DataFilterRule> rules) {
        this.rules = rules;
    }

    public List<DataFilterGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<DataFilterGroup> groups) {
        this.groups = groups;
    }

    public boolean isAppended() {
        if (rules != null && rules.size() > 0) {
            return true;
        }
        if (groups != null && groups.size() > 0) {
            return true;
        }
        return false;
    }
}
