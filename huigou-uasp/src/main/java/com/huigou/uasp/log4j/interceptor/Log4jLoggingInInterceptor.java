package com.huigou.uasp.log4j.interceptor;

import java.io.InputStream;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;

/**
 * 拦截器 日志输出
 * 
 * @author xiexin
 */
public class Log4jLoggingInInterceptor extends LoggingInInterceptor {
    private String loggerName;

    public Log4jLoggingInInterceptor() {
        super();
        this.loggerName = "log4j.loggingInInterceptor";
        this.setShowBinaryContent(false);
        this.setShowMultipartContent(false);
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void handleMessage(Message message) throws Fault {
        if (message.containsKey(LoggingMessage.ID_KEY)) {
            return;
        }
        String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null) {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        message.put(LoggingMessage.ID_KEY, id);
        final LoggingMessage buffer = new LoggingMessage("Inbound Message\n----------------------------", id);
        if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
            // avoid logging the default responseCode 200 for the decoupled responses
            Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
            if (responseCode != null) {
                buffer.getResponseCode().append(responseCode);
            }
        }
        String encoding = (String) message.get(Message.ENCODING);
        if (encoding != null) {
            buffer.getEncoding().append(encoding);
        }
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null) {
            buffer.getHttpMethod().append(httpMethod);
        }
        String ct = (String) message.get(Message.CONTENT_TYPE);
        if (ct != null) {
            buffer.getContentType().append(ct);
        }
        Object headers = message.get(Message.PROTOCOL_HEADERS);

        if (headers != null) {
            buffer.getHeader().append(headers);
        }
        String uri = (String) message.get(Message.REQUEST_URL);
        if (uri == null) {
            String address = (String) message.get(Message.ENDPOINT_ADDRESS);
            uri = (String) message.get(Message.REQUEST_URI);
            if (uri != null && uri.startsWith("/")) {
                if (address != null && !address.startsWith(uri)) {
                    if (address.endsWith("/") && address.length() > 1) {
                        address = address.substring(0, address.length());
                    }
                    uri = address + uri;
                }
            } else {
                uri = address;
            }
        }
        if (uri != null) {
            buffer.getAddress().append(uri);
            String query = (String) message.get(Message.QUERY_STRING);
            if (query != null) {
                buffer.getAddress().append("?").append(query);
            }
        }

        if (!isShowBinaryContent() && isBinaryContent(ct)) {
            buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
            log(buffer.toString());
            return;
        }
        if (!isShowMultipartContent() && isMultipartContent(ct)) {
            buffer.getMessage().append(MULTIPART_CONTENT_MESSAGE).append('\n');
            log(buffer.toString());
            return;
        }

        InputStream is = message.getContent(InputStream.class);
        if (is != null) {
            logInputStream(message, is, buffer, encoding, ct);
        } else {
            Reader reader = message.getContent(Reader.class);
            if (reader != null) {
                logReader(message, reader, buffer);
            }
        }
        log(formatLoggingMessage(buffer));
    }

    protected void log(String message) {
        message = transform(message);
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
        Logger logger = Logger.getLogger(this.getLoggerName());
        LogRecord lr = new LogRecord(Level.INFO, message);
        lr.setSourceClassName(logger.getName());
        lr.setSourceMethodName(null);
        lr.setLoggerName(logger.getName());
        if (logger.getAllAppenders().hasMoreElements()) {
            logger.info(message);
        } else {
            System.out.println(message);
        }
    }
}
