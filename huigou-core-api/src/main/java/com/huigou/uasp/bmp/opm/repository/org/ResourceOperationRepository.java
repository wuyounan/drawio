package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceKind;
import com.huigou.uasp.bmp.opm.domain.model.resource.ResourceOperation;

public interface ResourceOperationRepository extends JpaRepository<ResourceOperation, String> {
    @Query(name = "resourceOperation.findByCommonOperations", value = "from ResourceOperation where resourceKind = :resourceKind and status = 1   and isCommon = :isCommon order by sequence")
    List<ResourceOperation> findCommonOperations(@Param("resourceKind") ResourceKind resourceKind, @Param("isCommon") Integer commonOperation);
}
