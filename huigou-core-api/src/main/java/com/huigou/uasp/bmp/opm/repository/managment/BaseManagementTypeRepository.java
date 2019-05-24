package com.huigou.uasp.bmp.opm.repository.managment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bmp.opm.domain.model.management.BaseManagementType;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;

public interface BaseManagementTypeRepository extends JpaRepository<BaseManagementType, String>, JpaSpecificationExecutor<BaseManagementType> {

    BaseManagementType findFirstByBizManagementType(BizManagementType bizManagementType);

}
