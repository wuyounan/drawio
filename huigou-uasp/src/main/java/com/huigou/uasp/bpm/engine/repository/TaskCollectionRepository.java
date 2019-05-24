package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bpm.engine.domain.model.TaskCollection;

public interface TaskCollectionRepository extends JpaRepository<TaskCollection, String> {
    
    List<TaskCollection> findByPersonIdAndTaskId(String personId, String taskId);

}
