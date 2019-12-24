package com.huigou.uasp.bmp.configuration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.configuration.domain.model.PersonQueryScheme;

public interface PersonQuerySchemeRepository extends JpaRepository<PersonQueryScheme, String> {

    @Query(name = "personQuery.findPersonQuerySchemes", value = "select o from PersonQueryScheme o where personId = :personId and kindId = :kindId order by sequence")
    List<PersonQueryScheme> findPersonQuerySchemes(@Param("personId") String personId, @Param("kindId") String kindId);

    @Query(name = "personQuery.getMaxSequence", value = "select coalesce(max(sequence), 0) from PersonQueryScheme o where o.personId = :personId and o.kindId = :kindId")
    Integer getMaxSequence(@Param("personId") String personId, @Param("kindId") String kindId);

}
