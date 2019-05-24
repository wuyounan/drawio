package com.huigou.uasp.bmp.fn.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.cache.SystemCache;
import com.huigou.context.Operator;
import com.huigou.context.OrgNode;
import com.huigou.context.OrgUnit;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.jdbc.SQLQuery;
import com.huigou.domain.ValidStatus;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.OpmUtil;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.query.TenantDesc;
import com.huigou.uasp.bpm.engine.application.HandlerParseService;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

/**
 * 组织机构函数
 * 
 * @author gongmm
 */
@Service("orgFun")
public class OrgFun {

    @Resource(name = "sqlQuery")
    private SQLQuery sqlQuery;

    public void setSQLQuery(SQLQuery sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public Operator getOperator() {
        return OpmUtil.getBizOperator();
    }

    public String getManageRightAdmin() {
        return SystemCache.getParameter("adminMR", String.class);
    }

    private String getCompanyLeaderDeptCode() {
        return SystemCache.getParameter("companyLeaderDeptCode", String.class);
    }

    private String getBoardChairmanPosCode() {
        return SystemCache.getParameter("boardChairmanPosCode", String.class);
    }

    /**
     * 解析机构参数
     * 
     * @param org
     *            组织
     * @return 组织id
     */
    private static List<String> parseOrgParameter(Object org) {
        List<String> result = new ArrayList<String>();
        if (org == null) {
            return result;
        }

        if (org instanceof String) {
            if (Util.isNotEmptyString((String) org)) {
                result.add((String) org);
            }
        } else {
            if ((org instanceof List)) {
                @SuppressWarnings("unchecked")
                List<String> orgList = (List<String>) org;
                for (String item : orgList) {
                    if (Util.isNotEmptyString(item)) {
                        result.add(item);
                    }
                }
            } else {
                result.add(org + "");
            }
        }
        return result;
    }

    /**
     * 得到子组织SQL条件
     * 
     * @param org
     *            组织ID或者FullId
     * @param includeSelf
     *            包含自己
     * @return
     */
    private String getOrgChildrenCondition(Object org, boolean includeSelf) {
        String result = "";
        String orgCriteria;
        List<String> orgList = parseOrgParameter(org);
        for (String item : orgList) {
            if (Util.isNotEmptyString(item)) {
                if (Util.isNotEmptyString(result)) {
                    result = result + " or ";
                }
                // 0B2D6BF38A7C40D48A40B861A1C9375C
                if (Util.isEmptyString(CommonUtil.getExtOfFile(item))) {
                    if (includeSelf) {
                        orgCriteria = "manager.full_Id like '%" + item + "%'";
                    } else {
                        orgCriteria = "((manager.full_Id like '%" + item + "%') and (manager.id <> '" + item + "'))";
                    }
                } else {
                    // /27F4F0E421E14D4FB9BBBEE33005CE92.ogn/BA4301E5CCC54A7D855E475098C641A4.ogn/0B2D6BF38A7C40D48A40B861A1C9375C.dpt
                    if (includeSelf) {
                        orgCriteria = "manager.full_Id like '" + item + "%'";
                    } else {
                        orgCriteria = "manager.full_Id like '" + item + "/%'";
                    }
                }
                result = result + orgCriteria;
            }
        }

        if (Util.isNotEmptyString(result)) {
            result = "(" + result + ")";
        }

        return result;
    }

    /**
     * 组织转换为fullId
     * 
     * @param org
     *            组织Id或fullId(String或List)
     * @return
     */
    public List<String> toFullIds(Object org) {
        List<String> result = new ArrayList<String>();
        List<String> orgList = parseOrgParameter(org);

        if (orgList == null || orgList.isEmpty()) {
            return result;
        }

        List<String> orgIdList = new ArrayList<String>();

        for (String s : orgList) {
            if (Util.isNotEmptyString(s)) {
                if (Util.isEmptyString(CommonUtil.getExtOfFile(s))) {
                    orgIdList.add(s);
                } else {
                    result.add(s);
                }
            }
        }

        String condition = "";

        if (!orgIdList.isEmpty()) {
            for (String s : orgIdList) {
                if (Util.isNotEmptyString(condition)) {
                    condition = condition + " or ";
                }
                condition = condition + "(manager.id='" + s + "' or manager.person_id='" + s + "')";
            }
            result.addAll(orgUnitsToOrgFullIds(doFindOrgUnits(condition)));
        }
        return result;
    }

    /**
     * 组织单元转换组织机构fullId
     * 
     * @param orgUnits
     *            组织单元
     * @return
     */
    public List<String> orgUnitsToOrgFullIds(List<OrgUnit> orgUnits) {
        List<String> result = new ArrayList<String>(orgUnits.size());
        for (OrgUnit item : orgUnits) {
            if (Util.isNotEmptyString(item.getFullId())) {
                result.add(item.getFullId());
            }
        }
        return result;
    }

    private List<OrgUnit> doFindOrgUnits(String condition) {
        String sql = "select manager.full_id, manager.full_name from SA_OPOrg manager where manager.status = 1 ";

        if (Util.isNotEmptyString(condition)) {
            sql = sql + " and (" + condition + ")";
        }

        sql = sql + " order by manager.full_sequence";

        return this.sqlQuery.getJDBCDao().queryToList(sql, OrgUnit.class);
    }

    private List<OrgUnit> doFindPersonMember(List<OrgUnit> org) {
        if (org.isEmpty()) {
            return org;
        }

        List<OrgUnit> result = new ArrayList<OrgUnit>();
        List<String> orgFullIds = new ArrayList<String>();

        for (OrgUnit unit : org) {
            if (unit.getFullId().endsWith(".psm")) {
                result.add(unit);
            } else {
                if (!orgFullIds.contains(unit.getFullId())) {
                    orgFullIds.add(unit.getFullId());
                }
            }
        }

        String condition;
        String orgChildrenCondition = getOrgChildrenCondition(orgFullIds, true);
        if (Util.isNotEmptyString(orgChildrenCondition)) {
            condition = "(" + orgChildrenCondition + " and (manager.org_kind_id = 'psm'))";
            result.addAll(doFindOrgUnits(condition));
        }

        result = distinctOrgUnitsByFullId(result);
        return result;
    }

    public static List<OrgUnit> distinctOrgUnitsByFullId(List<OrgUnit> orgUnit) {
        HashSet<String> orgFullIds = new HashSet<String>();
        List<OrgUnit> result = new ArrayList<OrgUnit>();
        for (OrgUnit item : orgUnit) {
            if ((Util.isNotEmptyString(item.getFullId())) && (!orgFullIds.contains(item.getFullId()))) {
                orgFullIds.add(item.getFullId());
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 查找人员成员
     * 
     * @param orgCode
     *            机构编码
     * @param deptCode
     *            部门编码
     * @param positionCode
     *            岗位编码
     * @return 人员成员
     */
    public List<OrgUnit> findPersonMembers(String orgCode, String deptCode, String positionCode) {
        String condition = String.format(" org_code = '%s' and dept_code = '%s'  and position_code = '%s' and org_kind_id = 'psm'", orgCode, deptCode,
                                         positionCode);
        return this.doFindOrgUnits(condition);
    }

    /**
     * 查找中心管理者
     * 
     * @param orgCode
     *            机构编码
     * @param centerCode
     *            中心编码
     * @param manageType
     *            管理权限类别
     * @return 中心管理者
     */
    public List<OrgUnit> findCenterManagers(String orgCode, String centerCode, String manageType) {
        String sql = "select full_id from SA_OPOrg where org_code = ? and center_code = ? and org_kind_id = 'dpt' and is_center = 1";

        String orgFullId = this.sqlQuery.getJDBCDao().queryToString(sql, orgCode, centerCode);

        if (StringUtil.isBlank(orgFullId)) {
            throw new ApplicationException("执行“findCenterManagers()”出错， 没有找到中心。");
        }

        return this.findManagers(orgFullId, manageType, false, null);
    }

    /**
     * 查找部门管理者
     * 
     * @param orgCode
     *            机构编码
     * @param centerCode
     *            中心编码
     * @param deptCode
     *            部门编码
     * @param manageType
     *            管理权限类别
     * @return 部门管理者
     */
    public List<OrgUnit> findDeptManagers(String orgCode, String centerCode, String deptCode, String manageType) {
        String sql = "select full_id from sa_oporg where org_code = ? and center_code = ? and dept_code = ? and org_kind_id = 'dpt'";
        if (StringUtil.isBlank(centerCode)) centerCode = deptCode;

        String orgFullId = this.sqlQuery.getJDBCDao().queryToString(sql, orgCode, centerCode, deptCode);

        if (StringUtil.isBlank(orgFullId)) {
            throw new ApplicationException("执行“findDeptManagers()”出错， 没有找到部门。");
        }

        return this.findManagers(orgFullId, manageType, true, null);
    }

    /**
     * 获取指定组织单元的管理者
     * 
     * @param org
     *            组织单元; 允许两种情况: 单个组织单元ID或FullID(String类型);
     *            多个组织单元ID或FullID(String类型)形成的List;
     * @param manageType
     *            基础管理类型的Code,空表示所有管理类型
     * @param includeAllParent
     *            是否包括所有父的管理者
     * @param inOrg
     *            返回值必须在指定组织范围内, 允许三种情况: 空值, 表示不限制范围; 单个组织单元ID或FullID(String类型);
     *            多个组织单元ID或FullID(String类型)形成的List;
     * @return 指定组织单元的管理者
     */
    public List<OrgUnit> findManagers(Object org, String manageType, boolean includeAllParent, Object inOrg) {
        Assert.notNull(org, "调用findManagers出错，参数org不能为空。");
        Assert.hasText(manageType, "调用findManagers出错，参数manageType不能为空。");

        List<OrgUnit> result = new ArrayList<OrgUnit>();

        List<String> subordinationFullIds = toFullIds(org);
        String subordinationFullIdCondition, condition = null;
        for (String subordinationFullId : subordinationFullIds) {
            if (Util.isNotEmptyString(subordinationFullId)) {
                if (includeAllParent) {
                    subordinationFullIdCondition = "'" + subordinationFullId + "' like concat(subordination.full_id, '%')";
                } else {
                    subordinationFullIdCondition = " subordination.full_id ='" + subordinationFullId + "'";
                }
                if (Util.isEmptyString(condition)) {
                    condition = subordinationFullIdCondition;
                } else {
                    condition += " or " + subordinationFullIdCondition;
                }
            }
        }

        if (Util.isEmptyString(condition)) {
            return result;
        }

        condition = "(" + condition + ")";

        String inManagerCondition = getOrgChildrenCondition(inOrg, true);
        if (Util.isNotEmptyString(inManagerCondition)) {
            condition += " and " + inManagerCondition;
        }

        condition += " and base.code='" + manageType + "' ";

        StringBuilder sb = new StringBuilder();

        sb.append("select manager.full_id, manager.full_name");
        sb.append("  from SA_OPOrg manager, SA_OPBizmanagement m, SA_OPOrg subordination, SA_OPBizmanagementType biz, SA_OPBasemanagementType base");
        sb.append(" where manager.id = m.manager_id");
        sb.append("   and m.subordination_id = subordination.id");
        sb.append("   and manager.status = 1");
        sb.append("   and m.manage_type_id = biz.id");
        sb.append("   and base.biz_management_type_id = biz.id");
        sb.append(" and (");
        sb.append(condition + ")");
        sb.append(" order by manager.full_sequence desc");

        List<OrgUnit> orgUnitsByOrg = this.sqlQuery.getJDBCDao().queryToList(sb.toString(), OrgUnit.class);

        result.addAll(orgUnitsByOrg);

        result = distinctOrgUnitsByFullId(result);

        return result;
    }

    /**
     * 获取指定组织单元的最近管理者
     * 
     * @param org
     *            组织单元， 单个组织单元Id或FullId(String类型);
     * @param manageType
     *            管理类型的Code,空表示所有管理类型
     * @return 最近管理者
     */
    public List<OrgUnit> findNearestManagers(Object org, String manageType) {
        List<OrgUnit> result = new ArrayList<OrgUnit>();

        List<String> orgFullIds = toFullIds(org);

        String subordinationFullIdCondition, condition = null;

        for (String fullId : orgFullIds) {
            if (Util.isNotEmptyString(fullId)) {
                subordinationFullIdCondition = "'" + fullId + "' like concat(subordination.full_id, '%')";
                if (Util.isEmptyString(condition)) {
                    condition = subordinationFullIdCondition;
                } else {
                    condition += " or " + subordinationFullIdCondition;
                }
            }
        }

        if (Util.isEmptyString(condition)) {
            return result;
        }

        condition = "(" + condition + ")";

        if (Util.isNotEmptyString(manageType)) {
            condition += " and base.code='" + manageType + "' ";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("select manager.full_id, manager.full_name, subordination.full_id as subordination_org_full_id");
        sb.append("  from SA_OPOrg manager, SA_OPBizmanagement m, SA_OPOrg subordination, SA_OPBizmanagementType biz, SA_OPBasemanagementType base");
        sb.append(" where manager.id = m.manager_id");
        sb.append("   and m.subordination_id = subordination.id");
        sb.append("   and manager.status = 1");
        sb.append("   and m.manage_type_id = biz.id");
        sb.append("   and base.biz_management_type_id = biz.id");
        sb.append(" and (");
        sb.append(condition + ")");
        sb.append(" order by subordination.full_id desc, manager.full_sequence");

        List<Map<String, Object>> data = this.sqlQuery.getJDBCDao().queryToListMap(sb.toString());
        if (data.size() > 0) {
            String subordinationOrgFullId = data.get(0).get("subordinationOrgFullId").toString();
            for (Map<String, Object> item : data) {
                if (subordinationOrgFullId.equalsIgnoreCase(item.get("subordinationOrgFullId").toString())) {
                    OrgUnit orgUnit = new OrgUnit();
                    orgUnit.setFullId(item.get("fullId").toString());
                    orgUnit.setFullName(item.get("fullName").toString());
                    result.add(orgUnit);
                } else {
                    break;
                }
            }
        }

        result = distinctOrgUnitsByFullId(result);
        return result;
    }

    /**
     * 指定组织单元是否拥有管理权限
     * 
     * @param org
     *            组织单元; 允许两种情况
     *            <ul>
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param manageType
     *            管理类型的code,空表示所有管理类型
     * @return
     */
    public Boolean hasManageType(Object org, String manageType) {
        // 得到组织机构fullId
        List<String> orgFullIds = toFullIds(org);
        if (orgFullIds.isEmpty()) {
            return false;
        }
        // 构建组织机构fullId条件
        String condition = null, orgFullIdCriteria = null;
        for (String orgFullId : orgFullIds) {
            if (Util.isNotEmptyString(orgFullId)) {
                orgFullIdCriteria = "'" + orgFullId + "' like concat(manager.full_id, '%')";
                condition = condition == null ? "" : condition + " or ";
                condition = condition + orgFullIdCriteria;
            }
        }
        if (Util.isEmptyString(condition)) {
            return false;
        }

        condition = "(" + condition + ")";

        // 构建管理权限条件
        if (Util.isNotEmptyString(manageType)) {
            condition += " and (";
            String[] manageTypes = manageType.split(",");
            for (String item : manageTypes) {
                if (Util.isNotEmptyString(item)) {
                    condition += " base.code='" + item + "' or";
                }
            }
            if (condition.endsWith("or")) {
                condition = condition.substring(0, condition.length() - 2);
            }
            condition += ")";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) ");
        sb.append("  from SA_OPOrg manager, SA_OPBizmanagement m, SA_OPBizmanagementType biz, SA_OPBasemanagementType base");
        sb.append(" where manager.id = m.manager_id");
        sb.append("   and m.manage_type_id = biz.id");
        sb.append("   and base.biz_management_type_id = biz.id");
        sb.append("   and (");
        sb.append(condition + ")");

        Integer count = this.sqlQuery.getJDBCDao().queryToInt(sb.toString());

        return count > 0;
    }

    /**
     * 获取指定组织单元的下属
     * 
     * @param org
     *            组织单元; 允许两种情况
     *            <ul>
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param manageType
     *            管理类型的code,空表示所有管理类型
     * @param inOrg
     *            返回值必须在指定组织范围内
     *            <ul>
     *            允许三种情况:
     *            <li>空值, 表示不限制范围;
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param includeAllParent
     *            是否包括所有父级节点的管理权限
     * @param isPersonMember
     *            是否取到人员成员
     * @return 指定组织单元的下属
     */
    public List<OrgUnit> findSubordinations(Object org, String manageType, Object inOrg, Boolean includeAllParent, Boolean isPersonMember) {
        List<OrgUnit> result = new ArrayList<OrgUnit>();
        // 得到组织机构fullId
        List<String> orgFullIds = toFullIds(org);
        if (orgFullIds.isEmpty()) {
            return result;
        }
        // 构建组织机构fullId条件
        String condition = null, orgFullIdCriteria = null;
        for (String orgFullId : orgFullIds) {
            if (Util.isNotEmptyString(orgFullId)) {
                if (includeAllParent) {
                    orgFullIdCriteria = "'" + orgFullId + "' like concat(manager.full_id, '%')";
                } else {
                    orgFullIdCriteria = "manager.full_Id = '" + orgFullId + "'";
                }
                condition = condition == null ? "" : condition + " or ";
                condition = condition + orgFullIdCriteria;
            }
        }
        if (Util.isEmptyString(condition)) {
            return result;
        }

        condition = "(" + condition + ")";

        // 构建管理权限条件
        if (Util.isNotEmptyString(manageType)) {
            condition += " and (";
            String[] manageTypes = manageType.split(",");
            for (String item : manageTypes) {
                if (Util.isNotEmptyString(item)) {
                    condition += " base.code='" + item + "' or";
                }
            }
            if (condition.endsWith("or")) {
                condition = condition.substring(0, condition.length() - 2);
            }
            condition += ")";
        }

        // 构建包含条件
        String orgChildrenCondition = getOrgChildrenCondition(inOrg, true);
        if (Util.isNotEmptyString(orgChildrenCondition)) {
            condition = condition + " and " + orgChildrenCondition;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select subordination.full_id, subordination.full_name ");
        sb.append("  from SA_OPOrg manager, SA_OPBizmanagement m, SA_OPOrg subordination, SA_OPBizmanagementType biz, SA_OPBasemanagementType base");
        sb.append(" where manager.id = m.manager_id");
        sb.append("   and m.subordination_id = subordination.id");
        sb.append("   and subordination.status = 1");
        sb.append("   and m.manage_type_id = biz.id");
        sb.append("   and base.biz_management_type_id = biz.id");
        sb.append("   and (");
        sb.append(condition + ")");
        sb.append(" order by subordination.full_sequence");

        List<OrgUnit> orgUnitsByOrg = this.sqlQuery.getJDBCDao().queryToList(sb.toString(), OrgUnit.class);
        if (isPersonMember) {
            orgUnitsByOrg = doFindPersonMember(orgUnitsByOrg);
        }

        result.addAll(orgUnitsByOrg);
        result = distinctOrgUnitsByFullId(result);

        return result;
    }

    /**
     * 获取指定组织单元的下属
     * 
     * @param org
     *            组织单元; 允许两种情况
     *            <ul>
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param manageType
     *            管理类型的code,空表示所有管理类型
     * @param inOrg
     *            返回值必须在指定组织范围内
     *            <ul>
     *            允许三种情况:
     *            <li>空值, 表示不限制范围;
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param isPersonMember
     *            是否取到人员成员
     * @return 指定组织单元的下属
     */
    public List<OrgUnit> findSubordinationsForNearestManager(Object org, String manageType, Object inOrg, Boolean isPersonMember) {
        List<OrgUnit> result = new ArrayList<OrgUnit>();
        // 得到组织机构fullId
        List<String> orgFullIds = toFullIds(org);
        if (orgFullIds.isEmpty()) {
            return result;
        }
        // 构建组织机构fullId条件
        String condition = null, orgFullIdCriteria = null;
        for (String orgFullId : orgFullIds) {
            if (Util.isNotEmptyString(orgFullId)) {
                orgFullIdCriteria = "'" + orgFullId + "' like concat(manager.full_id, '%')";
                condition = condition == null ? "" : condition + " or ";
                condition = condition + orgFullIdCriteria;
            }
        }
        if (Util.isEmptyString(condition)) {
            return result;
        }

        condition = "(" + condition + ")";

        // 构建管理权限条件
        if (Util.isNotEmptyString(manageType)) {
            condition += " and (";
            String[] manageTypes = manageType.split(",");
            for (String item : manageTypes) {
                if (Util.isNotEmptyString(item)) {
                    condition += " base.code='" + item + "' or";
                }
            }
            if (condition.endsWith("or")) {
                condition = condition.substring(0, condition.length() - 2);
            }
            condition += ")";
        }

        // 构建包含条件
        String orgChildrenCondition = getOrgChildrenCondition(inOrg, true);
        if (Util.isNotEmptyString(orgChildrenCondition)) {
            condition = condition + " and " + orgChildrenCondition;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select subordination.full_id, subordination.full_name, manager.full_id manager_org_full_id");
        sb.append("  from SA_OPOrg manager, SA_OPBizmanagement m, SA_OPOrg subordination, SA_OPBizmanagementType biz, SA_OPBasemanagementType base");
        sb.append(" where manager.id = m.manager_id");
        sb.append("   and m.subordination_id = subordination.id");
        sb.append("   and subordination.status = 1");
        sb.append("   and m.manage_type_id = biz.id");
        sb.append("   and base.biz_management_type_id = biz.id");
        sb.append("   and (");
        sb.append(condition + ")");
        sb.append(" order by manager.full_sequence desc, subordination.full_sequence");

        List<Map<String, Object>> data = this.sqlQuery.getJDBCDao().queryToListMap(sb.toString());
        //TODO 一人多岗
        if (data.size() > 0) {
            String managerOrgFullId = data.get(0).get("managerOrgFullId").toString();
            for (Map<String, Object> item : data) {
                if (managerOrgFullId.equalsIgnoreCase(item.get("managerOrgFullId").toString())) {
                    OrgUnit orgUnit = new OrgUnit();
                    orgUnit.setFullId(item.get("fullId").toString());
                    orgUnit.setFullName(item.get("fullName").toString());
                    result.add(orgUnit);
                } else {
                    break;
                }
            }
        }
       
        return result;
    }

    /**
     * 获取指定组织单元的下属
     * 
     * @param org
     * @param manageType
     * @return
     */
    public List<OrgUnit> findSubordinationsByOrgManageType(Object org, String manageType) {
        return findSubordinations(org, manageType, "", true, false);
    }

    public static String getOrgId(String orgIdOrFullId) {
        if (Util.isNotEmptyString(orgIdOrFullId)) {
            String orgId = null;
            if (orgIdOrFullId.contains("/")) {
                orgId = orgIdOrFullId.substring(orgIdOrFullId.lastIndexOf("/") + 1);
            } else {
                orgId = orgIdOrFullId;
            }

            if (orgId.endsWith(".psm")) {
                if (orgId.contains("@")) {
                    return orgId.substring(0, orgId.indexOf("@"));
                }
                throw new ApplicationException(orgIdOrFullId + "不是合法的id或FullId, 人员成员的id或FulId要求的格式是: person id + @ + position id。");
            }

            if (orgId.endsWith(".pos")) {
                return orgId.substring(0, orgId.length() - ".pos".length());
            }
            if (orgId.endsWith(".dpt")) {
                return orgId.substring(0, orgId.length() - ".dpt".length());
            }
            if (orgId.endsWith(".ogn")) {
                return orgId.substring(0, orgId.length() - ".ogn".length());
            }
            if (orgId.endsWith(".grp")) {
                return orgId.substring(0, orgId.length() - ".grp".length());
            }

            return orgIdOrFullId;
        }

        return orgIdOrFullId;
    }

    private static List<String> toIds(Object org) {
        List<String> result = new ArrayList<String>();
        List<String> orgList = parseOrgParameter(org);
        if ((orgList == null) || (orgList.isEmpty())) {
            return result;
        }

        for (String item : orgList) {
            String orgId = getOrgId(item);
            if (Util.isNotEmptyString(orgId)) {
                result.add(orgId);
            }
        }

        return result;
    }

    /**
     * 获取指定组织下的人员成员
     * 
     * @param org
     *            组织单元; 允许两种情况: 单个组织单元id或fullId(String类型);
     *            多个组织单元id或FullId(String类型)形成的List;
     * @param includeAllChildren
     *            是否包括所有的子孙
     * @return
     */
    public List<OrgUnit> findPersonMembersInOrg(Object org, boolean includeAllChildren) {
        List<OrgUnit> result = new ArrayList<OrgUnit>();
        String orgChildrenCondition = "";

        if (includeAllChildren) {
            orgChildrenCondition = this.getOrgChildrenCondition(org, true);
        } else {
            List<String> orgFullIdList = toIds(org);
            for (String item : orgFullIdList) {
                if (Util.isNotEmptyString(item)) {
                    if (Util.isNotEmptyString(orgChildrenCondition)) {
                        orgChildrenCondition = orgChildrenCondition + " or ";
                    }
                    orgChildrenCondition = orgChildrenCondition + " manager.parent_id ='" + item + "'";
                }
            }
        }

        if (Util.isNotEmptyString(orgChildrenCondition)) {
            orgChildrenCondition = "((" + orgChildrenCondition + ") and (manager.org_kind_id = 'psm'))";
            result = doFindOrgUnits(orgChildrenCondition);
            result = distinctOrgUnitsByFullId(result);
        }

        return result;
    }

    public String currentTenantId() {
        return getOperator().getTenantId();
    }

    public String currentOrgFullId() {
        return getOperator().getFullId();
    }

    public String currentOrgId() {
        return getOperator().getOrgId();
    }

    public String currentOrgCode() {
        return getOperator().getOrgCode();
    }

    public String currentOrgName() {
        return getOperator().getOrgName();
    }

    public String currentDeptId() {
        return getOperator().getDeptId();
    }

    public String currentDeptCode() {
        return getOperator().getDeptCode();
    }

    public String currentDeptName() {
        return getOperator().getDeptName();
    }

    public String currentPositionId() {
        return getOperator().getPositionId();
    }

    public String currentPositionCode() {
        return getOperator().getPositionCode();
    }

    public String currentPositionName() {
        return getOperator().getPositionName();
    }

    public String currentPersonMemberId() {
        return getOperator().getPersonMemberId();
    }

    public String currentPersonMemberCode() {
        return getOperator().getPersonMemberCode();
    }

    public String currentPersonMemberName() {
        return getOperator().getPersonMemberName();
    }

    public String currentPersonId() {
        return getOperator().getUserId();
    }

    public String currentPersonName() {
        return getOperator().getName();
    }

    public String currentFullId() {
        return getOperator().getFullId();
    }

    public String currentFullName() {
        return getOperator().getFullName();
    }

    /**
     * 更加fullId得到组织机构信息
     * 
     * @param fullId
     * @return
     */
    public Org findOrgByFullId(String fullId) {
        String sql = "select * from SA_OPOrg org where full_id = ? and status = 1";
        return this.sqlQuery.getJDBCDao().queryToObject(sql, Org.class, fullId);
    }

    /**
     * 根据公司编码查找公司信息
     * 
     * @param organCode
     * @return
     */
    public Org findOrganByCode(String organCode) {
        if (StringUtil.isBlank(organCode)) {
            throw new ApplicationException("公司编码不能为空。");
        }
        String sql = "select * from SA_OPOrg org where code = ? and status = 1 and org_kind_id = 'ogn'";
        return this.sqlQuery.getJDBCDao().queryToObject(sql, Org.class, organCode);
    }

    public Org findOrgById(String id) {
        String sql = "select * from SA_OPOrg org where id = ? and status = 1";
        return this.sqlQuery.getJDBCDao().queryToObject(sql, Org.class, id);
    }

    /**
     * 查找公司的管理者
     * 
     * @param organCode
     * @param manageType
     * @return
     */
    public List<OrgUnit> findOrganManager(String organCode, String manageType) {
        Org organ = findOrganByCode(organCode);
        Assert.notNull(organ, String.format("未找到公司编码“%s”对应的公司。", organCode));

        Util.check(organ.getValidStatus() == ValidStatus.ENABLED, "公司“%s”已禁用或删除。", new Object[] { organ.getName() });

        return this.findManagers(organ.getId(), manageType, false, null);
    }

    private enum TransformOrgKind {
        CURRENT_ORG, BIZ_SEGMENTATION
    }

    private class TranslateOrg {
        private TransformOrgKind translateOrgKind;

        private Org org;

        public TransformOrgKind getTranslateOrgKind() {
            return translateOrgKind;
        }

        public void setTranslateOrgKind(TransformOrgKind translateOrgKind) {
            this.translateOrgKind = translateOrgKind;
        }

        public Org getOrg() {
            return org;
        }

        public void setOrg(Org org) {
            this.org = org;
        }

    }

    /**
     * 转换当前组织ID
     * 
     * @param orgId
     *            组织id
     * @return
     */
    private TranslateOrg translateCurrentOrg() {
        TranslateOrg result = new TranslateOrg();
        Long currentSegmentationId = ClassHelper.convert(ThreadLocalUtil.getVariable(HandlerParseService.CURRENT_BIZ_SEGEMENTATION_ID), Long.class, 0L);
        if (currentSegmentationId > 0L) {
            String sql = "select * from sa_oporg where id = (select t.org_id from wf_bizsegmentation t where t.biz_segmentation_id = ?)";
            Org org = this.sqlQuery.getJDBCDao().queryToObject(sql, Org.class, currentSegmentationId);
            Util.check(org != null, "调用“translateCurrentOrgId”函数出错，未找到业务ID“%s”对应的组织。", new Object[] { currentSegmentationId });
            result.setTranslateOrgKind(TransformOrgKind.BIZ_SEGMENTATION);
            result.setOrg(org);
            return result;
        } else {
            result.setTranslateOrgKind(TransformOrgKind.CURRENT_ORG);
        }
        return result;
    }

    /**
     * 通过中心ID查找第一负责人
     * 
     * @param centerId
     *            中心ID
     * @return 中心第一负责人
     */
    public List<OrgUnit> findCenterManagerById(String centerId) {
        Util.check(!StringUtil.isBlank(centerId), "调用“findCenterManagerById”函数出错，中心ID不能为空。");
        // gongmm 2015-12-17 支持多中心
        String[] splits = centerId.split(",");
        List<String> centerIdList = Arrays.asList(splits);

        return this.findManagers(centerIdList, getManageRightAdmin(), false, null);
    }

    /**
     * 当前机构总经理
     * 
     * @return 当前机构总经理
     */
    public List<OrgUnit> currentOrganGeneralManager() {
        TranslateOrg translateOrg = translateCurrentOrg();
        String currentOrgId = translateOrg.getTranslateOrgKind() == TransformOrgKind.CURRENT_ORG ? this.currentOrgId() : translateOrg.getOrg().getOrgId();
        return findOrganGeneralManagerByOrganId(currentOrgId);
    }

    /**
     * 得到指定公司总经理
     * 
     * @param organId
     * @return 指定公司总经理
     */
    public List<OrgUnit> findOrganGeneralManagerByOrganId(String organId) {
        return this.findManagers(organId, "GeneralBizManager", false, null);
    }

    /*
     * 当前公司某部门负责人
     */
    public List<OrgUnit> findCurrentOrganCenterManager(String centerCode) {
        return findCenterManagers(this.currentOrgCode(), centerCode, this.getManageRightAdmin());
    }

    /**
     * 获取当前人员的行政管理权限
     * 
     * @return 当前人员的行政管理权限
     */
    public List<OrgUnit> findCurrentPersonMemberAdminManager() {
        return this.findNearestManagers(this.currentFullId(), this.getManageRightAdmin());
    }

    /**
     * 查找业务线管理者
     * 
     * @return 人员成员列表
     */
    public List<OrgUnit> findBizLineManagers(Object org, String manageType) {
        Util.check(org != null, "调用”findBizLineManagers“函数出错，org为空。");
        Util.check(!StringUtil.isBlank(manageType), "调用”findBizLineManagers“函数出错，权限类别为空。");

        /**
         * a ---->b b----->c c----->d d----->e e----->(e)* root
         */
        int i = 1;
        List<OrgUnit> result = new ArrayList<OrgUnit>();

        List<String> managerFullIds, parentManagerFullIds, managerPersonMemberFullIds, parentManagerPersonMemberFullIds;
        List<OrgUnit> managerPersonMemberOrgUnits, parentManagerPersonMemberOrgUnits;

        List<OrgUnit> parentManagers = this.findNearestManagers(org, manageType);
        List<OrgUnit> managers = null;

        parentManagerFullIds = this.orgUnitsToOrgFullIds(parentManagers);
        parentManagerPersonMemberOrgUnits = this.findPersonMembersInOrg(parentManagerFullIds, true);

        result.addAll(parentManagerPersonMemberOrgUnits);

        while (parentManagers.size() > 0) {
            managers = parentManagers;

            managerFullIds = this.orgUnitsToOrgFullIds(managers);
            parentManagers = this.findNearestManagers(managerFullIds, manageType);
            parentManagerFullIds = this.orgUnitsToOrgFullIds(parentManagers);

            managerPersonMemberOrgUnits = this.findPersonMembersInOrg(managerFullIds, true);
            parentManagerPersonMemberOrgUnits = this.findPersonMembersInOrg(parentManagerFullIds, true);

            managerPersonMemberFullIds = this.orgUnitsToOrgFullIds(managerPersonMemberOrgUnits);
            parentManagerPersonMemberFullIds = this.orgUnitsToOrgFullIds(parentManagerPersonMemberOrgUnits);

            for (String managerPersonMemberFullId : managerPersonMemberFullIds) {
                if (parentManagerPersonMemberFullIds.contains(managerPersonMemberFullId)) {
                    return result;
                }
            }

            result.addAll(parentManagerPersonMemberOrgUnits);

            i++;
            if (i > 10) {
                throw new ApplicationException("调用findBizLineManagers出错，超出调用层次。");
            }
        }

        return result;
    }

    /**
     * 得到组织属性
     * 
     * @param orgId
     *            组织ID
     * @param propertyName
     *            属性名称
     * @return 组织属性
     */
    public String getOrgProperty(String orgId, String propertyName) {
        StringBuilder sb = new StringBuilder();

        sb.append("select p.property_value");
        sb.append("  from SA_OPOrgProperty p, SA_OPOrgPropertyDefinition d");
        sb.append(" where p.property_definition_id = d.id");
        sb.append("   and p.org_id = ?");
        sb.append("   and d.name = ?");

        return this.sqlQuery.getJDBCDao().queryToString(sb.toString(), orgId, propertyName);
    }

    /**
     * 根据机构ID获取组织机构类别
     * 
     * @param organId
     *            组织机构ID
     * @return 机构类别
     */
    public String getOrgAdminKindById(String organId) {
        return getOrgProperty(organId, "orgAdminKind");
    }

    /**
     * 人员是否在岗位列表中
     * 
     * @param personId
     *            人员ID
     * @param positionIds
     *            岗位id列表,允许两种情况: 单个岗位Id(String类型); 多个岗位Id(String类型)形成的List;
     * @return 人员是否在岗位列表中
     */
    public Boolean isPersonAtPostions(String personId, Object positionIds) {
        String sql = "select * from SA_OPOrg where person_id = ?";
        List<Org> orgs = this.sqlQuery.getJDBCDao().queryToList(sql, Org.class, personId);

        for (Org org : orgs) {
            if ((positionIds instanceof String)) {
                return org.getPositionId().equalsIgnoreCase((String) positionIds);
            } else {
                if ((positionIds instanceof List)) {
                    @SuppressWarnings("unchecked")
                    List<String> positionList = (List<String>) positionIds;
                    for (String positionId : positionList) {
                        if (positionId.equalsIgnoreCase(org.getPositionId())) {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 人员拥有业务岗位
     * 
     * @param orgId
     *            组织ID
     * @param personId
     *            人员ID
     * @param baseFunctionTypeIds
     *            基础职能角色ID列表,允许两种情况:基础职能角色Id(Long类型); 多个基础职能角色ID(Long类型)形成的List;
     * @return 人员是否有基础职能角色
     */
    public Boolean isPersonHasBaseFunctionType(String orgId, String personId, Object baseFunctionTypeIds) {
        StringBuilder sb = new StringBuilder();

        sb.append("select count(*)");
        sb.append("  from sa_oporg t");
        sb.append(" where t.org_id = ?");
        sb.append("   and t.id like ? and t.org_kind_id = 'FUN'");

        List<Object> params = new ArrayList<Object>();
        params.add(orgId);
        params.add(String.format("%s@%%", personId));

        if (baseFunctionTypeIds instanceof Long) {
            sb.append(" and t.type_id = ?");
            params.add(baseFunctionTypeIds);
        } else if (baseFunctionTypeIds instanceof List) {
            @SuppressWarnings("unchecked")
            List<Long> baseFunctionTypeList = (List<Long>) baseFunctionTypeIds;
            sb.append(" and t.type_id in (");
            boolean isFirst = true;
            for (Long baseFunctionTypeId : baseFunctionTypeList) {
                if (isFirst) {
                    isFirst = false;
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
                params.add(baseFunctionTypeId);
            }
            sb.append(")");
        }

        int count = this.sqlQuery.getJDBCDao().queryToInt(sb.toString(), params.toArray());
        return count > 0;

    }

    public String getNearestOrganIdFromFullId(String fullId) {
        if (!StringUtil.isBlank(fullId)) {
            String[] splits = fullId.split("/");

            String[] organInfo;
            for (int i = splits.length - 1; i >= 0; i--) {
                if (!StringUtil.isBlank(splits[i])) {
                    organInfo = splits[i].split("\\.");
                    if (organInfo[1].equalsIgnoreCase(OrgNode.ORGAN)) {
                        return organInfo[0];
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取指定组织单元的功能权限ID
     * 
     * @param org
     *            组织单元; 允许两种情况: 单个组织单元Id或FullId(String类型);
     *            多个组织单元Id或FullId(String类型)形成的List;
     * @param inFunction
     *            返回值必须在指定功能范围内, 允许三种情况: 空值, 表示不限制范围; 单个功能Id; 多个功能Id形成的List;
     * @return 获取指定组织单元的功能权限ID
     */
    @SuppressWarnings("unchecked")
    public List<String> findFunctionPermissions(Object org, Object inFunction) {
        List<String> result = new ArrayList<String>();
        List<String> orgFullIds = toFullIds(org);
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct pp.resource_id");
        sb.append("  from SA_OPOrg            o,");
        sb.append("       SA_OPAuthorize      a,");
        sb.append("       SA_OPRole           r,");
        sb.append("       SA_OPRolePermission rp,");
        sb.append("       SA_OPPermission     pp");
        sb.append(" where o.id = a.org_id");
        sb.append("   and a.role_id = r.id");
        sb.append("   and r.id = rp.role_id");
        sb.append("   and rp.permission_id = pp.id");
        sb.append("   and pp.resource_kind_id = 'fun'");
        sb.append("   and pp.node_kind_id in ('folder', 'fun')");
        sb.append("   and p.status = 1");
        sb.append("   and o.status = 1");
        sb.append("   and r.status = 1");
        sb.append("   and pp.status = 1");
        String fullIdCriteria = "";
        for (String fullId : orgFullIds) {
            if (Util.isNotEmptyString(fullId)) {
                if (Util.isEmptyString(fullIdCriteria)) {
                    fullIdCriteria = "'" + fullId + "' like concat(o.full_id, '%')";
                } else {
                    fullIdCriteria += " or '" + fullId + "' like concat(o.full_id, '%')";
                }
            }
        }
        if (Util.isEmptyString(fullIdCriteria)) {
            return result;
        }

        fullIdCriteria = String.format(" and (%s)", fullIdCriteria);

        String functionIdCriteria = "";
        if ((inFunction instanceof String)) {
            if (inFunction != null) {
                functionIdCriteria = String.format("'%s'", String.valueOf(inFunction));
            }
        } else {
            if ((inFunction instanceof List)) {
                List<String> functionList = (List<String>) inFunction;
                for (String item : functionList) {
                    if (item != null) {
                        functionIdCriteria += String.format("'%s',", item);
                    }
                }
                if (Util.isNotEmptyString(functionIdCriteria)) {
                    functionIdCriteria = functionIdCriteria.substring(0, functionIdCriteria.length() - 1);
                }
            }
        }
        if (Util.isNotEmptyString(functionIdCriteria)) {
            functionIdCriteria = String.format("  and pp.resource_id in (%s)", functionIdCriteria);
        }
        sb.append(fullIdCriteria);
        sb.append(functionIdCriteria);
        return this.sqlQuery.getJDBCDao().queryToList(sb.toString(), String.class);
    }

    /**
     * 根据项目id、基础职能查找人员成员
     * 
     * @param orgId
     *            项目组织id
     * @param baseFunctionTypeIds
     *            基础职能角色id列表,允许两种情况:基础职能角色Id(Long类型); 多个基础职能角色Id(Long类型)形成的List;
     * @return
     */
    public List<OrgUnit> findPersonMembersByProjectIdAndBaseFunctionTypeIds(String orgId, Object baseFunctionTypeIds) {
        if (StringUtil.isBlank(orgId)) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，orgId为空。");
        }
        if (baseFunctionTypeIds == null) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，baseFunctionTypeIds为空。");
        }

        String baseFunctionTypeIdCriteria = "";
        if ((baseFunctionTypeIds instanceof Long)) {
            Long baseFunctionTypeId = (Long) baseFunctionTypeIds;
            if (baseFunctionTypeId != null) {
                baseFunctionTypeIdCriteria = String.format("'%s'", String.valueOf(baseFunctionTypeId));
            }
        } else {
            if ((baseFunctionTypeIds instanceof List)) {
                @SuppressWarnings("unchecked")
                List<Long> baseFunctionTypeList = (List<Long>) baseFunctionTypeIds;
                for (Long item : baseFunctionTypeList) {
                    if (item != null) {
                        baseFunctionTypeIdCriteria += String.format("%s,", String.valueOf(item));
                    }
                }
                if (Util.isNotEmptyString(baseFunctionTypeIdCriteria)) {
                    baseFunctionTypeIdCriteria = baseFunctionTypeIdCriteria.substring(0, baseFunctionTypeIdCriteria.length() - 1);
                }
            }
        }
        if (Util.isEmptyString(baseFunctionTypeIdCriteria)) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，没有生成baseFunctionTypeId条件。");
        }

        baseFunctionTypeIdCriteria = String.format(" and b.id in (%s)", baseFunctionTypeIdCriteria);

        StringBuilder sb = new StringBuilder();

        sb.append("select p.full_id, p.full_name");
        sb.append("  from sa_oporg p, sa_oporg t, sa_opbasefunctiontype b");
        sb.append(" where t.center_id = ? and t.status = 1");
        sb.append("   and  t.parent_id = p.id and p.status = 1");
        sb.append("   and p.org_kind_id = 'psm'");
        sb.append("   and t.type_id = b.id ");

        String sql = String.format("%s %s", sb.toString(), baseFunctionTypeIdCriteria);

        List<OrgUnit> result = this.sqlQuery.getJDBCDao().queryToList(sql, OrgUnit.class, orgId);
        return distinctOrgUnitsByFullId(result);
    }

    /**
     * 根据项目ID、基础职能编码查找人员成员
     * 
     * @param orgId
     *            项目组织ID（组织节点为中心ID）
     * @param baseFunctionTypeCodes
     *            基础职能角色Code列表,允许两种情况:基础职能角色Code(String类型);
     *            多个基础职能角色Code(String类型)形成的List;
     * @return
     */
    public List<OrgUnit> findPersonMembersByProjectIdAndBaseFunctionTypeCodes(String orgId, Object baseFunctionTypeCodes) {
        if (StringUtil.isBlank(orgId)) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，orgId为空。");
        }
        if (baseFunctionTypeCodes == null) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，baseFunctionTypeIds为空。");
        }

        String baseFunctionTypeIdCriteria = "";
        if ((baseFunctionTypeCodes instanceof String)) {
            String baseFunctionTypeCode = (String) baseFunctionTypeCodes;
            if (baseFunctionTypeCode != null) {
                baseFunctionTypeIdCriteria = String.format("'%s'", String.valueOf(baseFunctionTypeCode));
            }
        } else {
            if ((baseFunctionTypeCodes instanceof List)) {
                @SuppressWarnings("unchecked")
                List<String> baseFunctionTypeList = (List<String>) baseFunctionTypeCodes;
                for (String item : baseFunctionTypeList) {
                    if (item != null) {
                        baseFunctionTypeIdCriteria += String.format("'%s',", String.valueOf(item));
                    }
                }
                if (Util.isNotEmptyString(baseFunctionTypeIdCriteria)) {
                    baseFunctionTypeIdCriteria = baseFunctionTypeIdCriteria.substring(0, baseFunctionTypeIdCriteria.length() - 1);
                }
            }
        }
        if (Util.isEmptyString(baseFunctionTypeIdCriteria)) {
            throw new ApplicationException("调用函数“findPersonMembersByProjectIdAndBaseFunctionTypeIds”出错，没有生成baseFunctionTypeId条件。");
        }

        baseFunctionTypeIdCriteria = String.format(" and b.code in (%s)", baseFunctionTypeIdCriteria);

        StringBuilder sb = new StringBuilder();

        sb.append("select p.full_id, p.full_name");
        sb.append("  from sa_oporg p, sa_oporg t, sa_opbasefunctiontype b");
        // sb.append(" where t.org_id = ? and t.status = 1");
        sb.append(" where t.center_id = ? and t.status = 1");
        sb.append("   and  t.parent_id = p.id and p.status = 1");
        sb.append("   and p.org_kind_id = 'psm'");
        sb.append("   and t.type_id = b.id ");

        String sql = String.format("%s %s", sb.toString(), baseFunctionTypeIdCriteria);

        List<OrgUnit> result = this.sqlQuery.getJDBCDao().queryToList(sql, OrgUnit.class, orgId);
        return distinctOrgUnitsByFullId(result);
    }

    /**
     * 人员是否拥有角色
     * 
     * @param personId
     *            人员ID
     * @param roleCode
     *            角色编码
     * @return
     */
    public Boolean isPersonHasRole(String personId, String roleCode) {
        if (StringUtil.isBlank(personId)) {
            throw new ApplicationException("调用函数“isPersonHasRole”出错，personId为空。");
        }
        if (roleCode == null) {
            throw new ApplicationException("调用函数“isPersonHasRole”出错，roleCode为空。");
        }

        String sql = " select t.full_id, full_name from sa_oporg t where t.person_id = ? and t.status = 1";
        List<OrgUnit> orgUnits = this.sqlQuery.getJDBCDao().queryToList(sql, OrgUnit.class, personId);

        if (orgUnits.size() == 0) {
            throw new ApplicationException(String.format("调用函数“isPersonHasRole”出错，未找到人员ID“%s”对应的组织。", personId));
        }

        String condition = null;

        List<String> params = new ArrayList<String>(orgUnits.size());

        for (OrgUnit orgUnit : orgUnits) {
            if (Util.isEmptyString(condition)) {
                condition = "? like concat(o.full_id, '%')";
            } else {
                condition += " or ? like concat(o.full_id, '%')";
            }
            params.add(orgUnit.getFullId());
        }

        params.add(roleCode);

        condition = String.format(" and ( %s )", condition);

        StringBuilder sb = new StringBuilder();

        sb.append("select count(*)");
        sb.append("  from sa_oporg o, sa_opauthorize a, sa_oprole t");
        sb.append(" where o.id = a.org_id");
        sb.append("   and a.role_id = t.id");
        sb.append("   and o.status = 1");
        sb.append("   and t.status = 1");
        sb.append(condition);
        sb.append("   and t.code = ?");

        int count = this.sqlQuery.getJDBCDao().queryToInt(sb.toString(), params.toArray());

        return count > 0;
    }

    /**
     * 获取指定组织单元的最近管理者
     * 
     * @param org
     *            组织单元， 单个组织单元Id或FullId(String类型);
     * @param manageType
     *            管理类型的Code,空表示所有管理类型
     * @return 最近管理者 只取一个人
     */
    public OrgUnit findNearestManagerOnePerson(Object org, String manageType) {
        List<OrgUnit> managers = findNearestManagers(org, manageType);
        if (managers.size() == 0) {
            return null;
        }
        OrgUnit orgUnit = null;
        for (OrgUnit ou : managers) {
            List<OrgUnit> personMembers = findPersonMembersInOrg(ou.getFullId(), true);
            if (personMembers.size() > 0) {
                orgUnit = personMembers.get(0);
                break;
            }
        }
        return orgUnit;
    }

    public void setOperatorExtensionProperties(Operator operator) {
        // 组织机构行政类别
        String orgAdminKind = getOrgAdminKindById(operator.getOrgId());
        operator.setOrgAdminKind(orgAdminKind);
        // 部门类别
        String deptKind = getOrgProperty(operator.getDeptId(), "deptKind");
        operator.setDeptKind(deptKind);
    }

    /**
     * 得到指定公司董事长
     * 
     * @param organId
     * @return 指定公司董事长
     */
    public List<OrgUnit> findOrganBoardChairmanByOrganId(String organId) {
        Org org = this.findOrgById(organId);
        if (org == null) {
            throw new ApplicationException(String.format("未找到“%s”对应的机构。", organId));
        }

        String deptCode = getCompanyLeaderDeptCode();
        String positionCode = getBoardChairmanPosCode();

        return findPersonMembers(org.getCode(), deptCode, positionCode);
    }

    /**
     * 当前机构董事长
     * 
     * @return 当前机构董事长
     */
    public List<OrgUnit> currentOrganBoardChairman() {
        TranslateOrg translateOrg = translateCurrentOrg();
        String currentOrgCode = translateOrg.getTranslateOrgKind() == TransformOrgKind.CURRENT_ORG ? this.currentOrgCode() : translateOrg.getOrg().getOrgCode();

        String deptCode = getCompanyLeaderDeptCode();
        String positionCode = getBoardChairmanPosCode();

        return findPersonMembers(currentOrgCode, deptCode, positionCode);
    }

    /**
     * 获取指定组织管理的租户
     * 
     * @param org
     *            组织单元; 允许两种情况
     *            <ul>
     *            <li>单个组织单元ID或FullID(String类型);
     *            <li>多个组织单元ID或FullID(String类型)形成的List;
     *            </ul>
     * @param manageType
     *            管理类型的code,空表示所有管理类型
     */
    public List<TenantDesc> findSubordinationTenants(Object org, String manageType) {
        List<OrgUnit> orgUnits = this.findSubordinationsByOrgManageType(org, manageType);

        if (orgUnits.size() == 0) {
            List<TenantDesc> result = new ArrayList<TenantDesc>();
            return result;
        }

        StringBuilder sb = new StringBuilder();
        List<String> params = new ArrayList<String>(orgUnits.size() + 1);
        sb.append("select id,code,name from SA_OPTenant where ");
        sb.append("  ? like concat(root_full_id, '%') ");
        params.add(ThreadLocalUtil.getOperator().getFullId());

        for (OrgUnit orgUnit : orgUnits) {
            sb.append(" or root_full_id like ?");
            params.add(orgUnit.getFullId() + "%");
        }
        return this.sqlQuery.getJDBCDao().queryToList(sb.toString(), TenantDesc.class, params.toArray());
    }
    
    public List<OrgUnit> findPersonMembersByOrgAndPositionTypeId(String orgId, String positionTypeId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select psm.full_id, psm.full_name");
        sb.append("  from sa_oporg pos, sa_oporgtype t, sa_oporg psm");
        sb.append(" where pos.type_id = t.id");
        sb.append("   and pos.org_id = ?");
        sb.append("   and t.id = ?");
        sb.append("   and pos.org_kind_id = 'pos'");
        sb.append("   and pos.status = 1");
        sb.append("   and psm.parent_id = pos.id");
        sb.append("   and psm.org_kind_id = 'psm'");
        sb.append("   and psm.status = 1");
        sb.append(" order by psm.full_sequence");
        return this.sqlQuery.getJDBCDao().queryToList(sb.toString(), OrgUnit.class, orgId, positionTypeId);
    }

}
