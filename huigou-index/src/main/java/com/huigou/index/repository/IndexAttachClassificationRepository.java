package com.huigou.index.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.index.domain.model.IndexAttachClassification;

public interface IndexAttachClassificationRepository extends JpaRepository<IndexAttachClassification, String> {

    List<IndexAttachClassification> findByIndexIdIn(List<String> indexIds);
}
