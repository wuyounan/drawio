package com.huigou.data.excel.reader;

/**
 * 通过该工具类分别读取excel2003和excel2007
 */
public class ExcelReaderUtil {

    // excel2003扩展名
    public static final String EXT_EXCEL03 = ".xls";

    // excel2007扩展名
    public static final String EXT_EXCEL07 = ".xlsx";

    /**
     * 读取Excel文件，可能是03也可能是07版本
     * 
     * @param excel03
     * @param excel07
     * @param fileName
     * @throws Exception
     */
    public static void readExcel(IExcelRowReader reader, String fileName) throws Exception {
        // 处理excel2003文件
        if (fileName.endsWith(EXT_EXCEL03)) {
            HxlsReader reader03 = new HxlsReader(fileName);
            reader03.setRowReader(reader);
            reader03.process();
            // 处理excel2007文件
        } else if (fileName.endsWith(EXT_EXCEL07)) {
            XxlsReader reader07 = new XxlsReader();
            reader07.setRowReader(reader);
            reader07.process(fileName);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
    }

    /**
     * 读取Excel文件，可能是03也可能是07版本
     * 
     * @param excel03
     * @param excel07
     * @param fileName
     * @throws Exception
     */
    public static void readExcel(IExcelRowReader reader, String filePath, String fileName) throws Exception {
        // 处理excel2003文件
        if (fileName.endsWith(EXT_EXCEL03)) {
            HxlsReader reader03 = new HxlsReader(filePath);
            reader03.setRowReader(reader);
            reader03.process();
            // 处理excel2007文件
        } else if (fileName.endsWith(EXT_EXCEL07)) {
            XxlsReader reader07 = new XxlsReader();
            reader07.setRowReader(reader);
            reader07.process(filePath);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
    }
}