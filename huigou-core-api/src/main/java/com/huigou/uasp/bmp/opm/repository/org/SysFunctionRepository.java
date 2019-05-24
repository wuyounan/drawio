package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;

public interface SysFunctionRepository extends JpaRepository<SysFunction, String>, JpaSpecificationExecutor<SysFunction> {

    Integer countByParentId(String parentId);

    SysFunction findByCode(String code);

    List<SysFunction> findByFullIdLikeOrderByFullId(String fullId);

    @Modifying
    @Transactional
    @Query(name = "sysFunction.updateStatus", value = "update SysFunction set status = ?2 where id in ?1")
    void updateStatus(List<String> ids, Integer status);
}
