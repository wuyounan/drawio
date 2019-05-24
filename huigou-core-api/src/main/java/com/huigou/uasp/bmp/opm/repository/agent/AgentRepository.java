package com.huigou.uasp.bmp.opm.repository.agent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bmp.opm.domain.model.agent.Agent;

/**
 * 组织类别资料库
 * 
 * @author gongmm
 */
public interface AgentRepository extends JpaRepository<Agent, String>, JpaSpecificationExecutor<Agent> {
    
}
