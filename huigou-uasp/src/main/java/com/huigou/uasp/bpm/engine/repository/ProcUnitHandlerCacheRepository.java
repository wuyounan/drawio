package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandlerCache;

public interface ProcUnitHandlerCacheRepository extends JpaRepository<ProcUnitHandlerCache, String>, JpaSpecificationExecutor<ProcUnitHandlerCache> {

    long countByBizCode(String bizCode);
    
    long deleteByBizCode(String bizCode);
    
    List<ProcUnitHandlerCache> findByBizCode(String bizCode);
    
    
}
