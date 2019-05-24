package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstExtension;

public interface HistoricTaskInstExtensionRepository extends JpaRepository<HistoricTaskInstExtension, String>,
                JpaSpecificationExecutor<HistoricTaskInstExtension> {

    @Query(name = "historicTaskInstExtension.waitingCoordinationHistoricTaskInst", value = "select o from HistoricTaskInstExtension o where o.businessKey = :businessKey and o.catalogId = :catalogId and o.kindId in :kindIds and o.statusId = :statusId")
    List<HistoricTaskInstExtension> findWaitingCoordinationHistoricTaskInst(@Param("businessKey") String businessKey, @Param("catalogId") String catalogId,
                                                                            @Param("kindIds") List<String> kindIds, @Param("statusId") String statusId);

    HistoricTaskInstExtension findFirstByProcUnitHandlerIdOrderByVersionDesc(String procUnitHandlerId);

    List<HistoricTaskInstExtension> findByProcUnitHandlerIdOrderByVersion(String procUnitHandlerId);

}
