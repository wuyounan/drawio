package com.huigou.uasp.tool.mail.impl;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import com.huigou.exception.MessageSendException;
import com.huigou.freemarker.FreemarkerUtil;
import com.huigou.uasp.tool.mail.EmailSender;
import com.huigou.util.StringUtil;

/**
 * 通用邮件发送类
 * 
 * @Title: EmailSenderImpl.java
 * @author xiexin
 * @version V1.0
 */
public class EmailSenderImpl implements EmailSender {
    private JavaMailSender mailSender;

    // 邮件发送者
    private String fromEmail;

    // 默认Freemarker 模板
    private String emailTemplate;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setEmailTemplate(String emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    /**
     * 解析模板组合html
     * 
     * @param template
     * @param maps
     * @return
     */
    private String getMessageText(String template, Map<String, Object> maps) {
        String templatePath = StringUtil.tryThese(template, emailTemplate);
        Assert.hasText(templatePath, "邮件发送模板不能为空!");
        if (!templatePath.endsWith(".ftl")) {
            templatePath = String.format(TEMPLATE_PATH, templatePath);
        }
        try {
            return FreemarkerUtil.generate(templatePath, maps);
        } catch (Exception e) {
            throw new MessageSendException(e);
        }
    }

    @Override
    public void doSend(String toEmail, String subject, String template, Map<String, Object> maps) {
        // 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            // 创建MimeMessageHelper对象，处理MimeMessage的辅助类
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            // 使用辅助类MimeMessage设定参数
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            // 读取Freemarker模板设置,组合发送内容
            String messageText = this.getMessageText(template, maps);
            // 第二个参数true，表示text的内容为html
            helper.setText(messageText, true);
        } catch (Exception e) {
            throw new MessageSendException(e);
        }
        mailSender.send(msg);
    }

    @Override
    public void doSend(String toEmail, String subject, Map<String, Object> maps) {
        this.doSend(toEmail, subject, null, maps);
    }

    @Override
    public void doSend(String toEmail, String subject, String text) {
        // 为普通邮件模板，支持文本
        SimpleMailMessage smm = new SimpleMailMessage();
        // 设定邮件参数
        smm.setFrom(fromEmail);
        smm.setTo(toEmail);
        smm.setSubject(subject);
        smm.setText(text);
        // 发送邮件
        mailSender.send(smm);
    }

}
