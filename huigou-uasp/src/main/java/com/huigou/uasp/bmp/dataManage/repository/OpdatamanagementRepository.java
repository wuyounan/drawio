package com.huigou.uasp.bmp.dataManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagement;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-05 17:15
 */
public interface OpdatamanagementRepository extends JpaRepository<Opdatamanagement, String> {
    Long countByDataManagedetalId(String dataManagedetalId);

    Integer countByManagerIdAndDataManagedetalId(String managerId, String dataManagedetalId);
}
