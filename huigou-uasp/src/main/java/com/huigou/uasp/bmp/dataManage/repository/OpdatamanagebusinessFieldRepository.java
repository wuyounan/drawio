package com.huigou.uasp.bmp.dataManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.dataManage.domain.model.OpdatamanagebusinessField;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-27 12:04
 */
public interface OpdatamanagebusinessFieldRepository extends JpaRepository<OpdatamanagebusinessField, String> {
    List<OpdatamanagebusinessField> findByDatamanagebusinessId(String datamanagebusinessId);

    @Modifying
    @Query("delete OpdatamanagebusinessField r where r.datamanagebusinessId=:datamanagebusinessId")
    void deleteByDatamanagebusinessId(@Param("datamanagebusinessId") String datamanagebusinessId);

    List<OpdatamanagebusinessField> findByDatamanagebusinessIdAndTableColumnAndTableAliasAndIsOrgCondition(String datamanagebusinessId, String tableColumn,
                                                                                                           String tableAlias, Integer isOrgCondition);
}
