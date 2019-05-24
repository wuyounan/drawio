package com.huigou.uasp.bmp.configuration.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.configuration.domain.model.CodeBuildRule;

/**
 * 单据编码生成规则资料库
 * 
 * @author gongmm
 */
public interface CodeBuildRuleRepository extends JpaRepository<CodeBuildRule, String> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select o from CodeBuildRule o where o.id= :id ")
    CodeBuildRule getByIdForUpdate(@Param("id") String id);
}
