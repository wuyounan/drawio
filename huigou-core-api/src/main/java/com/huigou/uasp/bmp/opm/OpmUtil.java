package com.huigou.uasp.bmp.opm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.huigou.context.Operator;
import com.huigou.context.OrgNode;
import com.huigou.context.PersonMember;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.uasp.bmp.opm.application.AuthenticationApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeData;
import com.huigou.uasp.bmp.opm.domain.model.org.OrgNodeKind;
import com.huigou.uasp.bmp.opm.domain.model.org.Person;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.StringUtil;
import com.huigou.util.Util;

public class OpmUtil {

    /**
     * 业务操作员字段名称
     */
    public final static String BIZ_OPERATOR_PSM_FIELD_NAME = "_BizOprPsm_";

    public final static String BIZ_OPERATOR_KEY = "_BizOpr_";

    /**
     * 业务管理组织字段名称
     */
    public final static String BIZ_MANAGE_ORG_ID_FIELD_NAME = "manageOrganId";

    /**
     * 得到组织机构根
     * 
     * @return
     */
    public static String getOrgRoot() {
        return OrgConstants.ORG_ROOT_ID;
    }

    /**
     * 返回第一个非空的参数
     * 
     * @param objs
     *            对象数组
     * @return
     */
    public static Object coalesce(Object[] objs) {
        for (Object item : objs) {
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    /**
     * 生成文件全名称
     * 
     * @param path
     *            路径
     * @param fileName
     *            文件名
     * @param fileKind
     *            文件类型
     * @return 文件名
     */
    public static String createFileFullName(String path, String fileName, String fileKind) {
        return CommonUtil.createFileFullName(path, fileName, fileKind);
    }

    public static void checkVersion(Long version, Long dbVersion, String type, String id) {
        Util.check((dbVersion == null) || (dbVersion.equals(Long.valueOf(version))), "%s失败，“%s”的数据已经被其他人修改，请刷新后重新操作！", new Object[] { type, id });
    }

    /**
     * 生成组织扩展属性
     * 
     * @param fullId
     *            Id全路径
     * @param fullOrgKindId
     *            orgKindId全路径
     * @param fullCode
     *            编码全路径
     * @param fullName
     *            名称全路径
     * @param orgInfo
     */
    public static void buildOrgIdCodeNameExtInfo(String fullId, String fullOrgKindId, String fullCode, String fullName, Map<String, Object> orgInfo) {
        Util.check(Util.isNotEmptyString(fullId), "调用“buildOrgIdCodeNameExtInfo”出错，“fullId”不能为空。");
        Util.check(Util.isNotEmptyString(fullOrgKindId), "调用“buildOrgIdCodeNameExtInfo”出错，“fullOrgKindId”不能为空。");
        Util.check(Util.isNotEmptyString(fullCode), "调用“buildOrgIdCodeNameExtInfo”出错，“fullCode”不能为空。");
        Util.check(Util.isNotEmptyString(fullName), "调用“buildOrgIdCodeNameExtInfo”出错，“fullName”不能为空。");

        // 组织机构路径： /*.ogn/*.ctr/*.ctr/*.dpt/*.pos/*.psm
        // 项目组织路径：/*.ogn/*.fld/*.fld/*.prj/*.grp/*.psm/*.fun
        // 部门和中心最近原则
        String[] orgKindIds = fullOrgKindId.substring(1).split("/");
        String[] orgIds = fullId.substring(1).split("/");
        String[] orgCodes = fullCode.substring(1).split("/");
        String[] orgNames = fullName.substring(1).split("/");

        Util.check(orgIds.length == orgNames.length, "调用“buildOrgIdNameExtInfo”出错，“fullId”与“fullName”长度不等。");
        int deptLevel = 0;
        int ctrLevel = 0;
        int orgLevel = 0;

        // 是否项目组织
        boolean isProjectOrg = fullId.contains(".prj");

        if (isProjectOrg) {
            for (int i = orgIds.length - 1; i >= 0; i--) {
                if (orgIds[i].endsWith(OrgNode.ORGAN)) { // 机构（公司）
                    orgLevel++;
                    if (orgLevel == 1) {
                        orgInfo.put("orgId", orgIds[i].replace("." + OrgNode.ORGAN, ""));
                        orgInfo.put("orgCode", orgCodes[i]);
                        orgInfo.put("orgName", orgNames[i]);
                    }
                } else if (orgIds[i].endsWith(OrgNode.PROJECT)) {// 项目为中心
                    orgInfo.put("centerId", orgIds[i].replace("." + OrgNode.PROJECT, ""));
                    orgInfo.put("centerCode", orgCodes[i]);
                    orgInfo.put("centerName", orgNames[i]);
                    if (!fullId.contains("." + OrgNode.GROUP)) {
                        orgInfo.put("deptId", orgInfo.get("centerId"));
                        orgInfo.put("deptCode", orgInfo.get("centerCode"));
                        orgInfo.put("deptName", orgInfo.get("centerName"));
                    }
                } else if (orgIds[i].endsWith(OrgNode.GROUP)) {// 职能部门为部门
                    orgInfo.put("deptId", orgIds[i].replace("." + OrgNode.GROUP, ""));
                    orgInfo.put("deptCode", orgCodes[i]);
                    orgInfo.put("deptName", orgNames[i]);
                } else if (orgIds[i].endsWith(OrgNode.POSITION)) { // 岗位
                    orgInfo.put("positionId", orgIds[i].replace("." + OrgNode.POSITION, ""));
                    orgInfo.put("positionCode", orgCodes[i]);
                    orgInfo.put("positionName", orgNames[i]);
                } else if (orgIds[i].endsWith(OrgNode.PERSONMEMBER)) {// 人员成员
                    orgInfo.put("personMemberId", orgIds[i].replace("." + OrgNode.PERSONMEMBER, ""));
                    orgInfo.put("personMemberCode", orgCodes[i]);
                    orgInfo.put("personMemberName", orgNames[i]);
                }
            }
            return;
        }

        for (int i = orgIds.length - 1; i >= 0; i--) {
            if (orgIds[i].endsWith(OrgNode.ORGAN)) {
                orgLevel++;
                if (orgLevel == 1) {
                    orgInfo.put("orgId", orgIds[i].replace("." + OrgNode.ORGAN, ""));
                    orgInfo.put("orgCode", orgCodes[i]);
                    orgInfo.put("orgName", orgNames[i]);
                }
            } else if (orgKindIds[i].equalsIgnoreCase(OrgNode.CENTER)) {
                ctrLevel++;
                if (ctrLevel == 1) {
                    // 最近中心
                    if (deptLevel == 0) {
                        // 末级为中心
                        orgInfo.put("deptId", orgIds[i].replace("." + OrgNode.DEPT, ""));
                        orgInfo.put("deptCode", orgCodes[i]);
                        orgInfo.put("deptName", orgNames[i]);
                    }
                    orgInfo.put("centerId", orgIds[i].replace("." + OrgNode.DEPT, ""));
                    orgInfo.put("centerCode", orgCodes[i]);
                    orgInfo.put("centerName", orgNames[i]);
                }
            } else if (orgKindIds[i].equalsIgnoreCase(OrgNode.DEPT)) {
                deptLevel++;
                if (deptLevel == 1) {
                    // 末级为部门
                    if (ctrLevel == 0) {
                        orgInfo.put("centerId", orgIds[i].replace("." + OrgNode.DEPT, ""));
                        orgInfo.put("centerCode", orgCodes[i]);
                        orgInfo.put("centerName", orgNames[i]);

                        orgInfo.put("deptId", orgIds[i].replace("." + OrgNode.DEPT, ""));
                        orgInfo.put("deptCode", orgCodes[i]);
                        orgInfo.put("deptName", orgNames[i]);
                    }
                }
            } else if (orgIds[i].endsWith(OrgNode.POSITION)) {
                orgInfo.put("positionId", orgIds[i].replace("." + OrgNode.POSITION, ""));
                orgInfo.put("positionCode", orgCodes[i]);
                orgInfo.put("positionName", orgNames[i]);
            } else if (orgIds[i].endsWith("." + OrgNode.PERSONMEMBER)) {
                orgInfo.put("personMemberId", orgIds[i].replace("." + OrgNode.PERSONMEMBER, ""));
                orgInfo.put("personMemberCode", orgCodes[i]);
                orgInfo.put("personMemberName", orgNames[i]);
            }
        }
    }

    public static OrgNodeData buildOrgNodeData(com.huigou.uasp.bmp.opm.domain.model.org.Org org) {
        Util.check(Util.isNotEmptyString(org.getFullId()), "调用“buildOrgIdCodeNameExtInfo”出错，“fullId”不能为空。");
        Util.check(Util.isNotEmptyString(org.getFullOrgKindId()), "调用“buildOrgIdCodeNameExtInfo”出错，“fullOrgKindId”不能为空。");
        Util.check(Util.isNotEmptyString(org.getFullCode()), "调用“buildOrgIdCodeNameExtInfo”出错，“fullCode”不能为空。");
        Util.check(Util.isNotEmptyString(org.getName()), "调用“buildOrgIdCodeNameExtInfo”出错，“fullName”不能为空。");
        // 组织机构路径： /.ogn/.ctr/.ctr/.dpt/.pos/.psm
        // 项目组织路径：/.ogn/.fld/.fld/.prj/.grp/.psm/.fun
        String[] orgIds = org.getFullId().substring(1).split("/");
        String[] orgCodes = org.getFullCode().substring(1).split("/");
        String[] orgNames = org.getFullName().substring(1).split("/");
        String[] orgKindIds = org.getFullOrgKindId().substring(1).split("/");
        Util.check(orgIds.length == orgCodes.length && orgIds.length == orgNames.length && orgIds.length == orgKindIds.length,
                   "调用“buildOrgIdNameExtInfo”出错，参数长度不等。");
        int deptLevel = 0;
        int orgLevel = 0;

        OrgNodeData orgNodeData = new OrgNodeData();
        for (int i = orgIds.length - 1; i >= 0; i--) {
            if (orgIds[i].endsWith(OrgNode.ORGAN)) {
                orgLevel++;
                if (orgLevel == 1) {
                    orgNodeData.setOrgId(orgIds[i].replace("." + OrgNode.ORGAN, ""));
                    orgNodeData.setOrgCode(orgCodes[i]);
                    orgNodeData.setOrgName(orgNames[i]);
                }
            } else if (orgKindIds[i].equalsIgnoreCase(OrgNode.DEPT)) {
                deptLevel++;
                if (deptLevel == 1) {
                    orgNodeData.setDeptId(orgIds[i].replace("." + OrgNode.DEPT, ""));
                    orgNodeData.setDeptCode(orgCodes[i]);
                    orgNodeData.setDeptName(orgNames[i]);
                }
            } else if (orgIds[i].endsWith(OrgNode.POSITION)) {
                orgNodeData.setPositionId(orgIds[i].replace("." + OrgNode.POSITION, ""));
                orgNodeData.setPositionCode(orgCodes[i]);
                orgNodeData.setPositionName(orgNames[i]);
            }
        }
        return orgNodeData;
    }

    public static void buildOrgIdNameExtInfo(String fullId, String fullName, Map<String, Object> orgInfo) {
        Util.check(Util.isNotEmptyString(fullId), "调用“buildOrgIdNameExtInfo”出错，“fullId”不能为空。");
        Util.check(Util.isNotEmptyString(fullName), "调用“buildOrgIdNameExtInfo”出错，“fullName”不能为空。");

        String[] orgIds = fullId.substring(1).split("/");
        String[] orgNames = fullName.substring(1).split("/");

        Util.check(orgIds.length == orgNames.length, "调用“buildOrgIdNameExtInfo”出错，“fullId”与“fullName”长度不等。");

        for (int i = 0; i < orgIds.length; i++) {
            if (orgIds[i].endsWith(OrgNode.ORGAN)) {
                orgInfo.put("orgId", orgIds[i].replace("." + OrgNode.ORGAN, ""));
                orgInfo.put("orgName", orgNames[i]);
            } else if (orgIds[i].endsWith(OrgNode.DEPT)) {
                orgInfo.put("deptId", orgIds[i].replace("." + OrgNode.DEPT, ""));
                orgInfo.put("deptName", orgNames[i]);
            } else if (orgIds[i].endsWith(OrgNode.POSITION)) {
                orgInfo.put("posId", orgIds[i].replace("." + OrgNode.POSITION, ""));
                orgInfo.put("posName", orgNames[i]);
            } else if (orgIds[i].endsWith("." + OrgNode.PERSONMEMBER)) {
                orgInfo.put("psmId", orgIds[i].replace("." + OrgNode.PERSONMEMBER, ""));
                orgInfo.put("psmName", orgNames[i]);
            }
        }
    }

    /**
     * 格式化人员成员ID
     * 
     * @param personId
     * @param parentOrgId
     * @return
     */
    public static String formatPersonMemberId(String personId, String parentOrgId) {
        return personId + "@" + parentOrgId;
    }

    /**
     * 从人员ID全路径中得到人员成员ID
     * 
     * @param fullId
     *            ID全路径
     * @return
     */
    public static String getPersonMemberIdFromFullId(String fullId) {
        String[] split = fullId.substring(1).split("/");
        for (int i = split.length - 1; i >= 0; i--) {
            if (split[i].endsWith(OrgNode.PERSONMEMBER)) {
                return split[i].replace("." + OrgNode.PERSONMEMBER, "");
            }
        }
        return null;
    }

    /**
     * 从人员ID全路径中得到人员ID
     * 
     * @param fullId
     *            ID全路径
     * @return
     */
    public static String getPersonIdFromFullId(String fullId) {
        Util.check(Util.isNotEmptyString(fullId), "调用“getPersonIdFromFullId”出错，“fullId”不能为空！", new Object[0]);

        String personMemberId = getPersonMemberIdFromFullId(fullId);
        return OpmUtil.getPersonIdFromPersonMemberId(personMemberId);
    }

    /**
     * 从人员成员中得到人员ID
     * 
     * @param personMemberId
     *            人员成员ID
     * @return 人员ID
     */
    public static String getPersonIdFromPersonMemberId(String personMemberId) {
        Util.check(Util.isNotEmptyString(personMemberId), "调用“getPersonIdFromPersonMemberId”出错，“personMemberId”不能为空！", new Object[0]);
        if (personMemberId.indexOf("@") > -1) {
            String[] split = personMemberId.split("@");
            return split[0];
        } else {
            return personMemberId;
        }
    }

    /**
     * 得到业务操作员
     * 
     * @return
     */
    public static Operator getBizOperator() {
        String personMemberId = ThreadLocalUtil.getVariable(OpmUtil.BIZ_OPERATOR_PSM_FIELD_NAME, String.class);
        boolean isChangeOperator = !StringUtil.isBlank(personMemberId);

        Operator operator;
        if (isChangeOperator) {
            operator = ThreadLocalUtil.getVariable(BIZ_OPERATOR_KEY, Operator.class);
            if (operator == null) {
                AuthenticationApplication authenticationApplication = ApplicationContextWrapper.getBean("authenticationApplication",
                                                                                                        AuthenticationApplication.class);
                operator = authenticationApplication.createOperatorByPersonMemberId(personMemberId);
                Util.check(operator != null, "没有找到psmIdOrFullId对应的操作员。");
                ThreadLocalUtil.putVariable(BIZ_OPERATOR_KEY, operator);
            }
        } else {
            operator = ThreadLocalUtil.getVariable(ThreadLocalUtil.PROC_APPROVAL_OPERATOR_KEY, Operator.class);
            if (operator == null) {
                operator = ThreadLocalUtil.getVariable(ThreadLocalUtil.OPERATOR_KEY, Operator.class);
            }
        }

        return operator;
    }

    /**
     * 构建登录结果数据
     */
    public static Map<String, Object> buildLoginResultData(Org personMember) {
        Map<String, Object> result = new HashMap<String, Object>(14);
        result.put("id", personMember.getId());
        result.put("organId", personMember.getOrgId());
        result.put("organName", personMember.getOrgName());
        result.put("deptId", personMember.getDeptId());
        result.put("deptName", personMember.getDeptName());
        result.put("mainOrgId", personMember.getPositionId());
        result.put("fullId", personMember.getFullId());
        result.put("fullCode", personMember.getFullCode());
        result.put("fullName", personMember.getFullName());
        result.put("personId", personMember.getPerson().getId());
        result.put("personCode", personMember.getPerson().getCode());
        result.put("personName", personMember.getPerson().getName());
        result.put("loginName", personMember.getPerson().getLoginName());
        result.put("tenantId", personMember.getPerson().getTenantId());
        result.put("securityGrade", personMember.getPerson().getSecurityGrade());

        return result;
    }

    public static PersonMember toPersonMember(Org personMemberEntity) {
        Assert.notNull(personMemberEntity, "参数personMemberEntity不能为空。");
        Assert.isTrue(personMemberEntity.getOrgKind() == OrgNodeKind.PSM, "参数personMemberEntity必须为人员成员。");
        Person person = personMemberEntity.getPerson();
        PersonMember result = new PersonMember();
        ClassHelper.copyProperties(personMemberEntity, result);
        result.setPersonId(person.getId());
        result.setCode(person.getCode());
        result.setName(person.getName());
        result.setSecurityGrade(person.getSecurityGrade());
        result.setLoginName(person.getLoginName());
        return result;
    }

    public static String getDefaultPassword() {
        return "000000";
    }

    /**
     * 得到默认加密密码
     * 
     * @return
     */
    public static String getDefaultEncryptPassword() {
        return Util.MD5(getDefaultPassword());
    }

}