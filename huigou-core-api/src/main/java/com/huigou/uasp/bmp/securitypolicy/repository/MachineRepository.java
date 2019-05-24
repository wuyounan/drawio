package com.huigou.uasp.bmp.securitypolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.securitypolicy.domain.model.Machine;

/**
 * MachineRepository
 * 
 * @author yuanwf
 */
public interface MachineRepository extends JpaRepository<Machine, String> {
    Machine findByMacAndStatus(String mac, Integer status);

    Machine findByIpAndStatus(String ip, Integer status);

    @Query(name = "machine.countByCode", value = "select count(o.id) from Machine o where code = ?1 and id != ?2")
    Integer countByCodeAndId(String code, String id);

    @Query(name = "machine.countByName", value = "select count(o.id) from Machine o where name = ?1 and id != ?2")
    Integer countByNameAndId(String name, String id);

    @Query(name = "machine.countByIpAndStatus", value = "select count(o.id) from Machine o where ip = ?1 and status=?2 and id != ?3")
    Integer countByIpAndStatusAndId(String ip, Integer status, String id);

    @Query(name = "machine.countByMacAndStatus", value = "select count(o.id) from Machine o where mac = ?1 and status=?2 and id != ?3")
    Integer countByMacAndStatusAndId(String mac, Integer status, String id);

    @Query(name = "machine.countByMacAndStatus", value = "select count(o.id) from Machine o where mac = ?1 and ip=?2 and id != ?3")
    Integer countByMacAndIpAndId(String mac, String ip, String id);

    Integer countByIpAndStatus(String ip, Integer status);

    Integer countByMacAndStatus(String mac, Integer status);

    Integer countByMacAndIp(String mac, String ip);
}