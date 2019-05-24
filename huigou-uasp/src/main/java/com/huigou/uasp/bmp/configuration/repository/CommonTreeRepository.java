package com.huigou.uasp.bmp.configuration.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.configuration.domain.model.CommonTree;

public interface CommonTreeRepository extends JpaRepository<CommonTree, String> {

    /**
     * 查询子节点个数
     * 
     * @param parentId
     * @return
     */
    Long countByParentId(String parentId);
    
    
    CommonTree findByKindIdAndCode(Integer Integer, String code);

}
