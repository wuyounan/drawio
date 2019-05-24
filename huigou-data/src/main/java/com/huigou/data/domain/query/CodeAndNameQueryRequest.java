package com.huigou.data.domain.query;

/**
 * 查询通用模型
 * 
 * @author Administrator
 */

public class CodeAndNameQueryRequest extends QueryAbstractRequest {
	protected String code;

	protected String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
