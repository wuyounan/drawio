package com.huigou.uasp.bmp.securitypolicy.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 人员登录限制
 * 
 * @author gongmm
 */
@Entity
@Table(name = "SA_PersonLoginLimit")
public class PersonLoginLimit extends AbstractEntity {

	private static final long serialVersionUID = -1566620852454185628L;

	/**
	 * 机器密级ID
	 */
	@Column(name = "Machine_Id")
	private String machineId;

	/**
	 * 人员ID
	 */
	@Column(name = "person_Id")
	private String personId;

	@Column(name = "full_Id")
	private String fullId;

	/**
	 * 登录名
	 */
	@Column(name = "Login_Name")
	private String loginName;

	/**
	 * 排序号
	 */
	private Integer sequence;

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFullId() {
		return fullId;
	}

	public void setFullId(String fullId) {
		this.fullId = fullId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
