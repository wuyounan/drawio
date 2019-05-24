package com.huigou.uasp.log.application;

import java.util.Map;

import com.huigou.uasp.log.domain.model.BizLog;
import com.huigou.uasp.log.domain.model.BizLogDetail;
import com.huigou.uasp.log.domain.query.OperationLogQueryRequest;

/**
 * 日志接口
 * 
 * @author
 */
public interface LogApplication {
    /**
     * 查询文件配置地址
     */
    public static final String QUERY_XML_FILE_PATH = "config/uasp/query/bmp/log.xml";

    /**
     * 保存日志
     * 
     * @param bizlog
     *            业务日志
     * @param bizlogDetail
     *            业务日志明细
     */
    void savelog(BizLog bizLog, BizLogDetail bizLogDetail);

    void savelog(BizLog bizLog);

    /**
     * 加载日志
     * 
     * @param id
     *            日志ID
     * @return
     */
    Map<String, Object> loadOperationLog(String id);

    //Map<String, Object> slicedQueryOperationLogs(String operatorRoleKindId, String roleKindId, String appName, String fullId, String statusId, String logType,
    //                                             String operationType, String ip, String personMemberId, String personMemberName, Date beginDate, Date endDate,
    //                                             int pageIndex, int pageSize, String sortName, String sortOrder, String exportHead, String exportType);

    /**
     * 分页查询操作日志
     * 
     * @param queryRequest
     *            查询请求
     * @return
     */
    Map<String, Object> slicedQueryOperationLogs(OperationLogQueryRequest queryRequest);
}
