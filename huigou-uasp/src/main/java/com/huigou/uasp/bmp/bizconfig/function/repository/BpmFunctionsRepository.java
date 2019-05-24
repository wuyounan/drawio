package com.huigou.uasp.bmp.bizconfig.function.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctions;

/**
 * Repository
 * 
 * @author
 * @date 2018-03-28 11:16
 */
public interface BpmFunctionsRepository extends JpaRepository<BpmFunctions, String> {
    List<BpmFunctions> findByCode(String code);
}
