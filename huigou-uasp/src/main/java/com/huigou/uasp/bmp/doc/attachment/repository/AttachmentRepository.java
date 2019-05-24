package com.huigou.uasp.bmp.doc.attachment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bmp.doc.attachment.domain.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {

    List<Attachment> findByBizId(String bizId);

    @Query(name = "attachment.findValidAttachmentsByBizKindAndBizId", value = "from Attachment where bizKindId = :bizKindId and bizId= :bizId and status = 1 order by sequence,version")
    List<Attachment> findValidAttachments(@Param("bizKindId") String bizKindId, @Param("bizId") String bizId);

    @Query(value = "from Attachment where bizKindId = :bizKindId and bizId= :bizId and bizSubKindId= :bizSubKindId  and status = 1 order by sequence,version")
    List<Attachment> findValidAttachments(@Param("bizKindId") String bizKindId, @Param("bizId") String bizId, @Param("bizSubKindId") String bizSubKindId);

    @Query(name = "attachment.countValidAttachments", value = "select count(*) from Attachment where bizKindId = :bizKindId and bizSubKindId = :bizSubKindId and bizId= :bizId and status = 1")
    long countValidAttachmentsBySubKindId(@Param("bizKindId") String bizKindId, @Param("bizSubKindId") String bizSubKindId, @Param("bizId") String bizId);

    @Query(name = "attachment.countValidAttachments", value = "select count(*) from Attachment where bizKindId = :bizKindId and bizId= :bizId and status = 1")
    long countValidAttachments(@Param("bizKindId") String bizKindId, @Param("bizId") String bizId);

}
