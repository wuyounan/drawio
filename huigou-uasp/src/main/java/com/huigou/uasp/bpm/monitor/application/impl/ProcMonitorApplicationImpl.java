package com.huigou.uasp.bpm.monitor.application.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bpm.monitor.application.ProcMonitorApplication;
import com.huigou.uasp.bpm.monitor.domain.model.Ask;
import com.huigou.uasp.bpm.monitor.domain.query.AskQueryRequest;
import com.huigou.uasp.bpm.monitor.repository.AskRepository;

@Service("procMonitorApplication")
public class ProcMonitorApplicationImpl extends BaseApplication implements ProcMonitorApplication {

    @Autowired
    private AskRepository askRepository;

    @Override
    public Ask loadAsk(String id) {
        return this.askRepository.findOne(id);
    }

    @Override
    public Map<String, Object> sliceQueryCompletedProcs(AskQueryRequest askQueryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryExecutingProcs");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, askQueryRequest);
    }

    @Override
    public Map<String, Object> sliceQueryExecutingProcs(AskQueryRequest askQueryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "queryCompletedProcs");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, askQueryRequest);
    }

}
