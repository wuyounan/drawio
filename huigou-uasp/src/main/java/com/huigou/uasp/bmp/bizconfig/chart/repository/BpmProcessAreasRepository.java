package com.huigou.uasp.bmp.bizconfig.chart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessAreas;

/**
 * Repository
 * 
 * @author
 * @date 2018-03-07 11:15
 */
public interface BpmProcessAreasRepository extends JpaRepository<BpmProcessAreas, String> {
    List<BpmProcessAreas> findByBusinessProcessId(String businessProcessId);

    @Modifying
    @Query("delete BpmProcessAreas a where a.businessProcessId = ?1")
    void deleteByProcessId(String businessProcessId);
}
