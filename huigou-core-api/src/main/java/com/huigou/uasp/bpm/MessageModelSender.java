package com.huigou.uasp.bpm;

/**
 * 消息发送者
 * 
 * @author xx
 */
public interface MessageModelSender {

    /**
     * 发送消息
     * 
     * @param messageSendModel
     */
    void send(MessageSendModel messageSendModel);

}
