package com.huigou.uasp.bmp.dataManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.dataManage.domain.model.Opdatakind;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-09-04 10:52
 */
public interface OpdatakindRepository extends JpaRepository<Opdatakind, String> {

    List<Opdatakind> findAllByOrderBySequenceAsc();
}
