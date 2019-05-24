package com.huigou.uasp.bpm.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstRelation;

public interface HistoricTaskInstRelationRepository extends JpaRepository<HistoricTaskInstRelation, String> {
    
    void deleteByBusinessKey(String bizId);
    
    

}
