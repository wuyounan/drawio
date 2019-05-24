package com.huigou.uasp.bmp.opm.repository.org;

import com.huigou.uasp.bmp.opm.domain.model.access.TMAuthorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TMAuthorizeRepository extends JpaRepository<TMAuthorize, String> {

    List<TMAuthorize> findByManagerIdAndSubordinationId(String managerId, String subordinationId);

    List<TMAuthorize> findByManagerIdAndRoleKindId(String managerId, String roleKindId);

    List<TMAuthorize> findBySubordinationIdAndSystemIdAndRoleKindId(String subordinationId, String systemId, String roleKindId);


    @Query(name = "tmAuthorize.findByManagerIdNotRole", nativeQuery = true, value = "select * from SA_TMAuthorize where manager_Id = :managerId and role_kind_id <> :roleKindId and rownum=1")
    TMAuthorize findByManagerIdNotRole(@Param("managerId") String managerId, @Param("roleKindId") String roleKindId);

    @Query(name = "tmAuthorize.findBySubordinationIdAndManagerId", nativeQuery = true, value = "select * from SA_TMAuthorize where subordination_Id = :subordinationId and manager_Id = :managerId and rownum=1")
    TMAuthorize findBySubordinationIdAndManagerId( @Param("managerId") String managerId, @Param("subordinationId") String subordinationId);

    /**
     * 查找三员授权
     */
    @Query(name = "tmAuthorize.findOneTMAuthorize", nativeQuery = true, value = "select * from SA_TMAuthorize where (:subordinationFullId like subordination_full_id || '%' or subordination_full_id like :subordinationFullId || '%') and (system_id = :systemId or system_id = '*') and role_kind_id = :roleKindId and manager_Id = :managerId")
    TMAuthorize findOneTMAuthorize(@Param("managerId") String managerId, @Param("subordinationFullId") String subordinationFullId, @Param("systemId") String systemId, @Param("roleKindId") String roleKindId);

    /**
	 * 查找三员授权
	 *
	 * @param managerId
	 *            管理者ID
	 * @param subordinationId
	 *            下属ID
	 * @param systemId
	 *            系统ID
	 * @return 三员授权实体
	 */
    @Query(name = "tmAuthorize.findOneTMAuthorize2", nativeQuery = true, value = "select * from SA_TMAuthorize where subordination_id = :subordinationId and system_id = :systemId and manager_id = :managerId and rownum=1")
    TMAuthorize findOneTMAuthorize(@Param("managerId") String managerId, @Param("subordinationId") String subordinationId, @Param("systemId") String systemId);

}