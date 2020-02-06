package com.huigou.report.common.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;

public class ReportDefinitionQueryRequest extends FolderAndCodeAndNameQueryRequest {
	protected Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
