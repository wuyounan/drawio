package com.huigou.uasp.bmp.bizconfig.function.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroup;

/**
 * Repository
 * 
 * @author
 * @date 2018-03-28 11:16
 */
public interface BpmFunctionsGroupRepository extends JpaRepository<BpmFunctionsGroup, String> {
    @Modifying
    @Query("delete BpmFunctionsGroup a where a.bpmFunctionsId = ?1")
    void deleteByBpmFunctionsId(String bpmFunctionsId);

    List<BpmFunctionsGroup> findByBpmFunctionsId(String bpmFunctionsId);
}
