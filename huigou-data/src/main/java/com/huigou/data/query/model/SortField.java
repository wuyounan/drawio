package com.huigou.data.query.model;

import com.huigou.util.StringUtil;

/**
 * 排序字段模型
 * 
 * @author xx
 */
public class SortField {
    /**
     * 字段名
     */
    private String name;

    /**
     * 排序方向
     */
    private String direction;

    /**
     * 数据库字段名
     */
    private String columnName;

    public SortField() {

    }

    public SortField(String name) {
        this.name = name;
        this.direction = null;
    }

    public SortField(String name, String direction) {
        this.name = name;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 解析对应数据库字段名
     * 
     * @return
     */
    public String getColumnName() {
        if (StringUtil.isNotBlank(columnName)) {
            return columnName;
        }
        String parseName = this.getName();
        if (StringUtil.isBlank(parseName)) {
            return "";
        }
        if (parseName.endsWith("TextView")) {
            parseName = parseName.replace("TextView", "");
        }
        if (parseName.indexOf("_") > -1) {
            columnName = parseName;
        } else {
            columnName = StringUtil.getUnderscoreName(parseName);
        }
        return columnName;
    }

    private String getEqualsKey() {
        String key = this.getColumnName();
        if (StringUtil.isBlank(key)) {
            return "";
        }
        key = key.split(" ")[0];
        return key;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName());
        if (StringUtil.isNotBlank(this.getDirection())) {
            if (this.getDirection().equalsIgnoreCase("desc") || this.getDirection().equalsIgnoreCase("asc")) {
                sb.append(" ").append(this.getDirection());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortField other = (SortField) o;
        return this.getEqualsKey().equalsIgnoreCase(other.getEqualsKey());
    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getName());
        sb.append(this.getDirection());
        return sb.hashCode();
    }

}
