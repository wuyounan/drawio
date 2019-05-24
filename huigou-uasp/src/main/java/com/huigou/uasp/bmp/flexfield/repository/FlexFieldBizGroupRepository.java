package com.huigou.uasp.bmp.flexfield.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.flexfield.domain.model.FlexFieldBizGroup;

public interface FlexFieldBizGroupRepository extends JpaRepository<FlexFieldBizGroup, String> {

    List<FlexFieldBizGroup> findByBizCodeAndVisible(String bizCode, Integer visible);
}
