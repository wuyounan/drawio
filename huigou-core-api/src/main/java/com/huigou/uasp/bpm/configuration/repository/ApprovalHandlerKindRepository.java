package com.huigou.uasp.bpm.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bpm.configuration.domain.model.ApprovalHandlerKind;


public interface ApprovalHandlerKindRepository extends JpaRepository<ApprovalHandlerKind, String>, JpaSpecificationExecutor<ApprovalHandlerKind>  {

}
