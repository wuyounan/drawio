package com.huigou.uasp.tool.dataimport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplate;

public interface ExcelImportTemplateRepository extends JpaRepository<ExcelImportTemplate, String>, JpaSpecificationExecutor<ExcelImportTemplate> {
    List<ExcelImportTemplate> findByCode(String code);
}
