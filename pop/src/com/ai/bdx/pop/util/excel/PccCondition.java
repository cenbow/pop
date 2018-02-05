package com.ai.bdx.pop.util.excel;

/**
 * 
 * creator：sjw create date：2015年6月7日 useful：pcc条件封装
 */
public class PccCondition {

	private String number;// 编号
	private String conditionName;// 条件名称
	private String oper;// 操作符
	private String val;// 条件取值
	private String remark;// 辅助信息

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
