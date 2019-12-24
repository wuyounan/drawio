package com.huigou.uasp.bpm.managment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;

public interface ProcDefinitionRespository extends JpaRepository<ProcDefinition, String>, JpaSpecificationExecutor<ProcDefinition> {
    ProcDefinition findByProcIdAndCode(String procId, String code);

    long countByParentId(String parentId);

    List<ProcDefinition> findByParentId(String parentId);
    
    @Query(name="procDefinition.findProc", value="from ProcDefinition o where o.procId = :procId and o.nodeKindId = 'proc'")
    ProcDefinition findProc(@Param("procId") String procId);

    @Query(name="procDefinition.findProcUnit", value="from ProcDefinition o where o.procId = :procId and code = :procUnitId and o.nodeKindId = 'procUnit'")
    ProcDefinition findProcUnit(@Param("procId") String procId, @Param("procUnitId")  String procUnitId);
    
    @Query(name="procDefinition.findOtherBindProcs", value="from ProcDefinition o where o.id != ?1 and o.procId = ?2 and o.nodeKindId = 'proc'")
    List<ProcDefinition> findOtherBindProcs(String id, String procId);
    
    @Query(name="procDefinition.findProcUnitsForSequnce", value="from ProcDefinition o where o.procId = :procId and o.nodeKindId = 'procUnit' order by o.sequence")
    List<ProcDefinition> findProcUnitsForSequnce(@Param("procId") String procId);

}
