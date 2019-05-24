package com.huigou.uasp.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.log.domain.model.DBBizLog;


/**
 * 日志资料库
 * 
 * @author yuanwf
 *
 */
public interface DBBizLogRepository extends JpaRepository<DBBizLog, String> {

}