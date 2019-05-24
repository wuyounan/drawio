package com.huigou.uasp.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.log.domain.model.DBBizLogDetail;

/**
 * 日志明细资料库
 * 
 * @author yuanwf
 *
 */
public interface DBBizLogDetailRepository extends JpaRepository<DBBizLogDetail, String> {
	DBBizLogDetail findByBizLogId(String bizLogId);
}
