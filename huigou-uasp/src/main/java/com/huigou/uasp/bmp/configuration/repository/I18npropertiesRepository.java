package com.huigou.uasp.bmp.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.I18nproperties;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-09-29 10:23
 */
public interface I18npropertiesRepository extends JpaRepository<I18nproperties, String> {
    int countByCode(String code);
}
