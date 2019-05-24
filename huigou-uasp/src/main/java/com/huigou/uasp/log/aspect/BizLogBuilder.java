package com.huigou.uasp.log.aspect;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.huigou.cache.ApplicationSystemDesc;
import com.huigou.cache.SystemCache;
import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.context.RoleKind;
import com.huigou.context.SecurityGrade;
import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.model.LogStatus;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.StringUtil;

/**
 * 业务日志构造器
 * 
 * @author gongmm
 */
public class BizLogBuilder {

    /**
     * 获取摘要
     * 
     * @return
     */
    /*
     * private static String getDescription(LogInfo logInfo) {
     * Object object = ThreadLocalUtil.getVariable(BizLog.DESCRIPTION_KEY);
     * if (object != null) {
     * Gson gson = new Gson();
     * return gson.toJson(object);
     * } else {
     * return logInfo.description();
     * }
     * }
     */

    /**
     * 获取前象
     * 
     * @return
     */
    // private static String getBeforeImage() {
    // Object beforeImage = ThreadLocalUtil.getVariable(BizLog.BEFORE_IMAGE_KEY);
    // Gson gson = new Gson();
    // return gson.toJson(parseObject(beforeImage));
    // }

    /**
     * 获取后象
     * 
     * @return
     */
    // private static String getAfterImage() {
    // Object afterImage = ThreadLocalUtil.getVariable(BizLog.AFTER_IMAGE_KEY);
    // Gson gson = new Gson();
    // return gson.toJson(parseObject(afterImage));
    // }
    /**
     * 解析对象
     * 
     * @param object
     * @return
     */
    /*
    private static Map<String, String> parseObject(Object object) {
        Map<String, String> objMap = new HashMap<String, String>();
        if (object == null) {
            objMap.put("数据", "无");
            return objMap;
        }
        Class<?> clazz = object.getClass();
        if (clazz.isAnnotationPresent(LogPorperty.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(LogPorperty.class)) {
                    String fieldName = field.getName();
                    String methodName = "get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
                    try {
                        Method method = clazz.getMethod(methodName);
                        if (method.isAnnotationPresent(LogPorperty.class)) {
                            Object value = method.invoke(object);
                            if (value == null) {
                                value = "";
                            }
                            Gson gson = new Gson();
                            objMap.put(fieldName, gson.toJson(value));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return objMap;
    }*/

    /**
     * 获取角色信息
     * 
     * @param requiresPermissions
     *            权限
     * @param operator
     *            操作员
     * @param isEnableTspm
     *            是否启用三员
     * @return
     */
    private static RoleKind getRoleKind(RequiresPermissions requiresPermissions, Operator operator, boolean isEnableTspm) {
        RoleKind roleKind = RoleKind.COMMON;
        if (requiresPermissions == null) {
            return roleKind;
        }
        if (isEnableTspm) {
            String[] permissions = requiresPermissions.value();
            String roleKindIds = null;
            String currentRoleKindIds = null;
            for (String permission : permissions) {
                currentRoleKindIds = SystemCache.getThreeMemberPermission(permission);
                if (StringUtil.isBlank(currentRoleKindIds)) {
                    String[] splits = permission.split(":");
                    currentRoleKindIds = SystemCache.getThreeMemberPermission(String.format("%s:*", splits[0]));
                }
                if (StringUtil.isNotBlank(currentRoleKindIds)) {
                    if (roleKindIds != null) {
                        roleKindIds = String.format("%s,%s", roleKindIds, currentRoleKindIds);
                    } else {
                        roleKindIds = currentRoleKindIds;
                    }
                }
            }
            // 操作为三员权限
            if (StringUtil.isNotBlank(roleKindIds)) {
                roleKind = operator.getRoleKind();
            }
        }

        return roleKind;
    }

