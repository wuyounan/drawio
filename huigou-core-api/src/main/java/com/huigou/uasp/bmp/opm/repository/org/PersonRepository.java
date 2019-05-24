package com.huigou.uasp.bmp.opm.repository.org;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.opm.domain.model.org.Person;

/**
 * 人员资料库
 * 
 * @author gongmm
 */
public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {

    @Query(name = "person.findByLoginName", value = "select o from Person o where upper(o.loginName) = ?1 and status = 1")
    Person findByLoginName(String loginName);

    Person findByCaNo(String caNo);

    Person findByCertificateNo(String certificateNo);

    int countByStatus(int status);
}
