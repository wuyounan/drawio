package com.huigou.report.cubesviewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.report.cubesviewer.domain.model.CubesViewerDefinition;

public interface CubesViewerDefinitionRepository extends JpaRepository<CubesViewerDefinition, String> {
    
    List<CubesViewerDefinition> findByCodeInAndStatus(List<String> code, int status);

}
