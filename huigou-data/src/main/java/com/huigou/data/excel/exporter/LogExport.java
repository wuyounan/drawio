package com.huigou.data.excel.exporter;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Element;

import com.huigou.cache.DictUtil;
import com.huigou.cache.SystemCache;
import com.huigou.data.exception.ExportExcelException;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.StringUtil;

/**
 * excel2007是使用xml格式来存储这里通过生成xml字符串创建xlsx
 * 
 * @author xx
 */
public class LogExport implements IExcelExport {

    private static final String XML_ENCODING = "UTF-8";

    private static final String TITLE_STYLE_NAME = "title_cell_style";

    private Element headRoot;

    private Map<String, Object> datas = null;// 数据

    private List<ExportField> fields = new ArrayList<ExportField>();// 需要显示的数据定义

    private List<CellRangeAddress> rangeAddress = new ArrayList<CellRangeAddress>();

    private Map<String, HeadCell> headCells = new HashMap<String, HeadCell>();

    private Map<String, XSSFCellStyle> stylesMap = new HashMap<String, XSSFCellStyle>();

    public static interface XSWriteDelegate {
        void onBeforeMergeCell(List<CellRangeAddress> rangeAddress);
    };

    private XSWriteDelegate delegate;

    public void setDelegate(XSWriteDelegate delegate) {
        this.delegate = delegate;
    }

    private class HeadCell {
        private int col;

        private String title;

        private HeadCell(int col, String title) {
            this.col = col;
            this.title = title;
        }

        public int getCol() {
            return col;
        }

        public String getTitle() {
            return StringUtil.isBlank(title) ? "" : title;
        }
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    public void setHeadRoot(Element root) {
        this.headRoot = root;
    }

    /**
     * 添加表头
     * 
     * @author
     * @param c
     * @throws
     */
    private void addHeadCell(Cell c) {
        String key = c.getRowIndex() + "," + c.getColumnIndex();
        headCells.put(key, new HeadCell(c.getColumnIndex(), c.getStringCellValue()));
    }

    /**
     * 根据行号获取表头
     * 
     * @author
     * @param rownum
     * @throws
     */
    private List<HeadCell> getHeadCells(int rownum) {
        List<HeadCell> heads = new ArrayList<HeadCell>(fields.size());
        for (String key : headCells.keySet()) {
            if (key.startsWith(rownum + ",")) {
                heads.add(headCells.get(key));
            }
        }
        Collections.sort(heads, new Comparator<HeadCell>() {
            public int compare(HeadCell o1, HeadCell o2) {
                return o1.getCol() > o2.getCol() ? 1 : -1;
            }
        });
        return heads;
    }

    public String expExcel() throws Exception {
        // 创建临时文件 用于生成样式及表头
        String template = FileHelper.createTmpFilePath("xlsx");
        File file = new File(template);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        // 返回数据写入索引
        int index = this.createHead(sheet);
        // 创建样式
        createStyles(wb);
        String sheetRef = sheet.getPackagePart().getPartName().getName();
        FileOutputStream os = new FileOutputStream(file);
        wb.write(os);
        os.close();
        // 临时文件用于生成数据
        File tmp = File.createTempFile("sheet", ".xml");
        Writer fw = new OutputStreamWriter(new FileOutputStream(tmp), XML_ENCODING);
        generate(fw, index);
        fw.close();
        String outFilePath = FileHelper.createTmpFilePath("xlsx");
        // 使用zip生成excel
        FileOutputStream out = new FileOutputStream(outFilePath);
        substitute(file, tmp, sheetRef.substring(1), out);
        out.close();
        if (file.exists()) {
            file.delete();
        }
        return outFilePath;
    }

