package com.ai.bdx.pop.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ai.bdx.pop.util.PopConstant;

/**
 * 自定义结构体，用于首页展示“待办列表”
 * 
 * @author zhuyq3
 * 
 */
public class ScheduleOnHome implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8441302163056456119L;
	private String sid;
	private String policyName;
	private Object createDate;
	private String creator;
	private String operation;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Object getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Object createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

//	public String getMore() {
//		return PopConstant.HYPERLINK_4_SCHEDULE;
//	}

}
