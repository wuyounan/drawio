package com.huigou.uasp.bmp.bizconfig.chart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.bizconfig.chart.domain.model.BpmProcessNode;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-04-17 09:23
 */
public interface BpmProcessNodeRepository extends JpaRepository<BpmProcessNode, String> {
    List<BpmProcessNode> findByBusinessProcessId(String businessProcessId);

    List<BpmProcessNode> findByBusinessProcessIdOrderByObjectKindCodeAsc(String businessProcessId);

    List<BpmProcessNode> findOneByBusinessProcessIdAndViewId(String businessProcessId, String viewId);

}