    public static void buildLogInfo(BizLog bizLog, BizLogDetail bizLogDetail, String className, Method method, Machine machine, boolean isEnableTspm) {
        Operator operator = ContextUtil.getOperator();
        LogInfo logInfo = method.getAnnotation(LogInfo.class);
        String code = logInfo.appCode();
        String ip = ContextUtil.getRequestIP();

        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        RoleKind roleKind = BizLogBuilder.getRoleKind(requiresPermissions, operator, isEnableTspm);

        SecurityGrade PersonSecurityGrade = operator.getLoginUser().getSecurityGrade();
        bizLog.setBeginDate(new Date());

        bizLog.setRoleKindId(roleKind.getId());
        bizLog.setRoleKindName(roleKind.getDisplayName());
        bizLog.setOrganName(operator.getOrgName());
        bizLog.setOrganId(operator.getOrgId());
        bizLog.setDeptName(operator.getDeptName());
        bizLog.setDeptId(operator.getDeptId());
        bizLog.setPositionId(operator.getPositionId());
        bizLog.setPositionName(operator.getPositionName());
        bizLog.setPersonMemberId(operator.getPersonMemberId());
        bizLog.setPersonMemberName(operator.getPersonMemberName());
        bizLog.setFullId(operator.getFullId());
        bizLog.setFullName(operator.getFullName());

        bizLog.setLogType(logInfo.logType().toString());
        if (StringUtil.isNotBlank(logInfo.operaionType().toString())) {
            bizLog.setOperateName(logInfo.operaionType().toString());
        } else {
            bizLog.setOperateName(logInfo.subType());
        }
        bizLog.setClassName(className);
        bizLog.setMethodName(method.getName());

        bizLog.setIp(ip);
        if (PersonSecurityGrade != null) {
            bizLog.setPersonSecurityLevelId(PersonSecurityGrade.getId());
            bizLog.setPersonSecurityLevelName(PersonSecurityGrade.getDisplayName());
        }
        // TODO
        bizLog.setResourceSecurityLevelId("");
        bizLog.setResourceSecurityLevelName("");

        if (machine != null) {
            bizLog.setMac(machine.getMac());
            SecurityGrade machineSecurityGrade = SecurityGrade.fromId(machine.getSecurityGrade());
            bizLog.setMachineSecurityLevelId(machineSecurityGrade.getId());
            bizLog.setMachineSecurityLevelName(machineSecurityGrade.getDisplayName());
        }

        Map<String, ApplicationSystemDesc> map = SystemCache.getApplicationSystem();
        ApplicationSystemDesc applicationSystemDesc = map.get(code);
        if (applicationSystemDesc == null) {
            applicationSystemDesc = map.get(getBizLogClassPrefix(className));
        }
        if (applicationSystemDesc != null) {
            bizLog.setAppId(applicationSystemDesc.getId());
            bizLog.setAppCode(applicationSystemDesc.getCode());
            bizLog.setAppName(applicationSystemDesc.getName());
        }

        bizLog.setStatusId(LogStatus.SUCCESS.getId());
        bizLog.setStatusName(LogStatus.SUCCESS.getName());

        bizLogDetail.setDescription(logInfo.description());
        bizLog.setDescription(bizLogDetail.getBriefDescription());

        /*
         * if (OperationType.UPDATE.equals(logInfo.operaionType())) {
         * bizLogDetail.setBeforeImage(BizLogBuilder.getBeforeImage());
         * bizLogDetail.setAfterImage(BizLogBuilder.getAfterImage());
         * }
         */
    }

    public static void buildLogInfo(BizLog bizLog, OperationType operationType, String className, String methodName) {
        Operator operator = ContextUtil.getOperator();
        String ip = ContextUtil.getRequestIP();

        bizLog.setBeginDate(new Date());

        bizLog.setOrganName(operator.getOrgName());
        bizLog.setOrganId(operator.getOrgId());
        bizLog.setDeptName(operator.getDeptName());
        bizLog.setDeptId(operator.getDeptId());
        bizLog.setPositionId(operator.getPositionId());
        bizLog.setPositionName(operator.getPositionName());
        bizLog.setPersonMemberId(operator.getPersonMemberId());
        bizLog.setPersonMemberName(operator.getPersonMemberName());
        bizLog.setFullId(operator.getFullId());
        bizLog.setFullName(operator.getFullName());

        bizLog.setLogType(LogType.BIZ.getId());
        bizLog.setOperateName(operationType.getId());
        bizLog.setClassName(className);
        bizLog.setMethodName(methodName);

        bizLog.setIp(ip);
        bizLog.setStatusId(LogStatus.SUCCESS.getId());
        bizLog.setStatusName(LogStatus.SUCCESS.getName());
        bizLog.setEndDate(new Date());
    }

    private static String getBizLogClassPrefix(String className) {
        String[] classPath = className.split("\\.");
        String result = "";
        if (classPath.length >= 2) {
            result = String.format("%s.%s.%s", classPath[0], classPath[1], classPath[2]);
        }
        return result;
    }

}
