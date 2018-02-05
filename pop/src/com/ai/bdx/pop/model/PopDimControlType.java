
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_control_type
	id	smallint(6)
	name	varchar(75)
	code    varchar(50)
*/
public class PopDimControlType extends PopModel<PopDimControlType> {
	private static final long serialVersionUID = 1427702670669L;

	public static PopDimControlType dao(){
		return new PopDimControlType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_CODE = "code";
}

