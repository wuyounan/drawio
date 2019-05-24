package com.huigou.uasp.bmp.opm.repository.managment;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;

public interface BizManagementTypeRepository extends JpaRepository<BizManagementType, String>, JpaSpecificationExecutor<BizManagementType> {
   List<BizManagementType> findByParentId(String parentId, Sort sort);
   
   long countByParentId(String parentId);
}
