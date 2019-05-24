package com.huigou.data.excel.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.huigou.util.StringUtil;

/**
 * 读取excel2007数据
 * 
 * @Description: 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 *               xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低
 *               内存的耗费，特别使用于大数据量的文件。
 */
public class XxlsReader extends DefaultHandler {
    private IExcelRowReader rowReader;

    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    private StylesTable stylesTable;

    private SharedStringsTable sst;

    private String lastContents;

    private boolean nextIsString;

    private int sheetIndex = -1;

    private List<String> rowlist = new ArrayList<String>();

    private int curRow = 0; // 当前行

    private int curCol = 0; // 当前列索引

    private int preCol = 0; // 上一列列索引

    private int titleRow = 0; // 标题行，一般情况下为0

    private int rowsize = 0; // 列数

    private short formatIndex;

    private String formatString;

    private DataFormatter formatter;

    private boolean isTElement;

    // 只遍历一个sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3
    public void processOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        this.stylesTable = r.getStylesTable();
        this.formatter = new DataFormatter();
        XMLReader parser = fetchSheetParser(sst);
        // rId2 found by processing the Workbook
        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId" + sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历 excel 文件
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        this.stylesTable = r.getStylesTable();
        this.formatter = new DataFormatter();
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => 单元格
        if (name.equals("c")) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            String rowStr = attributes.getValue("r");// 空单元格处理
            curCol = this.getRowIndex(rowStr);
            if ("s".equals(cellType)) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
            this.formatIndex = -1;
            this.formatString = "";
            String cellStyleStr = attributes.getValue("s");
            // b ==>bool e==>error inlineStr==>INLINESTR str==>FORMULA s==>nextIsString
            if (!"b".equals(cellType) && !"e".equals(cellType) && !"inlineStr".equals(cellType) && !"s".equals(cellType) && !"str".equals(cellType)) {
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (StringUtil.isBlank(this.formatString)) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                    if (!StringUtil.isBlank(this.formatString)) {
                        if (this.formatString.contains("m/d/yy") || this.formatString.contains("mm/dd/yy") || this.formatString.contains("yy/m/d")) {
                            this.formatString = "yyyy-MM-dd HH:mm:ss";
                        }
                    }
                }
            }
        }
        // 当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }
        // 置空
        lastContents = "";
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            } catch (Exception e) {
            }
        }
        if (isTElement) {
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTElement = false;
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        } else if (name.equals("v")) {
            String value = lastContents.trim();
            value = value.equals("") ? " " : value;
            if (!StringUtil.isBlank(value)) {
                if (!StringUtil.isBlank(this.formatString)) {
                    try {
                        value = formatter.formatRawCellContents(Double.parseDouble(value), this.formatIndex, this.formatString);
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }
            }
            int cols = curCol - preCol;
            if (cols > 1) {
                for (int i = 0; i < cols - 1; i++) {
                    rowlist.add(preCol, "");
                }
            }

            preCol = curCol;
            rowlist.add(curCol - 1, value.trim());
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                int tmpCols = rowlist.size();
                if (curRow > this.titleRow && tmpCols < this.rowsize) {
                    for (int i = 0; i < this.rowsize - tmpCols; i++) {
                        rowlist.add(rowlist.size(), "");
                    }
                }
                rowReader.getRows(sheetIndex, curRow, rowlist);
                if (curRow == this.titleRow) {
                    this.rowsize = rowlist.size();
                }
                rowlist.clear();
                curRow++;
                curCol = 0;
                preCol = 0;
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    // 得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
    // 如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
    public int getRowIndex(String rowStr) {
        rowStr = rowStr.replaceAll("[^A-Z]", "");
        byte[] rowAbc = rowStr.getBytes();
        int len = rowAbc.length;
        float num = 0;
        for (int i = 0; i < len; i++) {
            num += (rowAbc[i] - 'A' + 1) * Math.pow(26, len - i - 1);
        }
        return (int) num;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }
}