    /**
     * 创建Excel样式
     * 
     * @author
     * @param wb
     * @return
     * @throws
     */
    private void createStyles(XSSFWorkbook wb) {
        Map<String, XSSFCellStyle> tmpStylesMap = new HashMap<String, XSSFCellStyle>();
        tmpStylesMap.put("string", createStyles(wb, "string", null));
        tmpStylesMap.put("date", createStyles(wb, "date", null));
        tmpStylesMap.put("datetime", createStyles(wb, "datetime", null));
        tmpStylesMap.put("number", createStyles(wb, "number", null));
        tmpStylesMap.put("money", createStyles(wb, "money", null));
        XSSFCellStyle title = wb.createCellStyle();
        // XSSFFont headerFont = wb.createFont();
        // headerFont.setBold(true);
        // title.setFont(headerFont);
        title.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        title.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        stylesMap.put(TITLE_STYLE_NAME, title);
        String code = "", fieldType = "", backgroundColor = "";
        for (ExportField field : fields) {
            fieldType = field.getType();
            code = field.getField();
            backgroundColor = field.getBackgroundColor();
            if (StringUtil.isBlank(fieldType)) {
                fieldType = "string";
            }
            if (!StringUtil.isBlank(backgroundColor)) {
                stylesMap.put(code, createStyles(wb, fieldType, backgroundColor));
            } else {
                XSSFCellStyle s = tmpStylesMap.get(fieldType.toLowerCase());
                stylesMap.put(code, s != null ? s : title);
            }
        }
    }

    /**
     * 创建样式
     * 
     * @param wb
     * @param type
     * @return
     */
    private XSSFCellStyle createStyles(XSSFWorkbook wb, String type, String backgroundColor) {
        XSSFCellStyle style = wb.createCellStyle();
        if (type.equalsIgnoreCase("string")) {
            style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        } else if (type.equalsIgnoreCase("date")) {
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        } else if (type.equalsIgnoreCase("datetime")) {
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        } else if (type.equalsIgnoreCase("number")) {
            style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        } else if (type.equalsIgnoreCase("money")) {
            style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        } else {
            style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        }
        if (!StringUtil.isBlank(backgroundColor)) {
            Color color = parseColor(backgroundColor);
            if (color != null) {
                style.setFillForegroundColor(new XSSFColor(color));
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                // style.setFillForegroundColor(color);
            }
        }
        return style;
    }

    /**
     * 解析颜色字符串
     * 
     * @param colorString
     * @return
     */
    private Color parseColor(String colorString) {
        Color color = null;
        try {
            color = new Color(Integer.parseInt(colorString, 16));
        } catch (Exception e) {
        }
        return color;
    }

    /**
     * 根据编码取样式
     * 
     * @param type
     * @return
     */
    private XSSFCellStyle getStylesByCode(String type) {
        XSSFCellStyle style = stylesMap.get(type);
        if (style == null) {
            style = stylesMap.get(TITLE_STYLE_NAME);
        }
        return style;
    }

