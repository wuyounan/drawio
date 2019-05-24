package com.huigou.uasp.bpm.monitor.application;

import java.util.Map;

import com.huigou.uasp.bpm.monitor.domain.model.Ask;
import com.huigou.uasp.bpm.monitor.domain.query.AskQueryRequest;

/**
 * 流程监控应用
 * 
 * @author gongmm
 */
public interface ProcMonitorApplication {
    
    static String QUERY_XML_FILE_PATH = "config/uasp/query/bpm/monitor.xml";

    /**
     * 加载询问表
     * 
     * @param id
     *            询问表Id
     * @return 询问表实体
     */
    Ask loadAsk(String id);
    
    /**
     * 分页查询处理中的流程
     * 
     * @param askQueryRequest
     * @return 流程map集合
     */
    Map<String, Object> sliceQueryExecutingProcs(AskQueryRequest askQueryRequest);

    /**
     * 分页查询已完成的流程
     * 
     * @param askQueryRequest
     * @return 流程map集合
     */
    Map<String, Object> sliceQueryCompletedProcs(AskQueryRequest askQueryRequest);

}
