package com.huigou.uasp.bmp.securitypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.log.domain.model.ApplicationSystem;


public interface ApplicationSystemRepository extends JpaRepository<ApplicationSystem, String> {
    ApplicationSystem findByCode(String code);

    @Query(countName = "applicationSystem.countByCode", value = "select count(t) from ApplicationSystem t where t.code=?1 and t.id !=?2")
    Long countByCodeAndId(String code, String id);

    @Query(countName = "applicationSystem.countByName", value = "select count(t) from ApplicationSystem t where t.code=?1 and t.id !=?2")
    Long countByNameAndId(String name, String id);

    @Query(countName = "applicationSystem.countByName", value = "select count(t) from ApplicationSystem t where t.classPrefix = ?1 and t.id != ?2")
    Long countByClassPrefixAndId(String ClassPrefix, String id);

    @Query(name = "applicationSystem.findByClassPrefix", value = "from ApplicationSystem where :classPrefix like concat(classPrefix,'%')")
    ApplicationSystem findByClassPrefix(@Param("classPrefix") String classPrefix);

    Long countByCode(String code);

    Long countByName(String name);

    Long countByClassPrefix(String ClassPrefix);
}