    /**
     * 解析表头
     * 
     * @author
     * @param sheet
     * @return
     * @throws
     */
    private int createHead(Sheet sheet) {
        Object[] tables_obj = headRoot.elements().toArray();
        int rowsLength = 0;
        int iLen = tables_obj.length;
        int i = 0;
        int e_row = 0; // excel 行
        int e_rol = 0; // excel 列
        do {
            if (i >= iLen) {
                break;
            }
            Element table = (Element) tables_obj[i]; // 得到table元素子元素
            Object[] table_obj = table.elements().toArray();
            for (int j = 0; j < table_obj.length; j++) { // 循环table子元素
                // (得到row)table_obj.length
                Row srow = sheet.getRow(j);
                Element row = (Element) table_obj[j]; // 得到row元素
                Object[] row_obj = row.elements().toArray();
                e_row = j;
                e_rol = 0;
                for (int k = 0; k < row_obj.length; k++) { // 循环row元素子元素(得到rol)
                    Element col = (Element) row_obj[k]; // 得到col元素
                    int rowSpan = StringUtil.toInt(col.attributeValue("rowSpan")); // 跨行
                    int colSpan = StringUtil.toInt(col.attributeValue("colSpan")); // 跨列
                    int index = StringUtil.toInt(col.attributeValue("index")); // 索引
                    index = index < 0 ? fields.size() : index;
                    String titlename = col.getText(); // 名称
                    String field = col.attributeValue("field");
                    String coltype = col.attributeValue("type");
                    String dictionary = col.attributeValue("dictionary");
                    String backgroundColor = col.attributeValue("backgroundColor");
                    if (!StringUtil.isBlank(field)) {
                        ExportField fobj = new ExportField();
                        fobj.setTitle(titlename);
                        fobj.setField(field);
                        fobj.setType(coltype);
                        fobj.setIndex(index);
                        if (!StringUtil.isBlank(dictionary)) {
                            fobj.setDictionary(dictionary);
                        }
                        if (!StringUtil.isBlank(backgroundColor)) {
                            fobj.setBackgroundColor(backgroundColor);
                        }
                        fields.add(fobj);
                    }
                    // 首先填写值
                    if (titlename != null && !titlename.equals("")) {
                        if (srow == null) {
                            srow = sheet.createRow(e_row);
                        }
                        while (srow.getCell(e_rol) != null && (Cell.CELL_TYPE_BLANK == srow.getCell(e_rol).getCellType())) {
                            e_rol++;
                        }
                        Cell c = srow.getCell(e_rol);
                        if (c == null) {
                            c = srow.createCell(e_rol);
                        }
                        c.setCellValue(titlename);
                        addHeadCell(c);
                    }
                    // 合并行
                    if (rowSpan > 0) {
                        if (colSpan > 0) {
                            for (int m = e_row + 1; m <= (rowSpan + e_row - 1); m++) {
                                Row newrow = sheet.getRow(m);
                                if (newrow == null) {
                                    newrow = sheet.createRow(m);
                                }
                                addHeadCell(newrow.createCell(e_rol));
                                for (int x = 1; x < colSpan; x++) {
                                    addHeadCell(newrow.createCell((e_rol + x)));
                                }
                                rangeAddress.add(new CellRangeAddress(m, m, e_rol, (e_rol + colSpan - 1)));
                            }
                            rangeAddress.add(new CellRangeAddress(e_row, (rowSpan + e_row - 1), e_rol, (e_rol + colSpan - 1)));
                            e_rol += colSpan;
                        } else {
                            for (int m = e_row + 1; m <= (rowSpan + e_row - 1); m++) {
                                Row newrow = sheet.getRow(m);
                                if (newrow == null) {
                                    newrow = sheet.createRow(m);
                                }
                                addHeadCell(newrow.createCell(e_rol));
                            }
                            rangeAddress.add(new CellRangeAddress(e_row, (rowSpan + e_row - 1), e_rol, e_rol));
                            e_rol++;
                        }
                    } else if (colSpan > 0) { // 合并列
                        for (int x = 1; x < colSpan; x++) {
                            addHeadCell(srow.createCell((e_rol + x)));
                        }
                        rangeAddress.add(new CellRangeAddress(e_row, e_row, e_rol, (e_rol + colSpan - 1)));
                        e_rol += colSpan;
                    } else {
                        e_rol++;
                    }
                }
                rowsLength++;
            }
            i++;
        } while (true);// 迭代表头结束
        // 字段排序
        Collections.sort(fields, new Comparator<ExportField>() {
            public int compare(ExportField o1, ExportField o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });
        return rowsLength;
    }

    /**
     * 写入数据
     * 
     * @author
     * @param out
     * @param stylesMap
     * @param index
     * @throws Exception
     * @throws
     */
    private void generate(Writer out, int index) throws Exception {
        SpreadsheetWriter sw = new SpreadsheetWriter(out);
        sw.beginWorkSheet();
        sw.beginSheet();
        // 写入表头
        XSSFCellStyle title = this.getStylesByCode(TITLE_STYLE_NAME);
        for (int i = 0; i < index; i++) {
            sw.insertRow(i);
            for (HeadCell c : getHeadCells(i)) {
                sw.createCell(c.getCol(), c.getTitle(), title.getIndex());
            }
            sw.endRow();
        }
        out.flush();
        // 循环写入数据
        writeDatas(sw, index);
        sw.endSheet();
        if (this.delegate != null) {
            delegate.onBeforeMergeCell(rangeAddress);
        }
        // 合并单元格
        if (rangeAddress.size() > 0) {
            sw.beginMergerCell();
            for (CellRangeAddress cra : rangeAddress) {
                sw.setMergeCell(cra.getFirstRow(), cra.getFirstColumn(), cra.getLastRow(), cra.getLastColumn());
            }
            sw.endMergerCell();
        }
        sw.endWorkSheet();
    }

    /**
     * @throws SQLException
     *             循环写入数据
     * @author
     * @param sw
     * @param stylesMap
     * @param index
     * @throws
     */
    @SuppressWarnings("unchecked")
    private void writeDatas(SpreadsheetWriter sw, int index) throws IOException, SQLException {
        if (null == datas || datas.size() == 0) return;
        List<Map<String, Object>> list = (List<Map<String, Object>>) datas.get(Constants.ROWS);
        int rownum = index;
        int fieldLength = fields.size();
        String field = null;
        Object value = null;
        String fieldType = null;
        ExportField exportField = null;
        XSSFCellStyle cellStyle = null;
        if (null != list && list.size() > 0) {
            int exportExcelCount = ClassHelper.convert(SystemCache.getParameter("exportExcelCount", String.class), Integer.class, 10000);
            if (list.size() > exportExcelCount) {// 导出时限制数量
                throw new ExportExcelException("导出数据量太大,请适当调整查询条件!");
            }
            for (Map<String, Object> data : list) {
                sw.insertRow(rownum);
                for (int i = 0; i < fieldLength; i++) {
                    exportField = fields.get(i);
                    field = exportField.getField();
                    fieldType = exportField.getType();
                    value = ExportExcel.getObjectValue(exportField, data.get(field));
                    cellStyle = this.getStylesByCode(field);
                    this.writeDataCell(sw, i, fieldType, cellStyle, value);
                }
                sw.endRow();
                rownum++;
            }
        }

        this.writeTotalField(sw, rownum);
    }

    /**
     * @throws SQLException
     *             写单元格
     * @author
     * @param sw
     * @param index
     * @param exportField
     * @param value
     * @param stylesMap
     * @throws
     */
    private void writeDataCell(SpreadsheetWriter sw, int index, String fieldType, XSSFCellStyle style, Object value) throws IOException, SQLException {
        String valueStr = "";
        if (value instanceof java.util.Date) {
            if (!fieldType.equalsIgnoreCase("datetime") && !fieldType.equalsIgnoreCase("date")) {
                fieldType = "date";
            }
        }
        if (fieldType.equalsIgnoreCase("datetime")) { // 带时间的日期类型
            if (value instanceof java.util.Date) {
                valueStr = DateUtil.getDateFormat(9, (java.util.Date) value);
            } else if (value instanceof String) {
                valueStr = value.toString();
                if (valueStr.length() > 16) {
                    valueStr = valueStr.substring(0, 16);
                }
            }
            sw.createCell(index, valueStr, style.getIndex());
        } else if (fieldType.equalsIgnoreCase("date")) { // 带时间的日期类型
            if (value instanceof java.util.Date) {
                valueStr = DateUtil.getDateFormat(1, (java.util.Date) value);
            } else if (value instanceof String) {
                valueStr = value.toString();
                if (valueStr.length() > 10) {
                    valueStr = valueStr.substring(0, 10);
                }
            }
            sw.createCell(index, valueStr, style.getIndex());
        } else if (fieldType.equalsIgnoreCase("number") || fieldType.equalsIgnoreCase("money")) { // 数值类型金额
            if (value != null && !value.equals("")) {
                double b = Double.parseDouble(value.toString());
                sw.createCell(index, b, style.getIndex());
            } else {
                sw.createCell(index, "", style.getIndex());
            }
        } else if (value instanceof java.sql.Clob) {
            Clob clob = (Clob) value;
            String str = StringUtil.clobToString(clob);
            sw.createCell(index, str, style.getIndex());
        } else if (value == null) {
            value = "";
            sw.createCell(index, value.toString(), style.getIndex());
        } else {
            sw.createCell(index, value.toString(), style.getIndex());
        }
    }

    /**
     * 写入合计信息
     * 
     * @author
     * @param sw
     * @param rownum
     * @param stylesMap
     * @throws
     */
    @SuppressWarnings("unchecked")
    private void writeTotalField(SpreadsheetWriter sw, int rownum) throws IOException {
        if (null == datas || datas.size() == 0) return;
        Map<String, Object> totals = (Map<String, Object>) datas.get(Constants.TOTAL_FIELDS);
        // 存在合计数据
        if (totals != null && totals.size() > 0) {
            sw.insertRow(rownum);
            Object value = null;
            String field = "";
            XSSFCellStyle style = null;
            for (int i = 0; i < fields.size(); i++) {
                field = fields.get(i).getField();
                value = totals.get(field) != null ? totals.get(field) : "";
                style = this.getStylesByCode(field);
                if (value != null && !value.equals("")) {
                    double b = Double.parseDouble(value.toString());
                    sw.createCell(i, b, style.getIndex());
                } else {
                    sw.createCell(i, "", style.getIndex());
                }
            }
            sw.endRow();
        }
    }

    /**
     * 执行查询并导出
     * 
     * @author
     * @param queryModel
     * @return
     * @throws Exception
     * @throws
     */
    public String expExcel(List<Map<String, Object>> list) throws Exception {
        // 创建临时文件 用于生成样式及表头
        String template = FileHelper.createTmpFilePath("xlsx");
        File file = new File(template);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        // 返回数据写入索引
        int index = this.createHead(sheet);
        // 创建样式
        createStyles(wb);
        String sheetRef = sheet.getPackagePart().getPartName().getName();
        FileOutputStream os = new FileOutputStream(file);
        wb.write(os);
        os.close();
        // 临时文件用于生成数据
        File tmp = File.createTempFile("sheet", ".xml");
        Writer fw = new OutputStreamWriter(new FileOutputStream(tmp), XML_ENCODING);
        this.generate(fw, index, list);
        fw.close();
        String outFilePath = FileHelper.createTmpFilePath("xlsx");
        // 使用zip生成excel
        FileOutputStream out = new FileOutputStream(outFilePath);
        substitute(file, tmp, sheetRef.substring(1), out);
        out.close();
        if (file.exists()) {
            file.delete();
        }
        return outFilePath;
    }

    /**
     * 执行查询并写入数据
     * 
     * @author
     * @param out
     * @param stylesMap
     * @param index
     * @throws Exception
     * @throws
     */
    private void generate(Writer out, final int index, List<Map<String, Object>> list) throws Exception {
        final SpreadsheetWriter sw = new SpreadsheetWriter(out);
        sw.beginWorkSheet();
        sw.beginSheet();
        // 写入表头
        XSSFCellStyle title = this.getStylesByCode(TITLE_STYLE_NAME);
        for (int i = 0; i < index; i++) {
            sw.insertRow(i);
            for (HeadCell c : getHeadCells(i)) {
                sw.createCell(c.getCol(), c.getTitle(), title.getIndex());
            }
            sw.endRow();
        }
        out.flush();
        final int fieldLength = fields.size();
        // 执行查询的同时组合导出数据
        int count = 0;
        for (Map<String, Object> map : list) {
            sw.insertRow(count + 1);
            for (int i = 0; i < fieldLength; i++) {
                ExportField exportExcel = fields.get(i);
                writeDataCell(sw, i, exportExcel.getType(), getStylesByCode(exportExcel.getField()), map.get(exportExcel.getField()));
            }
            sw.endRow();
            count++;
        }
        this.writeTotalField(sw, index + list.size());
        list = null;
        sw.endSheet();
        // 合并单元格
        if (rangeAddress.size() > 0) {
            sw.beginMergerCell();
            for (CellRangeAddress cra : rangeAddress) {
                sw.setMergeCell(cra.getFirstRow(), cra.getFirstColumn(), cra.getLastRow(), cra.getLastColumn());
            }
            sw.endMergerCell();
        }
        sw.endWorkSheet();
    }

    /**
     * 从rs中读取需要的数据
     * 
     * @author
     * @param field
     * @param rs
     * @return
     * @throws
     */
    public Object getObjectValue(ExportField field, ResultSet rs) throws SQLException {
        String fieldName = field.getField();
        String dictionary = field.getDictionary();
        if (fieldName.endsWith("TextView")) {
            fieldName = fieldName.substring(0, fieldName.lastIndexOf("TextView"));
            dictionary = fieldName;
        }
        fieldName = StringUtil.getUnderscoreName(fieldName);
        Object value = null;
        try {
            value = rs.getObject(fieldName);
        } catch (Exception e) {
        }
        if (value == null) return "";
        if (value instanceof java.sql.Date) {
            return new java.util.Date(((java.sql.Date) value).getTime());
        } else if (value instanceof java.sql.Timestamp) {
            return new java.util.Date(((java.sql.Timestamp) value).getTime());
        }
        if (!StringUtil.isBlank(dictionary)) {// 取系统字典中的值
            String textView = DictUtil.getDictionaryDetailText(dictionary, value);
            if (StringUtil.isBlank(textView)) {
                return value;
            } else {
                return textView;
            }
        }
        return value;
    }

    /**
     * @param zipfile
     *            the template file
     * @param tmpfile
     *            the XML file with the sheet data
     * @param entry
     *            the name of the sheet entry to substitute, e.g.
     *            xl/worksheets/sheet1.xml
     * @param out
     *            the stream to write the result to
     */
    private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
        ZipFile zip = new ZipFile(zipfile);

        ZipOutputStream zos = new ZipOutputStream(out);

        @SuppressWarnings("unchecked")
        Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        while (en.hasMoreElements()) {
            ZipEntry ze = en.nextElement();
            if (!ze.getName().equals(entry)) {
                zos.putNextEntry(new ZipEntry(ze.getName()));
                InputStream is = zip.getInputStream(ze);
                copyStream(is, zos);
                is.close();
            }
        }
        zos.putNextEntry(new ZipEntry(entry));
        InputStream is = new FileInputStream(tmpfile);
        copyStream(is, zos);
        is.close();
        zos.close();
        zip.close();
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] chunk = new byte[1024];
        int count;
        while ((count = in.read(chunk)) >= 0) {
            out.write(chunk, 0, count);
        }
    }

