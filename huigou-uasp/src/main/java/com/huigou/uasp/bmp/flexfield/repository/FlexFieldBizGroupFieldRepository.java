package com.huigou.uasp.bmp.flexfield.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroupField;

public interface FlexFieldBizGroupFieldRepository extends JpaRepository<FlexFieldBizGroupField, String> {

    int countByFlexfielddefinitionId(String flexfielddefinitionId);

    int countByFlexfieldbizgroupIdAndFlexfielddefinitionId(String flexfieldbizgroupId, String flexfielddefinitionId);

    List<FlexFieldBizGroupField> findByFlexfieldbizgroupId(String flexfieldbizgroupId);

    @Modifying
    @Query("delete FlexFieldBizGroupField a where a.flexfieldbizgroupId = ?1")
    void deleteByFlexFieldbizgroupId(String flexfieldbizgroupId);
}
