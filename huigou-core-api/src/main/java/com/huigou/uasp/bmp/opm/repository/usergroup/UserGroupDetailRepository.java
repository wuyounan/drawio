package com.huigou.uasp.bmp.opm.repository.usergroup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.huigou.uasp.bmp.opm.domain.model.usergroup.UserGroupDetail;

public interface UserGroupDetailRepository extends JpaRepository<UserGroupDetail, String> {
    List<UserGroupDetail> findByGroupId(String groupId);

    @Modifying
    @Query("delete UserGroupDetail a where a.groupId = ?1")
    void deleteByGroupId(String groupId);

}
