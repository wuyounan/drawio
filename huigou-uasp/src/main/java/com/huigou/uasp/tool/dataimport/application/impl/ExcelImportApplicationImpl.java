package com.huigou.uasp.tool.dataimport.application.impl;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.huigou.context.MessageSourceContext;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.domain.model.Creator;
import com.huigou.data.domain.model.MessageConstants;
import com.huigou.data.domain.query.EmptyQueryRequest;
import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;
import com.huigou.data.domain.query.ParentIdQueryRequest;
import com.huigou.data.domain.query.QueryPageRequest;
import com.huigou.data.domain.query.QueryParameter;
import com.huigou.data.excel.reader.ExcelReaderUtil;
import com.huigou.data.excel.reader.IExcelRowReader;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.data.query.model.QueryModel;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.common.application.BaseApplication;
import com.huigou.uasp.bmp.opm.domain.model.org.CreatorOrgNodeData;
import com.huigou.uasp.tool.dataimport.application.ExcelImportAbstract;
import com.huigou.uasp.tool.dataimport.application.ExcelImportApplication;
import com.huigou.uasp.tool.dataimport.application.ExcelImportInterface;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportLog;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportStatus;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplate;
import com.huigou.uasp.tool.dataimport.domain.model.ExcelImportTemplateDetail;
import com.huigou.uasp.tool.dataimport.repository.ExcelImportLogRepository;
import com.huigou.uasp.tool.dataimport.repository.ExcelImportTemplateRepository;
import com.huigou.util.ApplicationContextWrapper;
import com.huigou.util.ClassHelper;
import com.huigou.util.CommonUtil;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.LogHome;
import com.huigou.util.StringUtil;

/**
 * Excel导入
 * 
 * @author xx
 */
@Service("excelImportApplication")
public class ExcelImportApplicationImpl extends BaseApplication implements ExcelImportApplication, ExcelImportInterface {

    @Autowired
    private ExcelImportTemplateRepository excelImportTemplateRepository;

    @Autowired
    private ExcelImportLogRepository excelImportLogRepository;

