package com.huigou.index.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.index.domain.model.IndexEntry;

public interface IndexEntryRepository extends JpaRepository<IndexEntry, String> {

    int countByIndexId(String indexId);

}
