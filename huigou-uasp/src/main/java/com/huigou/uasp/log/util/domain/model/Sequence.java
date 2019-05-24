package com.huigou.uasp.log.util.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 版本号
 * 
 * @author yuanwf
 *
 */
@Document(collection = "SA.Sequence")
public class Sequence {
	/**
	 * ID
	 */
	@Id
	private String id;
	/**
	 * 序列号
	 */
	private Long sequence;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
}
