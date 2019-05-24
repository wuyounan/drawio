package com.huigou.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.huigou.util.CommonUtil;
import com.huigou.util.Util;

public class OrgNode extends OrgUnit implements Serializable {

    private static final long serialVersionUID = 8465574914802539992L;

    /**
     * 人员成员
     */
    public static final String PERSONMEMBER = "psm";

    /**
     * 岗位
     */
    public static final String POSITION = "pos";

    /**
     * 部门
     */
    public static final String DEPT = "dpt";

    /**
     * 中心
     */
    public static final String CENTER = "ctr";

    /**
     * 机构
     */
    public static final String ORGAN = "ogn";

    /**
     * 文件夹
     */
    public static final String FOLDER = "fld";

    /**
     * 项目组织
     */
    public static final String PROJECT = "prj";

    /**
     * 工作组
     */
    public static final String GROUP = "grp";

    /**
     * 销售团队
     */
    public static final String SALE_TEAM = "stm";

    /**
     * 职能角色
     */
    public static final String FUNCTION = "fun";

    private String id = null;

    private String name = null;

    private String code = null;

    private String fullCode = null;

    private String type = null;

    private OrgNode parent = null;

    private List<OrgNode> children = new ArrayList<OrgNode>();

    private boolean isRoot = false;

    /**
     * 创建组织节点
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            编码
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路径
     * @param fullCode
     *            编码全路径
     * @param type
     *            组织节点类型
     * @return
     */
    public static OrgNode createOrgNode(String id, String name, String code, String fullId, String fullName, String fullCode, String type) {
        Util.check(("psm".equals(type)) || ("pos".equals(type)) || ("dpt".equals(type)) || ("ogn".equals(type)) || ("grp".equals(type)), "创建OrgUnit类型不支持，",
                   type);

        if ("psm".equals(type)) {
            return new PersonMember(id, name, code, fullId, fullName, fullCode);
        }
        return new OrgNode(id, name, code, fullId, fullName, fullCode, type);
    }

    /**
     * 创建组织机构节点
     * 
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路径
     * @param fullCode
     *            编码全路径
     * @param type
     *            组织节点类型
     * @return
     */
    public static OrgNode createOrgNode(String fullId, String fullName, String fullCode, String type) {
        Util.check(("psm".equals(type)) || ("pos".equals(type)) || ("dpt".equals(type)) || ("ogn".equals(type)) || ("grp".equals(type)), "创建OrgUnit类型不支持，",
                   type);

        if ("psm".equals(type)) {
            return new PersonMember(CommonUtil.getNameNoExtOfFile(fullId), CommonUtil.getNameOfFile(fullName), CommonUtil.getNameOfFile(fullCode), fullId,
                                    fullName, fullCode);
        }

        return new OrgNode(CommonUtil.getNameNoExtOfFile(fullId), CommonUtil.getNameOfFile(fullName), CommonUtil.getNameOfFile(fullCode), fullId, fullName,
                           fullCode, type);
    }

    public static OrgNode createRoot() {
        OrgNode orgNode = new OrgNode();
        orgNode.isRoot = true;
        return orgNode;
    }

    /**
     * 创建工作组
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            编码
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路径
     * @param fullCode
     *            编码全路径
     * @return
     */
    public static OrgNode createWorkGroup(String id, String name, String code, String fullId, String fullName, String fullCode) {
        return new OrgNode(id, name, code, fullId, fullName, fullCode, "grp");
    }

    /**
     * 创建组织机构
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            编码
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路径
     * @param fullCode
     *            编码全路径
     * @return
     */
    public static OrgNode createOrgan(String id, String name, String code, String fullId, String fullName, String fullCode) {
        return new OrgNode(id, name, code, fullId, fullName, fullCode, "ogn");
    }

    /**
     * 创建人员成员
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            路径
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路路径
     * @param fullCode
     *            编码全路径
     * @return
     */
    public static PersonMember createPersonMember(String id, String name, String code, String fullId, String fullName, String fullCode) {
        return new PersonMember(id, name, code, fullId, fullName, fullCode);
    }

    /**
     * 创建部门
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            路径
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路路径
     * @param fullCode
     *            编码全路径
     * @return
     */
    public static OrgNode createDept(String id, String name, String code, String fullId, String fullName, String fullCode) {
        return new OrgNode(id, name, code, fullId, fullName, fullCode, "dpt");
    }

    /**
     * 创建岗位
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            路径
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路路径
     * @param fullCode
     *            编码全路径
     * @return
     */
    public static OrgNode createPosition(String id, String name, String code, String fullId, String fullName, String fullCode) {
        return new OrgNode(id, name, code, fullId, fullName, fullCode, "pos");
    }

    /**
     * 创建组织节点
     * 
     * @param id
     *            id
     * @param name
     *            名称
     * @param code
     *            路径
     * @param fullId
     *            id全路径
     * @param fullName
     *            名称全路路径
     * @param fullCode
     *            编码全路径
     * @param type
     *            类型
     */
    public OrgNode(String id, String name, String code, String fullId, String fullName, String fullCode, String type) {
        super(fullId, fullName);
        this.id = id;
        this.name = name;
        this.type = type;
        this.code = code;
        this.fullCode = fullCode;
    }

    protected OrgNode() {
        super("/", "/");
    }

    /**
     * 得到名称
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 得到编码
     * 
     * @return
     */
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 得到id
     * 
     * @return
     */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    /**
     * 得到组织类型
     * 
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * 得到父节点
     * 
     * @return
     */
    public OrgNode getParent() {
        return this.parent;
    }

    /**
     * 添加子组织
     * 
     * @param child
     */
    public void addChildren(OrgNode child) {
        if (Util.isNotNull(child)) {
            this.children.add(child);
            if (!this.isRoot) child.parent = this;
        }
    }

    /**
     * 删除子组织
     * 
     * @param child
     */
    public void removeChildren(OrgNode child) {
        if (Util.isNotNull(child)) {
            this.children.remove(child);
            child.parent = null;
        }
    }

    /**
     * 得到子组织
     * 
     * @return
     */
    public List<OrgNode> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    /**
     * 得到子组织
     * 
     * @param id
     * @param type
     * @return
     */
    public OrgNode getChild(String id, String type) {
        if ((Util.isNotEmptyString(id)) && (Util.isNotEmptyString(type))) {
            for (OrgNode item : this.children) {
                if ((id.equals(item.getId())) && (type.equals(item.getType()))) return item;
            }
        }
        return null;
    }

    /**
     * 子组织是否存在
     * 
     * @param id
     * @param type
     * @return
     */
    public boolean existChild(String id, String type) {
        return null != getChild(id, type);
    }

    /**
     * 得到编码全路径
     * 
     * @return
     */
    public String getFullCode() {
        return this.fullCode;
    }
}
