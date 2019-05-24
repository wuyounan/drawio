package com.huigou.uasp.bmp.configuration.domain.query;

public class SysDictionaryDesc {

    private String id;

    private Integer kindId;

    private String parentId;

    private String code;

    private String name;

    private Integer status;

    private Long version;

    private String remark;

    public SysDictionaryDesc() {

    }

    public SysDictionaryDesc(String id, Integer kindId, String parentId, String code, String name, Integer sequence, Integer status, Long version, String remark) {
        super();
        this.id = id;
        this.kindId = kindId;
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.status = status;
        this.version = version;
        this.remark = remark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
