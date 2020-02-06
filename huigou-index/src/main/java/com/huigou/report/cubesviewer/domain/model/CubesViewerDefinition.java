package com.huigou.report.cubesviewer.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.BaseInfoWithFolderAbstractEntity;

@Entity
@Table(name = "RP_CUBES_VIEWER_DEFINITION")
public class CubesViewerDefinition extends BaseInfoWithFolderAbstractEntity {

	private static final long serialVersionUID = -1690944652250604836L;

	@Column(name = "json", length = 4000)
	private String json;

	@Column(name = "sequence")
	private Integer sequence;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
