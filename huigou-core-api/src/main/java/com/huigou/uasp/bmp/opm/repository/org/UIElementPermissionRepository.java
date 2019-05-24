package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.opm.domain.model.access.UIElementPermission;

public interface UIElementPermissionRepository extends JpaRepository<UIElementPermission, String> {
    
    List<UIElementPermission> findByPermissionId(String permissionId);
}
