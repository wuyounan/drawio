package com.huigou.uasp.bmp.bizconfig.chart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNodeFunction;

/**
 * Repository
 * 
 * @author
 * @date 2018-01-29 09:47
 */
public interface BpmProcessNodeFunctionRepository extends JpaRepository<BpmProcessNodeFunction, String> {
    List<BpmProcessNodeFunction> findByBusinessProcessIdOrderBySequenceAsc(String businessProcessId);

    List<BpmProcessNodeFunction> findByViewIdOrderBySequenceAsc(String viewId);

    @Modifying
    @Query("delete BpmProcessNodeFunction a where a.businessProcessId = ?1")
    void deleteByProcessId(String businessProcessId);

    @Modifying
    @Query("delete BpmProcessNodeFunction a where a.viewId = ?1")
    void deleteByViewId(String viewId);
}
