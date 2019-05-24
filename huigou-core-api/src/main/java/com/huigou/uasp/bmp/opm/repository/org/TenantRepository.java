package com.huigou.uasp.bmp.opm.repository.org;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.opm.domain.model.org.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, String> {
    
}

