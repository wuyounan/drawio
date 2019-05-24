package com.huigou.uasp.bmp.dataManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetype;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-04 11:58
 */
public interface OpdatamanagetypeRepository extends JpaRepository<Opdatamanagetype, String> {
    Integer countByParentId(String parentId);
}
