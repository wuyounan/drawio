package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerManuscript;

public interface ProcUnitHandlerManuscriptRepository extends JpaRepository<ProcUnitHandlerManuscript, String> {

    ProcUnitHandlerManuscript findByProcUnitHandlerId(String procUnitHandlerId);
    
    List<ProcUnitHandlerManuscript> findByBizId(String bizId);
}
