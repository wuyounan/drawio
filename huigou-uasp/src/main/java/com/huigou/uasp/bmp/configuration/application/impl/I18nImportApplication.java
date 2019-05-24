package com.huigou.uasp.bmp.configuration.application.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huigou.data.jdbc.util.BatchSqlUpdateDetail;
import com.huigou.data.query.model.QueryDescriptor;
import com.huigou.uasp.bmp.configuration.application.I18npropertiesApplication;
import com.huigou.uasp.tool.dataimport.application.ExcelImportAbstract;

/**
 * 导入国际化资源修改数据
 * 
 * @author xx
 */
@Service("i18nImportApplication")
public class I18nImportApplication extends ExcelImportAbstract {

    /**
     * 默认表头占一行
     * 
     * @return
     */
    public int getHeadRowspan(String code) {
        return 1;
    }

    /**
     * 返回表头xml
     * 
     * @return 返回null使用默认表头
     */
    public String getHeadXml(String code) {
        return null;
    }

    @Override
    public void executeImport(String batchNumber) {
        // 导入国际化资源修改数据
        QueryDescriptor queryDescriptor = this.sqlExecutorDao.getQuery(I18npropertiesApplication.QUERY_XML_FILE_PATH, "i18nproperties");
        String sql = queryDescriptor.getSqlByName("queryTempData");
        List<Map<String, Object>> datas = this.sqlExecutorDao.queryToListMap(sql, batchNumber);
        if (datas != null && datas.size() > 0) {
            String updataSql = queryDescriptor.getSqlByName("updataByCode");
            BatchSqlUpdateDetail batchDetail = BatchSqlUpdateDetail.newInstance(this.sqlExecutorDao.getDataSource(), updataSql, null);
            for (Map<String, Object> m : datas) {
                batchDetail.setRows(m);
            }
            batchDetail.flush();
            String deleteTempData = queryDescriptor.getSqlByName("deleteTempData");
            this.sqlExecutorDao.executeUpdate(deleteTempData, batchNumber);
        }
    }

}
