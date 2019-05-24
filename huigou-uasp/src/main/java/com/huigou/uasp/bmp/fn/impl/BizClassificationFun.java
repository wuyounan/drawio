package com.huigou.uasp.bmp.fn.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.bizClassification.application.BizClassificationApplication;
import com.huigou.uasp.bmp.common.application.BaseApplication;
/**
 * 业务参数系统函数
 * 
 * @author gongmm
 */
@Service("bizClassificationFun")
public class BizClassificationFun extends BaseApplication {

    @Resource
    private BizClassificationApplication application;

    private String getSqlByName(String name) {
        QueryDescriptor queryDescriptor = sqlExecutorDao.getQuery("config/uasp/query/bmp/bizClassificationFun.xml", "bizClassificationFun");
        return queryDescriptor.getSqlByName(name);
    }

    /**
     * 查询组织业务参数
     * 
     * @param orgId
     *            组织ID
     * @param bizCode
     *            业务编码
     * @param parameterCode
     *            参数编码
     * @return
     */
    public Object findBusinessParameter(String orgId, String bizCode, String parameterCode) {
        Assert.notNull(orgId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "orgId"));
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByOrgId");
        Object obj = this.sqlExecutorDao.queryToObject(sql, Object.class, bizCode, parameterCode, orgId);
        return obj;
    }

    /**
     * 查询组织业务参数
     * 
     * @param fullId
     * @param bizCode
     *            业务编码
     * @param parameterCode
     *            参数编码
     * @return
     */
    public Object findBusinessParameterByFullId(String fullId, String bizCode, String parameterCode) {
        Assert.notNull(fullId, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL, "fullId"));
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByFullId");
        Object obj = this.sqlExecutorDao.queryToObject(sql, Object.class, bizCode, parameterCode, fullId);
        return obj;
    }

    /**
     * 查询组织业务参数
     * 
     * @param orgId
     *            组织ID
     * @param bizCode
     *            业务编码
     * @param parameterCode
     *            参数编码
     * @return
     */
    public String findStringBusinessParameter(String orgId, String bizCode, String parameterCode) {
        Assert.notNull(orgId, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "orgId"));
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByOrgId");
        String str = this.sqlExecutorDao.queryToObject(sql, String.class, bizCode, parameterCode, orgId);
        return str;
    }

    /**
     * 查询组织业务参数
     * 
     * @param fullId
     * @param bizCode
     *            业务编码
     * @param parameterCode
     *            参数编码
     * @return
     */
    public String findStirngBusinessParameterByFullId(String fullId, String bizCode, String parameterCode) {
        Assert.notNull(fullId, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_NULL, "fullId"));
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByFullId");
        String str = this.sqlExecutorDao.queryToObject(sql, String.class, bizCode, parameterCode, fullId);
        return str;
    }

    /**
     * 根据租户ID查询业务参数
     * 
     * @param bizCode
     * @param parameterCode
     * @return
     */
    public Object findTenantBusinessParameter(String bizCode, String parameterCode) {
        String orgId = ThreadLocalUtil.getOperator().getTenantId();
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByOrgId");
        Object obj = this.sqlExecutorDao.queryToObject(sql, Object.class, bizCode, parameterCode, orgId);
        return obj;
    }

    /**
     * 根据租户ID查询业务参数
     * 
     * @param bizCode
     * @param parameterCode
     * @return
     */
    public Object findStringTenantBusinessParameter(String bizCode, String parameterCode) {
        String orgId = ThreadLocalUtil.getOperator().getTenantId();
        Assert.notNull(bizCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "bizCode"));
        Assert.notNull(parameterCode, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "parameterCode"));
        String sql = this.getSqlByName("findParameterByOrgId");
        String str = this.sqlExecutorDao.queryToObject(sql, String.class, bizCode, parameterCode, orgId);
        return str;
    }

}
