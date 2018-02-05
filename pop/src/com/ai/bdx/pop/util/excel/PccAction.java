package com.ai.bdx.pop.util.excel;
/**
 * 
 * creator：sjw 
 * create date：2015年6月7日
 * useful：pcc动作封装
 */
public class PccAction {
	private String number;// 编号
	private String actName;// 动作名称
	private String oper;// 操作符
	private String val;// 动作取值
	private String information;// 辅助信息
	private String instruction;//说明
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	
}
