package com.huigou.uasp.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.huigou.cache.SystemCache;
import com.huigou.context.ContextUtil;
import com.huigou.context.Operator;
import com.huigou.context.ThreadLocalUtil;
import com.huigou.data.pdf.PDFCreater;
import com.huigou.exception.ApplicationException;
import com.huigou.uasp.bmp.doc.attachment.domain.model.RangeSettings;
import com.huigou.uasp.bpm.engine.application.ProcUnitHandlerApplication;
import com.huigou.uasp.client.controller.ControllerBase;
import com.huigou.uasp.exception.PageForwardException;
import com.huigou.uasp.log.util.BizLogUtil;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.ContentTypeHelper;
import com.huigou.util.FileHelper;
import com.huigou.util.FilterUserInputContent;
import com.huigou.util.JSONUtil;
import com.huigou.util.LogHome;
import com.huigou.util.SDO;
import com.huigou.util.SpringBeanFactory;
import com.huigou.util.StringUtil;

/**
 * Controller基类
 *
 * @author xx
 */
public class CommonController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    protected final static String ID_KEY_NAME = "id";

    protected final static String CODE_KEY_NAME = "code";

    protected final static String PARENT_ID_KEY_NAME = "parentId";

    protected final static String FOLDER_ID_KEY_NAME = "folderId";

    protected final static String SEQUENCE_KEY_NAME = "sequence";

    protected final static String IDS_KEY_NAME = "ids";

    protected final static String STATUS_KEY_NAME = "status";

    protected final static String BIZ_ID_KEY_NAME = "bizId";

    protected final static String BIZ_CODE_KEY_NAME = "bizCode";

    protected final static String DATA_KEY_NAME = Constants.DATA;

    protected enum Status {
        SUCCESS, SUCCESS_TIPS, ERROR, ERROR_DIALOG;
    }

    protected SDO getSDO() {
        SDO sdo;
        if (ContextUtil.isAppRequest()) {
            sdo = (SDO) getRequest().getAttribute(Constants.SDO);
        } else {
            sdo = new SDO(true);
            HttpServletRequest request = this.getRequest();
            Enumeration<?> em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String key = (String) em.nextElement();
                String[] values = request.getParameterValues(key);
                if (values != null && values.length > 0) {
                    if (values.length == 1) {
                        sdo.putProperty(key, StringUtil.decode(values[0]));
                    } else {
                        sdo.putProperty(key, values);
                    }
                }
            }
        }
        sdo.setOperator(this.getOperator());
        ThreadLocalUtil.putVariable(Constants.SDO, sdo);
        return sdo;
    }

    /**
     * 获取request Parameter(使用 jsoup 来对用户输入内容进行过滤 防止xss攻击)
     *
     * @param key
     * @return String
     */
    protected String getFilterInputParameter(String key) {
        String[] para = (String[]) this.getParameterArray(key);
        if (para != null && para.length > 0) {
            String value = StringUtil.decode(para[0]);
            value = StringUtil.decode((String) value);
            return FilterUserInputContent.doFilter((String) value);
        }
        return null;
    }

    /**
     * 获取request getAttribute
     *
     * @param key
     * @return
     */
    protected Object getAttribute(String key) {
        return this.getRequest().getAttribute(key);
    }

    /**
     * 设置 request setAttribute
     *
     * @param key
     * @param value
     */
    protected void putAttribute(String key, Object value) {
        // 判读字段权限 如果key 无权限 则修改 value 为********
        // noaccessField 赋值在 ExecuteContextInterceptor
        @SuppressWarnings("unchecked")
        Map<String, Object> noaccessField = (Map<String, Object>) ThreadLocalUtil.getVariable("noaccessField");
        if (noaccessField != null) {
            if (noaccessField.containsKey(key)) {
                value = "********";
            }
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> attributeMap = (Map<String, Object>) ThreadLocalUtil.getVariable("requestAttributeMap");
        if (attributeMap == null) {
            attributeMap = new HashMap<String, Object>();
        }
        attributeMap.put(key, value);
        ThreadLocalUtil.putVariable("requestAttributeMap", attributeMap);
        this.getRequest().setAttribute(key, value);
    }

    /**
     * 设置 request putAttribute
     *
     * @param key
     * @param value
     */
    protected void putAttribute(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            Iterator<String> it = map.keySet().iterator();
            String key = null;
            while (it.hasNext()) {
                key = it.next();
                this.putAttribute(key, map.get(key));
            }
        }
    }

    /**
     * 获取登录用户
     *
     * @return
     */
    protected Operator getOperator() {
        return ContextUtil.findOperator();
    }

    /**
     * 得到页面路径
     *
     * @return
     */
    protected String getPagePath() {
        return null;
    }

    private String buildPagePath(String page) {
        if (StringUtil.isBlank(page)) {
            return null;
        } else if (page.endsWith(".jsp")) {// 指定到页面直接返回
            return page;
        }
        return getPagePath() + page + ".jsp";
    }

    /**
     * 跳转到页面,无需配置struts的result
     *
     * @param page
     * @param obj
     *            Map SDO POJO
     * @return String
     */
    @SuppressWarnings("unchecked")
    protected String forward(String page, Object obj) {
        if (obj != null) {
            if (obj instanceof Map) {
                this.putAttribute((Map<String, Object>) obj);
            } else if (obj instanceof SDO) {
                this.putAttribute(((SDO) obj).getProperties());
            } else {
                this.putAttribute(obj.getClass().getSimpleName() + "_", obj);
                this.putAttribute(ClassHelper.toMap(obj));
            }
        }

        if (ContextUtil.isAppRequest()) {
            Object attribute = ThreadLocalUtil.getVariable("requestAttributeMap");
            // 手机端访问请求
            return success(attribute);
        }
        try {
            this.getRequest().getRequestDispatcher(buildPagePath(page)).forward(this.getRequest(), this.getResponse());
        } catch (ServletException e) {
            throw new PageForwardException(e);
        } catch (IOException e) {
            throw new PageForwardException(e);
        }
        return NONE;
    }

    protected String forward(String page) {
        return forward(page, null);
    }

    protected String blank(Status status, String message, Object data) {
        Map<String, Object> object = new HashMap<String, Object>();
        object.put("status", status.ordinal());// 状态
        if (!StringUtil.isBlank(message)) {
            object.put("message", message);// 信息
        }
        if (data != null) {// 数据
            if (data instanceof Page<?>) {
                Page<?> page = (Page<?>) data;
                Map<String, Object> result = new HashMap<String, Object>(3);
                result.put(Constants.RECORD, page.getTotalElements());
                result.put(Constants.ROWS, page.getContent());
                object.put(Constants.DATA, result);
            } else if (data instanceof SDO) {
                object.put(Constants.DATA, ((SDO) data).getProperties());
            } else if (ClassHelper.isBaseType(data.getClass())) {
                object.put(Constants.DATA, data.toString());
            } else {
                object.put(Constants.DATA, data);
            }
        }
        return blank(JSONUtil.toString(object));
    }

    protected String success() {
        return blank(Status.SUCCESS_TIPS, null, "ok");
    }

    protected String success(Object data) {
        return blank(Status.SUCCESS_TIPS, null, data);
    }

    protected String success(String message, Object data) {
        return blank(Status.SUCCESS_TIPS, message, data);
    }

    protected String error(Throwable throwable) {
        LogHome.getLog(this).error(throwable.getMessage(), throwable);
        BizLogUtil.putBizLogException(throwable);
        if (throwable instanceof ActivitiException) {
            String message = throwable.getMessage();
            message = message.replaceAll("Exception while invoking TaskListener:", "");
            return error(message);
        }
        return error(throwable.getMessage());
    }

    protected String error(String message) {
        return error(message, null);
    }

    protected String error(String message, Object data) {
        if (BizLogUtil.getBizLogException() == null) {
            BizLogUtil.putBizLogException(message);
        }
        return blank(Status.ERROR, message, data);
    }

    protected String toResult(Object data) {
        return blank(Status.SUCCESS, null, data);
    }

    /**
     * 打包成Grid数据并返回数据
     *
     * @param data
     * @return
     */
    protected String packGridDataAndResult(Object data) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put(Constants.ROWS, data);
        if (List.class.isAssignableFrom(data.getClass())) {
            result.put(Constants.RECORD, ((List<?>) data).size());
        }
        return blank(Status.SUCCESS, null, result);
    }

    /**
     * 返回JS脚本弹出消息
     *
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @Title: alert
     * @author @param @param s
     */
    protected String alert(String s) {
        return blank("<script>alert('" + s + "');</script>");
    }

    /**
     * 打开页面输出流返回数据
     *
     * @param msg
     * @return
     */
    protected String blank(String msg) {
        String outMsg = msg;
        if (ContextUtil.isJsonpCall()) {
            String callback = ClassHelper.convert(this.getParameter("callback"), String.class);
            outMsg = String.format("%s(%s)", callback, msg);
        }
        HttpServletResponse response = this.getResponse();
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        LogHome.getLog(this).debug(msg);
        try {
            response.getWriter().write(outMsg);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            logger.error("输出数据出错", e);
        }
        return NONE;
    }

    /**
     * 返回页面
     *
     * @param map
     * @return
     * @throws Exception
     */
    protected String blank(Map<String, Object> map) throws Exception {
        return blank(JSONUtil.toString(map));
    }

    /**
     * 返回页面
     *
     * @param collection
     * @return
     * @throws Exception
     */
    protected String blank(Collection<?> collection) throws Exception {
        return blank(JSONUtil.toString(collection));
    }

    /**
     * 返回错误页面
     *
     * @param msg
     * @return
     */
    protected String errorPage(String msg) {
        this.putAttribute("tip", msg);
        if (BizLogUtil.getBizLogException() == null) {
            BizLogUtil.putBizLogException(msg);
        }
        return forward(Constants.ERROR_RESULT);
    }

    /**
     * 返回错误页面
     *
     * @param throwable
     * @return
     */
    protected String errorPage(Throwable throwable) {
        LogHome.getLog(this).error(throwable.getMessage(), throwable);

        BizLogUtil.putBizLogException(throwable);

        return errorPage(throwable.getMessage());
    }

    /**
     * 记录错误日志
     *
     * @param message
     * @param t
     */
    protected void logError(String message, Throwable t) {
        LogHome.getLog(this).error(message, t);
    }

    /**
     * 获取spring中的bean
     *
     * @param name
     * @return
     */
    protected Object getSpringBean(String name) {
        return SpringBeanFactory.getBean(this.getServletContext(), name);
    }

    /**
     * 排除条件
     */
    protected void excludeRootCriteria(Map<String, Object> params, String name) {
        if (params.containsKey(name)) {
            String value = ClassHelper.convert(params.get(name), String.class, "0");
            if (value.equals("0")) {
                params.remove(name);
            }
        }
    }

    /**
     * Response 输出文件
     *
     * @return String
     * @author
     */
    protected String outputFile(File file, String type) throws Exception {
        HttpServletResponse res = this.getResponse();
        ServletOutputStream out = null;
        res.setContentType(ContentTypeHelper.getContentType(type));
        res.setCharacterEncoding("utf-8");
        res.setHeader("Content-Disposition", "inline;fileName=" + System.currentTimeMillis() + "." + type + "");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            out = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            res.flushBuffer();
        } catch (final Exception e) {
            throw new ApplicationException(e);
        } finally {
            if (bis != null) bis.close();
            if (bos != null) bos.close();
            if (out != null) out.close();
        }
        return null;
    }

    /**
     * 跟换Freemarker模板生成PDF显示在网页上
     *
     * @param template
     * @param variables
     * @return String
     * @author
     */
    protected String outputPDF(String template, Map<String, Object> variables) {
        String imgHttpUrl = this.getRequest().getScheme() + "://" + this.getRequest().getServerName() + ":" + this.getRequest().getServerPort()
                            + SystemCache.getContextPath();
        variables.put("imgHttpUrl", imgHttpUrl);
        String path = PDFCreater.createByFreemarker(template, variables);
        File file = new File(FileHelper.getTmpdir(), path);
        if (file != null && file.exists()) {
            try {
                return outputFile(file, "pdf");
            } catch (Exception e) {
                return errorPage(e);
            } finally {
                file.delete();
            }
        }
        return null;
    }

    /**
     * Freemarker模板生成PDF显示在网页上(包含审批流程)
     * 
     * @param template
     *            Freemarker 模板
     * @param bizId
     *            业务单据ID
     * @param variables
     *            参数
     * @return
     */
    public String outputAndProcUnitHandlerPDF(String template, String bizId, Map<String, Object> variables) {
        ProcUnitHandlerApplication application = SpringBeanFactory.getBean(this.getServletContext(), "procUnitHandlerApplication",
                                                                           ProcUnitHandlerApplication.class);
        if (application != null && bizId != null) {
            List<Map<String, Object>> group = application.groupProcUnitHandlers(bizId, "Approve", "", "", "-1");
            Map<String, Object> a = new HashMap<String, Object>(2);
            a.put("taskExecutionList", group);
            variables.put("parameters", a);
        }
        return outputPDF(template, variables);
    }

    /**
     * 跟换Freemarker模板生成WORD显示在网页上
     *
     * @param template
     * @param variables
     * @return
     * @throws
     * @author
     */
    protected String outputWord(String template, Map<String, Object> variables) {
        String imgHttpUrl = this.getRequest().getScheme() + "://" + this.getRequest().getServerName() + ":" + this.getRequest().getServerPort()
                            + SystemCache.getContextPath();
        variables.put("imgHttpUrl", imgHttpUrl);
        String path = PDFCreater.createWord(template, variables);
        File file = new File(FileHelper.getTmpdir(), path);
        if (file != null && file.exists()) {
            try {
                return outputFile(file, "doc");
            } catch (Exception e) {
                return errorPage(e);
            } finally {
                file.delete();
            }
        }
        return null;
    }

    /********** HTTP 文件上传 *****************/

    /**
     * 获取上传文件及参数
     *
     * @return
     */
    protected SDO getUploadSDO() {
        HttpServletRequest request = this.getRequest();
        SDO sdo = new SDO(false);
        final String encoding = "UTF-8";
        // DiskFileItem工厂,主要用来设定上传文件的参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096); // 设置内存缓冲区的阀值
        // 使用fileItemFactory为参数实例化一个ServletFileUpload对象
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(encoding); // 设置编码格式，推荐 jsp 和 处理类 均为 UTF-8
        String result = SystemCache.getParameter("uploadFileSize", String.class);
        result = StringUtil.isNotBlank(result) ? result : "50";
        long fileSizeLimit = 50 * 1024L * 1024L;// 默认50M
        try {
            Integer size = Integer.parseInt(result);
            fileSizeLimit = size * 1024L * 1024L;
        } catch (NumberFormatException e) {
        }
        upload.setFileSizeMax(fileSizeLimit); // 设置上传数据的单个文件大小
        // 开始处理表单请求
        try {
            List<?> items = upload.parseRequest(request);
            Iterator<?> it = items.iterator();
            while (it.hasNext()) {
                FileItem item = (FileItem) it.next();
                if (item.isFormField()) { // 如果是表单字段
                    String name = item.getFieldName();
                    String value = StringUtil.decode(item.getString(encoding));
                    value = FilterUserInputContent.doFilter(value);
                    sdo.putProperty(name, value);
                } else { // 如果是文件字段
                    sdo.putProperty("uploadFileItem", item);
                    String uploadFileName = item.getName();
                    int index = uploadFileName.lastIndexOf("\\");
                    if (index != -1) {
                        uploadFileName = uploadFileName.substring(index + 1);
                    }
                    sdo.putProperty("uploadFileName", uploadFileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
        return sdo;
    }

    /********* http断点续传下载 ***********/
    /**
     * 断点续传下载
     *
     * @param request
     * @param response
     * @param downloadFile
     * @throws Exception
     */
    protected void downloadByBreakpoint(File downloadFile, String contentType, String downFileName) throws Exception {
        HttpServletRequest request = this.getRequest();
        HttpServletResponse response = this.getResponse();
        /*
         * StringBuffer headerValue = new StringBuffer();
         * headerValue.append("attachment;");
         * headerValue.append(" filename=\"").append(StringUtil.encode(downFileName)).append("\";");
         * headerValue.append(" filename*=utf-8''").append(StringUtil.encode(downFileName));
         * response.setHeader("Content-Disposition", headerValue.toString());
         */
        String userAgent = StringUtil.tryThese(request.getHeader("User-Agent"), "a");
        userAgent = userAgent.toLowerCase();
        String fileName = downFileName;
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("msie") || userAgent.contains("trident") || userAgent.contains("edge")) {
            fileName = StringUtil.encode(downFileName);
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
        response.setContentType(contentType);// set the MIME type.
        ServletOutputStream os = null;
        BufferedOutputStream out = null;
        RandomAccessFile raf = null;
        byte b[] = new byte[1024];// 暂存容器
        try {
            long pos = this.getHeaderSetting(request, response, downloadFile.length());
            os = response.getOutputStream();
            out = new BufferedOutputStream(os);
            raf = new RandomAccessFile(downloadFile, "r");
            raf.seek(pos);
            try {
                int n = 0;
                while ((n = raf.read(b, 0, 1024)) != -1) {
                    out.write(b, 0, n);
                }
                out.flush();
            } catch (IOException ie) {
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 断点续传支持
     *
     * @param file
     * @param request
     * @param response
     * @return 跳过多少字节
     * @throws Exception
     */
    private long getHeaderSetting(HttpServletRequest request, HttpServletResponse response, long fileLength) throws Exception {
        if (null == request.getHeader("Range")) {
            setResponse(new RangeSettings(fileLength), response);
            return 0;
        }
        String range = request.getHeader("Range").replaceAll("bytes=", "");
        RangeSettings settings = getSettings(fileLength, range);
        setResponse(settings, response);
        return settings.getStart();
    }

    /**
     * 设置响应头
     *
     * @param settings
     * @param fileName
     * @param response
     * @throws Exception
     */
    private void setResponse(RangeSettings settings, HttpServletResponse response) throws Exception {
        if (!settings.isRange()) {
            response.addHeader("Content-Length", String.valueOf(settings.getTotalLength()));
        } else {
            long start = settings.getStart();
            long end = settings.getEnd();
            long contentLength = settings.getContentLength();
            response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
            response.addHeader("Content-Length", String.valueOf(contentLength));
            String contentRange = new StringBuffer("bytes ").append(start).append("-").append(end).append("/").append(settings.getTotalLength()).toString();
            response.setHeader("Content-Range", contentRange);
        }
    }

    /**
     * 获取读取文件范围
     *
     * @param len
     * @param range
     * @return
     */
    private RangeSettings getSettings(long len, String range) {
        long contentLength = 0;
        long start = 0;
        long end = 0;
        if (range.startsWith("-")) {// -500，最后500个
            contentLength = Long.parseLong(range.substring(1));// 要下载的量
            end = len - 1;
            start = len - contentLength;
        } else if (range.endsWith("-")) {// 从哪个开始
            start = Long.parseLong(range.replace("-", ""));
            end = len - 1;
            contentLength = len - start;
        } else {// 从a到b
            String[] se = range.split("-");
            start = Long.parseLong(se[0]);
            end = Long.parseLong(se[1]);
            contentLength = end - start + 1;
        }
        return new RangeSettings(start, end, contentLength, len);
    }
}