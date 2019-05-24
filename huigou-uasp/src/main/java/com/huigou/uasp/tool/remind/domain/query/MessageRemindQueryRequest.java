package com.huigou.uasp.tool.remind.domain.query;

import com.huigou.data.domain.query.FolderAndCodeAndNameQueryRequest;

/**
 * 系统消息提醒配置表
 * 
 * @author xx
 *         SA_MESSAGE_REMIND
 * @date 2017-02-15 14:39
 */
public class MessageRemindQueryRequest extends FolderAndCodeAndNameQueryRequest {

    /**
     * @length 512
     * @comment 提示文本
     **/
    protected String remindTitle;

    public String getRemindTitle() {
        return remindTitle;
    }

    public void setRemindTitle(String remindTitle) {
        this.remindTitle = remindTitle;
    }

}
