package com.huigou.uasp.bmp.opm.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.RoleKind;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.application.AccessApplication;
import com.huigou.uasp.bmp.opm.application.OrgApplication;
import com.huigou.uasp.bmp.opm.application.TMAuthorizeApplication;
import com.huigou.uasp.bmp.opm.domain.model.access.Role;
import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.query.OrgQueryModel;
import com.huigou.uasp.bmp.opm.repository.org.RoleRepository;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.bmp.securitypolicy.application.ApplicationSystemApplication;
import com.huigou.uasp.log.domain.model.ApplicationSystem;
import com.huigou.util.SDO;

public class TMAuthorizeApplicationImpl extends BaseApplication implements TMAuthorizeApplication {

    // TODO move to application system
    private static String ALL_SYSTEM_ID = "*";

    private static String ALL_ORG_ID = "*";

    private static String ALL_SYSTEM_NAME = "所有系统";

    private TMAuthorizeRepository tmAuthorizeRepository;

    private OrgApplication orgApplication;

    private ApplicationSystemApplication applicationSystemApplication;

    private AccessApplication accessApplication;

    private RoleRepository roleRepository;

    private TmspmConifg tmspmConifg;

    public void setTMAuthorizeRepository(TMAuthorizeRepository tmAuthorizeRepository) {
        this.tmAuthorizeRepository = tmAuthorizeRepository;
    }

    public void setOrgApplication(OrgApplication orgApplication) {
        this.orgApplication = orgApplication;
    }

    public void setApplicationSystemApplication(ApplicationSystemApplication applicationSystemApplication) {
        this.applicationSystemApplication = applicationSystemApplication;
    }

