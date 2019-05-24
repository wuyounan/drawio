package com.huigou.uasp.bpm.engine;

import java.util.List;

import com.huigou.uasp.bpm.MessageModelSender;
import com.huigou.uasp.bpm.MessageSendModel;
import com.huigou.uasp.bpm.MessageSender;
import com.huigou.uasp.bpm.MessageSenderManager;

/**
 * 消息发送管理者
 * 
 * @author gongmm
 */
public class MessageSenderManagerImpl implements MessageSenderManager {

    private List<MessageSender> messageSenders;

    private List<MessageModelSender> messageModelSenders;

    public void setMessageSenders(List<MessageSender> messageSenders) {
        this.messageSenders = messageSenders;
    }

    public void setMessageModelSenders(List<MessageModelSender> messageModelSenders) {
        this.messageModelSenders = messageModelSenders;
    }

    @Override
    public void send(String bizId, String subject, String url, String sender, String receivers) {
        if (messageSenders != null && messageSenders.size() > 0) {
            for (MessageSender messageSender : messageSenders) {
               messageSender.send(bizId, subject, url, sender, receivers);
            }
        }
    }

    @Override
    public void send(MessageSendModel messageSendModel) {
        if (messageModelSenders != null && messageModelSenders.size() > 0) {
            for (MessageModelSender messageSender : messageModelSenders) {
                messageSender.send(messageSendModel);
            }
        }
    }

}