    public static class SpreadsheetWriter {
        private final Writer _out;

        private int _rownum;

        public SpreadsheetWriter(Writer out) {
            this._out = out;
        }

        public void beginWorkSheet() throws IOException {
            this._out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        }

        public void beginSheet() throws IOException {
            this._out.write("<sheetData>\n");
        }

        public void endSheet() throws IOException {
            this._out.write("</sheetData>");
            // 合并单元格
        }

        public void endWorkSheet() throws IOException {
            this._out.write("</worksheet>");
        }

        // 插入行 不带高度
        public void insertRow(int rownum) throws IOException {
            this._out.write("<row r=\"" + (rownum + 1) + "\">\n");
            this._rownum = rownum;
        }

        public void endRow() throws IOException {
            this._out.write("</row>\n");
        }

        public void beginMergerCell() throws IOException {
            this._out.write("<mergeCells>\n");
        }

        public void endMergerCell() throws IOException {
            this._out.write("</mergeCells>\n");
        }

        // 合并单元格 下标从0开始
        public void setMergeCell(int beginColumn, int beginCell, int endColumn, int endCell) throws IOException {
            this._out.write("<mergeCell ref=\"" + getExcelName(beginCell) + (beginColumn + 1) + ":" + getExcelName(endCell) + (endColumn + 1) + "\"/>\n");// 列行:列行
        }

