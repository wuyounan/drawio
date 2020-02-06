package com.huigou.report.cubesviewer.domain.model;

import com.huigou.report.cubesviewer.util.ReportParameterCoverter;

/**
 * 日期维度过来器
 * 
 * @author gongmm
 */
public class DateFilter extends DimensionFilter {

    private final static String RENDER_FORMATTER = "{\"dimension\":\"%s\",\"mode\":\"%s\",\"date_from\":\"%s\",\"date_to\":\"%s\"}";

    enum DateKind {
        FROM, TO
    };

    private String mode;

    private String date_from;

    private String date_to;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    private void parseDateParameter(DateKind dateKind) {
        boolean isDateFrom = dateKind == DateKind.FROM;

        String dateValue = isDateFrom ? date_from : date_to;
        if (this.isCustomFuntion(dateValue)) {
            String value = ReportParameterCoverter.covert(dateValue);
            if (isDateFrom) {
                this.setDate_from(value);
            } else {
                this.setDate_to(value);
            }
        }

    }

    public void parseCustomParameter() {
        parseDateParameter(DateKind.FROM);
        parseDateParameter(DateKind.TO);
    }

    @Override
    public String render() {
        return String.format(RENDER_FORMATTER, this.getDimension(), this.getMode(), this.getDate_from(), this.getDate_to());
    }

}