    private String getQuerySqlByName(String name) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "excelImportTemplate");
        return queryDescriptor.getSqlByName(name);
    }

    @Override
    public String saveExcelImportTemplate(ExcelImportTemplate excelImportTemplate) {
        Assert.notNull(excelImportTemplate, MessageSourceContext.getMessage(MessageConstants.PARAMETER_NOT_NULL_FORMAT, "excelImportTemplate"));
        excelImportTemplate = this.commonDomainService.loadAndFillinProperties(excelImportTemplate, ExcelImportTemplate.class);

        if (excelImportTemplate.isNew()) {
            excelImportTemplate.setCreator(Creator.newInstance());
        }
        excelImportTemplate.buildDetails();
        excelImportTemplate = (ExcelImportTemplate) this.commonDomainService.saveBaseInfoWithFolderEntity(excelImportTemplate, excelImportTemplateRepository);
        return excelImportTemplate.getId();
    }

    @Override
    public ExcelImportTemplate loadExcelImportTemplate(String id) {
        this.checkIdNotBlank(id);
        return this.excelImportTemplateRepository.findOne(id);
    }

    @Override
    public ExcelImportTemplate loadExcelImportTemplateByCode(String code) {
        Assert.hasText(code, "参数code不能为空。");
        List<ExcelImportTemplate> list = this.excelImportTemplateRepository.findByCode(code);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void deleteExcelImportTemplates(List<String> ids) {
        this.checkIdsNotEmpty(ids);
        List<ExcelImportTemplate> excelImportTemplates = this.excelImportTemplateRepository.findAll(ids);

        Assert.isTrue(ids.size() == excelImportTemplates.size(), MessageSourceContext.getMessage(MessageConstants.IDS_EXIST_INVALID_ID, "Excel导入模板"));

        this.excelImportTemplateRepository.delete(excelImportTemplates);
    }

    @Override
    public void deleteExcelImportTemplateDetails(String templateId, List<String> ids) {
        Assert.hasText(templateId, "参数templateId不能为空。");
        this.checkIdsNotEmpty(ids);

        ExcelImportTemplate excelImportTemplate = this.excelImportTemplateRepository.findOne(templateId);
        excelImportTemplate.removeDetails(ids);

        this.excelImportTemplateRepository.save(excelImportTemplate);
    }

    @Override
    public Map<String, Object> queryExcelImportTemplateDetails(String templateId, EmptyQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "excelImportTemplate");
        QueryModel queryModel = this.sqlExecutorDao.getQueryModel(queryDescriptor, queryRequest, "queryDetails");
        queryModel.putParam("templateId", templateId);
        return this.sqlExecutorDao.executeQuery(queryModel);
    }

    @Override
    public Map<String, Object> queryExcelImportGridHead(String templateId) {
        String sql = this.getQuerySqlByName("loadDetails");
        List<Map<String, Object>> data = this.sqlExecutorDao.queryToListMap(sql, templateId);
        for (Map<String, Object> item : data) {
            String columnName = ClassHelper.convert(item.get("columnName"), String.class);
            columnName = StringUtil.getHumpName(columnName);
            item.put("columnName", columnName);
        }

        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put(Constants.ROWS, data);
        return result;

    }

    @Override
    public Map<String, Object> slicedQueryExcelImportTemplates(FolderAndCodeAndNameQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "excelImportTemplate");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public void updateExcelImportTemplateStatus(List<String> ids, Integer status) {
        this.commonDomainService.updateStatus(ExcelImportTemplate.class, ids, status);
    }

    @Override
    public void moveExcelImportTemplates(List<String> ids, String folderId) {
        this.commonDomainService.moveForFolder(ExcelImportTemplate.class, ids, folderId);

    }

    /**
     * 组合写入临时表SQL
     * 
     * @param template
     * @return
     */
    private String buildExcelImportSql(ExcelImportTemplate template) {
        StringBuffer insert = new StringBuffer();
        StringBuffer values = new StringBuffer();
        insert.append("insert into ").append(template.getTableName());
        insert.append(" (tmp_id,batch_number,status");
        values.append(" values(?,?,?");

        for (ExcelImportTemplateDetail detail : template.getDetails()) {
            insert.append(",").append(detail.getColumnName());
            values.append(",?");
        }
        insert.append(")");
        values.append(")");

        return insert.append(values).toString();
    }

    /**
     * 解析excel 并写入临时表
     * 
     * @param fileName
     * @param sql
     * @param count
     * @param headRowSpan
     * @param objects
     * @throws Exception
     */
    private void internalImport(String fileName, String sql, int count, int headRowSpan, Object[] objects) throws Exception {
        BatchInsert batchInsert = new BatchInsert(this.sqlExecutorDao.getDataSource(), sql, headRowSpan);
        batchInsert.setParams(objects);// 设置固有参数
        batchInsert.setSqlParameter(count);// 设置输入参数个数
        ExcelReaderUtil.readExcel(batchInsert, fileName);
        batchInsert.flush();
    }

    /**
     * 调用接口 处理业务数据
     * 
     * @param procedureName
     * @param batchNumber
     */
    private void internalAfterImport(String procedureName, String batchNumber) {
        try {
            LogHome.getLog().info("---执行 【  " + procedureName + " 】开始---");
            Long startTime = System.currentTimeMillis();
            Object obj = ApplicationContextWrapper.getBean(procedureName);
            Assert.notNull(obj, String.format("未找到名称为[%s]的SpringBean", procedureName));
            Assert.isTrue(ClassHelper.isInterface(obj.getClass(), ExcelImportInterface.class), String.format("[%s]未实现接口ExcelImportInterface", procedureName));
            ((ExcelImportInterface) obj).executeImport(batchNumber);
            Long endTime = System.currentTimeMillis();
            LogHome.getLog().info("---执行 【  " + procedureName + " 】结束，耗时【" + (endTime - startTime) + "毫秒】---");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("调用接口" + procedureName + "异常:" + e.getMessage());
        }
    }

    /**
     * 获取导如处理业务bean
     * 
     * @param template
     */
    private ExcelImportAbstract getExcelImportApplication(ExcelImportTemplate template) {
        Assert.isTrue(template.hasProcedureName(), "未定义SpringBean!");
        String procedureName = template.getProcedureName();
        Object obj = ApplicationContextWrapper.getBean(procedureName);
        Assert.notNull(obj, String.format("未找到名称为[%s]的SpringBean", procedureName));
        if (ClassHelper.isSubClass(obj.getClass(), ExcelImportAbstract.class)) {
            return (ExcelImportAbstract) obj;
        }
        return null;
    }

    @Override
    @Transactional
    public String doImport(String templateId, String batchNumber, String fileName) throws Exception {
        ExcelImportTemplate template = this.loadExcelImportTemplate(templateId);
        Assert.isTrue(template != null && template.getDetails().size() > 0, "未找到Excel模板或者未设置Excel模板明细。");
        ExcelImportAbstract excelImportApplication = this.getExcelImportApplication(template);

        if (StringUtil.isBlank(batchNumber)) {
            batchNumber = CommonUtil.createGUID();
        }

        String sql = this.buildExcelImportSql(template);

        Object[] objects = new Object[] { batchNumber, ExcelImportStatus.START.getId() };

        // 表头列数
        int headRowSpan = 1;
        if (excelImportApplication != null) {
            headRowSpan = excelImportApplication.getHeadRowspan(template.getCode());
        }

        LogHome.getLog().info("---执行导入开始---");
        Long startTime = System.currentTimeMillis();

        internalImport(fileName, sql, template.getDetails().size(), headRowSpan, objects);

        Long endTime = System.currentTimeMillis();

        LogHome.getLog().info("---执行导入结束，耗时【" + (endTime - startTime) + "毫秒】---");
        ThreadLocalUtil.putVariable("ExcelImportTemplate_", template);

        if (template.hasProcedureName()) {
            internalAfterImport(template.getProcedureName(), batchNumber);
        }
        return batchNumber;
    }

    @Override
    public long countExcelImport(String templateId, String batchNumber, ExcelImportStatus excelImportStatus) {
        ExcelImportTemplate template = this.excelImportTemplateRepository.findOne(templateId);
        Assert.notNull(template, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, templateId, "Excel导入模板"));
        String sql = String.format("select count(0) from %s where batch_number = ? and status = ?", template.getTableName());
        return this.sqlExecutorDao.queryToInt(sql, batchNumber, excelImportStatus.getId());
    }

    @Override
    public String saveExcelImportLog(ExcelImportLog excelImportLog) {

        CreatorOrgNodeData creatorOrgNodeData = CreatorOrgNodeData.buildOperatorOrgNodeData();
        excelImportLog.setCreatorOrgNodeData(creatorOrgNodeData);
        excelImportLog.setCreatedDate(DateUtil.getDateTime());
        excelImportLog = excelImportLogRepository.save(excelImportLog);
        return excelImportLog.getId();
    }

    @Override
    public String buildExcelHeadByTemplateId(String templateId) {
        ExcelImportTemplate excelImportTemplate = this.loadExcelImportTemplate(templateId);
        Assert.isTrue(excelImportTemplate != null && excelImportTemplate.getDetails().size() > 0, "未找到Excel模板或者未设置Excel模板明细。");
        if (excelImportTemplate.hasProcedureName()) {
            ExcelImportAbstract excelImportApplication = this.getExcelImportApplication(excelImportTemplate);
            // 判读是否存在自定义表头
            if (excelImportApplication != null) {
                String headXml = excelImportApplication.getHeadXml(excelImportTemplate.getCode());
                if (headXml != null) {
                    return headXml;
                }
            }
        }
        StringBuffer excelHead = new StringBuffer("<tables><table><row>");

        for (ExcelImportTemplateDetail item : excelImportTemplate.getDetails()) {
            excelHead.append("<col field='").append(item.getColumnName()).append("'>").append(item.getExcelColumnName()).append("</col>");
        }
        excelHead.append("</row></table></tables>");

        return excelHead.toString();
    }

    @Override
    public Map<String, Object> slicedQueryExcelImportLogs(ParentIdQueryRequest queryRequest) {
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(QUERY_XML_FILE_PATH, "excelImportLog");
        return this.sqlExecutorDao.executeSlicedQuery(queryDescriptor, queryRequest);
    }

    @Override
    public Map<String, Object> slicedQueryExcelImportDetails(String templateId, String batchNumber, ExcelImportStatus status, QueryPageRequest pageRequest) {
        ExcelImportTemplate template = this.loadExcelImportTemplate(templateId);
        Assert.isTrue(template != null && template.getDetails().size() > 0, "未找到Excel模板或者未设置Excel模板明细。");
        QueryModel queryModel = QueryModel.newInstanceByPageRequest(pageRequest);
        String sql = template.buildSelectImportDetailSql(status);
        queryModel.setSql(sql);
        queryModel.putParam("batchNumber", batchNumber);
        if (status != null) {
            queryModel.putParam("status", status.getId());
        }
        queryModel.putDictionary("status", ExcelImportStatus.getMap());
        return this.sqlExecutorDao.executeSlicedQuery(queryModel);
    }

    @Override
    @Transactional
    public void deleteTemporaryData(String templateId, String batchNumber) {
        Assert.hasText(templateId, "参数templateId不能为空。");
        Assert.hasText(batchNumber, "参数batchNumber不能为空。");

        ExcelImportTemplate excelImportTemplate = this.loadExcelImportTemplate(templateId);

        Assert.notNull(excelImportTemplate, MessageSourceContext.getMessage(MessageConstants.OBJECT_NOT_FOUND_BY_ID, templateId, "Excel导入模板"));

        String sql = String.format("delete from %s  where batch_number = :batchNumber", excelImportTemplate.getTableName());

        this.generalRepository.updateByNativeSql(sql, QueryParameter.buildParameters("batchNumber", batchNumber));

    }

    class BatchInsert extends BatchSqlUpdate implements IExcelRowReader {
        private Object[] params;

        private Integer dataLength;

        private Integer headRowSpan;

        BatchInsert(DataSource dataSource, String sql, Integer headRowSpan) {
            super(dataSource, sql);
            LogHome.getLog(ExcelImportApplicationImpl.class).info(sql);
            setBatchSize(10000);// 批量个数
            this.headRowSpan = headRowSpan;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }

        public void setSqlParameter(int count) {
            this.dataLength = count;
            // 默认一个ID需要的参数
            declareParameter(new SqlParameter(Types.VARCHAR));
            // 默认参数
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Number) {
                    declareParameter(new SqlParameter(Types.NUMERIC));
                } else {
                    declareParameter(new SqlParameter(Types.VARCHAR));
                }
            }
            // EXCEL读取的数据默认为VARCHAR
            for (int i = 0; i < count; i++) {
                declareParameter(new SqlParameter(Types.VARCHAR));
            }
        }

        public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
            if (curRow < headRowSpan) {// 过滤表头列
                return;
            }
            if (rowlist.size() < this.dataLength) {
                throw new ApplicationException("导入excel格式不正确,缺少字段。");
            }
            Object[] dataSets = new Object[params.length + this.dataLength + 1];
            // 临时数据ID
            dataSets[0] = CommonUtil.createGUID();
            // 默认参数
            System.arraycopy(params, 0, dataSets, 1, params.length);
            // EXCEL读取的数据
            System.arraycopy(rowlist.toArray(), 0, dataSets, params.length + 1, this.dataLength);
            this.update(dataSets);
        }
    }

    /**
     * 测试导入接口调用
     */
    @Override
    public void executeImport(String batchNumber) {
        System.out.println("batchNumber:" + batchNumber);
    }

}
