package com.huigou.data.datamanagement;

import java.io.Serializable;

import com.huigou.util.StringUtil;

/**
 * 数据管理权限包含维度资源
 * 
 * @author xx
 * @date 2018-09-08 17:15
 */
public class DataManageResource implements Serializable {

    private static final long serialVersionUID = 690387598151743691L;

    private String dataKindCode;

    private String dataKind;

    private String orgDataKind;

    private String key;

    private String value;

    private String fullId;

    private String fullName;

    public String getDataKindCode() {
        return dataKindCode;
    }

    public void setDataKindCode(String dataKindCode) {
        this.dataKindCode = dataKindCode;
    }

    public String getDataKind() {
        return dataKind;
    }

    public void setDataKind(String dataKind) {
        this.dataKind = dataKind;
    }

    public String getOrgDataKind() {
        return orgDataKind;
    }

    public void setOrgDataKind(String orgDataKind) {
        this.orgDataKind = orgDataKind;
    }

    public String getKey() {
        if (StringUtil.isBlank(key)) {
            return "";
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFullId() {
        if (StringUtil.isBlank(fullId)) {
            return "";
        }
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

    private String getEqualsKey() {
        return String.format("%s@%s", this.getKey(), this.getFullId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataManageResource other = (DataManageResource) o;
        return this.getEqualsKey().equals(other.getEqualsKey());

    }

    @Override
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(dataKindCode);
        sb.append(dataKind);
        sb.append(orgDataKind);
        sb.append(key);
        sb.append(value);
        sb.append(fullId);
        sb.append(fullName);
        sb.append(this.getEqualsKey());
        return sb.hashCode();
    }
}
