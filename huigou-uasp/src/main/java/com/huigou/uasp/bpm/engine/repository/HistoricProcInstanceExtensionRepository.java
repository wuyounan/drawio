package com.huigou.uasp.bpm.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.HistoricProcessInstanceExtension;

public interface HistoricProcInstanceExtensionRepository extends JpaRepository<HistoricProcessInstanceExtension, String>{
    
    
    long countByBusinessKey(String businessKey);

    HistoricProcessInstanceExtension findByProcessInstanceId(String processInstanceId);
    
    HistoricProcessInstanceExtension findByBusinessKey(String businessKey);
}
