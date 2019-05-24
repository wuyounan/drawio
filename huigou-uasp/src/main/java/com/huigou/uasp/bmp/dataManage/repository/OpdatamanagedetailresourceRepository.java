package com.huigou.uasp.bmp.dataManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagedetailresource;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-05 17:15
 */
public interface OpdatamanagedetailresourceRepository extends JpaRepository<Opdatamanagedetailresource, String> {
    @Modifying
    @Query("delete Opdatamanagedetailresource r where r.dataManagedetalId=:dataManagedetalId")
    void deleteByDataManagedetalId(@Param("dataManagedetalId") String dataManagedetalId);

    Integer countByDataManagedetalIdAndDataKindIdAndResourceKey(String dataManagedetalId, String dataKindId, String resourceKey);

    List<Opdatamanagedetailresource> findByDataManagedetalId(String dataManagedetalId);

}