        public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
            String ref = new CellReference(this._rownum, columnIndex).formatAsString();
            this._out.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
            if (styleIndex != -1) this._out.write(" s=\"" + styleIndex + "\"");
            this._out.write(">");
            this._out.write("<is><t><![CDATA[" + checkXmlChar(value) + "]]></t></is>");
            this._out.write("</c>");
        }

        /**
         * 处理xml中无法识别的字符
         * 
         * @param data
         * @return
         */
        private String checkXmlChar(String data) {
            StringBuffer appender = new StringBuffer("");
            if (!StringUtil.isBlank(data)) {
                appender = new StringBuffer(data.length());
                for (int i = 0; i < data.length(); i++) {
                    char ch = data.charAt(i);
                    if ((ch == 0x9) || (ch == 0xA) || (ch == 0xD) || ((ch >= 0x20) && (ch <= 0xD7FF)) || ((ch >= 0xE000) && (ch <= 0xFFFD))
                        || ((ch >= 0x10000) && (ch <= 0x10FFFF))) {
                        appender.append(ch);
                    }
                }
            }
            String result = appender.toString();
            return result.replaceAll("]]>", "");
        }

        public void createCell(int columnIndex, String value) throws IOException {
            createCell(columnIndex, value, -1);
        }

        public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
            String ref = new CellReference(this._rownum, columnIndex).formatAsString();
            this._out.write("<c r=\"" + ref + "\" t=\"n\"");
            if (styleIndex != -1) this._out.write(" s=\"" + styleIndex + "\"");
            this._out.write(">");
            this._out.write("<v>" + value + "</v>");
            this._out.write("</c>");
        }

        public void createCell(int columnIndex, double value) throws IOException {
            createCell(columnIndex, value, -1);
        }

        /**
         * 通过列索引获取Excel其对应列的字母
         * 
         * @author
         * @param num
         * @return
         * @throws
         */
        public static String getExcelName(int num) {
            StringBuffer temp = new StringBuffer();
            double i = Math.floor(Math.log(25.0 * (num) / 26.0 + 1) / Math.log(26)) + 1;
            if (i > 1) {
                double sub = num - 26 * (Math.pow(26, i - 1) - 1) / 25;
                for (double j = i; j > 0; j--) {
                    temp.append((char) (sub / Math.pow(26, j - 1) + 65));
                    sub = sub % Math.pow(26, j - 1);
                }
            } else {
                temp.append((char) (num + 65));
            }
            return temp.toString();
        }
    }
}