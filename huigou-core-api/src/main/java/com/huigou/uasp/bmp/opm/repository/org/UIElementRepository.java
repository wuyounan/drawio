package com.huigou.uasp.bmp.opm.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huigou.uasp.bmp.opm.domain.model.resource.UIElement;

public interface UIElementRepository extends JpaRepository<UIElement, String> {
    
    Integer countByIdIn(List<String> ids);

}
