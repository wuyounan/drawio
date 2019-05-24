package com.huigou.uasp.bpm.monitor.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 询问表
 * 
 * @author renxs
 * 
 */
@Entity
@Table(name = "Jd_Ask")
public class Ask extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 业务ID
	 */
	@Column(name = "biz_id")
	private String bizId;
	/**
	 * 业务标题
	 */
	@Column(name = "biz_title")
	private String bizTitle;
	/**
	 * 类型ID 1、抽检 2、询问
	 */
	@Column(name = "kind_id")
	private Integer kindId;
	/**
	 * 异常类型ID
	 */
	@Column(name = "exception_kind_Id")
	private Integer exceptionkindId;
	/**
	 * 抽检时间
	 */
	@Column(name = "sampling_time")
	private Date samplingTime;
	/**
	 * 总结内容
	 */
	@Column(name = "summary_content")
	private String summaryContent;
	/**
	 * 询问状态
	 */
	@Column(name = "ask_status")
	private Integer askStatus;
	/**
	 * 询问发起人
	 */
	private String sponsor;
	/**
	 * 询问明细集合
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "Ask_Id")
	private List<AskDetail> askDetails;

	public List<AskDetail> getAskDetails() {
		return askDetails;
	}

	public void setAskDetails(List<AskDetail> askDetails) {
		this.askDetails = askDetails;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBizId() {
		return this.bizId;
	}

	public void setBizTitle(String bizTitle) {
		this.bizTitle = bizTitle;
	}

	public String getBizTitle() {
		return this.bizTitle;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public Integer getKindId() {
		return this.kindId;
	}

	public void setExceptionkindId(Integer exceptionkindId) {
		this.exceptionkindId = exceptionkindId;
	}

	public Integer getExceptionkindId() {
		return this.exceptionkindId;
	}
	
	public void setSamplingTime(Date samplingTime) {
		this.samplingTime = samplingTime;
	}

	public Date getSamplingTime() {
		return this.samplingTime;
	}

	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}

	public String getSummaryContent() {
		return this.summaryContent;
	}

	public void setAskStatus(Integer askStatus) {
		this.askStatus = askStatus;
	}

	public Integer getAskStatus() {
		return this.askStatus;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getSponsor() {
		return this.sponsor;
	}
}
