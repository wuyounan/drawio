package com.huigou.index.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.index.domain.model.IndexClassification;

public interface IndexClassificationRepository extends JpaRepository<IndexClassification, String> {

    IndexClassification findFirstByDimId(String dimId);

    int countByParentId(String parentId);
}
