package com.huigou.data.excel.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.Element;

import com.huigou.cache.SystemCache;
import com.huigou.data.exception.ExportExcelException;
import com.huigou.exception.ApplicationException;
import com.huigou.util.ClassHelper;
import com.huigou.util.Constants;
import com.huigou.util.DateUtil;
import com.huigou.util.FileHelper;
import com.huigou.util.StringUtil;

/**
 * 输入数据和表头成 Excel
 * 
 * @author
 * @注意 CellRangeAddress（int， int， int， int）参数起始行号，终止行号， 起始列号，终止列号
 */
public class HSSFExport implements IExcelExport {
    private Element headRoot;

    private Map<String, Object> datas = null;// 数据

    private Map<String, Font> fontMap = new HashMap<String, Font>();// 字体

    private Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();// 样式

    private List<ExportField> fields = new ArrayList<ExportField>();// 需要显示的数据定义

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    public void setHeadRoot(Element root) {
        this.headRoot = root;
    }

    /**
     * 创建工作空间
     * 
     * @return
     */
    private Workbook getWorkbook() {
        return new HSSFWorkbook();// 2003
    }

    /**
     * 获取字体
     * 
     * @param workbook
     * @param fontHeight
     * @param boldWeight
     * @return
     */
    private Font getFont(Workbook workbook, short fontHeight) {
        Font font = fontMap.get(fontHeight + "");
        if (font == null) {
            font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints(fontHeight);
            fontMap.put(fontHeight + "", font);
        }
        return font;

    }

    /**
     * 获取样式
     * 
     * @param workbook
     * @param fontHeight
     * @param boldWeight
     * @return
     */
    private CellStyle getStyle(Workbook wb, short alignment) {
        CellStyle style = styleMap.get(alignment + "");
        if (style == null) {
            style = wb.createCellStyle();
            style.setAlignment(alignment);
            // style.setWrapText(true);//换行
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
            styleMap.put(alignment + "", style);
        }
        return style;
    }

