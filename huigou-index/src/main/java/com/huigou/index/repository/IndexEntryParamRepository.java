package com.huigou.index.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.index.domain.model.IndexEntryFormulaParam;

public interface IndexEntryParamRepository extends JpaRepository<IndexEntryFormulaParam, String> {

    //int countByEntryId(String entryId);
}
