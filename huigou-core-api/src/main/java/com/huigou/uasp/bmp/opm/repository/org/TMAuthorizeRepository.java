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


    /**
     * @deprecated 该方法只支持Oracle数据库查询，请使用 {@link #findFirstByManagerIdAndRoleKindIdNot(String, String)} 替代。
     */
    @Query(name = "tmAuthorize.findByManagerIdNotRole", nativeQuery = true, value = "select * from SA_TMAuthorize where manager_Id = :managerId and role_kind_id <> :roleKindId and rownum=1")
    TMAuthorize findByManagerIdNotRole(@Param("managerId") String managerId, @Param("roleKindId") String roleKindId);

    /**
     * @since 1.1.3
     */
    TMAuthorize findFirstByManagerIdAndRoleKindIdNot(String managerId, String roleKindId);

    /**
     * @deprecated 该方法只支持Oracle数据库查询，请使用 {@link #findFirstBySubordinationIdAndManagerIdNot(String, String)} 替代。
     */
    @Deprecated
    @Query(name = "tmAuthorize.findBySubordinationIdAndManagerId", nativeQuery = true, value = "select * from SA_TMAuthorize where subordination_Id = :subordinationId and manager_Id = :managerId and rownum=1")
    TMAuthorize findBySubordinationIdAndManagerId(@Param("managerId") String managerId, @Param("subordinationId") String subordinationId);

    /**
     * @since 1.1.3
     */
    TMAuthorize findFirstBySubordinationIdAndManagerIdNot(String managerId, String subordinationId);

    /**
     * 查找三员授权
     */
    @Query(name = "tmAuthorize.findOneTMAuthorize", value = "from TMAuthorize where (:subordinationFullId like CONCAT(subordinationFullId,'%') or subordinationFullId like CONCAT(:subordinationFullId,'%')) and (systemId = :systemId or systemId = '*') and roleKindId = :roleKindId and managerId = :managerId")
    TMAuthorize findOneTMAuthorize(@Param("managerId") String managerId, @Param("subordinationFullId") String subordinationFullId, @Param("systemId") String systemId, @Param("roleKindId") String roleKindId);

    /**
     * 查找三员授权
     *
     * @param managerId       管理者ID
     * @param subordinationId 下属ID
     * @param systemId        系统ID
     * @return 三员授权实体
     * @deprecated 该方法只支持Oracle数据库查询，请使用 {@link #findFirstByManagerIdAndSubordinationIdAndSystemId(String, String, String)} 替代。
     */
    @Deprecated
    @Query(name = "tmAuthorize.findOneTMAuthorize2", nativeQuery = true, value = "select * from SA_TMAuthorize where subordination_id = :subordinationId and system_id = :systemId and manager_id = :managerId and rownum=1")
    TMAuthorize findOneTMAuthorize(@Param("managerId") String managerId, @Param("subordinationId") String subordinationId, @Param("systemId") String systemId);

    /**
     * @since 1.1.3
     */
    TMAuthorize findFirstByManagerIdAndSubordinationIdAndSystemId(@Param("managerId") String managerId, @Param("subordinationId") String subordinationId, @Param("systemId") String systemId);

}