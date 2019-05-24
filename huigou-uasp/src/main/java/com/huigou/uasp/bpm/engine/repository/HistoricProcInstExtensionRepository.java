package com.huigou.uasp.bpm.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.HistoricProcInstExtension;

public interface HistoricProcInstExtensionRepository extends JpaRepository<HistoricProcInstExtension, String>{
    
    
    long countByBusinessKey(String businessKey);

    HistoricProcInstExtension findByProcInstId(String procInstId);
    
    HistoricProcInstExtension findByBusinessKey(String businessKey);
}