    public void setAccessApplication(AccessApplication accessApplication) {
        this.accessApplication = accessApplication;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setTmspmConifg(TmspmConifg tmspmConifg) {
        this.tmspmConifg = tmspmConifg;
    }

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "tmAuthorize");
        return queryDescriptor.getSqlByName(name);
    }

    @Override
    public void saveTMAuthorizes(List<TMAuthorize> tmAuthorizes, String subordinationId, String systemId, String roleKindId) {
        Assert.notNull(tmAuthorizes, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));

        TMAuthorize authorize;

        String message, roleName, allocatedRoleName, systemName;
        Org org;

        for (TMAuthorize item : tmAuthorizes) {
            // 验证是否管理所有系统
            authorize = this.tmAuthorizeRepository.findOneTMAuthorize(item.getManagerId(), item.getSubordinationId(), ALL_SYSTEM_ID);
            if (authorize != null && !item.getSystemId().equals(authorize.getSystemId())) {
                org = this.orgApplication.loadOrg(authorize.getManagerId());
                ApplicationSystem applicationSystem = applicationSystemApplication.loadApplicationSystem(authorize.getSystemId());

                roleName = RoleKind.fromId(authorize.getRoleKindId()).getDisplayName();
                allocatedRoleName = RoleKind.fromId(item.getRoleKindId()).getDisplayName();
                systemName = applicationSystem == null ? ALL_SYSTEM_NAME : applicationSystem.getName();

                message = String.format("[%s]在[%s]系统中已经分配[%s]角色，不能再分配[%s]。", org.getName(), systemName, roleName, allocatedRoleName);
                throw new ApplicationException(message);
            }

            // 验证是否管理父级
            authorize = this.tmAuthorizeRepository.findOneTMAuthorize(item.getManagerId(), item.getSubordinationFullId(), item.getSystemId(),
                                                                      item.getRoleKindId());
            if (authorize != null && (item.getSystemId().equals(authorize.getSystemId()) && !authorize.getSubordinationId().equals(item.getSubordinationId()))) {

                org = this.orgApplication.loadOrg(authorize.getSubordinationId());
                Org personOrg = this.orgApplication.loadOrg(authorize.getManagerId());
                roleName = RoleKind.fromId(authorize.getRoleKindId()).getDisplayName();
                allocatedRoleName = RoleKind.fromId(item.getRoleKindId()).getDisplayName();

                message = String.format("[%s]在[%s]组织中已经分配[%s]角色，不能再分配[%s]。", personOrg.getName(), org.getName(), roleName, allocatedRoleName);
                throw new ApplicationException(message);
            }

            authorize = this.tmAuthorizeRepository.findByManagerIdNotRole(item.getManagerId(), item.getRoleKindId());
            if (authorize != null) {
                Org personOrg = this.orgApplication.loadOrg(authorize.getManagerId());
                roleName = RoleKind.fromId(authorize.getRoleKindId()).getDisplayName();
                allocatedRoleName = RoleKind.fromId(item.getRoleKindId()).getDisplayName();

                message = String.format("[%s]已经是[%s]角色，不能再分配[%s]角色。", personOrg.getName(), roleName, allocatedRoleName);
                throw new ApplicationException(message);
            }
        }

        // 先删除数据后再添加数据
        List<TMAuthorize> allocatedTMAuthorizes = this.tmAuthorizeRepository.findBySubordinationIdAndSystemIdAndRoleKindId(subordinationId, systemId,
                                                                                                                           roleKindId);
        this.tmAuthorizeRepository.delete(allocatedTMAuthorizes);
        this.tmAuthorizeRepository.save(tmAuthorizes);

        List<Role> roles = this.roleRepository.findByKindId(roleKindId);
        if (roles.size() == 0) {
            throw new ApplicationException(String.format("未找到角色类别ID“%s”对应的角色数据。", roleKindId));
        }

        List<String> roleIds = new ArrayList<String>();
        roleIds.add(roles.get(0).getId());

        for (TMAuthorize tmAuthorize : allocatedTMAuthorizes) {
            this.accessApplication.deallocateRoles(tmAuthorize.getManagerId(), roleIds);
        }
        for (TMAuthorize tmAuthorize : tmAuthorizes) {
            this.accessApplication.allocateRoles(tmAuthorize.getManagerId(), roleIds);
        }
    }

    @Override
    public Map<String, Object> queryTMAuthorizes(String subordinationId, String managerId) {
        Map<String, Object> result = null;
        Operator operator = ThreadLocalUtil.getOperator();

        if (operator.getRoleKind() == RoleKind.COMMON) {
            throw new ApplicationException("您没有权限操作此功能。");
        }

        if (operator.getRoleKind() == RoleKind.SUPER_ADMINISTRATOR) {
            result = this.querySuperAdminAuthorizes(managerId);
        } else {
            result = this.queryTMSystemRole(subordinationId, managerId);
        }

        return result;
    }

    @Override
    public Map<String, Object> queryDelegationOrgs(SDO sdo) {
        Map<String, Object> result;

        Operator operator = ThreadLocalUtil.getOperator();
        TMAuthorize tmAuthorize = null;
        if (operator.getRoleKind() == RoleKind.COMMON && tmspmConifg.isEnableTspm()) {
            // throw new ApplicationException("您没有权限操作此功能。");
        } else {
            // 管理所有组织的授权
            tmAuthorize = this.tmAuthorizeRepository.findBySubordinationIdAndManagerId(operator.getPersonMemberId(), ALL_ORG_ID);
        }
        if (operator.getRoleKind() == RoleKind.SUPER_ADMINISTRATOR || tmAuthorize != null || !tmspmConifg.isEnableTspm()) {
            OrgQueryModel orgQueryModel = new OrgQueryModel(sdo);
            result = orgApplication.queryOrgs(orgQueryModel);
        } else {
            OrgQueryModel orgQueryModel = this.buildTMOrgQueryModel(sdo);
            result = orgApplication.queryOrgs(orgQueryModel);
        }

        return result;
    }

    /**
     * 获取三员管理的组织查询模型
     *
     * @param sdo
     * @return
     */
    @SuppressWarnings("unchecked")
    private OrgQueryModel buildTMOrgQueryModel(SDO sdo) {
        // 当是三员查询的根的时候组装数据
        if (sdo.getString("parentId").equals("orgRoot")) {
            sdo.putProperty("customDefinedRoot", 1);
            Operator operator = ThreadLocalUtil.getOperator();
            Map<String, Object> reuslt = querySubordinations(operator.getPersonMemberId());
            sdo.putProperty("rootIds", "-1");
            List<Map<String, Object>> rowsData = (List<Map<String, Object>>) reuslt.get("Rows");
            if (rowsData.size() > 0) {
                StringBuilder orgIds = new StringBuilder();
                for (Map<String, Object> rowItem : rowsData) {
                    orgIds.append(rowItem.get("subordinationId").toString() + ",");
                }

                sdo.putProperty("rootIds", orgIds.length() > 0 ? orgIds.delete(orgIds.length() - 1, orgIds.length()).toString() : "-1");
            }
        }

        return new OrgQueryModel(sdo);
    }

    /**
     * 查询当前人员所管理的组织
     *
     * @param personMemberId
     *            人员成员ID
     * @return 三员授权对象
     */
    private Map<String, Object> querySubordinations(String personMemberId) {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(getQuerySqlByName("querySubordination"));
        queryModel.putParam("managerId", personMemberId);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    /**
     * 获取三员授权系统按角色查询
     *
     * @param parentSubordinationId
     *            父下属ID
     * @param subordinationId
     *            下属ID
     * @return
     */
    private Map<String, Object> queryTMSystemRole(String parentSubordinationId, String subordinationId) {
        List<Map<String, Object>> rowsData = new ArrayList<Map<String, Object>>();
        List<TMAuthorize> tmAuthorizes = null;
        Map<String, Object> result = new HashMap<String, Object>();

        Operator operator = ThreadLocalUtil.getOperator();

        TMAuthorize tmAuthorize = this.tmAuthorizeRepository.findOneTMAuthorize(operator.getPersonMemberId(), parentSubordinationId, ALL_SYSTEM_ID);

        // 虚拟一个所有系统
        if (tmAuthorize != null) {
            this.buildAuthorizations(rowsData, subordinationId, tmAuthorize.getRoleKindId(), ALL_SYSTEM_ID, ALL_SYSTEM_NAME);

            List<ApplicationSystem> applicationSystems = this.applicationSystemApplication.queryAll();
            for (ApplicationSystem applicationSystem : applicationSystems) {
                this.buildAuthorizations(rowsData, subordinationId, tmAuthorize.getRoleKindId(), applicationSystem.getId(), applicationSystem.getName());
            }
        } else {
            tmAuthorizes = this.tmAuthorizeRepository.findByManagerIdAndSubordinationId(operator.getPersonMemberId(), parentSubordinationId);
            for (TMAuthorize item : tmAuthorizes) {
                this.buildAuthorizations(rowsData, subordinationId, item.getRoleKindId(), item.getSystemId(),
                                         applicationSystemApplication.loadApplicationSystem(item.getSystemId()).getName());
            }
        }

        result.put("Rows", rowsData);

        return result;
    }

    /**
     * 获取超级管理员授权列表
     *
     * @param subordinationId
     *            下属ID
     * @return
     */
    private Map<String, Object> querySuperAdminAuthorizes(String subordinationId) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> rowsData = new ArrayList<Map<String, Object>>();
        this.buildAuthorizations(rowsData, subordinationId, RoleKind.SUPER_ADMINISTRATOR.getId(), ALL_SYSTEM_ID, ALL_SYSTEM_NAME);

        List<ApplicationSystem> applicationSystems = applicationSystemApplication.queryAll();
        for (ApplicationSystem applicationSystem : applicationSystems) {
            this.buildAuthorizations(rowsData, subordinationId, RoleKind.SUPER_ADMINISTRATOR.getId(), applicationSystem.getId(), applicationSystem.getName());
        }

        result.put("Rows", rowsData);

        return result;
    }

    /**
     * 组装系统角色数据
     *
     * @param list
     *            存放数据的集合
     * @param managedOrgId
     *            组织ID
     * @param roleKindId
     *            角色类别ID
     * @param id
     *            主键ID
     * @param name
     *            界面显示的ID
     */
    private void buildAuthorizations(List<Map<String, Object>> list, String managedOrgId, String roleKindId, String id, String name) {
        Map<String, Object> rowData = new HashMap<String, Object>();
        rowData.put("id", id);
        rowData.put("name", name);
        rowData.put("parentId", "");

        list.add(rowData);

        if (roleKindId.equals(RoleKind.ADMINISTRATOR.getId()) || roleKindId.equals(RoleKind.SUPER_ADMINISTRATOR.getId())) {
            list.add(buildAuthorizations(RoleKind.ADMINISTRATOR, id, managedOrgId, id));
        }
        if (roleKindId.equals(RoleKind.SECURITY_GUARD.getId()) || roleKindId.equals(RoleKind.SUPER_ADMINISTRATOR.getId())) {
            list.add(buildAuthorizations(RoleKind.SECURITY_GUARD, id, managedOrgId, id));
        }
        if (roleKindId.equals(RoleKind.AUDITOR.getId()) || roleKindId.equals(RoleKind.SUPER_ADMINISTRATOR.getId())) {
            list.add(buildAuthorizations(RoleKind.AUDITOR, id, managedOrgId, id));
        }
    }

    /**
     * 获取角色数据
     *
     * @param roleKind
     *            角色类别
     * @param parentId
     *            父ID
     * @param subordinationId
     *            下属ID
     * @param systemId
     *            系统ID
     * @return
     */
    private Map<String, Object> buildAuthorizations(RoleKind roleKind, String parentId, String subordinationId, String systemId) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", roleKind.getId());
        result.put("name", roleKind.getDisplayName());
        result.put("parentId", parentId);
        List<Map<String, Object>> selectedOrgs = this.getSelectedOrgs(systemId, roleKind.getId(), subordinationId);
        StringBuilder managerNames = new StringBuilder();
        for (Map<String, Object> selectedOrg : selectedOrgs) {
            managerNames.append(selectedOrg.get("name") + ";");
        }
        result.put("managerNames", managerNames.toString());
        result.put("managers", selectedOrgs);

        return result;
    }

    /**
     * 获取已经选择得到组织
     *
     * @param functionId
     *            系统ID
     * @param roleId
     *            角色ID
     * @param subordinationId
     *            下属ID
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getSelectedOrgs(String functionId, String roleId, String subordinationId) {
        QueryModel queryModel = new QueryModel();
        queryModel.setSql(getQuerySqlByName("queryManager"));
        queryModel.putParam("functionId", functionId);
        queryModel.putParam("roleId", roleId);
        queryModel.putParam("subordinationId", subordinationId);
        Map<String, Object> list = this.sqlExecutorDao.executeQuery(queryModel);
        List<Map<String, Object>> rows = (List<Map<String, Object>>) list.get("Rows");
        return rows;
    }
}