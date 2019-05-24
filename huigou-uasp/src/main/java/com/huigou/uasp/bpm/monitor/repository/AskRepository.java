package com.huigou.uasp.bpm.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.monitor.domain.model.Ask;

/**
 * 询问表Repository
 * 
 * @author renxs
 * 
 */
public interface AskRepository extends JpaRepository<Ask, String> {
	
	Ask findByBizIdAndKindId(String bizId, Integer kindId);
}
