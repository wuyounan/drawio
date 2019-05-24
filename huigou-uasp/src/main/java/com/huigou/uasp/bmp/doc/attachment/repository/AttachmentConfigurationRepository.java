package com.huigou.uasp.bmp.doc.attachment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.doc.attachment.domain.model.AttachmentConfiguration;

public interface AttachmentConfigurationRepository extends JpaRepository<AttachmentConfiguration, String>{

    AttachmentConfiguration findByCode(String code);
}
