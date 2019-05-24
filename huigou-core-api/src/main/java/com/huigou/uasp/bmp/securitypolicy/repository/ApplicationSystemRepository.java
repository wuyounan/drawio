package com.huigou.uasp.bmp.securitypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.log.domain.model.ApplicationSystem;



public interface ApplicationSystemRepository extends JpaRepository<ApplicationSystem, String> {
	ApplicationSystem findByCode(String code);
	
	@Query(countName="applicationSystem.countByCode",value="select count(*) from sa_applicationsystem t where t.code = ?1 and t.id != ?2",nativeQuery=true)
	Long countByCodeAndId(String code,String id);
	
	@Query(countName="applicationSystem.countByName",value="select count(*) from sa_applicationsystem t where t.code = ?1 and t.id != ?2",nativeQuery=true)
	Long countByNameAndId(String name,String id);
	
	@Query(countName="applicationSystem.countByName",value="select count(*) from sa_applicationsystem t where t.class_prefix = ?1 and t.id != ?2",nativeQuery=true)
	Long countByClassPrefixAndId(String ClassPrefix,String id);

	@Query(name = "applicationSystem.findByClassPrefix", nativeQuery = true, value = "select * from SA_ApplicationSystem where :classPrefix like class_prefix || '%'")
	ApplicationSystem findByClassPrefix(@Param("classPrefix") String classPrefix);
	
	Long countByCode(String code);
	
	Long countByName(String name);
	
	Long countByClassPrefix(String ClassPrefix);
}
