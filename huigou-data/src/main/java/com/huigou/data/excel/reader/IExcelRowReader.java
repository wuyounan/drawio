package com.huigou.data.excel.reader;

import java.util.List;

public interface IExcelRowReader {

	/**
	 * 业务逻辑实现方法
	 * 
	 * @param sheetIndex
	 * @param curRow
	 * @param rowlist
	 */
	public void getRows(int sheetIndex, int curRow, List<String> rowlist);
}
