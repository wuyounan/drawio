package com.huigou.uasp.bmp.bizconfig.chart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeLine;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-04-17 09:23
 */
public interface BpmProcessNodeLineRepository extends JpaRepository<BpmProcessNodeLine, String> {
    List<BpmProcessNodeLine> findByBusinessProcessId(String businessProcessId);

    @Modifying
    @Query("delete BpmProcessNodeLine a where a.businessProcessId = ?1")
    void deleteLineByProcessId(String businessProcessId);

    @Modifying
    @Query("delete BpmProcessNodeLine a where a.fromNode = ?1 or a.toNode= ?1")
    void deleteLineByNode(String id);
}
