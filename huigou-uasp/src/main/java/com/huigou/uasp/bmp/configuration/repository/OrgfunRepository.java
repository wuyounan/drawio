package com.huigou.uasp.bmp.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.Orgfun;

/**
 * Repository
 * 
 * @author xx
 * @date 2018-03-09 10:47
 */
public interface OrgfunRepository extends JpaRepository<Orgfun, String> {
    Long countByParentId(String parentId);

    List<Orgfun> findByParentId(String parentId);
}
