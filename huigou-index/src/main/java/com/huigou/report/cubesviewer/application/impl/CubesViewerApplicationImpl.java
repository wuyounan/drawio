package com.huigou.report.cubesviewer.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.SerializeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.CommonDomainConstants;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.domain.ValidStatus;
import com.huigou.report.cubesviewer.application.CubesViewerApplication;
import com.huigou.report.cubesviewer.domain.model.CubesViewerDefinition;
import com.huigou.report.cubesviewer.domain.model.CubesViewerOperatorQueryScheme;
import com.huigou.report.cubesviewer.repository.CubesViewerDefinitionRepository;
import com.huigou.report.cubesviewer.repository.CubesViewerOperatorQuerySchemeRepository;
import com.huigou.report.cubesviewer.util.CubesViewerParameterParser;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.util.HttpClientUtil;
import com.huigou.util.McsUtil;
import com.huigou.util.StringPool;



@Service("cubesViewerApplication")
public class CubesViewerApplicationImpl extends BaseApplication implements CubesViewerApplication {

    // private static Logger logger = LoggerFactory.getLogger(CubesViewerApplicationImpl.class);

    private static String COM_TEXT_NEW_STRING = "/com_text_new";

    private static String AGGREGATE_AJAX_STRING = "/aggregate.ajax";

    private static String ORGAN_NAME_STRING = "/organ_name";

    private static String RUT_NAME_STRING = "/rut_name";

    @Value("${cube.server.url}")
    private String cubesServerUrl;

    @Autowired
    private CubesViewerDefinitionRepository cubesViewerDefinitionRepository;

    @Autowired
    private CubesViewerParameterParser cubesViewerParameterParser;

   

    @Autowired
    private CubesViewerOperatorQuerySchemeRepository cubesViewerOperatorQuerySchemeRepository;

   

    @Qualifier("hanaRedisManager")
    @Autowired(required = false)
    private RedisManager redisManager;

    @Override
    public String getCubesServerUrl() {
        return cubesServerUrl;
    }

    @Override
    public Map<String, Object> getCubesViewerDefinition(List<String> cubesViewerCodes) {
        Assert.notEmpty(cubesViewerCodes, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "cubesViewerCodes"));

        List<CubesViewerDefinition> cubesViewerDefinitions = cubesViewerDefinitionRepository.findByCodeInAndStatus(cubesViewerCodes,
                                                                                                                   ValidStatus.ENABLED.getId());

        String coveredJson;
        StringBuilder sb = new StringBuilder();
        List<String> cubes = new ArrayList<String>(cubesViewerCodes.size());
        for (CubesViewerDefinition item : cubesViewerDefinitions) {
            coveredJson = cubesViewerParameterParser.parseFilterParamter(item.getJson());
          
            cubes.add(coveredJson);
            sb.append(item.getName());
        }

        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("cubes", cubes);
        result.put("cubeName", sb.toString());