    /**
     * 数据导出 bool true导出全部数据
     * 
     * @author 谢昕
     * @date 2008-11-24
     * @param form
     * @param bool
     * @param fieldsinfo
     * @return
     * @throws Exception
     */
    public String expExcel() throws Exception {
        Sheet sheet = null;
        FileOutputStream out = null;
        File file = null;
        String filePath = FileHelper.createTmpFilePath("xls");
        try {
            file = new File(filePath);
            out = new FileOutputStream(file);
            Object[] tables_obj = headRoot.elements().toArray();
            int iLen = tables_obj.length;
            int i = 0;
            Workbook wb = getWorkbook();
            sheet = wb.createSheet("sheet1");
            if (null == sheet) {
                return "";
            }
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
                        int rowSpan = StringUtil.toInt(StringUtil.tryThese(col.attributeValue("rowSpan"), col.attributeValue("rowspan"))); // 跨行
                        int colSpan = StringUtil.toInt(StringUtil.tryThese(col.attributeValue("colSpan"), col.attributeValue("colspan"))); // 跨列
                        int index = StringUtil.toInt(col.attributeValue("index")); // 索引
                        index = index < 0 ? fields.size() : index;
                        String titlename = col.getText(); // 名称
                        String field = col.attributeValue("field");
                        String coltype = col.attributeValue("type");
                        String dictionary = col.attributeValue("dictionary");
                        if (!StringUtil.isBlank(field)) {
                            ExportField fobj = new ExportField();
                            fobj.setTitle(titlename);
                            fobj.setField(field);
                            fobj.setType(coltype);
                            fobj.setIndex(index);
                            fobj.setDictionary(dictionary);
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
                            if (coltype != null && coltype.equals("showImage")) {
                                this.addPicture(wb, sheet, "image", titlename, e_rol, e_row);
                            }
                            Cell c = srow.getCell(e_rol);
                            if (c == null) {
                                c = srow.createCell(e_rol);
                            }
                            Font font = getFont(wb, (short) 10);
                            CellStyle style = getStyle(wb, CellStyle.ALIGN_CENTER);
                            style.setFont(font);
                            c.setCellStyle(style);
                            c.setCellType(Cell.CELL_TYPE_STRING);
                            c.setCellValue(titlename);
                        }
                        // 合并行
                        if (rowSpan > 0) {
                            if (colSpan > 0) {
                                for (int m = e_row + 1; m <= (rowSpan + e_row - 1); m++) {
                                    Row newrow = sheet.getRow(m);
                                    if (newrow == null) {
                                        newrow = sheet.createRow(m);
                                    }
                                    newrow.createCell(e_rol);
                                    for (int x = 1; x < colSpan; x++)
                                        newrow.createCell((e_rol + x));
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, e_rol, (e_rol + colSpan - 1)));
                                }
                                sheet.addMergedRegion(new CellRangeAddress(e_row, (rowSpan + e_row - 1), e_rol, (e_rol + colSpan - 1)));
                                e_rol += colSpan;
                            } else {

                                for (int m = e_row + 1; m <= (rowSpan + e_row - 1); m++) {
                                    Row newrow = sheet.getRow(m);
                                    if (newrow == null) {
                                        newrow = sheet.createRow(m);

                                    }
                                    newrow.createCell(e_rol);
                                }
                                sheet.addMergedRegion(new CellRangeAddress(e_row, (rowSpan + e_row - 1), e_rol, e_rol));
                                e_rol++;
                            }
                        } else if (colSpan > 0) { // 合并列
                            for (int x = 1; x < colSpan; x++)
                                srow.createCell((e_rol + x));
                            sheet.addMergedRegion(new CellRangeAddress(e_row, e_row, e_rol, (e_rol + colSpan - 1)));
                            e_rol += colSpan;
                        } else {
                            e_rol++;
                        }
                    }

                }
                i++;
            } while (true);// 迭代表头结束
            writeDatas(wb, sheet);// 写入数据并关闭文件
            /** 以下代码自动调整列宽度，考虑效率问题暂时屏蔽 ***/
            /***
             * for (int x = 0; x < fields.size(); x++) { sheet.autoSizeColumn(x,
             * true); }
             ***/
            wb.write(out);
        } catch (Exception e) {
            if (file != null && file.exists()) file.delete();
            e.printStackTrace();
            throw new ApplicationException(e.getMessage());
        } finally {
            if (out != null) out.close();
        }
        return filePath;
    }

    /**
     * 写入数据
     * 
     * @author kfbxx
     * @date 2008-11-25
     * @param sheet
     * @param eo
     * @param bool
     * @param l
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private void writeDatas(Workbook wb, Sheet sheet) {
        if (null == datas || datas.size() == 0) return;
        List list = (List) datas.get(Constants.ROWS);
        Map totals = (Map) datas.get(Constants.TOTAL_FIELDS);
        int e_row = sheet.getLastRowNum() + 1;
        Collections.sort(fields, new Comparator<ExportField>() {
            @Override
            public int compare(ExportField o1, ExportField o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });
        try {
            String field = "";
            Object value = "";
            ExportField fobj = null;
            if (null != list && list.size() > 0) {
                int length = list.size();
                int exportExcelCount = ClassHelper.convert(SystemCache.getParameter("exportExcelCount", String.class), Integer.class, 10000);
                if (length > exportExcelCount) {// 导出时限制数量
                    throw new ExportExcelException("导出数据量太大,请适当调整查询条件!");
                }
                for (int x = 0; x < length; x++) {
                    Map data = (Map) list.get(x);
                    for (int i = 0; i < fields.size(); i++) {
                        fobj = fields.get(i);
                        field = fobj.getField();
                        value = ExportExcel.getObjectValue(fobj, data.get(field));
                        if (fobj.getType().equals("image") || fobj.getType().equals("twoDimensionCode")) {
                            this.addPicture(wb, sheet, fobj.getType(), value.toString(), i, e_row);
                        }
                        Cell cell = null;
                        if (sheet.getRow(e_row) == null) {
                            cell = sheet.createRow(e_row).createCell(i);
                        } else {
                            cell = sheet.getRow(e_row).createCell(i);
                        }
                        Font font = getFont(wb, (short) 10);
                        CellStyle style = null;
                        if (fobj.getType().toLowerCase().equals("datetime")) { // 带时间的日期类型
                            String t = "";
                            if (value instanceof java.util.Date) {
                                t = DateUtil.getDateFormat(9, (java.util.Date) value);
                            } else if (value instanceof String) {
                                t = value.toString();
                                if (t.length() > 16) {
                                    t = t.substring(0, 16);
                                }
                            }
                            style = getStyle(wb, CellStyle.ALIGN_CENTER);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(t);
                        }
                        if (fobj.getType().toLowerCase().equals("date")) { // 带时间的日期类型
                            String t = "";
                            if (value instanceof java.util.Date) {
                                t = DateUtil.getDateFormat(1, (java.util.Date) value);
                            } else if (value instanceof String) {
                                t = value.toString();
                                if (t.length() > 10) {
                                    t = t.substring(0, 10);
                                }
                            }
                            style = getStyle(wb, CellStyle.ALIGN_CENTER);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(t);
                        } else if (fobj.getType().equals("number")) { // 数值类型
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            if (value != null && !value.equals("")) {
                                double b = Double.parseDouble(value.toString());
                                cell.setCellValue(b);
                            } else {
                                cell.setCellValue("");
                            }
                            style = getStyle(wb, CellStyle.ALIGN_RIGHT);
                        } else if (fobj.getType().equals("money")) { // 金额
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            if (value != null && !value.equals("")) {
                                String m = StringUtil.formatToCurrency(value.toString());
                                cell.setCellValue(m);
                            } else {
                                cell.setCellValue("");
                            }
                            style = getStyle(wb, CellStyle.ALIGN_RIGHT);
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(value.toString());
                            style = getStyle(wb, CellStyle.ALIGN_CENTER);
                        }
                        style.setFont(font);
                        cell.setCellStyle(style);
                    }
                    e_row++;
                }
            }
            if (totals != null) {
                for (int i = 0; i < fields.size(); i++) {
                    fobj = fields.get(i);
                    field = fobj.getField();
                    value = totals.get(field) != null ? totals.get(field) : "";
                    Cell cell = null;
                    if (sheet.getRow(e_row) == null) {
                        cell = sheet.createRow(e_row).createCell(i);
                    } else {
                        cell = sheet.getRow(e_row).createCell(i);
                    }
                    Font font = getFont(wb, (short) 10);
                    CellStyle style = getStyle(wb, CellStyle.ALIGN_RIGHT);
                    style.setFont(font);
                    cell.setCellStyle(style);
                    if (fobj.getType().equals("money")) { // 金额
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        if (value != null && !value.equals("")) {
                            String m = StringUtil.formatToCurrency(value.toString());
                            cell.setCellValue(m);
                        } else {
                            cell.setCellValue("");
                        }
                        style = getStyle(wb, CellStyle.ALIGN_RIGHT);
                    } else {
                        if (value != null && !value.equals("")) {
                            double b = Double.parseDouble(value.toString());
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(b);
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ApplicationException(ex.getMessage());
        }
    }

    /**
     * EXCEL中插入图片
     * 
     * @Title: addPicture
     * @author
     * @Description: TODO
     * @param @param wb
     * @param @param sheet
     * @param @param fieldtype
     * @param @param value
     * @param @param col
     * @param @param row
     * @return void
     * @throws
     */
    private void addPicture(Workbook wb, Sheet sheet, String fieldtype, String value, int col, int row) {

    }

}
