package com.huigou.uasp.bmp.portal.mainpage.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.domain.model.resource.SysFunction;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.uasp.bmp.opm.repository.org.SysFunctionRepository;
import com.huigou.uasp.bmp.portal.mainpage.application.PersonDesktopApplication;
import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreen;
import com.huigou.uasp.bmp.portal.mainpage.domain.model.PersonDesktopScreenFunction;
import com.huigou.uasp.bmp.portal.mainpage.repository.PersonDesktopScreenFunctionRepository;
import com.huigou.uasp.bmp.portal.mainpage.repository.PersonDesktopScreenRepository;

@Service("personDesktopApplication")
public class PersonDesktopApplicationImpl extends BaseApplication implements PersonDesktopApplication {

    @Autowired
    @Qualifier("accessApplicationProxy")
    private AccessApplicationProxy accessApplication;

    @Autowired
    private PersonDesktopScreenRepository personDesktopScreenRepository;

    @Autowired
    private PersonDesktopScreenFunctionRepository personDesktopScreenFunctionRepository;

    @Autowired
    private SysFunctionRepository sysFunctionRepository;

    @Override
    public String savePersonDesktopScreen() {
        PersonDesktopScreen personDesktopScreen = new PersonDesktopScreen();

        Operator operator = ThreadLocalUtil.getOperator();
        personDesktopScreen.setPersonId(operator.getUserId());

        personDesktopScreen = this.personDesktopScreenRepository.save(personDesktopScreen);

        return personDesktopScreen.getId();
    }

    @Override
    @Transactional
    public void savePersonDesktopScreenFunctions(String screenId, List<String> functionIds) {
        Assert.hasText(screenId, "参数screenId不能为空。");
        PersonDesktopScreen personDesktopScreen = personDesktopScreenRepository.findOne(screenId);
        Assert.notNull(personDesktopScreen, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, screenId, "用户屏幕"));
        personDesktopScreenFunctionRepository.deleteByScreenId(screenId);
        if (functionIds != null && functionIds.size() > 0) {
            List<PersonDesktopScreenFunction> funcs = new ArrayList<PersonDesktopScreenFunction>(functionIds.size());
            int i = 0;
            for (String functionId : functionIds) {
                PersonDesktopScreenFunction fun = new PersonDesktopScreenFunction();
                fun.setFunctionId(functionId);
                fun.setScreenId(screenId);
                fun.setSequence(i++);
                funcs.add(fun);
            }
            personDesktopScreenFunctionRepository.save(funcs);
        }
    }

    @Override
    @Transactional
    public String savePersonDesktopScreenAndFunctions(String screenId, List<String> functionIds) {
        if (screenId.equals("0")) {
            screenId = this.savePersonDesktopScreen();
        }
        this.savePersonDesktopScreenFunctions(screenId, functionIds);
        return screenId;
    }

    @Override
    @Transactional
    public void deletePersonDesktopScreen(String id) {
        Assert.notNull(id, "参数ID不能为空。");
        PersonDesktopScreen personDesktopScreen = this.personDesktopScreenRepository.findOne(id);
        this.personDesktopScreenRepository.delete(personDesktopScreen);
        this.personDesktopScreenFunctionRepository.deleteByScreenId(id);
    }

    @Override
    public List<PersonDesktopScreen> queryPersonDesktopScreens() {
        Operator operator = ThreadLocalUtil.getOperator();
        return this.personDesktopScreenRepository.findByPersonId(operator.getUserId());
    }

    @Override
    public List<Map<String, Object>> queryPersonDesktopFunctions(String personId) {
        Assert.hasText(personId, "参数personId不能为空。");
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/common.xml", "personScreen");
        String sql = queryDescriptor.getSqlByName("queryByPersonId");
        return this.sqlExecutorDao.queryToListMap(sql, personId);
    }

    @Override
    public List<Map<String, Object>> queryPersonFunctions(String parentId) {
        Assert.hasText(parentId, "参数parentId不能为空。");
        Operator operator = ThreadLocalUtil.getOperator();
        if (SysFunction.ROOT_ID.equals(parentId)) {
            return accessApplication.queryPersonFunctions(operator.getUserId(), parentId);
        } else {
            return accessApplication.queryPersonOneLevelAllFunctions(operator.getUserId(), parentId);
        }
    }

    @Override
    public List<Map<String, Object>> queryJobFunctions(Long parentId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> queryOftenUseFunctions() {
        return null;
    }

}
