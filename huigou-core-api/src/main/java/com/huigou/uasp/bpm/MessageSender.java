package com.huigou.uasp.bpm;


/**
 * 消息发送者
 * 
 * @author gongmm
 */
public interface MessageSender {
    
    /**
     * 发送消息
     * @param bizId
     * 发送消息
     * @param subject
     * 主题
     * @param url
     * url
     * @param sender
     * 发送人
     * @param receivers
     * 接收人
     */
    void send(String bizId, String subject, String url, String sender, String receivers);

}
