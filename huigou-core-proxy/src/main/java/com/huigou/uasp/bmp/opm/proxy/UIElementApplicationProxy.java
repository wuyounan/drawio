package com.huigou.uasp.bmp.opm.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huigou.data.domain.service.CommonDomainService;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.uasp.bmp.opm.application.UIElementApplication;
import com.huigou.uasp.bmp.opm.domain.model.resource.UIElement;
import com.huigou.uasp.bmp.opm.domain.query.UIElementsQueryRequest;
import com.huigou.uasp.bmp.opm.impl.UIElementApplicationImpl;
import com.huigou.uasp.bmp.opm.repository.org.UIElementRepository;

@Service("uiElementApplicationProxy")
public class UIElementApplicationProxy {

    @Autowired
    private CommonDomainService commonDomainService;

    @Autowired
    private SQLExecutorDao sqlExecutorDao;

    @Autowired
    private UIElementRepository uiElementRepository;

    @Autowired
    private CoreApplicationFactory coreApplicationFactory;

    private UIElementApplication uiElementApplication;

    void initProperties(UIElementApplicationImpl uiElementApplicationImpl) {
        uiElementApplicationImpl.setCommonDomainService(commonDomainService);
        uiElementApplicationImpl.setSqlExecutorDao(sqlExecutorDao);
        uiElementApplicationImpl.setUIElementRepository(uiElementRepository);
    }

    private UIElementApplication getUIElementApplication() {
        if (uiElementApplication == null) {
            //synchronized (UIElementApplicationProxy.class) {
                if (uiElementApplication == null) {
                    UIElementApplicationImpl uiElementApplicationImpl = coreApplicationFactory.getUIElementApplication();
                    uiElementApplication = uiElementApplicationImpl;
                }
            //}
        }
        return uiElementApplication;
    }

    @Transactional
    public String saveUIElement(UIElement uiElement) {
        return getUIElementApplication().saveUIElement(uiElement);
    }

    public UIElement loadUIElement(String id) {
        return getUIElementApplication().loadUIElement(id);
    }

    @Transactional
    public void deleteUIElements(List<String> ids) {
        getUIElementApplication().deleteUIElements(ids);
    }

    public Map<String, Object> slicedQueryUIElements(UIElementsQueryRequest queryRequest) {
        return getUIElementApplication().slicedQueryUIElements(queryRequest);
    }

    @Transactional
    public void updateUIElementsStatus(List<String> ids, Integer status) {
        getUIElementApplication().updateUIElementsStatus(ids, status);
    }

    @Transactional
    public Integer getUIElementNextSequence(String folderId) {
        return getUIElementApplication().getUIElementNextSequence(folderId);
    }

    @Transactional
    public void updateUIElementsSequence(Map<String, Integer> params) {
        getUIElementApplication().updateUIElementsSequence(params);
    }

    @Transactional
    public void moveUIElements(List<String> ids, String folderId) {
        getUIElementApplication().moveUIElements(ids, folderId);
    }

}
