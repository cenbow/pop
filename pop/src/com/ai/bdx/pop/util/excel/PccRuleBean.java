package com.ai.bdx.pop.util.excel;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * creator：sjw 
 * create date：2015年6月7日
 * useful：pcc规则封装
 */
public class PccRuleBean {

	private String rule_id;//规则ID
	private String rule_name;//规则名称
	private List<PccCondition>  condition=new ArrayList<PccCondition>();//条件
	private List<PccAction> action=new ArrayList<PccAction>();//动作
	private String c_desc;//C语言描述
	
	public String getRule_id() {
		return rule_id;
	}
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public List<PccCondition> getCondition() {
		return condition;
	}
	public void setCondition(List<PccCondition> condition) {
		this.condition = condition;
	}
	public List<PccAction> getAction() {
		return action;
	}
	public void setAction(List<PccAction> action) {
		this.action = action;
	}
	public String getC_desc() {
		return c_desc;
	}
	public void setC_desc(String c_desc) {
		this.c_desc = c_desc;
	}
	
	
}
