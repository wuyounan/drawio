package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bpm.engine.domain.model.RuntimeTaskExtension;

public interface RuntimeTaskExtensionRepository extends JpaRepository<RuntimeTaskExtension, String> {

    @Query(name = "runtimeTaskExtension.countReadyProcTasksByBusinessCode", value = "select count(1) from RuntimeTaskExtension o where o.businessCode = :businessCode and o.catalogId = 'process' and o.statusId = 'ready'")
    long countReadyProcTasksByBusinessCode(@Param("businessCode") String businessCode);

    @Query(name = "runtimeTaskExtension.countReplenishTasksByBusinessKey", value = "select count(1) from RuntimeTaskExtension o where o.businessKey = :businessKey and o.catalogId = 'task' and kindId = 'replenish'")
    long countReplenishTasksByBusinessKey(@Param("businessKey") String businessKey);

    @Query(name = "runtimeTaskExtension.queryCoordinationTaskInstances", value = "select o from RuntimeTaskExtension o where o.businessKey = :businessKey and o.catalogId = :catalogId and o.kindId in :kindIds")
    List<RuntimeTaskExtension> findCoordinationTaskInstances(@Param("businessKey") String businessKey, @Param("catalogId") String catalogId,
                                                             @Param("kindIds") List<String> kindIds);
}
