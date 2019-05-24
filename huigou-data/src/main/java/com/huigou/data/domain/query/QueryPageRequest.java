package com.huigou.data.domain.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huigou.data.query.model.SortField;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.JSONUtil;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.StringUtil;

/**
 * 页面分页排序信息
 * 
 * @author xx
 */

public class QueryPageRequest {

    public static final int DEFAULT_MAX_EXPORT_COUNT = 10000;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageIndex;

    /**
     * 排序字段名称
     */
    private String sortFieldName;

    /**
     * 排序顺序
     */
    private String sortOrder;

    /**
     * 页面排序字段列表
     */
    private String sortFields;

    /**
     * 统计字段名称
     */
    private String totalFields;

    private String orderBy;

    /**
     * 导出excel文件类型
     */
    private String exportExcelType;

    /**
     * 导出类型 all:全部, page:分页导出
     */
    private String exportType;

    /**
     * 导出表头
     */
    private String exportHead;

    public void initPageInfo(Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return;
        }
        Integer pageSize = ClassHelper.convert(params.get(Constants.PAGE_SIZE_PARAM_NAME), Integer.class, 20);
        Integer pageIndex = ClassHelper.convert(params.get(Constants.PAGE_PARAM_NAME), Integer.class, 1);
        String export = ClassHelper.convert(params.get(Constants.EXPORT_TYPE), String.class);
        if (export != null && export.equals("all")) {// 导出全部
            pageSize = -1;
        }
        this.setExportType(export);
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
        this.setTotalFields(ClassHelper.convert(params.get(Constants.TOTAL_FIELDS), String.class));
        this.setSortFieldName(ClassHelper.convert(params.get(Constants.SORT_NAME_PARAM_NAME), String.class));
        this.setSortOrder(ClassHelper.convert(params.get(Constants.SORT_ORDER_PARAM_NAME), String.class));
        this.setSortFields(ClassHelper.convert(params.get(Constants.SORT_FIELDS_PARM_NAME), String.class));
        this.setExportExcelType(ClassHelper.convert(params.get(Constants.EXPORT_EXCEL_TYPE), String.class));
        this.setExportHead(ClassHelper.convert(params.get(Constants.EXPORT_HEAD), String.class));
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public void setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        this.sortFields = sortFields;
    }

    public List<SortField> parseSortFieldList() {
        if (StringUtil.isBlank(this.getSortFields())) {
            return new ArrayList<>();
        }
        try {
            return JSONUtil.toListObject(this.getSortFields(), SortField.class);
        } catch (Exception e) {
            LogHome.getLog(this).error(e);
        }
        return new ArrayList<>();
    }

    public SortField getFirstSortField() {
        List<SortField> sortFields = this.getSortFieldList();
        if (sortFields != null && sortFields.size() > 0) {
            return sortFields.get(0);
        }
        return null;
    }

    public List<SortField> getSortFieldList() {
        List<SortField> sortFields = this.parseSortFieldList();
        if (StringUtil.isNotBlank(this.getSortFieldName())) {
            sortFields.add(new SortField(this.getSortFieldName(), this.getSortOrder()));
        }
        return sortFields;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalFields() {
        return totalFields;
    }

    public void setTotalFields(String totalFields) {
        this.totalFields = totalFields;
    }

    public void setExportExcelType(String exportExcelType) {
        this.exportExcelType = exportExcelType;
    }

    public int getExportExcelType() {
        if (StringUtil.isBlank(exportExcelType)) {
            return 1;
        } else if (exportExcelType.equals("xls")) {
            return 0;
        }
        return 1;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getExportHead() {
        return exportHead;
    }

    public void setExportHead(String exportHead) {
        this.exportHead = exportHead;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public static QueryPageRequest newInstance(SDO sdo) {
        QueryPageRequest model = new QueryPageRequest();
        model.initPageInfo(sdo.getProperties());
        return model;
    }

}
