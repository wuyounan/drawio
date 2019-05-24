package com.huigou.uasp.bpm.engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceRelation;

public interface HistoricTaskInstanceRelationRepository extends JpaRepository<HistoricTaskInstanceRelation, String> {
    void deleteByBusinessKey(String bizId);
}
