package com.huigou.uasp.bmp.opm.domain.model.org;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.OrgNode;
import com.huigou.data.domain.listener.VersionListener;
import com.huigou.domain.IdentifiedEntity;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.util.CommonUtil;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * 组织
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_OPOrg")
@EntityListeners({ VersionListener.class })
public class Org implements IdentifiedEntity, Serializable {

    private static final long serialVersionUID = -4359439039376343018L;

    public static final String ORG_ROOT_ID = "orgRoot";

    @Id
    private String id;

    /**
     * 实体版本号
     */
    private Long version;

    /**
     * 父节点ID
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * ID全路径
     */
    @Column(name = "full_id", length = 1024)
    private String fullId;

    /**
     * 名称全路径
     */
    @Column(name = "full_name", length = 1024)
    private String fullName;

    private Integer sequence;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 组织机构类型
     */
    @ManyToOne()
    @JoinColumn(name = "type_id")
    private OrgType orgType;

    /**
     * 全面名称
     */
    @Column(name = "long_name")
    private String longName;

    /**
     * 组织节点类型
     */
    @Column(name = "org_kind_id", nullable = false)
    private String orgKindId;

    /**
     * 部门级别
     */
    @Column(name = "dept_level")
    private String deptLevel;

    /**
     * 人员
     */
    @ManyToOne()
    @JoinColumn(name = "person_id")
    private Person person;

    /**
     * 描述
     */
    private String description;

    /**
     * 编码全路径
     */
    @Column(name = "full_code", nullable = false)
    private String fullCode;

    /**
     * 排序号全路径
     */
    @Column(name = "full_sequence", nullable = false)
    private String fullSequence;

    /**
     * 组件机构类别全路径
     */
    @Column(name = "full_org_kind_id", nullable = false)
    private String fullOrgKindId;

    /**
     * 是否虚拟组织
     */
    @Column(name = "is_virtual")
    private Integer isVirtual;

    /**
     * 公司ID
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 公司编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 公司名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 部门ID
     */
    @Column(name = "dept_id")
    private String deptId;

    /**
     * 部门编码
     */
    @Column(name = "dept_code")
    private String deptCode;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String deptName;

    /**
     * 岗位ID
     */
    @Column(name = "position_id")
    private String positionId;

    /**
     * 岗位编码
     */
    @Column(name = "position_code")
    private String positionCode;

