package com.huigou.uasp.tool.dataimport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportLog;

public interface ExcelImportLogRepository extends JpaRepository<ExcelImportLog, String>, JpaSpecificationExecutor<ExcelImportLog> {

}
