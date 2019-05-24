package com.huigou.context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 角色类别
 * 
 * @author gongmm
 */
public enum RoleKind {
    COMMON("common", "普通角色"),
    SUPER_ADMINISTRATOR("superAdministrator", "超级管理员"),
    ADMINISTRATOR("administrator", "管理员"),
    SECURITY_GUARD("securityGuard", "安全员"),
    AUDITOR("auditor", "审计员");

    private final String id;

    private final String displayName;

    private RoleKind(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static Map<String, String> getData() {
        Map<String, String> result = new LinkedHashMap<String, String>(5);
        for (RoleKind item : RoleKind.values()) {
            result.put(item.getId(), item.getDisplayName());
        }
        return result;
    }

    public static Map<String, String> getData(boolean isUseTspm) {
        Map<String, String> result;
        if (isUseTspm) {
            Operator operator = ThreadLocalUtil.getOperator();
            if (operator.getRoleKind() == RoleKind.SECURITY_GUARD) {
                // 安全员 TODO 是否可以查看SECURITY_GUARD
                result = new LinkedHashMap<String, String>(1);
                result.put(COMMON.getId(), COMMON.getDisplayName());
                return result;
            } else if (operator.getRoleKind() == RoleKind.SUPER_ADMINISTRATOR) {
                // 超级管理员只能三员
                result = new LinkedHashMap<String, String>(3);

                result.put(ADMINISTRATOR.getId(), ADMINISTRATOR.getDisplayName());
                result.put(SECURITY_GUARD.getId(), SECURITY_GUARD.getDisplayName());
                result.put(AUDITOR.getId(), AUDITOR.getDisplayName());
            } else {
                result = new LinkedHashMap<String, String>(0);
            }
        } else {
            result = new LinkedHashMap<String, String>(1);
            result.put(COMMON.getId(), COMMON.getDisplayName());
        }

        return result;
    }

    public static Map<String, String> getDataForErrorLog(boolean enableTspm) {
        Map<String, String> result;
        if (enableTspm) {
            Operator operator = ThreadLocalUtil.getOperator();
            if (operator.getRoleKind() == ADMINISTRATOR) {
                result = new LinkedHashMap<String, String>(1);
                result.put(COMMON.getId(), COMMON.getDisplayName());
                result.put(ADMINISTRATOR.getId(), ADMINISTRATOR.getDisplayName());
                result.put(SECURITY_GUARD.getId(), SECURITY_GUARD.getDisplayName());
                result.put(AUDITOR.getId(), AUDITOR.getDisplayName());
            } else {
                result = new LinkedHashMap<String, String>(0);
            }
        } else {
            result = new LinkedHashMap<String, String>(1);
            result.put(COMMON.getId(), COMMON.getDisplayName());
        }
        return result;
    }

    public static Map<String, String> getDataForOperationLog(boolean enableTspm) {
        Map<String, String> result;
        if (enableTspm) {
            // 管理员：查询错误日志、登录日志；
            // 安全员：系统管理员日志、登录日志；
            // 审计员：查询管理员、安全员、用户操作日志
            Operator operator = ThreadLocalUtil.getOperator();
            switch (operator.getRoleKind()) {
            case SECURITY_GUARD:
                result = new LinkedHashMap<String, String>(1);
                result.put(ADMINISTRATOR.getId(), ADMINISTRATOR.getDisplayName());
                break;
            case AUDITOR:
                result = new LinkedHashMap<String, String>(1);
                result.put(COMMON.getId(), COMMON.getDisplayName());
                result.put(ADMINISTRATOR.getId(), ADMINISTRATOR.getDisplayName());
                result.put(SECURITY_GUARD.getId(), SECURITY_GUARD.getDisplayName());
                break;
            default:
                result = new LinkedHashMap<String, String>(0);
            }
        } else {
            result = new LinkedHashMap<String, String>(1);
            result.put(COMMON.getId(), COMMON.getDisplayName());
        }

        return result;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RoleKind fromId(String id) {
        for (RoleKind item : RoleKind.values()) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        throw new IllegalArgumentException(String.format("无效的角色类型“%s”。", id));
    }

    public static boolean isTspm(String kindId) {
        return !COMMON.getId().equals(kindId);
    }
}
