package com.huigou.uasp.bmp.bizconfig.function.application.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.cache.DictUtil;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.i18n.annotation.I18n;
import com.huigou.data.i18n.annotation.I18nMapping;
import com.huigou.data.i18n.annotation.I18nResource;
import com.huigou.data.i18n.model.I18nInitResourceInterface;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.bizconfig.function.application.FunctionGroupPermissionsApplication;
import com.huigou.uasp.bmp.bizconfig.function.application.GroupFunctionApplication;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctions;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetails;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsDetailsDto;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroup;
import com.huigou.uasp.bmp.bizconfig.function.domain.model.BpmFunctionsGroupDto;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsDetailsQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsGroupQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.domain.query.FunctionsQueryRequest;
import com.huigou.uasp.bmp.bizconfig.function.repository.BpmFunctionsDetailsRepository;
import com.huigou.uasp.bmp.bizconfig.function.repository.BpmFunctionsGroupRepository;
import com.huigou.uasp.bmp.bizconfig.function.repository.BpmFunctionsRepository;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.proxy.AccessApplicationProxy;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.StringUtil;

/**
 * 业务功能分组管理
 * 
 * @ClassName: BizFunctionApplicationImpl
 * @author
 * @date 2018-03-28 11:16
 * @version V1.0
 */
@Service("groupFunctionApplication")
public class GroupFunctionApplicationImpl extends BaseApplication implements GroupFunctionApplication, I18nInitResourceInterface {
    @Autowired
    private BpmFunctionsRepository bpmFunctionsRepository;

    @Autowired
    private BpmFunctionsGroupRepository bpmFunctionsGroupRepository;

    @Autowired
    private BpmFunctionsDetailsRepository bpmFunctionsDetailsRepository;

    @Autowired
    private AccessApplicationProxy accessApplication;

