package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bpm.engine.domain.model.HistoricTaskInstanceExtension;

public interface HistoricTaskInstanceExtensionRepository extends JpaRepository<HistoricTaskInstanceExtension, String>{

    //@Query(name = "historicTaskInstExtension.waitingCoordinationHistoricTaskInst", value = "select o from HistoricTaskInstanceExtension o where o.businessKey = :businessKey and o.catalogId = :catalogId and o.kindId in :kindIds and o.statusId = :statusId")
    //List<HistoricTaskInstanceExtension> findWaitingCoordinationHistoricTaskInst(@Param("businessKey") String businessKey, @Param("catalogId") String catalogId,
    //                                                                        @Param("kindIds") List<String> kindIds, @Param("statusId") String statusId);
    
    
    HistoricTaskInstanceExtension findFirstByProcUnitHandlerIdOrderByVersionDesc(String procUnitHandlerId);
    
    List<HistoricTaskInstanceExtension> findByProcUnitHandlerIdOrderByVersion(String procUnitHandlerId);
    
    @Query(name = "historicTaskInstExtension.findApplicantTask", value = "from HistoricTaskInstanceExtension o where o.businessKey = ?1 and o.taskDefinitionKey = 'Apply' and o.previousId is null")
    HistoricTaskInstanceExtension findApplicantTask(String bizId);

    @Query(name = "historicTaskInstExtension.findApplicantTaskByBizCode", value = "from HistoricTaskInstanceExtension o where o.businessCode = ?1 and o.taskDefinitionKey = 'Apply' and o.previousId is null")
    HistoricTaskInstanceExtension findApplicantTaskByBizCode(String bizCode);

    List<HistoricTaskInstanceExtension> findByBusinessKeyAndTaskDefinitionKey(String bizId, String procUnitId);
}
