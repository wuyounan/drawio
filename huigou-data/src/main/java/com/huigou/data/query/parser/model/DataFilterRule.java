package com.huigou.data.query.parser.model;

import com.huigou.util.StringUtil;

/**
 * 高级自定义查询规则模型
 * 
 * @author xx
 */
public class DataFilterRule extends ConditionModel {

    private static final long serialVersionUID = -352836293831475904L;

    private String op;

    private String used;

    private String value;

    private String text;

    private String fullId;

    private String fullName;

    public String getOp() {
        if (StringUtil.isBlank(op)) {
            return "equal";
        }
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getUsed() {
        if (StringUtil.isBlank(used)) {
            return "value";
        }
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFullId() {
        return fullId;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 字段类型解析
     */
    public String getType() {
        String superType = super.getType();
        if (StringUtil.isBlank(superType)) {
            return "java.lang.String";
        }
        switch (superType) {
        case "string":
        case "text":
            return "java.lang.String";
        case "int":
        case "number":
        case "float":
        case "money":
            return "java.math.BigDecimal";
        case "date":
        case "dateTime":
        case "datetime":
            return "java.util.Date";
        default:
            return "java.lang.String";
        }
    }

    /**
     * 操作符解析
     */
    public String getFormatSymbol() {
        String o = this.getOp();
        switch (o) {
        case "equal":
            return "=";
        case "notequal":
            return "!=";
        case "greater":
            return ">";
        case "greaterorequal":
            return ">=";
        case "less":
            return "<";
        case "lessorequal":
            return "<=";
        case "startwith":
        case "endwith":
        case "like":
            return "like";
        case "in":
            return "in";
        case "notin":
            return "not in";
        default:
            return "=";
        }
    }

    /**
     * 公式替换 替换参数名称
     */
    public String getFormula() {
        String ruleFormula = super.getFormula();
        if (StringUtil.isBlank(ruleFormula)) {
            return null;
        }
        String thisName = this.getName();
        return ruleFormula.replaceAll(":" + thisName, ":" + this.getParamName());
    }

    /**
     * 字段名解析
     */
    public String getColumn() {
        String superColumn = super.getColumn();
        if (StringUtil.isBlank(superColumn)) {
            String thisName = this.getName();
            if (thisName.endsWith("TextView")) {
                thisName = thisName.replace("TextView", "");
            }
            return StringUtil.getUnderscoreName(thisName);
        }
        return superColumn;
    }

    public boolean isIn() {
        String o = this.getOp();
        if (StringUtil.isNotBlank(o)) {
            return o.equalsIgnoreCase("in") || o.equalsIgnoreCase("notin");
        }
        return false;
    }

    public boolean isLike() {
        String o = this.getOp();
        if (StringUtil.isNotBlank(o)) {
            return o.equalsIgnoreCase("like");
        }
        return false;
    }

    public boolean isStartwith() {
        String o = this.getOp();
        if (StringUtil.isNotBlank(o)) {
            return o.equalsIgnoreCase("startwith");
        }
        return false;
    }

    public boolean isEndwith() {
        String o = this.getOp();
        if (StringUtil.isNotBlank(o)) {
            return o.equalsIgnoreCase("endwith");
        }
        return false;
    }
}
