package com.huigou.data.excel.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 读取 excel2003 数据
 * 
 * @Title: HxlsReader.java
 * @Description: TODO 通过实现HSSFListener监听器，采用事件驱动模式解析excel2003
 *               中的内容，遇到特定事件才会触发，大大减少了内存的使用。
 * @author 
 * @date 2013-9-8 上午12:20:24
 * @version V1.0
 */
public class HxlsReader implements HSSFListener {
    private IExcelRowReader rowReader;

    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    private int titleRow = 0; // 标题行，一般情况下为0

    private int rowsize = 0; // 列数

    private POIFSFileSystem fs;

    /** Should we output the formula, or the value it has? */
    private boolean outputFormulaValues = true;

    /** For parsing Formulas */
    private SheetRecordCollectingListener workbookBuildingListener;

    private HSSFWorkbook stubWorkbook;

    // Records we pick up as we process
    private SSTRecord sstRecord;

    private FormatTrackingHSSFListener formatListener;

    /** So we known which sheet we're on */
    private int sheetIndex = -1;

    private BoundSheetRecord[] orderedBSRs;

    @SuppressWarnings("rawtypes")
	private ArrayList boundSheetRecords = new ArrayList();

    // For handling formulas with string results
    private int nextRow;

    private int nextColumn;

    private boolean outputNextStringRecord;

    private int curRow;

    private List<String> rowlist;

    @SuppressWarnings("unused")
    private String sheetName;

    public HxlsReader(POIFSFileSystem fs) throws Exception {
        this.fs = fs;
        this.curRow = 0;
        this.rowlist = new ArrayList<String>();
    }

    public HxlsReader(String file) throws Exception {
        this(new POIFSFileSystem(new FileInputStream(file)));
    }

    /**
     * 遍历 excel 文件
     */
    public void process() throws IOException {
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);

        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();

        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }

        factory.processWorkbookEvents(request, fs);
    }

    /**
     * HSSFListener 监听方法
     */
    @SuppressWarnings({ "unchecked", "unused" })
    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String value = null;
        switch (record.getSid()) {
        case BoundSheetRecord.sid:
            boundSheetRecords.add(record);
            break;
        case BOFRecord.sid:
            BOFRecord br = (BOFRecord) record;
            if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                // Create sub workbook if required
                if (workbookBuildingListener != null && stubWorkbook == null) {
                    stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                }

                // Works by ordering the BSRs by the location of
                // their BOFRecords, and then knowing that we
                // process BOFRecords in byte offset order
                sheetIndex++;
                if (orderedBSRs == null) {
                    orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                }
                sheetName = orderedBSRs[sheetIndex].getSheetname();
            }
            break;

        case SSTRecord.sid:
            sstRecord = (SSTRecord) record;
            break;

        case BlankRecord.sid:
            BlankRecord brec = (BlankRecord) record;

            thisRow = brec.getRow();
            thisColumn = brec.getColumn();

            rowlist.add(thisColumn, "");

            break;
        case BoolErrRecord.sid:
            BoolErrRecord berec = (BoolErrRecord) record;
            thisRow = berec.getRow();
            thisColumn = berec.getColumn();
            rowlist.add(thisColumn, berec.getBooleanValue() + "");
            break;

        case FormulaRecord.sid:
            FormulaRecord frec = (FormulaRecord) record;
            thisRow = frec.getRow();
            thisColumn = frec.getColumn();
            // if (outputFormulaValues) {
            // outputNextStringRecord = true;
            // nextRow = frec.getRow();
            // nextColumn = frec.getColumn();
            // } else {
            // rowlist.add(thisColumn, "");
            // }
            try {
                value = formatListener.formatNumberDateCell(frec).trim();
                rowlist.add(thisColumn, value);
            } catch (Exception e) {
                rowlist.add(thisColumn, "");
            }
            break;
        case StringRecord.sid:
            if (outputNextStringRecord) {
                // String for formula
                StringRecord srec = (StringRecord) record;
                value = srec.getString().trim();
                thisRow = nextRow;
                thisColumn = nextColumn;
                outputNextStringRecord = false;
                rowlist.add(thisColumn, value);
            }
            break;

        case LabelRecord.sid:
            LabelRecord lrec = (LabelRecord) record;
            curRow = thisRow = lrec.getRow();
            thisColumn = lrec.getColumn();
            value = lrec.getValue().trim();
            rowlist.add(thisColumn, value);
            break;
        case LabelSSTRecord.sid:
            LabelSSTRecord lsrec = (LabelSSTRecord) record;
            curRow = thisRow = lsrec.getRow();
            thisColumn = lsrec.getColumn();
            if (sstRecord == null) {
                rowlist.add(thisColumn, "");
            } else {
                value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                rowlist.add(thisColumn, value);
            }
            break;
        case NumberRecord.sid:
            NumberRecord numrec = (NumberRecord) record;
            curRow = thisRow = numrec.getRow();
            thisColumn = numrec.getColumn();
            value = formatListener.formatNumberDateCell(numrec).trim();
            rowlist.add(thisColumn, value);
            break;
        default:
            break;
        }

        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            curRow = thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            rowlist.add(thisColumn, "");
        }

        // 行结束时
        if (record instanceof LastCellOfRowDummyRecord) {
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
        }
    }
}
