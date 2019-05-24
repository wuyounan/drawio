package com.huigou.data.excel.exporter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.aop.AopInvocationException;

import com.huigou.cache.SystemCache;
import com.huigou.data.excel.exporter.XSSFExport.XSWriteDelegate;
import com.huigou.data.exception.ExportExcelException;
import com.huigou.util.FileHelper;
import com.huigou.util.StringUtil;

/**
 * 输入数据和表头成 Excel
 * 
 * @author
 */
public class ExportExcel {

    /**
     * 解析xml
     * 
     * @return
     */
    public static Element readXml(String xmlHeads, String xmlFilePath) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = null;
        if (xmlHeads != null && !xmlHeads.equals("")) {
            // 将字符串转换为
            document = reader.read(new ByteArrayInputStream(xmlHeads.getBytes("UTF-8")));
            // 得到xml的根节点(message)
            return document.getRootElement();
        } else if (xmlFilePath != null && !xmlFilePath.equals("")) {
            File file = new File("" + FileHelper.FILE_SEPARATOR + xmlFilePath);
            if (file != null && file.exists()) {
                document = reader.read(file);
            } else {
                document = reader.read(new File(xmlFilePath));
            }
            return document.getRootElement();
        }
        throw new AopInvocationException("表头不存在，不能导出Excel!");
    }

    /**
     * 值改变类型
     * 
     * @Title: changeValue
     * @author
     * @Description: TODO
     * @param @param value
     * @param @return
     * @return Object
     * @throws
     */
    public static Object getObjectValue(ExportField field, Object value) throws SQLException {
        if (value == null) return "";
        if (value instanceof java.util.Date) {
            return new java.util.Date(((java.util.Date) value).getTime());
        }
        if (value instanceof oracle.sql.TIMESTAMP) {
            value = new java.util.Date(((oracle.sql.TIMESTAMP) value).dateValue().getTime());
        }
        if (!StringUtil.isBlank(field.getDictionary())) {// 取系统字典中的值
            String textView = SystemCache.getDictionaryDetailText(field.getDictionary(), value);
            if (textView != null) {
                return textView;
            }
        }
        return value;
    }

    /**
     * 根据xml创建excel
     * 
     * @author
     * @param xml
     * @return
     * @throws
     */
    public static String createExcel(String xml) {
        IExcelExport exporter = new XSSFExport();
        try {
            Element headRoot = readXml(xml, null);
            exporter.setHeadRoot(headRoot);
            String s = exporter.expExcel();
            File file = new File(s);
            if (file.exists()) {
                return s;
            } else {
                throw new ExportExcelException("文件生成失败!");
            }
        } catch (Exception e) {
            throw new ExportExcelException(e);
        }
    }

    public static Map<String, Object> doExport(String head) {
        return doExport(null, head, null, 1);
    }

    public static Map<String, Object> doExport(Map<String, Object> datas, String head) {
        return doExport(datas, head, null, 1);
    }

    public static Map<String, Object> doExport(Map<String, Object> datas, String head, String xmlPath) {
        return doExport(datas, head, xmlPath, 1);
    }

    public static Map<String, Object> doExport(XSWriteDelegate delegate, Map<String, Object> datas, String head) {
        Map<String, Object> m = new HashMap<String, Object>(1);
        XSSFExport exporter = new XSSFExport();

        try {
            Element headRoot = readXml(head, null);
            exporter.setDatas(datas);
            exporter.setHeadRoot(headRoot);
            exporter.setDelegate(delegate);
            String s = exporter.expExcel();
            File file = new File(s);
            if (file.exists()) {
                m.put("file", file.getName());
            } else {
                throw new ExportExcelException("文件生成失败!");
            }
        } catch (Exception e) {
            throw new ExportExcelException(e);
        }
        return m;
    }

    /**
     * 静态导出方法
     * 
     * @author
     * @param datas
     * @param head
     * @param xmlPath
     * @return Map<String,Object>
     */
    public static Map<String, Object> doExport(Map<String, Object> datas, String head, String xmlPath, int type) {
        Map<String, Object> m = new HashMap<String, Object>(1);
        IExcelExport exporter = null;
        if (type == 0) {
            exporter = new HSSFExport();
        } else {
            exporter = new XSSFExport();
        }
        try {
            Element headRoot = readXml(head, xmlPath);
            exporter.setDatas(datas);
            exporter.setHeadRoot(headRoot);
            String s = exporter.expExcel();
            File file = new File(s);
            if (file.exists()) {
                m.put("file", file.getName());
            } else {
                throw new ExportExcelException("文件生成失败!");
            }
        } catch (Exception e) {
            throw new ExportExcelException(e);
        }
        return m;
    }
}
