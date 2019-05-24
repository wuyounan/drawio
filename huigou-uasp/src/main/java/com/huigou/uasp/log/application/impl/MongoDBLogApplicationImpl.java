package com.huigou.uasp.log.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.TmspmConifg;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.query.model.SortField;
import com.huigou.uasp.bmp.opm.repository.org.TMAuthorizeRepository;
import com.huigou.uasp.log.application.LogApplication;
import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.model.MongoDBBizLog;
import com.huigou.uasp.log.domain.model.MongoDBBizLogDetail;
import com.huigou.uasp.log.domain.query.OperationLogQueryRequest;
import com.huigou.uasp.log.util.BizLogUtil;
import com.huigou.uasp.log.util.MongoDBUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * MongoDB 实现
 * 
 * @author yuanwf
 */
public class MongoDBLogApplicationImpl implements LogApplication {

    @Resource(name = "mongoOperations")
    private MongoOperations mongoOperations;

    @Autowired
    private TMAuthorizeRepository tmAuthorizeRepository;

    @Autowired
    private TmspmConifg tmspmConifg;

    @Override
    public void savelog(BizLog bizLog, BizLogDetail bizLogDetail) {
        Assert.notNull(bizLog, "参数bizlog不能为空。");
        Assert.notNull(bizLogDetail, "参数bizlogDetail不能为空。");
        Long version = MongoDBUtil.getNextSequence(mongoOperations);
        bizLog.setVersion(version);
        mongoOperations.save(bizLog);
        bizLogDetail.setVersion(version);
        bizLogDetail.setBizLogId(bizLog.getId());
        mongoOperations.save(bizLogDetail);
    }

    @Override
    public void savelog(BizLog bizLog) {
        Assert.notNull(bizLog, "参数bizlog不能为空。");
        Long version = MongoDBUtil.getNextSequence(mongoOperations);
        bizLog.setVersion(version);
        mongoOperations.save(bizLog);
    }

    @Override
    public Map<String, Object> loadOperationLog(String id) {
        Assert.hasText(id, MessageSourceContext.getMessage(MessageConstants.ID_NOT_BLANK));

        Map<String, Object> data = new HashMap<String, Object>();
        MongoDBBizLog mongoDBBizlog = mongoOperations.findOne(new Query(Criteria.where("_id").is(id)), MongoDBBizLog.class);
        MongoDBBizLogDetail mongoDBBizlogDetail = mongoOperations.findOne(new Query(Criteria.where("operationLogId").is(id)), MongoDBBizLogDetail.class);
        data = ClassHelper.toMap(mongoDBBizlog);
        if (mongoDBBizlogDetail != null) {
            data.putAll(ClassHelper.toMap(mongoDBBizlogDetail));
        }
        return data;
    }