    /**
     * 岗位名称
     */
    @Column(name = "position_name")
    private String positionName;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "SA_OPAuthorize", joinColumns = { @JoinColumn(name = "org_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "org_id")
    private List<OrgProperty> orgProperties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @JsonIgnore
    public OrgType getOrgType() {
        return orgType;
    }

    @JsonIgnore
    public void setOrgType(OrgType orgType) {
        this.orgType = orgType;
    }

    public String getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(String deptLevel) {
        this.deptLevel = deptLevel;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getOrgKindId() {
        return orgKindId;
    }

    public void setOrgKindId(String orgKindId) {
        this.orgKindId = orgKindId;
    }

    @JsonIgnore
    public Person getPerson() {
        return person;
    }

    @JsonIgnore
    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getFullSequence() {
        return fullSequence;
    }

    public void setFullSequence(String fullSequence) {
        this.fullSequence = fullSequence;
    }

    public String getFullOrgKindId() {
        return fullOrgKindId;
    }

    public void setFullOrgKindId(String fullOrgKindId) {
        this.fullOrgKindId = fullOrgKindId;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @JsonIgnore
    public String getTenantField_() {
        return "tenantId";
    }

    @JsonIgnore
    public String getTenantId_() {
        return tenantId;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public List<OrgProperty> getOrgProperties() {
        return orgProperties;
    }

    @JsonIgnore
    public void setOrgProperties(List<OrgProperty> orgProperties) {
        this.orgProperties = orgProperties;
    }

    /**
     * 构建冗余数据
     */
    public void buildRedundantData(Org parent) {
        buildFullInfo(parent);
        buildOrgNodeData();
    }

    @JsonIgnore
    public OrgNodeKind getOrgKind() {
        return OrgNodeKind.fromValue(orgKindId);
    }

    @JsonIgnore
    public ValidStatus getValidStatus() {
        return ValidStatus.fromId(this.getStatus());
    }

    private void buildOrgNodeData() {
        String[] orgIds = this.getFullId().substring(1).split("/");
        String[] orgCodes = this.getFullCode().substring(1).split("/");
        String[] orgNames = this.getFullName().substring(1).split("/");
        String[] orgKindIds = this.getFullOrgKindId().substring(1).split("/");

        Util.check(orgIds.length == orgCodes.length && orgIds.length == orgNames.length && orgIds.length == orgKindIds.length,
                   "调用“buildOrgIdNameExtInfo”出错，参数长度不等。");

        int deptLevel = 0;
        int orgLevel = 0;

        for (int i = orgIds.length - 1; i >= 0; i--) {
            if (orgIds[i].endsWith(OrgNode.ORGAN)) {
                orgLevel++;
                if (orgLevel == 1) {
                    this.setOrgId(orgIds[i].replace("." + OrgNode.ORGAN, ""));
                    this.setOrgCode(orgCodes[i]);
                    this.setOrgName(orgNames[i]);
                }
            } else if (orgKindIds[i].equalsIgnoreCase(OrgNode.DEPT)) {
                deptLevel++;
                if (deptLevel == 1) {
                    this.setDeptId(orgIds[i].replace("." + OrgNode.DEPT, ""));
                    this.setDeptCode(orgCodes[i]);
                    this.setDeptName(orgNames[i]);
                }
            } else if (orgIds[i].endsWith(OrgNode.POSITION)) {
                this.setPositionId(orgIds[i].replace("." + OrgNode.POSITION, ""));
                this.setPositionCode(orgCodes[i]);
                this.setPositionName(orgNames[i]);
            }
        }
    }

    private void buildFullInfo(Org parent) {

        String sequence = CommonUtil.lpad(3, this.getSequence());

        String fullId = OpmUtil.createFileFullName(parent == null ? "" : parent.getFullId(), this.getId(), getOrgKindId());
        String fullCode = OpmUtil.createFileFullName(parent == null ? "" : parent.getFullCode(), getCode(), "");
        String fullName = OpmUtil.createFileFullName(parent == null ? "" : parent.getFullName(), getName(), "");
        String fullSequence = OpmUtil.createFileFullName(parent == null ? "" : parent.getFullSequence(), sequence, "");
        String fullOrgKindId = OpmUtil.createFileFullName(parent == null ? "" : parent.getFullOrgKindId(), getOrgKindId(), "");

        setFullId(fullId);
        setFullCode(fullCode);
        setFullName(fullName);
        setFullSequence(fullSequence);
        setFullOrgKindId(fullOrgKindId);

    }

    private void checkOrgKindRule(Org parent, String operateKind) {
        // 公司+ -->部门+ --岗位 -->人员
        // 公司+-->项目组织分类+ -->项目组织--> 分组 -->人员-->职能角色
        // 公司+-->项目组织分类+ -->销售团队--> 部门+ -->人员
        if (parent == null) {
            Util.check(OrgNodeKind.OGN.equals(this.getOrgKind()), "%s失败，组织根下只能创机构节点。", new Object[] { operateKind });
        } else {
            // 组织层级判断
            Util.check(this.getOrgKind().getLevel() >= parent.getOrgKind().getLevel(), "%s失败，“%s”的上级组织不能是“%s”！", new Object[] {
                                                                                                                               operateKind,
                                                                                                                               getOrgKind().getDisplayName(),
                                                                                                                               parent.getOrgKind()
                                                                                                                                     .getDisplayName() });
            // 公司下面只能建机构、部门、项目分类（文件夹）
            if (OrgNodeKind.OGN.equals(parent.getOrgKind())) {
                Util.check(OrgNodeKind.OGN.equals(this.getOrgKind()) || OrgNodeKind.DPT.equals(this.getOrgKind()) || OrgNodeKind.FLD.equals(this.getOrgKind()),
                           "%s失败，机构下不能建“%s”。", new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            }
            // 部门下面只能建部门和岗位
            else if (OrgNodeKind.DPT.equals(parent.getOrgKind())) {
                Util.check(OrgNodeKind.DPT.equals(this.getOrgKind()) || OrgNodeKind.POS.equals(this.getOrgKind()), "%s失败，部门下不能建“%s”。",
                           new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            }
            // 岗位只能建人员
            else if (OrgNodeKind.POS.equals(parent.getOrgKind())) {
                Util.check(OrgNodeKind.PSM.equals(this.getOrgKind()), "%s失败，岗位下不能建“%s”。", new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            }
            // 项目组织分类下项目组织分类或项目组织
            else if (OrgNodeKind.FLD.equals(parent.getOrgKind())) {
                Util.check(OrgNodeKind.FLD.equals(this.getOrgKind()) || OrgNodeKind.PRJ.equals(this.getOrgKind()), "%s失败，项目组织分类下不能建“%s”。",
                           new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            }
            // 项目组织下只能建分组
            else if (OrgNodeKind.PRJ.equals(parent.getOrgKind())) {
                // Util.check(OrgNodeKind.GRP.equals(this.getOrgKind()) || OrgNodeKind.STM.equals(this.getOrgKind()), "%s失败，项目组织下不能建“%s”。",
                // new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            }
            // 销售团队下只能建部门
            // else if (OrgNodeKind.stm.equals(parent.getOrgKind())) {
            // Util.check(OrgNodeKind.dpt.equals(this.getOrgKind()), "%s失败，项目组织下不能建“%s”。", new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            // }
            // 分组下只能建人员
            // else if (OrgNodeKind.grp.equals(parent.getOrgKind())) {
            // Util.check(OrgNodeKind.psm.equals(this.getOrgKind()), "%s失败，分组下不能建“%s”。", new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            // }
            // 人员下只能建职能角色
            // else if (OrgKind.psm.equals(parent.getOrgKind())) {
            // Util.check(OrgKind.fun.equals(this.getOrgKind()), "%s失败，人员下不能建“%s”。", new Object[] { operateKind, this.getOrgKind().getDisplayName() });
            // }
        }
    }

    /**
     * 验证状态规则
     * 
     * @param parent
     * @param operateKind
     */
    public void checkStatusRule(Org parent, String operateKind) {
        if (parent != null) {
            Util.check(this.getValidStatus().getId() <= parent.getValidStatus().getId(), "%s失败，上级组织：“%s”的状态是“%s”，下级组织：“%s”的状态不能是“%s”。",
                       new Object[] { operateKind, parent.getName(), parent.getValidStatus().getDisplayName(), this.getName(),
                                     this.getValidStatus().getDisplayName() });
        }
    }

    /**
     * 验证组织ID合法性
     */
    private void checkOrgId() {
        Util.check(Util.isNotEmptyString(this.getId()), "标识不能为空。", new Object[0]);
        Util.check((this.getId().indexOf('/') < 0) && (this.getId().indexOf('@') < 0) && (this.getId().indexOf('.') < 0) && (this.getId().indexOf('%') < 0),
                   "无效的标识“%s”，不能包含字符“/”、“@”、“.”、“%%”。", new Object[] { this.getId() });
    }

    /**
     * 验证组织编码的合法性
     * 
     * @param code
     *            组织编码
     */
    private void checkOrgCode() {
        Util.check(Util.isNotEmptyString(this.getCode()), "编码“%s”不能为空。", new Object[] { this.getCode() });
        Util.check((this.getCode().indexOf('/') < 0) && (this.getCode().indexOf('%') < 0) && (this.getCode().indexOf(',') < 0),
                   "无效的编码“%s”，不能包含字符“/”、“%%”、“,”。", new Object[] { this.getCode() });
    }

    /**
     * 验证组织名称合法性
     * 
     * @param name
     *            组织名称
     */
    private void checkOrgName() {
        Util.check(Util.isNotEmptyString(this.getName()), "名称“%s”不能为空。", new Object[] { this.getName() });
        Util.check((this.getName().indexOf('/') < 0) && (this.getName().indexOf('%') < 0) && (this.getName().indexOf(',') < 0),
                   "无效的名称“%s”，不能包含字符“/”、“%%”、“,”。", new Object[] { this.getName() });
    }

    private void checkNamedValidity() {
        if (!OrgNodeKind.PSM.equals(this.getOrgKind())) {
            this.checkOrgId();
        }
        this.checkOrgCode();
        this.checkOrgName();
    }

    public void checkConstraints(Org parent, Org other, String operateKind) {
        checkNamedValidity();
        checkOrgKindRule(parent, operateKind);
        checkStatusRule(parent, operateKind);
        if (other != null) {
            if (this.getCode().equalsIgnoreCase(other.getCode())) {
                throw new ApplicationException("编码不能重复。");
            }
            if (this.getName().equalsIgnoreCase(other.getName())) {
                throw new ApplicationException("名称不能重复。");
            }
        }
    }

    /**
     * 删除角色
     * 
     * @param ids
     *            角色ID列表
     */
    public void removeRoles(List<String> roleIds) {
        for (Iterator<Role> iter = this.getRoles().iterator(); iter.hasNext();) {
            if (roleIds.contains(iter.next().getId())) {
                iter.remove();
            }
        }
    }

    /**
     * 构建角色实体列表
     * 
     * @param inputRoles
     * @retu
     */
    public void buildRoles(List<Role> inputRoles) {
        List<Role> roles = this.getRoles();
        if (roles == null) {
            this.setRoles(inputRoles);
            return;
        }

        boolean found;

        for (Role input : inputRoles) {
            found = false;
            for (Role role : roles) {
                if (input.equals(role)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                roles.add(input);
            }
        }
    }

    public void fromMap(Map<String, Object> params) {
        try {
            BeanUtils.populate(this, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return StringUtil.isBlank(this.id);
    }

    @JsonIgnore
    public boolean isRootChild() {
        return ORG_ROOT_ID.equals(this.parentId);
    }

    @Override
    public void setUpdateFields_(Collection<String> names) {
        // TODO Auto-generated method stub

    }

//    @Override
//    public void setUpdateFields_(String... updateFields) {
//        // TODO Auto-generated method stub
//    }

    public void reviseProperties() {
        if (isVirtual == null) {
            isVirtual = 0;
        }
    }

}
