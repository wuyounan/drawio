package com.huigou.data.query.parser.model;

import java.io.Serializable;

import com.huigou.util.ClassHelper;
import com.huigou.util.StringUtil;

/**
 * 查询条件模型
 * 
 * @author xx
 */
public class ConditionModel implements Serializable {
    
    private static final long serialVersionUID = -1034649022016117464L;

    private String symbol;

    private String alias;

    private String column;

    private String name;

    private String formula;

    private String append;

    private String type;

    private String paramName;

    public String getSymbol() {
        return symbol;
    }

    public String getFormatSymbol() {
        if (symbol.equalsIgnoreCase("HALF_LIKE")) {
            return "like";
        }
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAlias() {
        if (StringUtil.isBlank(alias)) {
            return "t";
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getAppend() {
        return append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParamName() {
        if (StringUtil.isBlank(paramName)) return name;
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public boolean isFormula() {
        if (StringUtil.isBlank(formula)) {
            return false;
        }
        return true;
    }

    public boolean isAppendCondition() {
        if (StringUtil.isNotBlank(append)) {
            return !append.equalsIgnoreCase("false");
        }
        return true;
    }

    public boolean isLike() {
        if (StringUtil.isNotBlank(symbol)) {
            return symbol.equalsIgnoreCase("LIKE") || symbol.equalsIgnoreCase("HALF_LIKE");
        }
        return true;
    }

    public boolean isHalfLike() {
        if (StringUtil.isNotBlank(symbol)) {
            return symbol.equalsIgnoreCase("HALF_LIKE");
        }
        return false;
    }

    public boolean isEqual() {
        if (StringUtil.isNotBlank(symbol)) {
            return symbol.equalsIgnoreCase("=");
        }
        return false;
    }

    /**
     * 组合条件语句
     * 
     * @return
     */
    public String formatCondition() {
        if (this.isFormula()) {
            return this.getFormula();
        }
        return String.format("%s.%s %s :%s", this.getAlias(), this.getColumn(), this.getFormatSymbol(), this.getParamName());
    }

    public static ConditionModel newInstance(Object obj) {
        ConditionModel model = new ConditionModel();
        ClassHelper.copyProperties(obj, model);
        return model;
    }

}
