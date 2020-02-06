package com.huigou.index.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.index.domain.model.Index;

public interface IndexRepository extends JpaRepository<Index, String> {
    
    int countByclassificationId(String classificationId);

}
