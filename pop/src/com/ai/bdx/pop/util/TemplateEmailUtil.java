package com.ai.bdx.pop.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import com.ai.bdx.pop.bean.PolicyBussTemplateBean;
import com.ai.bdx.pop.model.PopDimCampChannel;
import com.ai.bdx.pop.model.PopDimContactFreq;
import com.ai.bdx.pop.model.PopDimControlType;
import com.ai.bdx.pop.model.PopPolicyRuleAct;
import com.ai.bdx.pop.model.PopPolicyRuleEventCon;
import com.asiainfo.biframe.utils.string.StringUtil;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 生成业务模板工具
 * @author luozn
 *
 */

public class TemplateEmailUtil {
	 public static void createExcel(OutputStream os,PolicyBussTemplateBean bean) throws WriteException,IOException {
	        //创建工作薄
	        WritableWorkbook workbook = Workbook.createWorkbook(os);
	        //创建新的一页
	        WritableSheet sheet = workbook.createSheet("First Sheet", 0);
	        //构造表头
	        sheet.mergeCells(0, 0, 4, 0);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
	        WritableFont bold = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);//设置字体种类和黑体显示,字体为Arial,字号大小为10,采用黑体显示
	        WritableCellFormat titleFormate = new WritableCellFormat(bold);//生成一个单元格样式控制对象
	        titleFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
	        titleFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中
	        Label title = new Label(0,0,"策略条件和动作选择",titleFormate);
	        sheet.setRowView(0, 600, false);//设置第一行的高度
	        sheet.addCell(title);
	        //创建要显示的具体内容
	        WritableFont color = new WritableFont(WritableFont.ARIAL);//选择字体
	        color.setColour(Colour.GOLD);//设置字体颜色为金黄色
	        WritableCellFormat colorFormat = new WritableCellFormat(color);
	        //规则定义
	        Label formate = new Label(0,1,"PCC策略",colorFormat);
	        sheet.addCell(formate);
	        Label policyCode = new Label(1,1,"规则编号");
	        sheet.addCell(policyCode);
	        Label codePollicy = new Label(2,1,"编号");
	        sheet.addCell(codePollicy);	        
	        Label conditionName = new Label(3,1,"条件名称");
	        sheet.addCell(conditionName);
	        Label operateLabel = new Label(4,1,"操作符");
	        sheet.addCell(operateLabel);
	        //动作定义    
	        Label actionCode = new Label(5,1,"编号");
	        sheet.addCell(actionCode);
	        Label actionName = new Label(6,1,"动作名称");
	        sheet.addCell(actionName);
	        Label actionValue = new Label(7,1,"动作取值");
	        sheet.addCell(actionValue);
	        Label information = new Label(8,1,"辅助信息");
	        sheet.addCell(information);
	        Label instruction = new Label(9,1,"说明");
	        sheet.addCell(instruction);
	        //添加类C语言描述
	        Label policyDescript = new Label(2,10,"类C语言描述");
	        sheet.addCell(policyDescript);
	        Label actionDescript = new Label(6,10,"类C语言描述");
	        sheet.addCell(actionDescript);
	       //为动作添加值信息 
	        Label actionName1 = new Label(6,2,bean.getControltype());
	        sheet.addCell(actionName1);
	        Label actionValue1 = new Label(7,2,bean.getControlInformation());
	        sheet.addCell(actionValue1);
	        Label actionValue11 = new Label(8,2,bean.getControlParam());
	        sheet.addCell(actionValue11);
	        //添加短信通知信息
	        String execInfo=bean.getExecNotice();
	        String invalInfo=bean.getInvaliteNotice();
	        if(execInfo!=null&&!"".equals(execInfo)&&invalInfo!=null&&!"".equals(invalInfo)){
		        String[] exe=execInfo.split(",");
		        String[] inval=invalInfo.split(",");
		        //添加执行短信通知  和取消短信通知
		        for(Integer i=0;i<exe.length;i++){
		        	Label execModel = new Label(6+i,3,exe[i]);
			        sheet.addCell(execModel);
			        Label invalModel = new Label(6+i,4,inval[i]);
			        sheet.addCell(invalModel);
		        }
	        }
	        //添加条件的说明
	        String simpleCondition=bean.getSimpleConditionDesc();
	        if(simpleCondition!=null&&!"".equals(simpleCondition)){
	        	String[]condition=simpleCondition.split(",");
		        for(Integer i=0;i<condition.length;i++){
		        	String smallcond[]=condition[i].split(":");
		        	Integer temp=0;
		        	if(i==0){temp=1;}
		        	Label smallCond1 = new Label(3,2+i,smallcond[temp]);
		   	        sheet.addCell(smallCond1);
		   	        Label smallCond2 = new Label(4,2+i,smallcond[temp+1]);
		   	        sheet.addCell(smallCond2);
		        }
	        }
	        
