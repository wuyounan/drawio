package com.huigou.uasp.bmp.portal.personown.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.portal.personown.domain.model.Personcalendar;

/**
 * Repository
 * 
 * @author xx
 * @date 2017-03-01 09:40
 */
public interface PersoncalendarRepository extends JpaRepository<Personcalendar, String> {
    @Query(name = "personcalendar.findPersoncalendarByPersonIdAndDate", value = "from Personcalendar where personId = :personId and startTime between :startDate and :endDate order by startTime asc")
    List<Personcalendar> findPersoncalendarByPersonIdAndDate(@Param("personId") String personId, @Param("startDate") Date startDate,
                                                             @Param("endDate") Date endDate);
}
