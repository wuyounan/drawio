package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.uasp.bmp.opm.domain.model.access.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String>, JpaSpecificationExecutor<Permission> {

    List<Permission> findByParentId(String parentId, Sort sort);

    List<Permission> findByParentIdAndResourceKindId(String parentId, String resourceKindId);

    Integer countByParentIdAndResourceKindId(String parentId, String resourceKindId);

    Integer countByResourceId(String resourceId);

    Integer countByParentId(String parentId);

    @Query(name = "permission.findFunctionByResourceId", value = "from Permission where resourceId = ?1 and nodeKindId in ('folder','fun')")
    Permission findFunctionByResourceId(String resourceId);

    @Query(name = "permission.findFunctionByResourceId", value = "from Permission where resourceId = ?1 and resourceKindId = ?2")
    Permission findByResourceIdAndResourceKindId(String resourceId, String resourceKindId);

    @Modifying
    @Transactional
    @Query(name = "permission.deleteByResourceIds", value = "delete from Permission where resourceId in ?1")
    void deleteByResourceIds(List<String> ResourceId);

    @Modifying
    @Transactional
    @Query(name = "permission.updateStatusByResourceId", value = "update Permission set status = ?2 where resourceId in ?1")
    void updateStatusByResourceId(List<String> ResourceIds, Integer status);
}