	       //设置黑体和下划线
	       //WritableFont boldDate = new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE,WritableFont.BOLD,false,UnderlineStyle.SINGLE);
	       //WritableCellFormat boldDateFormate = new WritableCellFormat(boldDate,DateFormats.FORMAT1);
	       //Calendar c = Calendar.getInstance();
	       // Date date = c.getTime();
	       // DateTime dt = new DateTime(4,2,date,boldDateFormate);
	       // sheet.addCell(dt);
	       //把创建的内容写入到输出流中，并关闭输出流
	        workbook.write();
	        workbook.close();
	        os.close();
	    }
	
	
	 
	 /**
	 * 查出对应模板信息Bean
	 * @param policyId
	 * @param ruleId
	 * @return
	 */
	 public static PolicyBussTemplateBean getBussinessTemplateInfo(String ruleId){
		 PolicyBussTemplateBean bussTemplate=new PolicyBussTemplateBean();
			List<PopDimCampChannel> popDimCampChannel=PopDimCampChannel.dao().findAll();
			List<PopDimContactFreq> popDimContactFreq=PopDimContactFreq.dao().findAll();
			List<PopDimControlType> popDimControlType=PopDimControlType.dao().findAll();
			List<PopPolicyRuleAct> acts = PopPolicyRuleAct.dao().find("select * from pop_policy_rule_act where policy_rule_id=?", ruleId);
			List<PopPolicyRuleEventCon> events = PopPolicyRuleEventCon.dao().find("select * from pop_policy_rule_event_con where policy_rule_id=?", ruleId);
			if(acts.size()>0){
				PopPolicyRuleAct act= acts.get(0);
				//添加管控类型
				for(PopDimControlType dimControlType : popDimControlType){
					if(StringUtil.isNotEmpty(dimControlType.get(act.getStr(act.COL_CONTROL_TYPE_ID)))){
						bussTemplate.setControltype(String.valueOf(dimControlType.get(act.getStr(act.COL_CONTROL_TYPE_ID))));
						break;
					}
				}
				//添加控制参数
				bussTemplate.setControlParam(act.getStr(act.COL_CONTROL_PARAM));
				//添加控制类访问类型
				bussTemplate.setActRuleFlag(act.getStr(act.COL_POLICY_ACT_TYPE_ID));
				StringBuffer exec=new StringBuffer();
				StringBuffer inval=new StringBuffer();

				//拼接执行通知方式  失效通知方式
				for(PopDimCampChannel campChannel:popDimCampChannel){
					if(StringUtil.isNotEmpty(campChannel.getStr(act.getStr(act.COL_EXEC_CHANNEL_ID)))){
						exec.append(campChannel.getStr(act.getStr(act.COL_EXEC_CHANNEL_ID))).append(",");
						
					}
					if(StringUtil.isNotEmpty(campChannel.getStr(act.getStr(act.COL_INVALID_CHANNEL_ID)))){
						inval.append(campChannel.getStr(act.getStr(act.COL_INVALID_CHANNEL_ID))).append(",");
						
					}
				}
				//成功通知和失败通知频次
				for(PopDimContactFreq contactFreq:popDimContactFreq){
					if(StringUtil.isNotEmpty(contactFreq.getStr(act.getStr(act.COL_EXEC_CHANNEL_ID)))){
						exec.append(contactFreq.getStr(act.getStr(act.COL_EXEC_CHANNEL_ID))).append(",");
					}
					if(StringUtil.isNotEmpty(contactFreq.getStr(act.getStr(act.COL_INVALID_CAMP_FREQUENCY)))){
						inval.append(contactFreq.getStr(act.getStr(act.COL_INVALID_CAMP_FREQUENCY))).append(",");
					}
				}
				
				//执行通知内容
				exec.append(act.getStr(act.COL_EXEC_CAMP_CONTENT));
				//执行通知内容
				inval.append(act.getStr(act.COL_INVALID_CAMP_CONTENT));
				bussTemplate.setExecNotice(exec.toString());
				bussTemplate.setInvaliteNotice(inval.toString());
			}
		
			if(events.size()>0){
				PopPolicyRuleEventCon event=events.get(0);
				bussTemplate.setSimpleCondtionData(event.getStr(event.COL_SIMPLE_CONDTIONS_DATA));
				bussTemplate.setSimpleConditionDesc(event.getStr(event.COL_SIMPLE_CONDTIONS_DESC));
			}
			
		 return bussTemplate;
	 }
	
}
