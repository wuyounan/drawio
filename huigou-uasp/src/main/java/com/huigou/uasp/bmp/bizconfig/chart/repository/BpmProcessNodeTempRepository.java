package com.huigou.uasp.bmp.bizconfig.chart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeTemp;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-04-17 09:23
 */
public interface BpmProcessNodeTempRepository extends JpaRepository<BpmProcessNodeTemp, String> {
    List<BpmProcessNodeTemp> findByBusinessProcessId(String businessProcessId);

    List<BpmProcessNodeTemp> findOneByBusinessProcessIdAndViewId(String businessProcessId, String viewId);

    @Modifying
    @Query("delete BpmProcessNodeTemp a where a.businessProcessId = ?1")
    void deleteByProcessId(String businessProcessId);

}
