package com.gdyc.mcs.routinesupervision.indexalert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.gdyc.mcs.constant.IndexConstant;
import com.gdyc.mcs.fn.GDYCFun;
import com.gdyc.mcs.hana.application.IndexSampleApplication;
import com.gdyc.mcs.hana.domain.model.HanaOrgContrast;
import com.gdyc.mcs.routinesupervision.indexalert.application.IndexAlertApplication;
import com.gdyc.mcs.routinesupervision.indexalert.domain.query.IndexAlertQty;
import com.gdyc.mcs.routinesupervision.indexalert.domain.query.IndexAlertQueryRequest;
import com.gdyc.mcs.routinesupervision.indexalert.domain.query.IndexClassAlertSummary;
import com.gdyc.mcs.routinesupervision.indexalertprocess.domain.query.IndexAlertProcessQueryRequest;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.annotation.ControllerMapping;
import com.huigou.uasp.client.CommonController;
import com.huigou.uasp.log.annotation.LogInfo;
import com.huigou.uasp.log.domain.model.LogType;
import com.huigou.uasp.log.domain.model.OperationType;
import com.huigou.util.CommonUtil;
import com.huigou.util.SDO;
import com.huigou.util.StringPool;

import net.sf.json.JSONArray;

/**
 * 指标预警
 * 
 * @author 
 */
@Controller
@ControllerMapping("indexAlert")
public class IndexAlertController extends CommonController {

    @Deprecated
    private static final String INDEX_ALERT_HOME_PAGE_DEPRECATED = "homePage";

    private static final String INDEX_ALERT_HOME_PAGE = "indexAlertHomePage";

    private static final String ORG_INDEX_ALERT_PAGE = "orgIndexAlert";

    private static final String INDEX_ORG_ALERT_CROSS_TABLE_PAGE = "indexOrgAlertCrossTable";

    private static final String INDEX_ORG_ALERT_STATISTICS_PAGE = "indexOrgAlertStatistics";

    private static final String INDEX_MONTH_ALERT_STATISTICS_PAGE = "indexMonthAlertStatistics";
    
    private static final String INDEX_CUST_ALERT_STATISTICS_PAGE = "indexCustAlertStatistics";

    private static final String INDEX_ALERT_ALL_DETAIL_PAGE = "indexAlertAllDetail";

    private static final String INDEX_ALERT_DETAIL_PAGE = "indexAlertDetail";

    private static final String INDEX_CLASSIFICATION_LIST_STR = "indexClassificationList";

    private static final String ORG_LIST_FIELD_NAME = "orgList";

    @Autowired
    private IndexAlertApplication indexAlertApplication;

    @Autowired
    private IndexSampleApplication indexSampleApplication;

    @Autowired
    private GDYCFun gdycFun;

    protected String getPagePath() {
        return "/biz/mcs/routinesupervision/indexalert/";
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardHomePage() {
        IndexAlertQueryRequest queryRequest = this.getSDO().toQueryRequest(IndexAlertQueryRequest.class);

        Operator operator = ThreadLocalUtil.getOperator();

        HanaOrgContrast orgContrast = gdycFun.getOrgContrastByOrgId(operator.getOrgId());

        Map<String, Object> data = indexAlertApplication.queryDefaultIndexClassification(queryRequest);

        this.putAttribute(IndexConstant.BIZ_ORG_KIND_FIELD_NAME, operator.getOrgAdminKind());
        this.putAttribute(IndexConstant.BIZ_ORG_ID_FIELD_NAME, orgContrast.getYxCode());
        this.putAttribute(IndexConstant.BIZ_ORG_NAME_FIELD_NAME, orgContrast.getYxName());

        this.putAttribute(IndexConstant.ALERT_DATE_FIELD_NAME, CommonUtil.getCurrentDate());
        this.putAttribute(IndexConstant.DATA_UPDATE_TIME_FIELD_NAME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date())));

