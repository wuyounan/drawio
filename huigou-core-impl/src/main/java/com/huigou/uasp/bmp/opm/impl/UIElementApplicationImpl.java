package com.huigou.uasp.bmp.opm.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.opm.application.UIElementApplication;
import com.huigou.uasp.bmp.opm.domain.model.resource.UIElement;
import com.huigou.uasp.bmp.opm.domain.query.UIElementsQueryRequest;
import com.huigou.uasp.bmp.opm.repository.org.UIElementRepository;

public class UIElementApplicationImpl extends BaseApplication implements UIElementApplication {

    private UIElementRepository uiElementRepository;

    public void setUIElementRepository(UIElementRepository uiElementRepository) {
        this.uiElementRepository = uiElementRepository;
    }

    @Override
    public String saveUIElement(UIElement uiElement) {
        Assert.notNull(uiElement, "参数uiElement不能为空。");
        uiElement = this.commonDomainService.loadAndFillinProperties(uiElement, UIElement.class);
        uiElement = (UIElement) this.commonDomainService.saveBaseInfoWithFolderEntity(uiElement, uiElementRepository);
        return uiElement.getId();
    }

    @Override
    public UIElement loadUIElement(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return this.uiElementRepository.findOne(id);
    }

    @Override
    public void deleteUIElements(List<String> ids) {
        Assert.notEmpty(ids, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        List<UIElement> uiElements = this.uiElementRepository.findAll(ids);
        Assert.isTrue(ids.size() == uiElements.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "界面元素"));

        // TODO 删除逻辑检查

        this.uiElementRepository.deleteInBatch(uiElements);
    }

    public void updateUIElementsStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(UIElement.class, ids, status);
    }

    @Override
    public Integer getUIElementNextSequence(String folderId) {
        return this.commonDomainService.getNextSequence(UIElement.class, CommonDomainConstants.FOLDER_ID_FIELD_NAME, folderId);
    }

    @Override
    public void updateUIElementsSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(UIElement.class, params);
    }

    @Override
    @Transactional
    public void moveUIElements(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(UIElement.class, ids, folderId);
    }

    @Override
    public Map<String, Object> slicedQueryUIElements(UIElementsQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "uiElement");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }
}
