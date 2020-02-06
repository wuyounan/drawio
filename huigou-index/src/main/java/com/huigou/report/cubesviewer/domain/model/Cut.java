package com.huigou.report.cubesviewer.domain.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huigou.report.cubesviewer.util.ReportParameterCoverter;

/**
 * 切片
 * 
 * @author gongmm
 */
public class Cut extends DimensionFilter {

    private static final String RENDER_FORMATTER = "{\"dimension\":\"%s\",\"value\":\"%s\",\"invert\":%s}";

    private static final String PARAMETER_FORMATTER = "@\\w+";

    private String value;
    
    private String invert;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
    public String getInvert() {
        return invert;
    }

    public void setInvert(String invert) {
        this.invert = invert;
    }

    private void parseValueParameter() {
        // "value":"@p1;north_america;@p2"
        String value = this.getValue();
        String coveredValue = value;
        String parameterName, parameterValue;
        if (this.isCustomFuntion(value)) {

            Pattern pattern = Pattern.compile(PARAMETER_FORMATTER);
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                parameterName = matcher.group(0);
                parameterValue = ReportParameterCoverter.covert(parameterName);
                coveredValue = coveredValue.replaceFirst(parameterName, parameterValue);
            }
            this.setValue(coveredValue);
        }
    }

    public void parseCustomParameter() {
        parseValueParameter();
    }

    @Override
    public String render() {
        return String.format(RENDER_FORMATTER, this.getDimension(), this.getValue(), this.getInvert());
    }

}
