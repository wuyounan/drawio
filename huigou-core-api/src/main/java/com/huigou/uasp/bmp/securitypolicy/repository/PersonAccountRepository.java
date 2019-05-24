package com.huigou.uasp.bmp.securitypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.securitypolicy.domain.model.PersonAccount;


public interface PersonAccountRepository extends JpaRepository<PersonAccount, String> {
    
    PersonAccount findByLoginName(String loginName);
    
}
