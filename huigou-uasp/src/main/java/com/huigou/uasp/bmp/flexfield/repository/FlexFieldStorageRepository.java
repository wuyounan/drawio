package com.huigou.uasp.bmp.flexfield.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldStorage;

public interface FlexFieldStorageRepository extends JpaRepository<FlexFieldStorage, String>, JpaSpecificationExecutor<FlexFieldStorage> {

    List<FlexFieldStorage> findByBizKindIdAndBizIdOrderBySequence(String bizKindId, String bizId);

    @Modifying
    @Query("delete FlexFieldStorage a where a.bizKindId = ?1 and a.bizId =?2")
    void deleteByBizKindIdAndBizId(String bizKindId, String bizId);

    @Modifying
    @Query("delete FlexFieldStorage a where a.bizId =?1")
    void deleteByBizId(String bizId);
}
