package com.huigou.uasp.bmp.bizconfig.function.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetails;

/**
 * Repository
 * 
 * @author
 * @date 2018-03-28 11:16
 */
public interface BpmFunctionsDetailsRepository extends JpaRepository<BpmFunctionsDetails, String> {
    @Modifying
    @Query("delete BpmFunctionsDetails a where a.bpmFunctionsId = ?1")
    void deleteByBpmFunctionsId(String bpmFunctionsId);

    Integer countByFunctionsGroupId(String functionsGroupId);
    
    List<BpmFunctionsDetails> findByBpmFunctionsId(String bpmFunctionsId);
}
