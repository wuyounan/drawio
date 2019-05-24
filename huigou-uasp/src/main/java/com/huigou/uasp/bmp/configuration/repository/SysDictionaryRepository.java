package com.huigou.uasp.bmp.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.SysDictionary;

/**
 * 系统参数资料
 * 
 * @author gongmm
 */
public interface SysDictionaryRepository extends JpaRepository<SysDictionary, String>{

    List<SysDictionary> findByStatus(Integer status);
}
