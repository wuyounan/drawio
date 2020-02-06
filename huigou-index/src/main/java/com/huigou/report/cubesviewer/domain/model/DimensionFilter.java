package com.huigou.report.cubesviewer.domain.model;

import com.huigou.util.StringPool;
import com.huigou.util.StringUtil;

/**
 * 维度过滤器
 * 
 * @author gongmm
 */
public class DimensionFilter {

	public static final String CITY_DIMENSION = "com_text_new:com_text_new";

	public static final String ORGAN_NAME_DIMENSION = "organ_name:organ_name";

	public static final String RUT_NAME_DIMENSION = "rut_name:rut_name";

	private String dimension;

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	protected boolean isCustomFuntion(String value) {
		if (StringUtil.isNotBlank(value)) {
			return value.contains(StringPool.AT);
		}
		return false;
	}

	public void parseCustomParameter() {

	}

	public String render() {
		return null;
	}

	public boolean isCity() {
		return CITY_DIMENSION.equals(this.dimension);
	}

	public void setCityDimension() {
		this.dimension = CITY_DIMENSION;
	}

	public boolean isOrganName() {
		return ORGAN_NAME_DIMENSION.equals(this.dimension);
	}

	public void setOrganNameDimension() {
		this.dimension = ORGAN_NAME_DIMENSION;
	}

	public boolean isRutName() {
		return RUT_NAME_DIMENSION.equals(this.dimension);
	}

	public void setRutNameDimension() {
		this.dimension = RUT_NAME_DIMENSION;
	}

}
