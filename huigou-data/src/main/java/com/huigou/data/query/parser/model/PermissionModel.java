package com.huigou.data.query.parser.model;

import com.huigou.data.query.PermissionKind;
import com.huigou.util.ClassHelper;

/**
 * 权限条件模型
 * 
 * @author xx
 */
public class PermissionModel extends ConditionModel {
    private static final long serialVersionUID = 1944278079978730331L;

    private String expr;

    private String kind;

    private String manageType;

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    public PermissionKind getPermissionKind() {
        return PermissionKind.findById(kind);
    }

    public String formatCondition(String symbol, String name) {
        return String.format("%s.%s %s :%s", this.getAlias(), this.getColumn(), symbol, name);
    }

    public static PermissionModel newInstance(Object obj) {
        PermissionModel model = new PermissionModel();
        ClassHelper.copyProperties(obj, model);
        return model;
    }

    private String getEqualsKey() {
        return String.format("%s@%s@%s@%s", this.getAlias(), this.getColumn(), this.getKind(), this.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionModel other = (PermissionModel) o;
        return this.getEqualsKey().equals(other.getEqualsKey());

    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getAlias());
        sb.append(this.getColumn());
        sb.append(this.getSymbol());
        sb.append(this.getFormula());
        sb.append(this.getManageType());
        sb.append(this.getKind());
        sb.append(this.getType());
        sb.append(this.getEqualsKey());
        return sb.hashCode();
    }

}
