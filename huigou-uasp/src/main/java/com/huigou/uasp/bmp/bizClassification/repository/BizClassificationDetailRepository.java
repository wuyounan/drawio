package com.huigou.uasp.bmp.bizClassification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassificationDetail;

/**
 * 业务分类配置明细Repository
 */
public interface BizClassificationDetailRepository extends JpaRepository<BizClassificationDetail, String> {

    Integer countByBizClassificationId(String bizClassificationId);

    Integer countByBizPropertyId(String bizPropertyId);

    Integer countByBizClassificationIdAndBizPropertyId(String bizClassificationId, String bizPropertyId);
}
