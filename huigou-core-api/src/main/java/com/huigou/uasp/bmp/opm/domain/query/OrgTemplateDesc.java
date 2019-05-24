package com.huigou.uasp.bmp.opm.domain.query;

import com.huigou.uasp.bmp.opm.domain.model.org.OrgTemplate;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgType;

/**
 * 组织机构模板查询结果
 * 
 * @author Gerald
 */
public class OrgTemplateDesc {

    private String id;

    private String orgKindId;

    private String parentId;

    private String code;

    private String name;

    private String typeId;

    private Integer sequence;

    private Long version;

    private Integer hasChildren;

    private Integer isexpand = 0;

    private String nodeUrl = "Folder";

    public OrgTemplateDesc() {

    }

    public OrgTemplateDesc(String id, String orgKindId, String parentId, String code, String name, String typeId, Integer sequence, Long version,
                           Integer hasChildren) {
        super();
        this.id = id;
        this.orgKindId = orgKindId;
        this.parentId = parentId;
        this.code = code;
        this.name = name;
        this.typeId = typeId;
        this.sequence = sequence;
        this.hasChildren = hasChildren;
        this.version = version;
    }

    public static OrgTemplateDesc createRoot(int childrenCount) {
        OrgTemplateDesc result = new OrgTemplateDesc();

        result.setId(OrgTemplate.ROOT_ID_VALUE);
        result.setOrgKindId(OrgType.OrgKind.ogn.getId());
        result.setParentId(OrgTemplate.ROOT_PARENT_ID_VALUE);
        result.setCode(OrgTemplate.ROOT_CODE_VALUE);
        result.setName(OrgTemplate.ROOT_NAME_VALUE);
        result.setHasChildren(childrenCount);
        result.setVersion(1L);

        return result;
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

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    /*
     * public static List<OrgTemplateDesc> fromModels(List<OrgTemplate> orgTemplates) {
     * List<OrgTemplateDesc> results = new ArrayList<OrgTemplateDesc>(orgTemplates.size());
     * for (OrgTemplate orgTemplate : orgTemplates) {
     * OrgTemplateDesc orgTemplateDesc = new OrgTemplateDesc();
     * orgTemplateDesc.setId(orgTemplate.getId());
     * orgTemplateDesc.setParentId(orgTemplate.getParentId());
     * orgTemplateDesc.setOrgKindId(orgTemplate.getOrgType().getOrgKindId());
     * orgTemplateDesc.setTypeId(orgTemplate.getOrgType().getId());
     * orgTemplateDesc.setCode(orgTemplate.getOrgType().getCode());
     * orgTemplateDesc.setName(orgTemplate.getOrgType().getName());
     * orgTemplateDesc.setSequence(orgTemplate.getSequence());
     * orgTemplateDesc.setVersion(orgTemplate.getVersion());
     * results.add(orgTemplateDesc);
     * }
     * return results;
     * }
     */
}
