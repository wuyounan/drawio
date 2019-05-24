package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.uasp.bmp.opm.domain.model.access.RolePermission;


public interface RolePermissionRepository extends  JpaRepository<RolePermission, String>{
    
    @Modifying
    @Transactional
    @Query(name="rolePermission.deleteByRoleIdAndPermissionIds", value = "delete RolePermission where roleId = ?1 and permissionId in ?2")
    void deleteByRoleIdAndPermissionIds(String roleId, List<String> permissionIds);
    
    @Modifying
    @Transactional
    @Query(name="rolePermission.deleteByPermissionIds", value = "delete RolePermission where permissionId in ?1")
    void deleteByPermissionIds(List<String> permissionIds);
    
    RolePermission findFirstByPermissionId(String permissionId);
    
    Long countByRoleId(String roleId);
}
