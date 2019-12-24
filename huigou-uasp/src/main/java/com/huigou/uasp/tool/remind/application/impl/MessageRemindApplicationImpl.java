package com.huigou.uasp.tool.remind.application.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.data.domain.model.BaseInfoStatus;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.exception.ApplicationException;
import com.huigou.express.ExpressManager;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.tool.remind.RemindOpenKind;
import com.huigou.uasp.tool.remind.RemindReplaceKind;
import com.huigou.uasp.tool.remind.application.MessageRemindApplication;
import com.huigou.uasp.tool.remind.domain.model.MessageRemind;
import com.huigou.uasp.tool.remind.domain.query.MessageRemindQueryRequest;
import com.huigou.uasp.tool.remind.repository.MessageRemindRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * 消息提醒配置后台
 * 
 * @ClassName: MessageRemindApplicationImpl
 * @author xx
 * @date 2017-02-15 14:39
 * @version V1.0
 */
@Service("messageRemindApplication")
public class MessageRemindApplicationImpl extends BaseApplication implements MessageRemindApplication {

    @Autowired
    private MessageRemindRepository messageRemindRepository;

    @Transactional
    @Override
    public String save(MessageRemind messageRemind) {
        Assert.notNull(messageRemind, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL));
        messageRemind = this.commonDomainService.loadAndFillinProperties(messageRemind, MessageRemind.class);
        if (messageRemind.isNew() && messageRemind.getStatus() == null) {
            messageRemind.setStatus(BaseInfoStatus.ENABLED.getId());
        }
        this.commonDomainService.checkBaseInfoEntity(messageRemind);
        messageRemind = messageRemindRepository.save(messageRemind);
        return messageRemind.getId();
    }

    @Override
    public MessageRemind load(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        return messageRemindRepository.findOne(id);
    }

    @Override
    @Transactional
    public void delete(String[] ids) {
        for (String id : ids) {
            messageRemindRepository.delete(id);
        }
    }

    @Override
    public Map<String, Object> slicedQuery(MessageRemindQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/messageRemind.xml", "messageRemind");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        queryModel.putDictionary("openKind", RemindOpenKind.getData());
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    @Transactional
    public void updateStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(MessageRemind.class, ids, status);
    }

    @Transactional
    @Override
    public void updateFolderId(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(MessageRemind.class, ids, folderId);
    }

    @Transactional
    @Override
    public void updateSequence(Map<String, Integer> params) {
        this.commonDomainService.updateSequence(MessageRemind.class, params);
    }

    /**
     * 测试提醒执行结果
     * 
     * @author
     * @param id
     *            void
     */
    public List<Object> testParseRemindFun(String id) {
        List<Object> list = new java.util.ArrayList<Object>();
        MessageRemind messageRemind = this.messageRemindRepository.findOne(id);
        Object obj = parseRemindFun(messageRemind);
        if (obj != null) {
            if (ClassHelper.isInterface(obj.getClass(), List.class)) {
                list.addAll((List<?>) obj);
            } else {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 解析定义的函数组合提示信息
     * 
     * @author
     * @param map
     * @return Object
     */
    private Object parseRemindFun(MessageRemind messageRemind) {
        String executeFunc = messageRemind.getExecuteFunc();
        String remindTitle = messageRemind.getRemindTitle();
        String remindUrl = messageRemind.getRemindUrl();
        String name = messageRemind.getName();
        String code = messageRemind.getCode();
        if (StringUtil.isBlank(executeFunc)) {
            return null;
        }
        // URL打开方式
        RemindOpenKind openKind = RemindOpenKind.findById(messageRemind.getOpenKind());
        // 替换方式
        RemindReplaceKind replaceKind = RemindReplaceKind.findById(messageRemind.getReplaceKind());
        if (replaceKind == RemindReplaceKind.ORDER) {// 顺序替换
            String[] executeFuncs = executeFunc.split(",");
            List<Object> list = new java.util.ArrayList<Object>(executeFuncs.length);
            for (String fun : executeFuncs) {
                try {
                    Object value = ExpressManager.evaluate(fun);
                    if (ClassHelper.isBaseType(value.getClass())) {
                        list.add(value);
                    } else {
                        throw new ApplicationException("返回结果不是基础数据类型!");
                    }
                } catch (Exception e) {
                    LogHome.getLog(this).error("evaluateExpr:" + fun, e);
                    throw new ApplicationException("evaluateExpr:" + fun, e);
                }
            }
            try {
                Map<String, Object> result = new HashMap<String, Object>(3);
                String parseRemindTitle = StringUtil.patternParser(remindTitle, list);// 解析显示内容
                String parseRemindUrl = StringUtil.patternParser(remindUrl, list);// 解析显示URL
                result.put("remindTitle", parseRemindTitle);
                result.put("remindUrl", parseRemindUrl);
                result.put("openKind", openKind.getId());
                result.put("name", name);
                result.put("code", code);
                return result;
            } catch (Exception e) {
                throw new ApplicationException(e);
            }
        } else if (replaceKind == RemindReplaceKind.NAME) {// 名称替换
            try {
                // 执行表示式
                Object value = ExpressManager.evaluate(executeFunc);
                if (ClassHelper.isInterface(value.getClass(), Map.class)) {// 返回结果为map
                    Map<String, Object> result = new HashMap<String, Object>(3);
                    String parseRemindTitle = StringUtil.patternParser(remindTitle, (Map<?, ?>) value);// 解析显示内容
                    String parseRemindUrl = StringUtil.patternParser(remindUrl, (Map<?, ?>) value);// 解析显示URL
                    result.put("remindTitle", parseRemindTitle);
                    result.put("remindUrl", parseRemindUrl);
                    result.put("openKind", openKind.getId());
                    result.put("name", name);
                    result.put("code", code);
                    return result;
                } else if (ClassHelper.isInterface(value.getClass(), List.class)) {// 返回结果为List
                    List<?> list = (List<?>) value;
                    int length = list.size();
                    List<Map<String, Object>> result = new java.util.ArrayList<Map<String, Object>>(length);
                    for (int i = 0; i < length; i++) {
                        Map<String, Object> resultMap = new HashMap<String, Object>(3);
                        String parseRemindTitle = StringUtil.patternParser(remindTitle, (Map<?, ?>) list.get(i));// 解析显示内容
                        String parseRemindUrl = StringUtil.patternParser(remindUrl, (Map<?, ?>) list.get(i));// 解析显示URL
                        resultMap.put("remindTitle", parseRemindTitle);
                        resultMap.put("remindUrl", parseRemindUrl);
                        resultMap.put("openKind", openKind.getId());
                        resultMap.put("name", name);
                        resultMap.put("code", code);
                        result.add(resultMap);
                    }
                    return result;
                }
            } catch (Exception e) {
                LogHome.getLog(this).error("evaluateExpr:" + executeFunc, e);
                throw new ApplicationException("evaluateExpr:" + executeFunc, e);
            }
        } else if (replaceKind == RemindReplaceKind.EXIST) {// 存在则显示
            List<Object> list = new java.util.ArrayList<Object>(1);
            Object value = null;
            try {
                value = ExpressManager.evaluate(executeFunc);
                if (ClassHelper.isBaseType(value.getClass())) {
                    list.add(value);
                } else {
                    throw new ApplicationException("返回结果不是基础数据类型!");
                }
            } catch (Exception e) {
                LogHome.getLog(this).error("evaluateExpr:" + executeFunc, e);
                throw new ApplicationException("evaluateExpr:" + executeFunc, e);
            }
            if (value != null && ClassHelper.convert(value, BigDecimal.class).doubleValue() > 0) {
                try {
                    Map<String, Object> result = new HashMap<String, Object>(3);
                    String parseRemindTitle = StringUtil.patternParser(remindTitle, list);// 解析显示内容
                    String parseRemindUrl = StringUtil.patternParser(remindUrl, list);// 解析显示URL
                    result.put("remindTitle", parseRemindTitle);
                    result.put("remindUrl", parseRemindUrl);
                    result.put("openKind", openKind.getId());
                    result.put("name", name);
                    result.put("code", code);
                    return result;
                } catch (Exception e) {
                    throw new ApplicationException(e);
                }
            }
        }
        return null;
    }

    @Override
    public List<Object> queryRemindByPersonId(String personId) {
        List<Object> list = new java.util.ArrayList<Object>();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery("config/uasp/query/bmp/messageRemind.xml", "messageRemind");
        String sql = queryDescriptor.getSqlByName("queryRemindByPersonId");
        Map<String, Object> params = new HashMap<>(1);
        params.put("personId", personId);
        List<MessageRemind> reminds = this.sqlExecutorDao.queryToListByMapParam(sql, MessageRemind.class, params);
        for (MessageRemind messageRemind : reminds) {
            Object obj = parseRemindFun(messageRemind);
            if (obj != null) {
                if (ClassHelper.isInterface(obj.getClass(), List.class)) {
                    list.addAll((List<?>) obj);
                } else {
                    list.add(obj);
                }
            }
        }
        return list;
    }

}
