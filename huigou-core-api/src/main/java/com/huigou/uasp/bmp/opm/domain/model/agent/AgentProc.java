package com.huigou.uasp.bmp.opm.domain.model.agent;

import javax.persistence.*;

import com.huigou.data.domain.model.AbstractEntity;

@Entity
@Table(name = "SA_OPAgentProc")
public class AgentProc extends AbstractEntity {

    private static final long serialVersionUID = -3568626121054774536L;

    @Column(name="proc_id")
    private String procId;

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }
}
