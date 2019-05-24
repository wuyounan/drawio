package com.huigou.uasp.tool.remind.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.tool.remind.domain.model.MessageRemind;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-02-15 14:39
 */
public interface MessageRemindRepository extends JpaRepository<MessageRemind, String> {

}