    @Override
    public Map<String, String> getGroupColorMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("alert-success", "绿色");
        map.put("alert-info", "蓝色");
        map.put("alert-warning", "黄色");
        map.put("alert-danger", "红色");
        return map;
    }

    @Override
    public Map<String, String> getFuncColorMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("btn-danger", "红色");
        map.put("btn-primary", "深蓝色");
        map.put("btn-warning", "黄色");
        map.put("btn-info", "浅蓝色");
        map.put("btn-success", "绿色");
        return map;
    }

    @Override
    @Transactional
    public String saveFunctions(BpmFunctions functions) {
        Assert.notNull(functions, CommonDomainConstants.OBJECT_NOT_NULL);
        functions = (BpmFunctions) this.commonDomainService.loadAndFillinProperties(functions);
        functions = (BpmFunctions) this.commonDomainService.saveBaseInfoEntity(functions, bpmFunctionsRepository);
        return functions.getId();
    }

    @Override
    public BpmFunctions loadFunctions(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return bpmFunctionsRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteFunctions(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        bpmFunctionsRepository.delete(id);
        bpmFunctionsGroupRepository.deleteByBpmFunctionsId(id);
        bpmFunctionsDetailsRepository.deleteByBpmFunctionsId(id);
    }

    @Override
    public Map<String, Object> slicedQueryFunctions(FunctionsQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "functions");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("isToHide", DictUtil.getDictionary("yesorno"));
        return this.sqlExecutorDao.executeSlicedQuery(model);
    }

    @Override
    @Transactional
    public String saveFunctionsGroup(BpmFunctionsGroup functionsGroup) {
        Assert.notNull(functionsGroup, CommonDomainConstants.OBJECT_NOT_NULL);
        if (functionsGroup.isNew()) {
            functionsGroup.setSequence(this.commonDomainService.getNextSequence(BpmFunctionsGroup.class, "bpmFunctionsId", functionsGroup.getBpmFunctionsId()));
        }
        functionsGroup = (BpmFunctionsGroup) this.commonDomainService.loadAndFillinProperties(functionsGroup);
        functionsGroup = bpmFunctionsGroupRepository.save(functionsGroup);
        return functionsGroup.getId();
    }

    @Override
    public BpmFunctionsGroup loadFunctionsGroup(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return bpmFunctionsGroupRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteFunctionsGroup(List<String> ids) {
        List<BpmFunctionsGroup> objs = bpmFunctionsGroupRepository.findAll(ids);
        for (BpmFunctionsGroup obj : objs) {
            Integer count = bpmFunctionsDetailsRepository.countByFunctionsGroupId(obj.getId());
            Assert.isTrue(count == 0, String.format("%s存在功能定义不能删除!", obj.getNameZh()));
        }
        bpmFunctionsGroupRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryFunctionsGroup(FunctionsGroupQueryRequest queryRequest) {
        queryRequest.checkConstraints();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "functionsGroup");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("color", this.getGroupColorMap());
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    @Transactional
    public void updateFunctionsGroupSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(BpmFunctionsGroup.class, map);
    }

    @Override
    @Transactional
    public String saveFunctionsDetails(BpmFunctionsDetails functionsDetails) {
        Assert.notNull(functionsDetails, CommonDomainConstants.OBJECT_NOT_NULL);
        if (functionsDetails.isNew()) {
            functionsDetails.setSequence(this.commonDomainService.getNextSequence(BpmFunctionsDetails.class, "bpmFunctionsId",
                                                                                  functionsDetails.getBpmFunctionsId()));
        }
        functionsDetails = (BpmFunctionsDetails) this.commonDomainService.loadAndFillinProperties(functionsDetails);
        functionsDetails = bpmFunctionsDetailsRepository.save(functionsDetails);
        return functionsDetails.getId();
    }

    @Override
    public BpmFunctionsDetails loadFunctionsDetails(String id) {
        Assert.hasText(id, CommonDomainConstants.ID_NOT_BLANK);
        return bpmFunctionsDetailsRepository.findOne(id);
    }

    @Override
    @Transactional
    public void deleteFunctionsDetails(List<String> ids) {
        List<BpmFunctionsDetails> objs = bpmFunctionsDetailsRepository.findAll(ids);
        bpmFunctionsDetailsRepository.delete(objs);
    }

    @Override
    public Map<String, Object> queryFunctionsDetails(FunctionsDetailsQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "functionsDetails");
        QueryModel model = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest);
        model.putDictionary("color", this.getFuncColorMap());
        return this.sqlExecutorDao.executeQuery(model);
    }

    @Override
    @Transactional
    public void updateFunctionsDetailsSequence(Map<String, Integer> map) {
        Assert.notNull(map, MessageSourceContext.getMessage("object.not.null"));
        this.commonDomainService.updateSequence(BpmFunctionsDetails.class, map);
    }

    @Override
    @Transactional
    public void updateFunctionsDetailsGroup(List<String> ids, String groupId) {
        Assert.notNull(ids, MessageSourceContext.getMessage("object.not.null"));
        Assert.hasText(groupId, "groupId不能为空!");
        this.commonDomainService.move(BpmFunctionsDetails.class, ids, "functions_group_id", groupId);
    }

    @Override
    @Transactional
    public void updateFunctionsDetailsColor(List<String> ids, String color) {
        Assert.notNull(ids, MessageSourceContext.getMessage("object.not.null"));
        Assert.hasText(color, "color不能为空!");
        this.commonDomainService.move(BpmFunctionsDetails.class, ids, "color", color);
    }

    private List<BpmFunctionsGroup> findGroupById(String id) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "functionsGroup");
        String sql = queryDescriptor.getSqlByName("queryFunctionsGroupById");
        return this.sqlExecutorDao.queryToList(sql, BpmFunctionsGroup.class, id);
    }

    private List<BpmFunctionsDetails> findFunctionsDetailsById(String id) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "functionsDetails");
        String sql = queryDescriptor.getSqlByName("queryFunctionsDetailsById");
        return this.sqlExecutorDao.queryToList(sql, BpmFunctionsDetails.class, id);
    }

    @Override
    @I18nMapping(i18n = @I18n(name = "name", code = "groupFunction.{code}.{id}"))
    public List<BpmFunctionsGroupDto> queryFunctionsGroup(String code) {
        Assert.hasText(code, "编码不能为空!");
        List<BpmFunctions> functions = bpmFunctionsRepository.findByCode(code);
        Assert.notEmpty(functions, String.format("未找到[%s]对应的功能分组定义!", code));
        return this.queryFunctionsGroup(functions.get(0));
    }

    /**
     * 获取分组及权限
     * 
     * @param functions
     * @return
     */
    private List<BpmFunctionsGroupDto> queryFunctionsGroup(BpmFunctions functions) {
        String beanName = functions.getCheckBeanName();
        List<BpmFunctionsGroup> groups = this.findGroupById(functions.getId());
        List<BpmFunctionsDetails> details = this.findFunctionsDetailsById(functions.getId());
        Locale locale = MessageSourceContext.getLocale();
        boolean isChina = locale.getLanguage().equalsIgnoreCase("zh");
        List<BpmFunctionsGroupDto> groupDtos = new ArrayList<>(groups.size());
        for (BpmFunctionsGroup group : groups) {
            BpmFunctionsGroupDto dto = new BpmFunctionsGroupDto();
            dto.setCode(functions.getCode());
            dto.setId(group.getId());
            dto.setColor(group.getColor());
            if (isChina) {
                dto.setName(group.getNameZh());
            } else {
                if (StringUtil.isNotBlank(group.getNameEn())) {
                    dto.setName(group.getNameEn());
                } else {
                    dto.setName(group.getNameZh());
                }
            }
            groupDtos.add(dto);
        }
        List<BpmFunctionsDetailsDto> detailDtos = new ArrayList<>(details.size());
        for (BpmFunctionsDetails detail : details) {
            BpmFunctionsDetailsDto dto = new BpmFunctionsDetailsDto();
            dto.setCode(detail.getCode());
            dto.setGroupId(detail.getFunctionsGroupId());
            dto.setColor(detail.getColor());
            dto.setIcon(detail.getIcon());
            dto.setSequence(detail.getSequence());
            if (isChina) {
                dto.setName(detail.getNameZh());
            } else {
                if (StringUtil.isNotBlank(detail.getNameEn())) {
                    dto.setName(detail.getNameEn());
                } else {
                    dto.setName(MessageSourceContext.getMessageAsDefault(String.format("function.%s.name", detail.getCode()), detail.getNameZh()));
                }
            }
            if (StringUtil.isBlank(detail.getUrl())) {
                dto.setUrl(detail.getFunUrl());
            } else {
                dto.setUrl(detail.getUrl());
            }
            detailDtos.add(dto);
        }
        FunctionGroupPermissionsApplication application = null;
        if (StringUtil.isNotBlank(beanName)) {
            application = ApplicationContextWrapper.getBean(beanName, FunctionGroupPermissionsApplication.class);
        }
        // 校验权限
        if (application == null) {
            detailDtos = this.checkFunctions(detailDtos);
        } else {
            detailDtos = application.checkFunctions(detailDtos);
        }
        Integer isToHide = functions.getIsToHide();
        isToHide = isToHide == null ? 0 : isToHide;
        // 无权限时隐藏功能节点
        if (isToHide == 1) {
            detailDtos = this.noPermissionToRemove(detailDtos);
        }
        // 组合功能到分组中
        for (BpmFunctionsDetailsDto dto : detailDtos) {
            for (BpmFunctionsGroupDto group : groupDtos) {
                if (StringUtil.isNotBlank(dto.getGroupId())) {
                    if (dto.getGroupId().equals(group.getId())) {
                        group.addFuns(dto);
                    }
                }
            }
        }
        return groupDtos;
    }

    /**
     * 默认系统权限校验
     * 
     * @param nodeFunctions
     * @return
     */
    private List<BpmFunctionsDetailsDto> checkFunctions(List<BpmFunctionsDetailsDto> datas) {
        Operator operator = ThreadLocalUtil.getOperator();
        List<BpmFunctionsDetailsDto> objs = new ArrayList<>();
        if (datas == null || datas.size() == 0) {
            return objs;
        }
        for (BpmFunctionsDetailsDto dto : datas) {
            dto.setHasPermission(accessApplication.checkPersonFunPermissions(operator.getUserId(), dto.getCode()));
            objs.add(dto);
        }
        return objs;
    }

    /**
     * 删除无权限的功能节点
     * 
     * @param datas
     * @return
     */
    private List<BpmFunctionsDetailsDto> noPermissionToRemove(List<BpmFunctionsDetailsDto> datas) {
        List<BpmFunctionsDetailsDto> objs = new ArrayList<>();
        if (datas == null || datas.size() == 0) {
            return objs;
        }
        for (BpmFunctionsDetailsDto dto : datas) {
            if (dto.isHasPermission()) {
                objs.add(dto);
            }
        }
        return objs;
    }

    @Override
    @I18nResource(i18n = { @I18n(name = "name", code = "groupFunction.{code}.{id}") })
    public List<?> loadI18nInitResources() {
        String sql = "select t.id, t.name_zh as name, f.code from bpm_functions_group t, bpm_functions f where t.bpm_functions_id = f.id and f.status = 1";
        return this.sqlExecutorDao.queryToListMap(sql);
    }

}
