package com.huigou.uasp.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.log.domain.model.OnlineSession;

public interface OnlineSessionRepository extends JpaRepository<OnlineSession, String> ,JpaSpecificationExecutor<OnlineSession> {
}
