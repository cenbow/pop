package com.ai.bdx.pop.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.ai.bdx.pop.wsclient.util.PoiExcelUtil;

/**
 * 基站割接时的工具类
 * @author 林
 *
 */
public class BatchStationUtil {
	private static final Logger log = LogManager.getLogger(BatchStationUtil.class);
	/**
	 * 读取割接上传文件，转换成bean对象集合
	 * @param filepath
	 * @return
	 */
	public static List<ImportBatchCutoverBean> readCpetStationExcel(String filepath){
		List<ImportBatchCutoverBean> batchList = new ArrayList<ImportBatchCutoverBean>();
		File file = new File(filepath);
		Workbook wb = getWorkbook(file);
		int sheets = wb.getNumberOfSheets();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ImportBatchCutoverBean c;
//		for (int i = 0; i < sheets; i++)
//		{
			HashMap<String, String> map = getExcelStringData(file,0);
			Integer num = Integer.parseInt(map.get("rowSize").toString());
			for (int j = 2; j < num; j++) {
				Date date = new Date();
				int k = 1;
				try{
					if(map.get(j+"/8")==null||map.get(j+"/10")==null){
						continue;
					}
					 c = new ImportBatchCutoverBean();
					String importTime = sdf.format(date);
					c.setCity_name(String.valueOf(map.get(j+"/0")));k++;
					c.setCounty_name(String.valueOf(map.get(j+"/1")));k++;
					c.setEnodeid_old(String.valueOf(map.get(j+"/2")));k++;
					c.setEnodeid_new(String.valueOf(map.get(j+"/3")));k++;
					c.setEnodeName_old(String.valueOf(map.get(j+"/4")));k++;
					c.setEnodeName_new(String.valueOf(map.get(j+"/5")));k++;
					c.setCell_name_old(String.valueOf(map.get(j+"/6")));k++;
					c.setCell_name_new(String.valueOf(map.get(j+"/7")));k++;
					c.setLac_dec_id_old(String.valueOf(map.get(j+"/8")));k++;
					c.setLac_hex_code_old(String.valueOf(map.get(j+"/9")));
					c.setCi_dec_id_old(String.valueOf(map.get(j+"/10")));k++;
					c.setCi_hex_code_old(String.valueOf(map.get(j+"/11")));
					c.setLac_dec_id_new(String.valueOf(map.get(j+"/12")));k++;
					c.setLac_hex_code_new(String.valueOf(map.get(j+"/13")));
					c.setCi_dec_id_new(String.valueOf(map.get(j+"/14")));k++;
					c.setCi_hex_code_new(String.valueOf(map.get(j+"/15")));
					c.setChg_date(String.valueOf(map.get(j+"/16")));k++;
					c.setImport_time(importTime);
					batchList.add(c);
				}catch(Exception e){
					log.info("第"+(j+1)+"行,第"+k+"列数据异常");
					e.printStackTrace();
					return null;
				}
			}
//		}
		return batchList;
	}
	/**
	 * 功能：获取EXCEL数据 Object 类型
	 * @param file
	 * @param index
	 * @return
	 */
	public static HashMap<String, Object> getExcelObjectData(File file, int index) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map = getSheetObjectData(getWorkbook(file), index);
		return map;
	}
	/**
	 * 功能：获取EXCEL数据String类型
	 * @param file
	 * @param index
	 * @return
	 */
	public static HashMap<String, String> getExcelStringData(File file, int index) {
		HashMap<String, String> map = new HashMap<String, String>();
		map = getSheetStringData(getWorkbook(file), index);
		return map;
	}
	/**
	 * 
	 * @param wb
	 * @param index
	 * @return
	 */
	private static HashMap<String, Object> getSheetObjectData(Workbook wb, int index) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Sheet sheet = wb.getSheetAt(index);
		map.put("rowSize", String.valueOf(sheet.getLastRowNum() + 1));
		for (int r = 0; r < sheet.getLastRowNum() + 1; r++) {
			Row row = sheet.getRow(r);
			// 行为空
			if (row != null)
				for (int c = 0; c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell != null)
						map.put(r + "/" + c, getCellObjectValue(cell));
				}
		}
		return map;
	}
	/**
	 * 
	 * @param wb
	 * @param index
	 * @return
	 */
	private static HashMap<String, String> getSheetStringData(Workbook wb, int index) {
		HashMap<String, String> map = new HashMap<String, String>();
		Sheet sheet = wb.getSheetAt(index);
		map.put("rowSize", String.valueOf(sheet.getLastRowNum() + 1));
		for (int r = 0; r < sheet.getLastRowNum() + 1; r++) {
			Row row = sheet.getRow(r);
			// 行为空
			if (row != null)
				for (int c = 0; c < row.getLastCellNum(); c++) {
					Cell cell = row.getCell(c);
					if (cell != null)
						map.put(r + "/" + c, PoiExcelUtil.getCellStringValue(cell));
				}
		}
		return map;
	}
	public static Workbook getWorkbook(File file) {
		if (file.getName().contains(".xlsx")) {
			try {
				XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
				return xwb;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (file.getName().contains(".xls")) {
			try {
				HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
				return hwb;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取sheet内的值 getStringCellValue 取CELL值
	 * 
	 * @Title: getStringCellValue
	 * @Description: TODO
	 * @param @param cell
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getCellStringValue(Cell cell) {
		String v_excelData = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字日期类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 日期格式
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				v_excelData = dateFormat.format(cell.getDateCellValue());
			} else {
				v_excelData = cell.getNumericCellValue() + "";
				BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
				v_excelData = bd.toPlainString();
			}
			break;
		case Cell.CELL_TYPE_STRING:
			// 字符串
			v_excelData = cell.getStringCellValue();
			v_excelData = v_excelData.trim();
			if (v_excelData.equals("") || v_excelData.length() <= 0)
				v_excelData = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 公式
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			v_excelData = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			// 空白
			v_excelData = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 布尔型
			v_excelData = cell.getBooleanCellValue() + "";
			break;
		case Cell.CELL_TYPE_ERROR:
			// Error
			v_excelData = "";
			break;
		}
		return v_excelData;
	}

	/**
	 * 
	 * getCellObjectValue 取CELL值
	 * 
	 * @Title: getCellObjectValue
	 * @Description: TODO
	 * @param @param cell
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	public static Object getCellObjectValue(Cell cell) {
		Object v_excelData = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字日期型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				v_excelData = cell.getDateCellValue();
			} else {
				v_excelData = cell.getNumericCellValue();
			}
			break;
		case Cell.CELL_TYPE_STRING:
			// 字串型
			v_excelData = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			// 公式型
			v_excelData = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BLANK:
			// 空白型
			v_excelData = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			// 布朗型
			v_excelData = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			// Error
			v_excelData = cell.getErrorCellValue();
			break;
		}
		return v_excelData;
	}

}
