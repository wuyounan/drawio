package com.huigou.uasp.bmp.opm.domain.model.agent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.util.Assert;

import com.huigou.annotation.JsonIgnore;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.AbstractEntity;
import com.huigou.data.domain.model.Creator;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;

@Entity
@Table(name = "SA_OPAgent")
public class Agent extends AbstractEntity {

    private static final long serialVersionUID = 155975124805326820L;

    /**
     * 委托人ID
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Org client;

    /**
     * 代理人ID
     */
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Org agent;

    /**
     * 开始时间
     */
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    /**
     * 代理方式ID
     */
    @Column(name = "proc_agent_kind_id")
    private Integer procAgentKindId;

    @Column(name = "can_tran_agent")
    private Integer canTranAgent;

    @Embedded
    private Creator creator;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "agent_id")
    private List<AgentProc> details;

    @JsonIgnore
    public Org getClient() {
        return client;
    }

    public void setClient(Org client) {
        this.client = client;
    }

    @JsonIgnore
    public Org getAgent() {
        return agent;
    }

    public void setAgent(Org agent) {
        this.agent = agent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getProcAgentKindId() {
        return procAgentKindId;
    }

    public void setProcAgentKindId(Integer procAgentKindId) {
        this.procAgentKindId = procAgentKindId;
    }

    public Integer getCanTranAgent() {
        return canTranAgent;
    }

    public void setCanTranAgent(Integer canTranAgent) {
        this.canTranAgent = canTranAgent;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public List<AgentProc> getDetails() {
        return details;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setDetails(List<? extends AbstractEntity> details) {
        this.details = (List<AgentProc>) details;
    }

    @JsonIgnore
    public ProcAgentKind getProcAgentKind(){
        return ProcAgentKind.fromId(this.procAgentKindId);
    }
    
    /**
     * 包含指定的流程
     * 
     * @param ProcessDefinitionId
     *            流程定义id
     * @return
     */
    @JsonIgnore
    public boolean isDetailsContains(String procId) {
        boolean result = false;
        for (AgentProc agentProc : this.getDetails()) {
            if (agentProc.getProcId().equalsIgnoreCase(procId)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    @Override
    public void checkConstraints() {
        super.checkConstraints();

        Assert.notNull(this.getClient(), "委托人不能为空。");
        Assert.notNull(this.getAgent(), "代理人不能为空。");

        Assert.isTrue(!getClient().equals(this.getAgent()), "代理人和委托人不能相同。");

        Assert.isTrue(startDate.compareTo(endDate) < 0, "代理开始时间不能大于结束时间。");
        Assert.isTrue(this.getCreator().getCreatedDate().compareTo(startDate) < 0, "创建时间不能大于代理开始时间。");

        Assert.isTrue(this.canModify(), String.format("您不能编辑“%s”创建的代理。", this.getClient().getName()));
    }

    public boolean canModify() {
        Operator operator = ThreadLocalUtil.getOperator();
        return this.getClient().getId().contains(operator.getLoginUser().getId());
    }

    /**
     * 移去重复的代理流程
     */
    public List<AgentProc> removeDuplicateAgentProcs(List<AgentProc> agentProcs) {
        List<AgentProc> result = new ArrayList<AgentProc>(agentProcs.size());
        List<AgentProc> detials = new ArrayList<AgentProc>(agentProcs.size());
        detials.addAll(agentProcs);
        if (this.getDetails() != null) {
            detials.addAll(this.getDetails());
        }

        boolean found;
        for (int i = 0; i < agentProcs.size(); i++) {
            found = false;
            for (int j = i + 1; j < detials.size(); j++) {
                if (agentProcs.get(i).getProcId().equals(detials.get(j).getProcId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(agentProcs.get(i));
            }
        }

        return result;
    }

    /**
     * 流程代理方式
     * 
     * @author gongmm
     */
    public enum ProcAgentKind {
        ALL(1, "全部"), CUSTOM(2, "自定义");
        private final int id;

        private final String displayName;

        private ProcAgentKind(int id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public static ProcAgentKind fromId(int id) {
            switch (id) {
            case 1:
                return ALL;
            case 2:
                return CUSTOM;
            default:
                throw new ApplicationException(String.format("无效的流程代理方式“%s”。", new Object[] { Integer.valueOf(id) }));
            }
        }
    }
}
