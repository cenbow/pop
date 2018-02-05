package com.ai.bdx.pop.util.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ai.bdx.pop.util.PathUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;

public class MailExcel {
	private static final Logger log = LogManager.getLogger(MailExcel.class);
	public static final int CUST_SHEET_START_ROW = 12;//目标对象定义页起始行
	public static final int RULE_SHEET_START_ROW = 24;//策略条件和动作页起始行

	/**
	 * 
	 * createExcel:根据模板生成Excel
	 * @param poliyBean 
	 * @param outFilePath
	 * @return void
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static synchronized void createExcel(PccPolicyBean poliyBean, String outFilePath) throws FileNotFoundException,
			IOException {
		String filePath = getFileTempletePath();
		XSSFWorkbook book = ExcelUtil2007.getWorkBook(filePath);//取得模板
		log.debug("Excel模版路径：{}", filePath);
		//获取单元格样式
		XSSFCellStyle cellStyle1 = book.getSheetAt(0).getRow(2).getCell(2).getCellStyle();
		cellStyle1.setWrapText(true);
		XSSFCellStyle cellStyle2 = book.getSheetAt(0).getRow(3).getCell(2).getCellStyle();
		cellStyle2.setWrapText(true);
		//构建目标对象定义的sheet页
		buildCustSheet(poliyBean.getCustomers(), book.getSheetAt(1), cellStyle1, cellStyle2);
		//构建策略条件和动作选择页
		buildConActSheet(poliyBean.getPolicy_name(), poliyBean.getRule(), book.getSheetAt(2), cellStyle1, cellStyle2);
		//写文件
		book.write(new FileOutputStream(outFilePath));
		book.close();
		log.debug("生成的Excel文件路径：{}", outFilePath);
	}

	/**
	 * 构造策略条件和动作页
	 * @param policyName
	 * @param pccRules
	 * @param conActSheet
	 * @param cellStyle1
	 * @param cellStyle2
	 */
	public static void buildConActSheet(String policyName, List<PccRuleBean> pccRules, XSSFSheet conActSheet,
			XSSFCellStyle cellStyle1, XSSFCellStyle cellStyle2) {
		//标题
		XSSFRow row = conActSheet.createRow(RULE_SHEET_START_ROW);
		ExcelUtil2007.setCellStyle(row.createCell(0), "PCC策略", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(1), "规则编号", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(2), "规则定义", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(5), "", cellStyle1);
		ExcelUtil2007.setCellStyle(row.createCell(6), "", cellStyle1);
		conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW, RULE_SHEET_START_ROW, 2, 6));
		//遍历规则
		int ruleRow = 1;
		XSSFCellStyle cellStyle3 = (XSSFCellStyle) cellStyle2.clone();
		cellStyle3.setFillForegroundColor(new XSSFColor(java.awt.Color.ORANGE));
		cellStyle3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		for (PccRuleBean rule : pccRules) {
			int ruleRowCount = 0;
			row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
			ruleRowCount++;
			ExcelUtil2007.setCellStyle(row.createCell(0), policyName, cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), rule.getRule_id(), cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "条件", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(5), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(6), "", cellStyle1);
			conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + ruleRow - 1, RULE_SHEET_START_ROW
					+ ruleRow - 1, 2, 6));
			row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
			ruleRowCount++;
			ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "编号", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "条件名称", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "操作符", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(5), "条件取值", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(6), "辅助信息", cellStyle1);
			//遍历条件
			for (PccCondition con : rule.getCondition()) {
				ruleRowCount++;
				row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
				ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(2), con.getNumber(), cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(3), con.getConditionName(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(4), con.getOper(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(5), con.getVal(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(6), con.getRemark(), cellStyle2);
			}
			row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
			ruleRowCount++;
			ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), rule.getRule_id(), cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "动作", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(5), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(6), "", cellStyle1);
			conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + ruleRow - 1, RULE_SHEET_START_ROW
					+ ruleRow - 1, 2, 6));
			row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
			ruleRowCount++;
			ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "编号", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "动作名称", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "动作取值", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(5), "辅助信息", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(6), "说明", cellStyle1);
			//遍历动作
			for (PccAction act : rule.getAction()) {
				ruleRowCount++;
				row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
				ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(2), act.getNumber(), cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(3), act.getActName(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(4), act.getVal(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(5), act.getInformation(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(6), act.getInstruction(), cellStyle2);
			}
			row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
			ruleRowCount++;
			ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "类C语言描述", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(5), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(6), "", cellStyle1);
			conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + ruleRow - 1, RULE_SHEET_START_ROW
					+ ruleRow - 1, 2, 6));
			for (int i = 0; i < 2; i++) {
				ruleRowCount++;
				row = conActSheet.createRow(RULE_SHEET_START_ROW + (ruleRow++));
				ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(1), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(2), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(5), "", cellStyle3);
				ExcelUtil2007.setCellStyle(row.createCell(6), "", cellStyle3);
			}
			conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + ruleRow - 2, RULE_SHEET_START_ROW
					+ ruleRow - 1, 2, 6));
			//合并规则编号列
			conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + ruleRow - ruleRowCount,
					RULE_SHEET_START_ROW + ruleRow - 1, 1, 1));
		}
		conActSheet.addMergedRegion(new CellRangeAddress(RULE_SHEET_START_ROW + 1, RULE_SHEET_START_ROW + ruleRow - 1,
				0, 0));
	}

	/**
	 * 构造目标对象页
	 * @param custSheet
	 * @param pccCusts
	 * @param cellStyle1
	 * @param cellStyle2
	 */
	private static void buildCustSheet(List<PccCustomer> pccCusts, XSSFSheet custSheet, XSSFCellStyle cellStyle1,
			XSSFCellStyle cellStyle2) {
		int custRow = 0;
		for (PccCustomer customer : pccCusts) {
			XSSFRow row = custSheet.createRow(CUST_SHEET_START_ROW + (custRow++));
			ExcelUtil2007.setCellStyle(row.createCell(0), "用户群定义", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), customer.getCustomerGroupName(), cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle1);
			custSheet.addMergedRegion(new CellRangeAddress(CUST_SHEET_START_ROW + custRow - 1, CUST_SHEET_START_ROW
					+ custRow - 1, 1, 4));
			row = custSheet.createRow(CUST_SHEET_START_ROW + (custRow++));
			ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(1), "序号", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(2), "筛选标准", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(3), "取值", cellStyle1);
			ExcelUtil2007.setCellStyle(row.createCell(4), "说明", cellStyle1);
			int conRow = 1;
			for (Map.Entry<String, String> con : customer.getParam().entrySet()) {
				row = custSheet.createRow(CUST_SHEET_START_ROW + (custRow++));
				ExcelUtil2007.setCellStyle(row.createCell(0), "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(1), (conRow++) + "", cellStyle1);
				ExcelUtil2007.setCellStyle(row.createCell(2), con.getKey(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(3), con.getValue(), cellStyle2);
				ExcelUtil2007.setCellStyle(row.createCell(4), "", cellStyle2);
			}
		}
		custSheet.addMergedRegion(new CellRangeAddress(CUST_SHEET_START_ROW, CUST_SHEET_START_ROW + custRow - 1, 0, 0));
	}

	/**
	 * 
	 * getFileTempletePath:获取模板文件路径
	 * @return 
	 * @return String
	 */
	private static String getFileTempletePath() {
		 String path = PathUtil.getWebRootPath() + File.separator + "upload"+File.separator+"templete"+File.separator+"pccTemplete.xlsx";
		return path;
	}

	public static String getFilePath() {
		 String path = PathUtil.getWebRootPath() + File.separator + "upload"+File.separator+"jituan";
		 String SYS_COMMON_UPLOAD_PATH=Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
		 if(StringUtil.isNotEmpty(SYS_COMMON_UPLOAD_PATH)){
			 if(SYS_COMMON_UPLOAD_PATH.endsWith("\\")||SYS_COMMON_UPLOAD_PATH.endsWith(File.separator)){
				 path= SYS_COMMON_UPLOAD_PATH + "upload"+File.separator+"jituan";
			 }else{
				 path= SYS_COMMON_UPLOAD_PATH+ File.separator + "upload"+File.separator+"jituan";
			 }
		 }
		return path;
	}
	public static String getNow(){
		SimpleDateFormat  fmt=new SimpleDateFormat("yyyyMMddHHmmss");
		return fmt.format(new java.util.Date());
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException {
		log.debug("test start............");
		PccPolicyBean p = new PccPolicyBean();
		List<PccCustomer> custs = new ArrayList<PccCustomer>();
		List<PccRuleBean> rules = new ArrayList<PccRuleBean>();
		p.setPolicy_id("test201501121");
		p.setPolicy_name("测试策略");
		for (int i = 0; i < 2; i++) {
			PccCustomer cust = new PccCustomer();
			cust.setCustomerGroupName("4G超套餐策略用户群" + i);
			TreeMap<String, String> custCon = new TreeMap<String, String>();
			custCon.put("已更换为4G USIM卡" + i, "");
			custCon.put("已更换4G终端" + i, "");
			custCon.put("剔除用户列表" + i, "自动加油包及安心包用户、主动回复“取消限速”的用户");
			cust.setParam(custCon);
			custs.add(cust);
		}
		for (int j = 0; j < 2; j++) {
			PccRuleBean rule = new PccRuleBean();
			rule.setRule_id("201506072019" + j);
			rule.setRule_name("测试规则" + j);
			rule.setC_desc("if con1 & con2 then 1");
			List<PccCondition> cons = new ArrayList<PccCondition>();
			for (int k = 0; k < 3; k++) {
				PccCondition con = new PccCondition();
				con.setConditionName("接入类型" + k);
				con.setNumber(k + 1 + "");
				con.setOper("等于");
				con.setRemark("...");
				con.setVal("2G,3G");
				cons.add(con);
			}
			List<PccAction> acts = new ArrayList<PccAction>();
			for (int k = 0; k < 2; k++) {
				PccAction act = new PccAction();
				act.setActName("QCI" + k);
				act.setNumber(k + 1 + "");
				act.setVal(k + "");
				act.setInformation("辅助信息");
				act.setInstruction("说明");
				act.setOper("等于");
				acts.add(act);
			}

			rule.setCondition(cons);
			rule.setAction(acts);
			rules.add(rule);
		}
		p.setCustomers(custs);
		p.setRule(rules);
		createExcel(p, getFileTempletePath().replace(".xlsx", System.currentTimeMillis() + ".xlsx"));

		log.debug("test end............");
	}
}
