package com.huigou.uasp.bmp.bizconfig.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.bizconfig.process.domain.model.BpmBusinessProcess;

/**
 * Repository
 *
 * @author
 * @date 2017-04-07 14:00
 */
public interface BpmBusinessProcessRepository extends JpaRepository<BpmBusinessProcess, String> {

    @Query(value = "select max(b.sequence) from BpmBusinessProcess b where b.parentId = :parentId")
    Integer getNextSequence(@Param("parentId") String parentId);

    Integer countByParentId(String parentId);

    List<BpmBusinessProcess> findByParentIdOrderBySequenceAsc(String parentId);

    List<BpmBusinessProcess> findByCode(String code);
}
