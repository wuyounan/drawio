package com.huigou.uasp.bmp.dataManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatamanagetypekind;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-04 11:58
 */
public interface OpdatamanagetypekindRepository extends JpaRepository<Opdatamanagetypekind, String> {
    List<Opdatamanagetypekind> findByDataManageId(String dataManageId);

    @Modifying
    @Query("delete Opdatamanagetypekind r where r.dataManageId=:dataManageId")
    void deleteByDataManageId(@Param("dataManageId") String dataManageId);

    Integer countByDataManageIdAndDataKindId(String dataManageId, String dataKindId);

    Long countByDataKindId(String dataKindId);
}
