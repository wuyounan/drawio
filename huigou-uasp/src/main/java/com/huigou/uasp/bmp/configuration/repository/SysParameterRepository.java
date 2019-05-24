package com.huigou.uasp.bmp.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.SysParameter;

/**
 * 系统参数资料
 * 
 * @author gongmm
 */
public interface SysParameterRepository extends JpaRepository<SysParameter, String> {

}
