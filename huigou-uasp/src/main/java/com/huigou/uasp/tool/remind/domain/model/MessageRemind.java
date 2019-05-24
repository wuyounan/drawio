package com.huigou.uasp.tool.remind.domain.model;

import javax.persistence.*;

import com.huigou.data.domain.model.BaseInfoAbstractEntity;

/**
 * 系统消息提醒配置表
 * 
 * @author xx
 *         SA_MESSAGE_REMIND
 * @date 2017-02-15 14:39
 */
@Entity
@Table(name = "SA_MESSAGE_REMIND")
public class MessageRemind extends BaseInfoAbstractEntity {

    private static final long serialVersionUID = 2516147794398045055L;

    /**
     * @length 32
     * @comment 文件夹ID
     **/
    @Column(name = "FOLDER_ID", updatable = false)
    protected String folderId;

    /**
     * @length 22
     * @comment 排序号
     **/
    @Column(name = "SEQUENCE")
    protected Integer sequence;

    /**
     * @length 512
     * @comment 提示文本
     **/
    @Column(name = "REMIND_TITLE")
    protected String remindTitle;

    /**
     * @length 512
     * @comment 连接地址
     **/
    @Column(name = "REMIND_URL")
    protected String remindUrl;

    /**
     * @length 128
     * @comment 函数
     **/
    @Column(name = "EXECUTE_FUNC")
    protected String executeFunc;

    /**
     * @length 22
     * @comment 替换类别 0 顺序替换 2 名称替换
     **/
    @Column(name = "REPLACE_KIND")
    protected Integer replaceKind;

    /**
     * @length 22
     * @comment 页面打开方式 0 新窗口 1 弹出
     **/
    @Column(name = "OPEN_KIND")
    protected Integer openKind;

    /* VARCHAR2 32 CanNull Y 文件夹ID */
    public java.lang.String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /* NUMBER 22 CanNull Y 排序号 */
    public java.lang.Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /* VARCHAR2 512 CanNull Y 提示文本 */
    public java.lang.String getRemindTitle() {
        return this.remindTitle;
    }

    public void setRemindTitle(String remindTitle) {
        this.remindTitle = remindTitle;
    }

    /* VARCHAR2 512 CanNull Y 连接地址 */
    public java.lang.String getRemindUrl() {
        return this.remindUrl;
    }

    public void setRemindUrl(String remindUrl) {
        this.remindUrl = remindUrl;
    }

    /* VARCHAR2 128 CanNull Y 函数 */
    public java.lang.String getExecuteFunc() {
        return this.executeFunc;
    }

    public void setExecuteFunc(String executeFunc) {
        this.executeFunc = executeFunc;
    }

    /* NUMBER 22 CanNull Y 替换类别 0 顺序替换 2 名称替换 */
    public java.lang.Integer getReplaceKind() {
        return this.replaceKind;
    }

    public void setReplaceKind(Integer replaceKind) {
        this.replaceKind = replaceKind;
    }

    /* NUMBER 22 CanNull Y 页面打开方式 0 新窗口 1 弹出 */
    public java.lang.Integer getOpenKind() {
        return this.openKind;
    }

    public void setOpenKind(Integer openKind) {
        this.openKind = openKind;
    }

}