    /*
     * @Override
     * public Map<String, Object> slicedQueryOperationLogs(String operatorRoleKindId, String roleKindId, String appName, String fullId, String statusId,
     * String logType, String operationType, String ip, String personMemberId, String personMemberName,
     * Date beginDate, Date endDate, int pageIndex, int pageSize, String sortName, String sortOrder,
     * String exportHead, String exportType) {
     * if (StringUtil.isBlank(operatorRoleKindId) || StringUtil.isBlank(personMemberId)) {
     * return null;
     * }
     * Map<String, String> map = BizLogUtil.getLogQueryCriteria(operatorRoleKindId, tmAuthorizeRepository, personMemberId, tmspmConifg.isEnableTspm());
     * String targetRoleKindId = map.get("targetRoleKindId");
     * String targetStatusId = map.get("targetStatusId");
     * String targetFullId = map.get("targetFullId");
     * String targetAppId = map.get("targetAppId");
     * BasicDBList dbList = new BasicDBList();
     * if (targetFullId.indexOf("*") < 0 && StringUtil.isNotBlank(targetFullId)) {
     * DBObject dbObject = new BasicDBObject();
     * BasicDBList basicDBListFullId = new BasicDBList();
     * String[] targetFullIds = targetFullId.split(",");
     * for (String string : targetFullIds) {
     * BasicDBObject basicDBObject = new BasicDBObject();
     * basicDBObject.append("fullId", Pattern.compile("^.*" + string + ".*$"));
     * basicDBListFullId.add(basicDBObject);
     * }
     * dbObject.put("$or", basicDBListFullId);
     * dbList.add(dbObject);
     * }
     * if (StringUtil.isNotBlank(targetRoleKindId)) {
     * DBObject dbObject = new BasicDBObject();
     * BasicDBList basicDBListRoleKindId = new BasicDBList();
     * String[] targetRoleKindIds = targetRoleKindId.split(",");
     * for (String string : targetRoleKindIds) {
     * BasicDBObject basicDBObject = new BasicDBObject();
     * basicDBObject.put("roleKindId", string);
     * basicDBListRoleKindId.add(basicDBObject);
     * }
     * dbObject.put("$or", basicDBListRoleKindId);
     * dbList.add(dbObject);
     * }
     * DBObject queryDB = new BasicDBObject();
     * if (dbList.size() > 0) {
     * queryDB.put("$and", dbList);
     * }
     * Query query = new BasicQuery(queryDB);
     * if (StringUtil.isNotBlank(roleKindId)) {
     * query.addCriteria(Criteria.where("roleKindId").is(roleKindId));
     * }
     * if (StringUtil.isNotBlank(fullId)) {
     * query.addCriteria(Criteria.where("fullId").is(Pattern.compile("^.*" + fullId + ".*$")));
     * }
     * if (targetAppId.indexOf("*") < 0 && StringUtil.isNotBlank(targetAppId)) {
     * List<Pattern> AppIdList = new ArrayList<Pattern>();
     * String[] targetAppIds = targetAppId.split(",");
     * for (String string : targetAppIds) {
     * AppIdList.add(Pattern.compile("^.*" + string + ".*$"));
     * }
     * query.addCriteria(Criteria.where("appId").in(AppIdList));
     * }
     * if (StringUtil.isNotBlank(appName)) {
     * query.addCriteria(Criteria.where("appName").regex(appName));
     * }
     * if (StringUtil.isNotBlank(targetStatusId)) {
     * query.addCriteria(Criteria.where("statusId").is(Integer.parseInt(targetStatusId)));
     * } else if (StringUtil.isNotBlank(statusId)) {
     * query.addCriteria(Criteria.where("statusId").is(Integer.parseInt(statusId)));
     * }
     * if (StringUtil.isNotBlank(ip)) {
     * query.addCriteria(Criteria.where("ip").regex(ip));
     * }
     * if (StringUtil.isNotBlank(personMemberName)) {
     * query.addCriteria(Criteria.where("personMemberName").regex(personMemberName));
     * }
     * if (beginDate != null) {
     * query.addCriteria(Criteria.where("beginDate").gte(beginDate));
     * }
     * if (endDate != null) {
     * query.addCriteria(Criteria.where("endDate").lte(endDate));
     * }
     * if (StringUtil.isNotBlank(logType)) {
     * query.addCriteria(Criteria.where("logType").is(logType));
     * }
     * if (StringUtil.isNotBlank(operationType)) {
     * query.addCriteria(Criteria.where("operateName").is(operationType));
     * }
     * if (sortOrder.equals(MongoDBUtil.DESC)) {
     * query.with(new Sort(Direction.DESC, sortName));
     * } else {
     * query.with(new Sort(Direction.ASC, sortName));
     * }
     * long totalCount = this.mongoOperations.count(query, MongoDBBizLog.class);
     * // 设置分页取数
     * if (StringUtil.isNotBlank(exportType) && exportType.equals("all")) {
     * int maxExportCount = 5000;
     * Assert.isTrue(totalCount <= maxExportCount, "导出数据已超过设定上限,请调整查询条件。");
     * } else {
     * PageRequest pageRequest = new PageRequest(pageIndex - 1, pageSize);
     * query.skip(pageRequest.getOffset());
     * query.limit(pageSize);
     * }
     * List<MongoDBBizLog> logList = mongoOperations.find(query, MongoDBBizLog.class);
     * List<Map<String, Object>> logDetailList = new ArrayList<Map<String, Object>>();
     * if (StringUtil.isNotBlank(exportHead)) {
     * for (MongoDBBizLog mongoDBBizLog : logList) {
     * Map<String, Object> data = new HashMap<String, Object>();
     * MongoDBBizLogDetail logDetail = mongoOperations.findOne(new Query(Criteria.where("operationLogId").is(mongoDBBizLog.getId())),
     * MongoDBBizLogDetail.class);
     * data = ClassHelper.toMap(mongoDBBizLog);
     * if (logDetail != null) {
     * data.putAll(ClassHelper.toMap(logDetail));
     * }
     * logDetailList.add(data);
     * }
     * String fileName = BizLogUtil.exportExecute(logDetailList, exportHead);
     * Map<String, Object> data = new HashMap<String, Object>();
     * data.put("file", fileName);
     * return data;
     * }
     * Map<String, Object> data = new HashMap<String, Object>();
     * data.put("Total", totalCount);
     * data.put("Rows", logList);
     * return data;
     * }
     */

