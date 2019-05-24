package com.huigou.uasp.bmp.dataManage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetail;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-05 17:15
 */
public interface OpdatamanagedetailRepository extends JpaRepository<Opdatamanagedetail, String> {
    Integer countByDataManageId(String dataManageId);
}
