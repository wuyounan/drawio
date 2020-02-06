package com.huigou.report.cboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.report.cboard.domain.model.CboardDefinition;

public interface CboardDefinitionRepository extends JpaRepository<CboardDefinition, String> {

    CboardDefinition findByCode(String code);
}
