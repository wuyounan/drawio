package com.huigou.uasp.bpm.configuration.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bpm.configuration.domain.model.ApprovalElement;
import com.huigou.uasp.bpm.configuration.domain.model.ProcApprovalElement;
import com.huigou.uasp.bpm.managment.domain.model.ProcDefinition;

public interface ProcApprovalElementRepository extends JpaRepository<ProcApprovalElement, String>, JpaSpecificationExecutor<ProcApprovalElement> {

    long countByApprovalElement(ApprovalElement approvalElement);

    List<ProcApprovalElement> findByProcDefinition(ProcDefinition procDefinition, Sort sort);
    
    ProcApprovalElement findFirstByApprovalElement(ApprovalElement approvalElement);
}
