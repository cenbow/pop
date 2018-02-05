package com.ai.bdx.pop.util.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil2007 {
	private static final Logger log = LogManager.getLogger(ExcelUtil2007.class);

	public static void main(String args[]) throws FileNotFoundException, IOException {
		String file = "D:/work/w1/pop/src/main/webapp/upload/templete/pccTemplete{X}.xlsx";
		XSSFWorkbook book = getWorkBook(file);
		book.write(new FileOutputStream(file.replace("X", "" + System.currentTimeMillis())));
		book.close();
	}

	/**
	 * 
	 * getWorkBook:根据路径读取Excel2007模板
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException 
	 * @return XSSFWorkbook
	 */
	public static XSSFWorkbook getWorkBook(String filePath) throws FileNotFoundException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
		return workbook;
	}

	/**
	 * 设置单元格的样式
	 * @param cell
	 * @param value
	 * @param cellStyle
	 */
	public static void setCellStyle(XSSFCell cell, String value, XSSFCellStyle cellStyle) {
		cell.setCellStyle(cellStyle);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}
}