        return result;
    }

    private String getCubesViewerOperatorQuerySchemeSqlByName(String sqlName) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, CUBES_VIEWER_OPERATOR_QUERY_SCHEME_ENTITY_NAME);
        return queryDescriptor.getSqlByName(sqlName);
    }

    @Override
    @Transactional
    public String saveCubesViewerOperatorQueryScheme(CubesViewerOperatorQueryScheme cubesViewerOperatorQueryScheme) {
        Assert.notNull(cubesViewerOperatorQueryScheme, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "cubesViewerOperatorQueryScheme"));

        cubesViewerOperatorQueryScheme.checkConstraints();

        String sql = this.getCubesViewerOperatorQuerySchemeSqlByName("findDuplicationEnities");

        Map<String, Object> params = new HashMap<String, Object>(4);

        boolean isNew = cubesViewerOperatorQueryScheme.isNew();
        params.put(CommonDomainConstants.ID_FIELD_NAME, isNew ? StringPool.AT : cubesViewerOperatorQueryScheme.getId());
        params.put("createdById", ThreadLocalUtil.getOperator().getPersonMemberId());
        params.put("functionCode", cubesViewerOperatorQueryScheme.getFunctionCode());
        params.put(CommonDomainConstants.NAME_FIELD_NAME, cubesViewerOperatorQueryScheme.getName().toUpperCase());
        long count = this.generalRepository.coungByNativeSql(sql, params);
        Assert.isTrue(count == 0L, MessageSourceContext.getMessage(MessageConstants.NAME_NOT_DUPLICATE));

        cubesViewerOperatorQueryScheme = this.cubesViewerOperatorQuerySchemeRepository.save(cubesViewerOperatorQueryScheme);

        return cubesViewerOperatorQueryScheme.getId();
    }

    @Override
    @Transactional
    public void renameCubesViewerOperatorQueryScheme(String cubesViewerOperatorQuerySchemeId, String newName) {
        Assert.hasText(cubesViewerOperatorQuerySchemeId, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "cubesViewerOperatorQuerySchemeId"));
        Assert.hasText(newName, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "newName"));

        CubesViewerOperatorQueryScheme cubesViewerOperatorQueryScheme = this.cubesViewerOperatorQuerySchemeRepository.findOne(cubesViewerOperatorQuerySchemeId);
        Assert.state(cubesViewerOperatorQueryScheme != null,
                     String.format(CommonDomainConstants.OBJECT_NOT_FOUND_BY_ID, cubesViewerOperatorQuerySchemeId, "用户查询方案"));

        cubesViewerOperatorQueryScheme.setName(newName);

        this.cubesViewerOperatorQuerySchemeRepository.save(cubesViewerOperatorQueryScheme);
    }

    @Override
    @Transactional
    public void deleteCubesViewerOperatorQueryScheme(String id) {
        this.checkIdNotBlank(id);
        this.cubesViewerOperatorQuerySchemeRepository.delete(id);
    }

    @Override
    public List<Map<String, Object>> queryCubesViewerOperatorQuerySchemesForCurrentOperator(String functionCode) {
        Assert.hasText(functionCode, String.format(CommonDomainConstants.PARAMETER_NOT_NULL_FORMAT, "functionCode"));
        String sql = this.getCubesViewerOperatorQuerySchemeSqlByName("queryForCurrentOperator");
        return this.sqlExecutorDao.queryToListMap(sql, ThreadLocalUtil.getOperator().getPersonMemberId(), functionCode);
    }

    @Override
    public String callCubesViewerService(String cubesViewerAction, String queryString) {
        // TODO 验证规则 如日期范围
        /*if (!gdycFun.isProvince()) {
            // 查询com_text_new:com_text_new 直接返回权限数据
            if (cubesViewerAction.indexOf(COM_TEXT_NEW_STRING) > -1) {
                return this.dataPrivilegeConstructor.getPrivilegeOrgNamesJson();
            }
            // 查询organ_name\rut_name 时添加com_text_new:com_text_new的权限数据
            if (cubesViewerAction.indexOf(ORGAN_NAME_STRING) > -1 || cubesViewerAction.indexOf(RUT_NAME_STRING) > -1) {
                queryString = this.dataPrivilegeConstructor.applyDataPrivilegeForUrlOrganName(queryString);
            }
            // 聚合数据时验证queryString的com_text_new:com_text_new权限
            if (cubesViewerAction.indexOf(AGGREGATE_AJAX_STRING) > -1) {
                queryString = this.dataPrivilegeConstructor.applyDataPrivilegeForUrlAggregate(queryString);
            }
        }*/

        if ((cubesViewerAction != null) && (cubesViewerAction.length() > 0)) {
            int index = cubesViewerAction.lastIndexOf('.');
            if ((index > -1) && (index < (cubesViewerAction.length() - 1))) {
                cubesViewerAction = cubesViewerAction.substring(0, index);
            }
        }

        String url = this.getCubesServerUrl() + cubesViewerAction;
        if (queryString != null) {
            url += "?" + queryString;
        }

        String hashCode = Hashing.md5().newHasher().putString(url, Charsets.UTF_8).hash().toString();
        byte[] cacheKey = McsUtil.getCacheKey(McsUtil.CUBES_VIEWER_CACHE_KIND, hashCode);
        synchronized (hashCode.intern()) {
            byte[] cachedValue = redisManager.get(cacheKey);
            String result;
            if (cachedValue != null) {
                result = (String) SerializeUtils.deserialize(cachedValue);
            } else {
                result = HttpClientUtil.get(url);
                byte[] value = SerializeUtils.serialize(result);

                this.redisManager.set(cacheKey, value, redisManager.getExpire());
            }
            return result;
        }
    }
}
