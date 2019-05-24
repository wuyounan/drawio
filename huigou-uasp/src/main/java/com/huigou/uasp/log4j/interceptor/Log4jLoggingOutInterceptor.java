package com.huigou.uasp.log4j.interceptor;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;

/**
 * 拦截器 日志输出
 * 
 * @author xiexin
 */
public class Log4jLoggingOutInterceptor extends LoggingOutInterceptor {
    private String loggerName;

    private static final String LOG_SETUP = Log4jLoggingOutInterceptor.class.getName() + ".log-setup";

    public Log4jLoggingOutInterceptor() {
        super();
        this.loggerName = "log4j.loggingOutInterceptor";
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void handleMessage(Message message) throws Fault {
        final OutputStream os = message.getContent(OutputStream.class);
        final Writer iowriter = message.getContent(Writer.class);
        if (os == null && iowriter == null) {
            return;
        }
        // Write the output while caching it for the log message
        boolean hasLogged = message.containsKey(LOG_SETUP);
        if (!hasLogged) {
            message.put(LOG_SETUP, Boolean.TRUE);
            if (os != null) {
                final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
                if (threshold > 0) {
                    newOut.setThreshold(threshold);
                }
                if (limit > 0) {
                    newOut.setCacheLimit(limit);
                }
                message.setContent(OutputStream.class, newOut);
                newOut.registerCallback(new LoggingCallback(message, os));
            } else {
                message.setContent(Writer.class, new LogWriter(message, iowriter));
            }
        }
    }

    private LoggingMessage setupBuffer(Message message) {
        String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null) {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        final LoggingMessage buffer = new LoggingMessage("Outbound Message\n---------------------------", id);

        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (responseCode != null) {
            buffer.getResponseCode().append(responseCode);
        }

        String encoding = (String) message.get(Message.ENCODING);
        if (encoding != null) {
            buffer.getEncoding().append(encoding);
        }
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null) {
            buffer.getHttpMethod().append(httpMethod);
        }
        String address = (String) message.get(Message.ENDPOINT_ADDRESS);
        if (address != null) {
            buffer.getAddress().append(address);
            String uri = (String) message.get(Message.REQUEST_URI);
            if (uri != null && !address.startsWith(uri)) {
                if (!address.endsWith("/") && !uri.startsWith("/")) {
                    buffer.getAddress().append("/");
                }
                buffer.getAddress().append(uri);
            }
        }
        String ct = (String) message.get(Message.CONTENT_TYPE);
        if (ct != null) {
            buffer.getContentType().append(ct);
        }
        Object headers = message.get(Message.PROTOCOL_HEADERS);
        if (headers != null) {
            buffer.getHeader().append(headers);
        }
        return buffer;
    }

    private class LogWriter extends FilterWriter {
        StringWriter out2;

        int count;

        Message message;

        final int lim;

        LogWriter(Message message, Writer writer) {
            super(writer);
            this.message = message;
            if (!(writer instanceof StringWriter)) {
                out2 = new StringWriter();
            }
            lim = limit == -1 ? Integer.MAX_VALUE : limit;
        }

        public void write(int c) throws IOException {
            super.write(c);
            if (out2 != null && count < lim) {
                out2.write(c);
            }
            count++;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            super.write(cbuf, off, len);
            if (out2 != null && count < lim) {
                out2.write(cbuf, off, len);
            }
            count += len;
        }

        public void write(String str, int off, int len) throws IOException {
            super.write(str, off, len);
            if (out2 != null && count < lim) {
                out2.write(str, off, len);
            }
            count += len;
        }

        public void close() throws IOException {
            LoggingMessage buffer = setupBuffer(message);
            if (count >= lim) {
                buffer.getMessage().append("(message truncated to " + lim + " bytes)\n");
            }
            StringWriter w2 = out2;
            if (w2 == null) {
                w2 = (StringWriter) out;
            }
            String ct = (String) message.get(Message.CONTENT_TYPE);
            try {
                writePayload(buffer.getPayload(), w2, ct);
            } catch (Exception ex) {
                // ignore
            }
            log(formatLoggingMessage(buffer));
            message.setContent(Writer.class, out);
            super.close();
        }
    }

    protected String formatLoggingMessage(LoggingMessage buffer) {
        return buffer.toString();
    }

    class LoggingCallback implements CachedOutputStreamCallback {

        private final Message message;

        private final OutputStream origStream;

        private final int lim;

        LoggingCallback(final Message msg, final OutputStream os) {
            this.message = msg;
            this.origStream = os;
            this.lim = limit == -1 ? Integer.MAX_VALUE : limit;
        }

        public void onFlush(CachedOutputStream cos) {

        }

        public void onClose(CachedOutputStream cos) {
            LoggingMessage buffer = setupBuffer(message);

            String ct = (String) message.get(Message.CONTENT_TYPE);
            if (!isShowBinaryContent() && isBinaryContent(ct)) {
                buffer.getMessage().append(BINARY_CONTENT_MESSAGE).append('\n');
                log(formatLoggingMessage(buffer));
                return;
            }
            if (!isShowMultipartContent() && isMultipartContent(ct)) {
                buffer.getMessage().append(MULTIPART_CONTENT_MESSAGE).append('\n');
                log(formatLoggingMessage(buffer));
                return;
            }

            if (cos.getTempFile() == null) {
                // buffer.append("Outbound Message:\n");
                if (cos.size() >= lim) {
                    buffer.getMessage().append("(message truncated to " + lim + " bytes)\n");
                }
            } else {
                buffer.getMessage().append("Outbound Message (saved to tmp file):\n");
                buffer.getMessage().append("Filename: " + cos.getTempFile().getAbsolutePath() + "\n");
                if (cos.size() >= lim) {
                    buffer.getMessage().append("(message truncated to " + lim + " bytes)\n");
                }
            }
            try {
                String encoding = (String) message.get(Message.ENCODING);
                writePayload(buffer.getPayload(), cos, encoding, ct);
            } catch (Exception ex) {
                // ignore
            }

            log(formatLoggingMessage(buffer));
            try {
                // empty out the cache
                cos.lockOutputStream();
                cos.resetOut(null, false);
            } catch (Exception ex) {
                // ignore
            }
            message.setContent(OutputStream.class, origStream);
        }
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
