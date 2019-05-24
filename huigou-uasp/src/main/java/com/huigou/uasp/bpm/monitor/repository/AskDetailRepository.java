package com.huigou.uasp.bpm.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.monitor.domain.model.AskDetail;

/**
 * 询问明细表Repository
 * 
 * @author renxs
 * 
 */
public interface AskDetailRepository extends JpaRepository<AskDetail, String> {

	AskDetail[] findByAskId(String askId);
}
