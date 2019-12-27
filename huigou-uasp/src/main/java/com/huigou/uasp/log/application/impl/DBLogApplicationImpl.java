package com.huigou.uasp.log.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.executor.SQLExecutorDao;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.log.application.LogApplication;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.model.DBBizLog;
import com.huigou.uasp.log.domain.model.DBBizLogDetail;
import com.huigou.uasp.log.domain.query.OperationLogQueryRequest;
import com.huigou.uasp.log.repository.DBBizLogDetailRepository;
import com.huigou.uasp.log.repository.DBBizLogRepository;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

/**
 * 日志应用
 *
 * @author
 */
public class DBLogApplicationImpl implements LogApplication {

    @Autowired
    @Qualifier("logSqlExecutorDao")
    protected SQLExecutorDao sqlExecutorDao;

    @Autowired
    private DBBizLogRepository dbBizLogRepository;

    @Autowired
    private TMAuthorizeRepository tmAuthorizeRepository;

    @Autowired
    private DBBizLogDetailRepository dbBizLogDetailRepository;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Override
    public void savelog(BizLog bizLog, BizLogDetail bizLogDetail) {
        Assert.notNull(bizLog, "参数bizlog不能为空。");
        Assert.notNull(bizLogDetail, "参数bizlogDetail不能为空。");
        bizLog = this.dbBizLogRepository.save((DBBizLog) bizLog);
        bizLogDetail.setBizLogId(bizLog.getId());
        this.dbBizLogDetailRepository.save((DBBizLogDetail) bizLogDetail);
    }

    @Override
    public void savelog(BizLog bizLog) {
        Assert.notNull(bizLog, "参数bizlog不能为空。");
        bizLog = this.dbBizLogRepository.save((DBBizLog) bizLog);
    }

