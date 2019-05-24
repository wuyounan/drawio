package com.huigou.uasp.bmp.bizClassification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.bizClassification.domain.model.BizClassification;

/**
 * 业务分类配置Repository
 */
public interface BizClassificationRepository extends JpaRepository<BizClassification, String> {
    List<BizClassification> findByFullIdLikeOrderByFullId(String fullId);

    Integer countByParentId(String parentId);
}