    @Override
    public Map<String, Object> slicedQueryOperationLogs(OperationLogQueryRequest queryRequest) {
        Assert.notNull(queryRequest, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "queryRequest"));
        if (!queryRequest.allowQuery()) {
            return new HashMap<String, Object>(1);
        }

        Map<String, String> map = queryRequest.getLogQueryCriteria(tmAuthorizeRepository, tmspmConifg.isEnableTspm());
        String targetRoleKindId = map.get("targetRoleKindId");
        String targetStatusId = map.get("targetStatusId");
        String targetFullId = map.get("targetFullId");
        String targetAppId = map.get("targetAppId");

        BasicDBList dbList = new BasicDBList();
        if (targetFullId.indexOf(StringPool.STAR) < 0 && StringUtil.isNotBlank(targetFullId)) {
            DBObject dbObject = new BasicDBObject();
            BasicDBList basicDBListFullId = new BasicDBList();
            String[] targetFullIds = targetFullId.split(StringPool.COMMA);
            for (String fullId : targetFullIds) {
                BasicDBObject basicDBObject = new BasicDBObject();
                basicDBObject.append("fullId", Pattern.compile("^.*" + fullId + ".*$"));
                basicDBListFullId.add(basicDBObject);
            }
            dbObject.put("$or", basicDBListFullId);
            dbList.add(dbObject);
        }
        if (StringUtil.isNotBlank(targetRoleKindId)) {
            DBObject dbObject = new BasicDBObject();
            BasicDBList basicDBListRoleKindId = new BasicDBList();
            String[] targetRoleKindIds = targetRoleKindId.split(StringPool.COMMA);
            for (String string : targetRoleKindIds) {
                BasicDBObject basicDBObject = new BasicDBObject();
                basicDBObject.put("roleKindId", string);
                basicDBListRoleKindId.add(basicDBObject);
            }
            dbObject.put("$or", basicDBListRoleKindId);
            dbList.add(dbObject);
        }
        DBObject queryDB = new BasicDBObject();
        if (dbList.size() > 0) {
            queryDB.put("$and", dbList);
        }
        Query query = new BasicQuery(queryDB);
        if (StringUtil.isNotBlank(queryRequest.getRoleKindId())) {
            query.addCriteria(Criteria.where("roleKindId").is(queryRequest.getRoleKindId()));
        }
        if (StringUtil.isNotBlank(queryRequest.getFullId())) {
            query.addCriteria(Criteria.where("fullId").is(Pattern.compile("^.*" + queryRequest.getFullId() + ".*$")));
        }
        if (targetAppId.indexOf(StringPool.STAR) < 0 && StringUtil.isNotBlank(targetAppId)) {
            List<Pattern> AppIdList = new ArrayList<Pattern>();
            String[] targetAppIds = targetAppId.split(StringPool.COMMA);
            for (String string : targetAppIds) {
                AppIdList.add(Pattern.compile("^.*" + string + ".*$"));
            }
            query.addCriteria(Criteria.where("appId").in(AppIdList));
        }
        if (StringUtil.isNotBlank(queryRequest.getAppName())) {
            query.addCriteria(Criteria.where("appName").regex(queryRequest.getAppName()));
        }
        if (StringUtil.isNotBlank(targetStatusId)) {
            query.addCriteria(Criteria.where("statusId").is(Integer.parseInt(targetStatusId)));
        } else if (StringUtil.isNotBlank(queryRequest.getStatusId())) {
            query.addCriteria(Criteria.where("statusId").is(Integer.parseInt(queryRequest.getStatusId())));
        }
        if (StringUtil.isNotBlank(queryRequest.getIp())) {
            query.addCriteria(Criteria.where("ip").regex(queryRequest.getIp()));
        }
        if (StringUtil.isNotBlank(queryRequest.getPersonMemberName())) {
            query.addCriteria(Criteria.where("personMemberName").regex(queryRequest.getPersonMemberName()));
        }
        if (queryRequest.getBeginDate() != null) {
            query.addCriteria(Criteria.where("beginDate").gte(queryRequest.getBeginDate()));
        }
        if (queryRequest.getEndDate() != null) {
            query.addCriteria(Criteria.where("endDate").lte(queryRequest.getEndDate()));
        }
        if (StringUtil.isNotBlank(queryRequest.getLogType())) {
            query.addCriteria(Criteria.where("logType").is(queryRequest.getLogType()));
        }
        if (StringUtil.isNotBlank(queryRequest.getOperationType())) {
            query.addCriteria(Criteria.where("operateName").is(queryRequest.getOperationType()));
        }
        SortField sortField = queryRequest.getPageModel().getFirstSortField();
        if (sortField != null) {
            if (MongoDBUtil.DESC.equalsIgnoreCase(sortField.getDirection())) {
                query.with(new Sort(Direction.DESC, sortField.getColumnName()));
            } else {
                query.with(new Sort(Direction.ASC, sortField.getColumnName()));
            }
        }
        long totalCount = this.mongoOperations.count(query, MongoDBBizLog.class);
        // 设置分页取数
        if ("all".equals(queryRequest.getPageModel().getExportType())) {
            int maxExportCount = 5000;
            Assert.isTrue(totalCount <= maxExportCount, "导出数据已超过设定上限，请调整查询条件。");
        } else {
            PageRequest pageRequest = new PageRequest(queryRequest.getPageModel().getPageIndex() - 1, queryRequest.getPageModel().getPageSize());
            query.skip(pageRequest.getOffset());
            query.limit(queryRequest.getPageModel().getPageSize());
        }

        List<MongoDBBizLog> logList = mongoOperations.find(query, MongoDBBizLog.class);
        List<Map<String, Object>> logDetailList = new ArrayList<Map<String, Object>>();
        if (StringUtil.isNotBlank(queryRequest.getPageModel().getExportHead())) {
            for (MongoDBBizLog mongoDBBizLog : logList) {
                Map<String, Object> data = new HashMap<String, Object>();
                MongoDBBizLogDetail logDetail = mongoOperations.findOne(new Query(Criteria.where("operationLogId").is(mongoDBBizLog.getId())),
                                                                        MongoDBBizLogDetail.class);
                data = ClassHelper.toMap(mongoDBBizLog);
                if (logDetail != null) {
                    data.putAll(ClassHelper.toMap(logDetail));
                }
                logDetailList.add(data);
            }
            String fileName = BizLogUtil.exportExecute(logDetailList, queryRequest.getPageModel().getExportHead());
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("file", fileName);
            return data;
        }
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("Total", totalCount);
        data.put("Rows", logList);
        return data;
    }
}
