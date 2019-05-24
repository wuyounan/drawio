package com.huigou.uasp.bmp.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.ApprovalRejectedReason;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-09-11 11:16
 */
public interface ApprovalRejectedReasonRepository extends JpaRepository<ApprovalRejectedReason, String> {

}
