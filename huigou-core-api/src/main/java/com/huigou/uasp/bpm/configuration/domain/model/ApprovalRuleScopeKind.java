package com.huigou.uasp.bpm.configuration.domain.model;

/**
 * 审批规则范围类别
 * 
 * @author gongmm
 */
public enum ApprovalRuleScopeKind {
    CURRENT_ORG(1, "当前组织"), CUSTOM_ORG(2, "自定义组织");

    private final Integer id;

    private final String displayName;

    private ApprovalRuleScopeKind(Integer id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public Integer getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static ApprovalRuleScopeKind fromId(Integer id) {
        switch (id) {
        case 1:
            return CURRENT_ORG;
        case 2:
            return CUSTOM_ORG;
        default:
            throw new RuntimeException(String.format("无效的审批规则范围类别“%s”。", id));
        }
    }
}
