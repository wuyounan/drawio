package com.huigou.uasp.tool.mail;

import java.util.Map;

import javax.mail.MessagingException;

public interface EmailSender {
    public static final String TEMPLATE_PATH = "/email/%s.ftl";

    /**
     * 按模板发送邮件
     * 
     * @param toEmail
     * @param subject
     * @param template
     * @param maps
     */
    void doSend(String toEmail, String subject, String template, Map<String, Object> maps);

    /**
     * 使用默认模板发送邮件
     * 
     * @param toEmail
     * @param subject
     * @param maps
     */
    void doSend(String toEmail, String subject, Map<String, Object> maps);

    /**
     * 发送简单邮件
     * 
     * @param toEmail
     * @param subject
     * @throws MessagingException
     */
    public void doSend(String toEmail, String subject, String text);
}
