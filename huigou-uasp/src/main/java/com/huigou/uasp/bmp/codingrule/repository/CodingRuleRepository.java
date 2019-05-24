package com.huigou.uasp.bmp.codingrule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.codingrule.domain.model.CodingRule;

/**
 * 单据编码生成规则资料库
 * 
 * @author gongmm
 */
public interface CodingRuleRepository extends JpaRepository<CodingRule, String> {
    
    CodingRule findByCode(String code);

}
