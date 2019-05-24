package com.huigou.uasp.bmp.opm.repository.managment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.huigou.uasp.bmp.opm.domain.model.management.BizManagement;
import com.huigou.uasp.bmp.opm.domain.model.management.BizManagementType;
import com.huigou.uasp.bmp.opm.domain.model.org.Org;

public interface BizManagementRepository extends JpaRepository<BizManagement, String>, JpaSpecificationExecutor<BizManagement> {

    List<BizManagement> findByManagerInAndBizManagementTypeAndSubordinationIn(Collection<Org> managers, BizManagementType bizManagementType,
                                                                            Collection<Org> subordinations);

}
