package com.huigou.uasp.bmp.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.CommonHandler;

public interface CommonHandlerRepository extends JpaRepository<CommonHandler, String> {

    List<CommonHandler> findByBizIdOrderBySequence(String bizId);
}
