package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

	/** POP_DIM_PCC_ID
	id varchar(32)
	pccname varchar(64)
	used_flag smallint)
	*/

public class PopDimPccId extends PopModel<PopDimPccId>{

	private static final long serialVersionUID = 1L;
	public static PopDimPccId dao(){
		return new PopDimPccId();
	}
	public static final String id = "id";
	public static final String pccname = "pccname";
	public static final String use_flag = "use_flag";
	public static final String remark = "remark";
	public static final String addtime = "addtime";
	

}
