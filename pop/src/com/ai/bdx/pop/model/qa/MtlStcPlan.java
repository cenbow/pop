package com.ai.bdx.pop.model.qa;

import com.jfinal.plugin.activerecord.Model;

public class MtlStcPlan extends Model<MtlStcPlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 189235174461892312L;
	
	public static final String COL_ID = "ID";
	public static final String COL_PLAN_ID = "PLAN_ID";
	public static final String COL_PLAN_NAME = "PLAN_NAME";
	public static final String COL_PLAN_TYPE = "TYPE_ID";
	public static final String COL_PLAN_START_DATE = "PLAN_STARTDATE";
	public static final String COL_PLAN_END_DATE = "PLAN_ENDDATE";
	public static final String COL_PLAN_CREATE_DATE = "CREATE_DATE";
	public static final String COL_PLAN_STATUS = "STATUS";
}
