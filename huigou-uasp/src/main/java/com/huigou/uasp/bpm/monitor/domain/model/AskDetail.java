package com.huigou.uasp.bpm.monitor.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huigou.data.domain.model.AbstractEntity;

/**
 * 询问明细表
 * 
 * @author renxs
 * 
 */
@Entity
@Table(name = "Jd_Ask_Detail")
public class AskDetail extends AbstractEntity {

	 private static final long serialVersionUID = 1L;
	
	 /**
	  * 询问ID
	  */
	 @Column(name = "ask_id")
	 private String askId;
	 /**
	  * 发送人ID
	  */
	 @Column(name = "sender_id")
	 private String senderId;
	 /**
	  * 发送人姓名
	  */
	 @Column(name = "sender_name")
	 private String senderName;
	 /**
	  * 问题内容
	  */
	 @Column(name = "ask_content")
	 private String askContent;
	 /**
	  * 发送时间
	  */
	 @Column(name = "send_time")
	 private Date sendTime;
	 /**
	  * 接受人ID
	  */
	 @Column(name = "accepter_id")
	 private String accepterId;
	 /**
	  * 接受人姓名
	  */
	 @Column(name = "accepter_name")
	 private String accepterName;
	 /**
	  * 回复内容
	  */
	 @Column(name = "reply_content")
	 private String replyContent;
	 /**
	  * 回复时间
	  */
	 @Column(name = "reply_time")
	 private Date replyTime;
	 /**
	  * 状态
	  */
	 @Column(name = "status")
	 private Integer status;

	 public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setAskId(String askId)
	 {
	 	 this.askId = askId;
	 }

	 public String getAskId()
	 {
	 	 return this.askId;
	 }

	 public void setSenderId(String senderId)
	 {
	 	 this.senderId = senderId;
	 }

	 public String getSenderId()
	 {
	 	 return this.senderId;
	 }

	 public void setSenderName(String senderName)
	 {
	 	 this.senderName = senderName;
	 }

	 public String getSenderName()
	 {
	 	 return this.senderName;
	 }

	 public void setAskContent(String askContent)
	 {
	 	 this.askContent = askContent;
	 }

	 public String getAskContent()
	 {
	 	 return this.askContent;
	 }

	 public void setSendTime(Date sendTime)
	 {
	 	 this.sendTime = sendTime;
	 }

	 public Date getSendTime()
	 {
	 	 return this.sendTime;
	 }

	 public void setAccepterId(String accepterId)
	 {
	 	 this.accepterId = accepterId;
	 }

	 public String getAccepterId()
	 {
	 	 return this.accepterId;
	 }

	 public void setAccepterName(String accepterName)
	 {
	 	 this.accepterName = accepterName;
	 }

	 public String getAccepterName()
	 {
	 	 return this.accepterName;
	 }

	 public void setReplyContent(String replyContent)
	 {
	 	 this.replyContent = replyContent;
	 }

	 public String getReplyContent()
	 {
	 	 return this.replyContent;
	 }

	 public void setReplyTime(Date replyTime)
	 {
	 	 this.replyTime = replyTime;
	 }

	 public Date getReplyTime()
	 {
	 	 return this.replyTime;
	 }
 }
