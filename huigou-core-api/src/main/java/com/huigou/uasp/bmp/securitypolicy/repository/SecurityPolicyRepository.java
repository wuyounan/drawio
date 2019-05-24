package com.huigou.uasp.bmp.securitypolicy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.securitypolicy.domain.model.SecurityPolicy;

public interface SecurityPolicyRepository extends JpaRepository<SecurityPolicy, String> {

    List<SecurityPolicy> findBySecurityGradeAndStatus(String securityGrade, Integer statsu);

    @Query(value="select count(o.id) from SecurityPolicy o where o.id != ?1 and o.securityGrade = ?2 and o.status = ?3")
    int countDuplicate(String id, String securityGrade, Integer status);
}