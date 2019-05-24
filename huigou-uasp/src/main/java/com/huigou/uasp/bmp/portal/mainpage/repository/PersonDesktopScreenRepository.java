package com.huigou.uasp.bmp.portal.mainpage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreen;

public interface PersonDesktopScreenRepository extends JpaRepository<PersonDesktopScreen, String> {
    
    @Query(name="personDesktopScreen.findByPersonId", value="from PersonDesktopScreen where personId = :personId order by version")
    List<PersonDesktopScreen> findByPersonId(@Param("personId") String personId);
}
