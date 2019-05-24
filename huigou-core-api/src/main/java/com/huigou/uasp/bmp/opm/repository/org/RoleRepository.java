package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.opm.domain.model.access.Role;


public interface RoleRepository extends JpaRepository<Role, String> {
    
    Long countByParentId(String parentId);
    
    @Modifying
    @Query("update Role set status = -1 where kindId = 'superAdministrator'")
    int hideSuperAdministrator();
     
    List<Role> findByKindId(String kindId);
    
    @Query("select count(p) from Role r join r.permissions p where r.id = ?1")
    int countPermissionsByRoleId(String roleId);
}
