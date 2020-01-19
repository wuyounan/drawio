package com.huigou.uasp.bpm.configuration.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.listener.CreatorAndModifierListener;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.Modifier;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.model.TreeEntity;

@Entity
@Table(name = "WF_ApprovalRule")
@EntityListeners({CreatorAndModifierListener.class})
public class ApprovalRule extends TreeEntity {

    public static final String ROOT_ID = "1";

    private static final long serialVersionUID = 6869029638335427649L;

    @Column(name = "proc_id")
    private String procId;

    @Column(name = "proc_name")
    private String procName;

    @Column(name = "proc_unit_id")
    private String procUnitId;

    @Column(name = "proc_unit_name")
    private String procUnitName;

    /**
     * 节点类型
     */
    @Column(name = "node_kind_id")
    private Integer nodeKindId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拥有组织
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 适用范围ID
     */
    @Column(name = "scope_kind_id")
    private String scopeKindId;

    @Embedded
    private Creator creator;

    @Embedded
    private Modifier modifier;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_id")
    @OrderBy("sequence")
    private List<ApprovalRuleElement> approvalRuleElements;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_id")
    @OrderBy("groupId")
    private List<ApprovalRuleHandler> approvalRuleHandlers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_id")
    private List<ApprovalRuleHandlerGroup> approvalRuleHandlerGroups;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "approval_rule_id")
    private List<ApprovalRuleScope> approvalRuleScopes;

    @Transient
    private List<ApprovalRuleScope> inputApprovalRuleScopes_;

    @Transient
    private List<ApprovalRuleElement> inputApprovalRuleElements_;

    @Transient
    private List<ApprovalRuleHandler> inputApprovalRulHandlers_;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcUnitId() {
        return procUnitId;
    }

    public void setProcUnitId(String procUnitId) {
        this.procUnitId = procUnitId;
    }

    public String getProcUnitName() {
        return procUnitName;
    }

    public void setProcUnitName(String procUnitName) {
        this.procUnitName = procUnitName;
    }

    public Integer getNodeKindId() {
        return nodeKindId;
    }

    public void setNodeKindId(Integer nodeKindId) {
        this.nodeKindId = nodeKindId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getScopeKindId() {
        return scopeKindId;
    }

    public void setScopeKindId(String scopeKindId) {
        this.scopeKindId = scopeKindId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    @JsonIgnore
    public List<ApprovalRuleElement> getApprovalRuleElements() {
        return approvalRuleElements;
    }

    public void setApprovalRuleElements(List<ApprovalRuleElement> approvalRuleElements) {
        this.approvalRuleElements = approvalRuleElements;
    }

    @JsonIgnore
    public List<ApprovalRuleHandler> getApprovalRuleHandlers() {
        return approvalRuleHandlers;
    }

    public void setApprovalRuleHandlers(List<ApprovalRuleHandler> approvalRuleHandlers) {
        this.approvalRuleHandlers = approvalRuleHandlers;
    }

    @JsonIgnore
    public List<ApprovalRuleHandlerGroup> getApprovalRuleHandlerGroups() {
        return approvalRuleHandlerGroups;
    }

    public void setApprovalRuleHandlerGroups(List<ApprovalRuleHandlerGroup> approvalRuleHandlerGroups) {
        this.approvalRuleHandlerGroups = approvalRuleHandlerGroups;
    }

    public List<ApprovalRuleScope> getApprovalRuleScopes() {
        return approvalRuleScopes;
    }

    public void setApprovalRuleScopes(List<ApprovalRuleScope> approvalRuleScopes) {
        this.approvalRuleScopes = approvalRuleScopes;
    }

    @JsonIgnore
    public List<ApprovalRuleScope> getInputApprovalRuleScopes_() {
        return inputApprovalRuleScopes_;
    }

    public void setInputApprovalRuleScopes_(List<ApprovalRuleScope> inputApprovalRuleScopes_) {
        this.inputApprovalRuleScopes_ = inputApprovalRuleScopes_;
    }

    @JsonIgnore
    public List<ApprovalRuleElement> getInputApprovalRuleElements_() {
        return inputApprovalRuleElements_;
    }

    public void setInputApprovalRuleElements_(List<ApprovalRuleElement> inputApprovalRuleElements_) {
        this.inputApprovalRuleElements_ = inputApprovalRuleElements_;
    }

    @JsonIgnore
    public List<ApprovalRuleHandler> getInputApprovalRulHandlers_() {
        return inputApprovalRulHandlers_;
    }

    public void setInputApprovalRulHandlers_(List<ApprovalRuleHandler> inputApprovalRulHandlers_) {
        this.inputApprovalRulHandlers_ = inputApprovalRulHandlers_;
    }

    public ApprovalRuleHandler findApprovalRuleHandler(String approvalRuleHandlerId) {
        Assert.hasText(approvalRuleHandlerId, "参数approvalRuleHandlerId不能为空。");
        for (ApprovalRuleHandler item : this.getApprovalRuleHandlers()) {
            if (item.getId().equals(approvalRuleHandlerId)) {
                return item;
            }
        }
        return null;
    }

    public void addApprovalRuleHandlers(List<ApprovalRuleHandler> entities) {
        if (this.isNew()) {
            for (ApprovalRuleHandler entity : entities) {
                entity.setId(null);
            }
            this.setApprovalRuleHandlers(entities);
            return;
        }

        int index;

        for (ApprovalRuleHandler entity : entities) {
            index = this.getApprovalRuleHandlers().indexOf(entity);
            if (index == -1) {
                this.getApprovalRuleHandlers().add(entity);
            } else {
                entity.setUIElmentPermissions(this.getApprovalRuleHandlers().get(index).getUIElmentPermissions());
                entity.setAssists(this.getApprovalRuleHandlers().get(index).getAssists());
                this.getApprovalRuleHandlers().set(index, entity);
            }
        }
    }

    public ApprovalRuleHandlerGroup findApprovalRuleHandlerGroup(Integer groupId) {
        List<ApprovalRuleHandlerGroup> approvalRuleHandlerGroups = this.getApprovalRuleHandlerGroups();
        if (approvalRuleHandlerGroups != null) {
            for (ApprovalRuleHandlerGroup group : approvalRuleHandlerGroups) {
                if (Objects.equals(group.getGroupId(), groupId)) {
                    return group;
                }
            }
        }
        return null;
    }

    public void checkConstraints(ApprovalRule other) {
        if (other != null) {
            Assert.isTrue(!this.getName().equalsIgnoreCase(other.getName()), MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));
        }
    }

    @SuppressWarnings("unchecked")
    public void buildRuleScopes() {
        List<ApprovalRuleScope> ruleScopes = this.getApprovalRuleScopes();
        List<ApprovalRuleScope> inputDetails = this.getInputApprovalRuleScopes_();
        ruleScopes = (List<ApprovalRuleScope>) this.buildDetails(ruleScopes, inputDetails);
        this.setApprovalRuleScopes(ruleScopes);
    }

    @SuppressWarnings("unchecked")
    public void buildRuleElements() {
        List<ApprovalRuleElement> ruleElements = this.getApprovalRuleElements();
        List<ApprovalRuleElement> inputDetails = this.getInputApprovalRuleElements_();
        ruleElements = (List<ApprovalRuleElement>) this.buildDetails(ruleElements, inputDetails);
        this.setApprovalRuleElements(ruleElements);
    }

    public enum NodeKind {
        ROOT(0, "根节点"), CATEGORY(1, "分类"), RULE(2, "规则");
        private final int id;

        private final String displayName;

        private NodeKind(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static Map<Integer, String> getData() {
            Map<Integer, String> result = new HashMap<Integer, String>();
            for (NodeKind item : NodeKind.values()) {
                if (item != ROOT) result.put(item.getId(), item.getDisplayName());
            }
            return result;
        }

        public static boolean isCategory(int id) {
            return CATEGORY.getId() == id;
        }
    }
}