    @Override
    public Map<String, Object> loadOperationLog(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));
        DBBizLog dbBizLog = this.dbBizLogRepository.findOne(id);
        DBBizLogDetail dbBizLogDetail = this.dbBizLogDetailRepository.findByBizLogId(dbBizLog.getId());
        Map<String, Object> data = new HashMap<String, Object>();
        data = ClassHelper.toMap(dbBizLog);
        if (dbBizLogDetail != null) {
            data.putAll(ClassHelper.toMap(dbBizLogDetail));
        }
        return data;
    }

    @Override
    public Map<String, Object> slicedQueryOperationLogs(OperationLogQueryRequest queryRequest) {
        Assert.notNull(queryRequest, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "queryRequest"));
        if (!queryRequest.allowQuery()) {
            return new HashMap<String, Object>(1);
        }

        QueryModel queryModel = queryRequest.initQueryModel();

        Map<String, String> map = queryRequest.getLogQueryCriteria(tmAuthorizeRepository, tmspmConifg.isEnableTspm());
        String targetRoleKindId = map.get("targetRoleKindId");
        String targetStatusId = map.get("targetStatusId");
        String targetFullId = map.get("targetFullId");
        String targetAppId = map.get("targetAppId");

        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "log");

        StringBuilder sb = new StringBuilder();
        sb.append(queryDescriptor.getSqlByName("queryOperationlog"));

        int i = 0;
        if (StringUtil.isNotBlank(targetRoleKindId)) {
            String[] targetRoleKindIds = targetRoleKindId.split(StringPool.COMMA);
            sb.append(" and t.Role_Kind_Id in (");
            for (String item : targetRoleKindIds) {
                if (i < targetRoleKindIds.length - 1) {
                    sb.append(String.format(":roleKindId%s,", i));
                } else {
                    sb.append(String.format(":roleKindId%s", i));
                }
                queryModel.putParam(String.format("roleKindId%s", i), item);

                i++;
            }
            sb.append(")");
        }

        if (targetAppId.indexOf(StringPool.STAR) < 0 && StringUtil.isNotBlank(targetAppId)) {
            String[] targetAppIds = targetAppId.split(StringPool.COMMA);
            i = 0;
            sb.append("and t.App_Id in (");
            for (String item : targetAppIds) {
                if (i < targetAppIds.length) {
                    sb.append(String.format(":appId%s,", i));
                } else {
                    sb.append(String.format(":appId%s", i));
                }
                queryModel.putParam(String.format("appId%s", i), item);

                i++;
            }
            sb.append(")");
        }

        if (targetFullId.indexOf(StringPool.STAR) < 0 && StringUtil.isNotBlank(targetFullId)) {
            String[] targetFullIds = targetFullId.split(StringPool.COMMA);
            i = 0;
            for (String item : targetFullIds) {
                if (i == 0) {
                    sb.append(String.format(" and (t.Full_Id like :targetFullId%s", i));
                } else {
                    sb.append(String.format(" or t.Full_Id like :targetFullId%s", i));
                }
                queryModel.putStartWithParam(String.format("targetFullId%s", i), item);
                i++;
            }
            sb.append(")");
        }
        if (StringUtil.isNotBlank(queryRequest.getRoleKindId())) {
            sb.append(" and t.Role_Kind_Id = :roleKindId");
            queryModel.putParam("roleKindId", queryRequest.getRoleKindId());
        }
        if (StringUtil.isNotBlank(queryRequest.getFullId())) {
            sb.append(" and t.Full_Id like :fullId");
            queryModel.putStartWithParam("fullId", queryRequest.getFullId());
        }
        if (StringUtil.isNotBlank(queryRequest.getAppName())) {
            sb.append(" and t.App_Name like :appName");
            queryModel.putLikeParam("appName", queryRequest.getAppName());
        }
        if (StringUtil.isNotBlank(targetStatusId)) {
            sb.append(" and t.Status_Id = :statusId");
            queryModel.putParam("statusId", targetStatusId);
        } else if (StringUtil.isNotBlank(queryRequest.getStatusId())) {
            sb.append(" and t.Status_Id = :statusId");
            queryModel.putParam("statusId", queryRequest.getStatusId());
        }

        if (StringUtil.isNotBlank(queryRequest.getDescription())) {
            sb.append(" and t.Description like :description");
            queryModel.putLikeParam("description", queryRequest.getDescription());
        }

        if (StringUtil.isNotBlank(queryRequest.getIp())) {
            sb.append(" and t.IP = :ip");
            queryModel.putLikeParam("ip", queryRequest.getIp());
        }
        if (StringUtil.isNotBlank(queryRequest.getPersonMemberName())) {
            sb.append(" and t.Person_Member_Name like :personMemberName");
            queryModel.putLikeParam("personMemberName", queryRequest.getPersonMemberName());
        }
        if (queryRequest.getBeginDate() != null) {
            sb.append(" and t.Begin_Date >= :beginDate");
            queryModel.putParam("beginDate", queryRequest.getBeginDate());
        }
        if (queryRequest.getEndDate() != null) {
            sb.append(" and t.End_Date <= :endDate");
            queryModel.putParam("endDate", queryRequest.getEndDate());
        }

        if (StringUtil.isNotBlank(queryRequest.getLogType())) {
            sb.append(" and t.Log_Type = :logType");
            queryModel.putParam("logType", queryRequest.getLogType());
        }

        if (StringUtil.isNotBlank(queryRequest.getOperationType())) {
            sb.append(" and t.Operate_Name = :operationType");
            queryModel.putParam("operationType", queryRequest.getOperationType());
        }

        queryModel.setSql(sb.toString());

        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    /*
    @Override
    public Map<String, Object> slicedQueryOperationLogs(String operatorRoleKindId, String roleKindId, String appName, String fullId, String statusId,
                                                        String logType, String operationType, String ip, String personMemberId, String personMemberName,
                                                        Date beginDate, Date endDate, int pageIndex, int pageSize, String sortName, String sortOrder,
                                                        String exportHead, String exportType) {
        QueryModel queryModel = new QueryModel();

        if (StringUtil.isBlank(operatorRoleKindId) || StringUtil.isBlank(personMemberId)) {
            return null;
        }

        Map<String, String> map = BizLogUtil.getLogQueryCriteria(operatorRoleKindId, tmAuthorizeRepository, personMemberId, tmspmConifg.isEnableTspm());
        String targetRoleKindId = map.get("targetRoleKindId");
        String targetStatusId = map.get("targetStatusId");
        String targetFullId = map.get("targetFullId");
        String targetAppId = map.get("targetAppId");

        StringBuilder sb = new StringBuilder();
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "log");
        sb.append(queryDescriptor.getSqlByName("query"));

        int i = 0;
        if (StringUtil.isNotBlank(targetRoleKindId)) {
            String[] targetRoleKindIds = targetRoleKindId.split(",");
            sb.append(" and t.Role_Kind_Id in (");
            for (String item : targetRoleKindIds) {
                if (i < targetRoleKindIds.length - 1) {
                    sb.append(String.format(":roleKindId%s,", i));
                } else {
                    sb.append(String.format(":roleKindId%s", i));
                }
                queryModel.putParam(String.format("roleKindId%s", i), item);
                i++;
            }
            sb.append(")");
        }

        if (targetAppId.indexOf("*") < 0 && StringUtil.isNotBlank(targetAppId)) {
            String[] targetAppIds = targetAppId.split(",");
            i = 0;
            sb.append("and t.App_Id in (");
            for (String item : targetAppIds) {
                if (i < targetAppIds.length) {
                    sb.append(String.format(":appId%s,", i));
                } else {
                    sb.append(String.format(":appId%s", i));
                }
                queryModel.putParam(String.format("appId%s", i), item);
                i++;
            }
            sb.append(")");
        }

        if (targetFullId.indexOf("*") < 0 && StringUtil.isNotBlank(targetFullId)) {
            String[] targetFullIds = targetFullId.split(",");
            i = 0;
            for (String item : targetFullIds) {
                if (i == 0) {
                    sb.append(String.format(" and (t.Full_Id like :targetFullId%s", i));
                } else {
                    sb.append(String.format(" or t.Full_Id like :targetFullId%s", i));
                }
                queryModel.putStartWithParam(String.format("targetFullId%s", i), item);
                i++;
            }
            sb.append(")");
        }
        if (StringUtil.isNotBlank(roleKindId)) {
            sb.append(" and t.Role_Kind_Id = :roleKindId");
            queryModel.putParam("roleKindId", roleKindId);
        }
        if (StringUtil.isNotBlank(fullId)) {
            sb.append(" and t.Full_Id like :fullId");
            queryModel.putStartWithParam("fullId", fullId);
        }
        if (StringUtil.isNotBlank(appName)) {
            sb.append(" and t.App_Name like :appName");
            queryModel.putLikeParam("appName", appName);
        }
        if (StringUtil.isNotBlank(targetStatusId)) {
            sb.append(" and t.Status_Id = :statusId");
            queryModel.putParam("statusId", targetStatusId);
        } else if (StringUtil.isNotBlank(statusId)) {
            sb.append(" and t.Status_Id = :statusId");
            queryModel.putParam("statusId", statusId);
        }
        if (StringUtil.isNotBlank(ip)) {
            sb.append(" and t.IP = :ip");
            queryModel.putLikeParam("ip", ip);
        }
        if (StringUtil.isNotBlank(personMemberName)) {
            sb.append(" and t.Person_Member_Name like :personMemberName");
            queryModel.putLikeParam("personMemberName", personMemberName);
        }
        if (beginDate != null) {
            sb.append(" and t.Begin_Date >= to_Date(:beginDate,'yyyy/mm/dd')");
            queryModel.putParam("beginDate", BizLogUtil.convert(beginDate));
        }
        if (endDate != null) {
            sb.append(" and t.End_Date <= to_Date(:endDate,'yyyy/mm/dd')");
            queryModel.putParam("endDate", BizLogUtil.convert(endDate));
        }

        if (StringUtil.isNotBlank(logType)) {
            sb.append(" and t.Log_Type = :logType");
            queryModel.putParam("logType", logType);
        }

        if (StringUtil.isNotBlank(operationType)) {
            sb.append(" and t.Operate_Name = :operationType");
            queryModel.putParam("operationType", operationType);
        }

        if (exportType != null && exportType.equals("all")) {
            pageSize = -1;
        }
        queryModel.setExportType(exportType);
        queryModel.setExportHead(exportHead);

        queryModel.setPageIndex(pageIndex);
        queryModel.setPageSize(pageSize);

        queryModel.setSortFieldName(sortName);
        queryModel.setSortOrder(sortOrder);

        queryModel.setSql(sb.toString());

        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }*/
}
