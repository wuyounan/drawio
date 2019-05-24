package com.huigou.uasp.bpm.monitor.domain.query;

import com.huigou.data.domain.query.QueryAbstractRequest;

public class AskQueryRequest extends QueryAbstractRequest {
	/**
	 * 流程ID
	 */
	private String procId;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 流程名称
	 */
	private String procName;
	/**
	 * 发起部门
	 */
	private String deptName;
	/**
	 * 询问类型ID
	 */
	private Integer kindId;
	/**
	 * 主题
	 */
	private String title;
	/**
	 * 处理人
	 */
	private String personMemberName;
	/**
	 * askId
	 */
	private String askId;
	private String accepterId;
	private String status;

	public String getProcId() {
		return procId;
	}

	public void setProcId(String procId) {
		this.procId = procId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		this.procName = procName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPersonMemberName() {
		return personMemberName;
	}

	public void setPersonMemberName(String personMemberName) {
		this.personMemberName = personMemberName;
	}

	public String getAskId() {
		return askId;
	}

	public void setAskId(String askId) {
		this.askId = askId;
	}

	public String getAccepterId() {
		return accepterId;
	}

	public void setAccepterId(String accepterId) {
		this.accepterId = accepterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
