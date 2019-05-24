package com.huigou.uasp.bmp.securitypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonLoginLimit;

public interface PersonLoginLimitRepository extends JpaRepository<PersonLoginLimit, String> {

	//List<PersonLoginLimit> findByLoginName(String loginName);

	Long countByMachineId(String machineId);
}
