package com.huigou.uasp.bmp.portal.mainpage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreenFunction;

public interface PersonDesktopScreenFunctionRepository extends JpaRepository<PersonDesktopScreenFunction, String> {

    @Modifying
    @Query("delete PersonDesktopScreenFunction a where a.screenId = ?1")
    void deleteByScreenId(String screenId);
}
