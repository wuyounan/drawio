package com.huigou.uasp.bmp.configuration.domain.query;

public class CommonTreeDesc {

    private String id;

    private Integer kindId;

    private String nodeKindId;

    private String parentId;

    private String shortCode;

    private String code;

    private String name;

    private Integer sequence;

    private Integer status;

    private Long version;

    private Integer hasChildren;

    private Integer isexpand = 0;

    private String nodeUrl = "Folder";

    private String fullId;

    private String fullName;

    private String remark;

    public CommonTreeDesc() {

    }

    public CommonTreeDesc(String id, Integer kindId, String nodeKindId, String parentId, String shortCode, String code, String name, Integer sequence,
                          Integer status, Long version, Integer hasChildren, String fullId, String fullName, String remark) {
        super();
        this.id = id;
        this.kindId = kindId;
        this.nodeKindId = nodeKindId;
        this.parentId = parentId;
        this.shortCode = shortCode;
        this.code = code;
        this.name = name;
        this.sequence = sequence;
        this.status = status;
        this.hasChildren = hasChildren;
        this.version = version;
        this.fullId = fullId;
        this.fullName = fullName;
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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getIsexpand() {
        return isexpand;
    }

    public void setIsexpand(Integer isexpand) {
        this.isexpand = isexpand;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public Integer getKindId() {
        return kindId;
    }

    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    public String getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(String nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
