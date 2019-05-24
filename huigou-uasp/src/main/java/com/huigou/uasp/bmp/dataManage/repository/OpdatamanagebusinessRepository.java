package com.huigou.uasp.bmp.dataManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagebusiness;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-27 12:04
 */
public interface OpdatamanagebusinessRepository extends JpaRepository<Opdatamanagebusiness, String> {
    Integer countByParentId(String parentId);
}
