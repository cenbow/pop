
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_busi_type
	id	int(11)
	name	varchar(75)
	p_id	int(11)
*/
public class PopDimBusiType extends PopModel<PopDimBusiType> {
	private static final long serialVersionUID = 1427702669700L;

	public static PopDimBusiType dao(){
		return new PopDimBusiType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	public static final String COL_P_ID = "p_id";
}