        this.putAttribute(INDEX_CLASSIFICATION_LIST_STR, data.get(com.huigou.util.Constants.ROWS));
        return forward(INDEX_ALERT_HOME_PAGE_DEPRECATED);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexAlertHomePage() {
        return forward(INDEX_ALERT_HOME_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardOrgIndexAlert() {
        Operator operator = ThreadLocalUtil.getOperator();
        // String orgKindId = operator.getOrgAdminKind();
        // McsOrgKind mcsOrgKind = McsOrgKind.fromId(orgKindId);
        // if (mcsOrgKind.isCounty()) {
        HanaOrgContrast orgContrast = this.gdycFun.getOrgContrastByOrgId(operator.getOrgId());
        Assert.notNull(orgContrast, String.format("%s未设置组织对照。", operator.getOrgName()));
        this.putAttribute(IndexConstant.BIZ_ORG_ID_FIELD_NAME, orgContrast.getYxCode());
        this.putAttribute(IndexConstant.BIZ_ORG_NAME_FIELD_NAME, orgContrast.getYxName());
        // }

        this.putAttribute(IndexConstant.ALERT_DATE_FIELD_NAME, CommonUtil.getCurrentDate());
        this.buildOrgList();
        return forward(ORG_INDEX_ALERT_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexOrgAlertCrossTable() {
        this.putAttribute(IndexConstant.ALERT_DATE_FIELD_NAME, CommonUtil.getCurrentDate());
        return forward(INDEX_ORG_ALERT_CROSS_TABLE_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexOrgAlertStatistics() {
        Date toDay = CommonUtil.getCurrentDate();
        this.putAttribute(IndexConstant.START_DATE_FIELD_NAME, CommonUtil.getFirstDateOfLastMonth(toDay));
        this.putAttribute(IndexConstant.END_DATE_FIELD_NAME, toDay);
        return forward(INDEX_ORG_ALERT_STATISTICS_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexMonthAlertStatistics() {
        Date toDay = CommonUtil.getCurrentDate();
        this.putAttribute(IndexConstant.START_DATE_FIELD_NAME, CommonUtil.getFirstDateOfYear(toDay));
        this.putAttribute(IndexConstant.END_DATE_FIELD_NAME, toDay);
        return forward(INDEX_MONTH_ALERT_STATISTICS_PAGE);
    }
    
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexCustAlertStatistics() {
    	Date toDay = CommonUtil.getCurrentDate();
    	this.putAttribute(IndexConstant.START_DATE_FIELD_NAME, CommonUtil.getFirstDateOfYear(toDay));
    	this.putAttribute(IndexConstant.END_DATE_FIELD_NAME, toDay);
    	return forward(INDEX_CUST_ALERT_STATISTICS_PAGE);
    }
    
    
    

    private void buildOrgList() {
        SDO sdo = this.getSDO();
        IndexAlertQueryRequest queryRequest = sdo.toQueryRequest(IndexAlertQueryRequest.class);
        if (queryRequest.getAlertDate() == null) {
            queryRequest.setAlertDate(CommonUtil.getCurrentDate());
        }
        Object bizOrgIdObj = this.getAttribute(IndexConstant.BIZ_ORG_ID_FIELD_NAME);
        if (bizOrgIdObj != null) {
            queryRequest.setParentBizOrgId(String.valueOf(bizOrgIdObj));
        } else {
            Operator operator = ThreadLocalUtil.getOperator();
            HanaOrgContrast orgContrast = gdycFun.getOrgContrastByOrgId(operator.getOrgId());
            queryRequest.setParentBizOrgId(orgContrast.getYxCode());
        }

        queryRequest.setIndexClassId("");

        List<IndexAlertQty> indexAlertQtys = indexSampleApplication.queryIndexClassAlertOrganRank(queryRequest.getParentBizOrgId(), queryRequest.getAlertDate(),
                                                                                                  queryRequest.getIndexClassId());
        Map<String, Object> orgList = new LinkedHashMap<String, Object>(indexAlertQtys.size());

        for (IndexAlertQty item : indexAlertQtys) {
            orgList.put(item.getBizOrgId(), item.getBizOrgName());
        }

        this.putAttribute(ORG_LIST_FIELD_NAME, orgList);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String forwardIndexAlertAllDetail() {
        Date currentDate = CommonUtil.getCurrentDate();
        this.putAttribute("startDate", CommonUtil.getFirstDateOfMonth(currentDate));
        this.putAttribute("endDate", currentDate);
        return forward(INDEX_ALERT_ALL_DETAIL_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String forwardIndexAlertDetail() {
        return forward(INDEX_ALERT_DETAIL_PAGE);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryDefaultIndexClassification() {
        IndexAlertQueryRequest queryRequest = this.getSDO().toQueryRequest(IndexAlertQueryRequest.class);
        Map<String, Object> data = indexAlertApplication.queryDefaultIndexClassification(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String querySingleOrgIndexAlert() {
        IndexAlertQueryRequest queryRequest = this.getSDO().toQueryRequest(IndexAlertQueryRequest.class);
        Map<String, Object> data = this.indexSampleApplication.querySingleOrgIndexAlert(queryRequest);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexOrgAlertCrossTable() {
        IndexAlertQueryRequest queryRequest = this.getSDO().toQueryRequest(IndexAlertQueryRequest.class);
        Map<String, Object> data = this.indexSampleApplication.queryIndexOrgAlertCrossTable(queryRequest);
        return toResult(data);
    }
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexOrgAlertStatistics() {
        SDO sdo = this.getSDO();
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        Map<String, Object> data = this.indexSampleApplication.queryIndexOrgAlertStatistics(startDate, endDate);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexMonthAlertStatistics() {
        SDO sdo = this.getSDO();
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        String indexType = sdo.getString("indexType");
		Map<String, Object> data = this.indexSampleApplication.queryIndexMonthAlertStatistics(startDate, endDate,indexType);
        return toResult(data);
    }
    
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexCustAlertStatistics() {
    	SDO sdo = this.getSDO();
    	String custCode = sdo.getString("custCode");
    	String comText = sdo.getString("comText");
    	Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
    	Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
    	Map<String, Object> data = this.indexSampleApplication.queryIndexCustAlertStatistics(startDate, endDate,custCode,comText);
    	return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexOrgAlertQty() {
        SDO sdo = this.getSDO();
        String bizOrgId = sdo.getString("bizOrgId");
        String indexEntryId = sdo.getString("indexEntryId");
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        List<Map<String, Object>> data = this.indexSampleApplication.queryIndexOrgAlertQty(bizOrgId, indexEntryId, startDate, endDate);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexOrgAlertStatisticsDetail() {
        SDO sdo = this.getSDO();
        String bizOrgId = sdo.getString("bizOrgId");
        String indexEntryId = sdo.getString("indexEntryId");
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        List<Map<String, Object>> data = this.indexSampleApplication.queryIndexOrgAlertStatisticsDetail(bizOrgId, indexEntryId, startDate, endDate);
        return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexMonthAlertStatisticsDetail() {
        SDO sdo = this.getSDO();
        String yearMonth = sdo.getString("yearMonth");
        String indexEntryId = sdo.getString("indexEntryId");
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        List<Map<String, Object>> data = this.indexSampleApplication.queryIndexMonthAlertStatisticsDetail(yearMonth, indexEntryId, startDate, endDate);
        return toResult(data);
    }
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexCustAlertStatisticsDetail() {
    	SDO sdo = this.getSDO();
    	Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
    	Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
    	String custCode = sdo.getString("custCode");
    	List<Map<String, Object>> data = this.indexSampleApplication.queryIndexCustAlertStatisticsDetail(custCode, startDate, endDate);
    	return toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.DETALL, description = "")
    public String loadIndexAlertDetailDefinition() {
        SDO sdo = this.getSDO();
        String indexEntryId = sdo.getString(IndexConstant.INDEX_ENTRY_ID_FIELD_NAME);
        String bizOrgId = sdo.getString(IndexConstant.BIZ_ORG_ID_FIELD_NAME);
        Map<String, Object> data = this.indexAlertApplication.loadIndexAlertDetailDefinition(bizOrgId, indexEntryId);
        return this.toResult(data);
    }
    
    
    /**
     * 指标分类 月
     * @return 
     */
   // @RequiresPermissions("MCSIndexAlertProcessStatistics:query")
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String loadIndexAlertProcessMonthIndexType(){
    	SDO sdo = this.getSDO();
        IndexAlertQueryRequest queryRequest = sdo.toQueryRequest(IndexAlertQueryRequest.class);;
        List<Map<String,Object>> data = indexSampleApplication.loadIndexAlertProcessMonthIndexType(queryRequest);
        return toResult(data);
    }
    

    /**
     * CBoard代理接口
     * 
     * @return
     */
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String getAggregateAllData() {
        SDO sdo = this.getSDO();
        String indexEntryId = sdo.getString(IndexConstant.INDEX_ENTRY_ID_FIELD_NAME);
        String indexEntryTabId = sdo.getString(IndexConstant.INDEX_ENTRY_TAB_ID_FIELD_NAME);

        String bizOrgId = sdo.getString("bizOrgId");
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);

        Map<String, Object> cascadeParams = sdo.getObjectMap("cascadeParams");

        JSONArray data = this.indexAlertApplication.getAggregateAllData(indexEntryId, indexEntryTabId, cascadeParams, bizOrgId, startDate, endDate);
        return this.toResult(data);
    }

    /**
     * CBoard代理接口
     * 
     * @return
     */
    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String getAggregateData() {
        SDO sdo = this.getSDO();
        String indexEntryId = sdo.getString(IndexConstant.INDEX_ENTRY_ID_FIELD_NAME);
        String indexEntryTabId = sdo.getString(IndexConstant.INDEX_ENTRY_TAB_ID_FIELD_NAME);

        Map<String, Object> cascadeParams = sdo.getObjectMap("cascadeParams");

        JSONArray data = this.indexAlertApplication.getAggregateData(indexEntryId, indexEntryTabId, cascadeParams);

        return this.toResult(data);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexAlertOrgRank() {
        SDO sdo = this.getSDO();
        IndexAlertQueryRequest queryRequest = sdo.toQueryRequest(IndexAlertQueryRequest.class);
        if (queryRequest.getAlertDate() == null) {
            queryRequest.setAlertDate(new Date());
        }
        if (queryRequest.getIndexClassId() == null) {
            queryRequest.setIndexClassId("");
        }
        List<IndexAlertQty> indexAlertQtys = this.indexAlertApplication.queryIndexClassAlertOrganRank(queryRequest.getBizOrgId(), queryRequest.getAlertDate(),
                                                                                                      queryRequest.getIndexClassId());
        Integer allIndexQty = this.indexSampleApplication.queryAllIndexQty(queryRequest.getBizOrgId());
        IndexAlertQty allIndexQtyObejct = new IndexAlertQty();
        allIndexQtyObejct.setIndexClassId(StringPool.AT);
        allIndexQtyObejct.setIndexQty(allIndexQty);

        indexAlertQtys.add(0, allIndexQtyObejct);
        return this.toResult(indexAlertQtys);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryOrganIndexAlertSummary() {
        SDO sdo = this.getSDO();
        IndexAlertQueryRequest queryRequest = sdo.toQueryRequest(IndexAlertQueryRequest.class);
        if (queryRequest.getBizOrgId() == null) {
            Operator operator = ThreadLocalUtil.getOperator();
            HanaOrgContrast orgContrast = this.gdycFun.getOrgContrastByOrgId(operator.getOrgId());
            queryRequest.setBizOrgId(orgContrast.getYxCode());
        }
        if (queryRequest.getAlertDate() == null) {
            queryRequest.setAlertDate(new Date());
        }
        if (queryRequest.getIndexClassId() == null) {
            queryRequest.setIndexClassId("");
        }
        Map<String, Object> result = this.indexSampleApplication.queryOrganIndexAlertSummary(queryRequest.getBizOrgId(), queryRequest.getAlertDate());

        return this.toResult(result);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexAlertRank() {
        SDO sdo = this.getSDO();
        IndexAlertQueryRequest queryRequest = sdo.toQueryRequest(IndexAlertQueryRequest.class);
        List<IndexAlertQty> result = this.indexAlertApplication.queryIndexAlertRank(queryRequest);
        return this.toResult(result);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String loadIndexClassAlertSummaryComponent() {
        SDO sdo = this.getSDO();
        Operator operator = ThreadLocalUtil.getOperator();
        HanaOrgContrast orgContrast = this.gdycFun.getOrgContrastByOrgId(operator.getOrgId());
        Date alertDate = sdo.getProperty(IndexConstant.ALERT_DATE_FIELD_NAME, Date.class, CommonUtil.getCurrentDate());

        List<IndexClassAlertSummary> result = this.indexAlertApplication.queryIndexClassAlertSummaryComponent(orgContrast.getYxCode(), alertDate, "");

        this.putAttribute(IndexConstant.INDEX_CLASS_ALERTS_FIELD_NAME, result);
        this.putAttribute(IndexConstant.BIZ_ORG_KIND_FIELD_NAME, operator.getOrgAdminKind());
        this.putAttribute(IndexConstant.BIZ_ORG_ID_FIELD_NAME, orgContrast.getYxCode());
        this.putAttribute(IndexConstant.BIZ_ORG_NAME_FIELD_NAME, orgContrast.getYxName());

        return this.forward("/biz/mcs/routinesupervision/component/indexClassAlertComp.jsp");
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.VIEW, description = "")
    public String loadIndexClassAlertSummaryComponentForBizProcess() {
        SDO sdo = this.getSDO();
        String indexClassId = sdo.getString("indexClassId");
        Operator operator = ThreadLocalUtil.getOperator();
        HanaOrgContrast orgContrast = this.gdycFun.getOrgContrastByOrgId(operator.getOrgId());
        Date alertDate = sdo.getProperty(IndexConstant.ALERT_DATE_FIELD_NAME, Date.class, CommonUtil.getCurrentDate());
        List<IndexClassAlertSummary> result = this.indexAlertApplication.queryIndexClassAlertSummaryComponent(orgContrast.getYxCode(), alertDate, indexClassId);

        this.putAttribute(IndexConstant.INDEX_CLASS_ALERTS_FIELD_NAME, result);

        return this.forward("/biz/mcs/routinesupervision/component/indexClassAlertCompForBizProcess.jsp");
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexClassAlertSummary() {
        SDO sdo = this.getSDO();
        String bizOrgId = sdo.getString(IndexConstant.BIZ_ORG_ID_FIELD_NAME);
        Date alertDate = sdo.getProperty(IndexConstant.ALERT_DATE_FIELD_NAME, Date.class);
        if (alertDate == null) {
            alertDate = CommonUtil.getCurrentDate();
        }
        IndexClassAlertSummary result = this.indexAlertApplication.queryIndexClassAlertSummary(bizOrgId, alertDate);
        return this.toResult(result);
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.LIST, description = "")
    public String forwardIndexOrgAlertQtyStatistics() {
        Date toDay = CommonUtil.getCurrentDate();
        this.putAttribute(IndexConstant.START_DATE_FIELD_NAME, CommonUtil.getFirstDateOfMonth(toDay));
        this.putAttribute(IndexConstant.END_DATE_FIELD_NAME, toDay);
        return forward("indexOrgAlertQtyStatistics");
    }

    @LogInfo(logType = LogType.BIZ, subType = "", operaionType = OperationType.QUERY, description = "")
    public String queryIndexOrgAlertQtyStatistics() {
        SDO sdo = this.getSDO();
        Date startDate = sdo.getProperty(IndexConstant.START_DATE_FIELD_NAME, Date.class);
        Date endDate = sdo.getProperty(IndexConstant.END_DATE_FIELD_NAME, Date.class);
        Map<String, Object> data = this.indexAlertApplication.queryIndexOrgAlertQtyStatistics(startDate, endDate);
        return toResult(data);
    }
    
    
    public String forwardDemo() {
        IndexAlertQueryRequest queryRequest = this.getSDO().toQueryRequest(IndexAlertQueryRequest.class);

        Operator operator = ThreadLocalUtil.getOperator();

       // HanaOrgContrast orgContrast = gdycFun.getOrgContrastByOrgId(operator.getOrgId());

        Map<String, Object> data = indexAlertApplication.queryDefaultIndexClassification(queryRequest);

//        this.putAttribute(IndexConstant.BIZ_ORG_KIND_FIELD_NAME, operator.getOrgAdminKind());
//        this.putAttribute(IndexConstant.BIZ_ORG_ID_FIELD_NAME, orgContrast.getYxCode());
//        this.putAttribute(IndexConstant.BIZ_ORG_NAME_FIELD_NAME, orgContrast.getYxName());
//
//        this.putAttribute(IndexConstant.ALERT_DATE_FIELD_NAME, CommonUtil.getCurrentDate());
//        this.putAttribute(IndexConstant.DATA_UPDATE_TIME_FIELD_NAME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date())));
//
//        this.putAttribute(INDEX_CLASSIFICATION_LIST_STR, data.get(com.huigou.util.Constants.ROWS));
        return forward("indexDemo");
    }

    
}