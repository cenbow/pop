
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_action_type
	id	smallint(6)
	name	varchar(75)
*/
public class PopDimActionType extends PopModel<PopDimActionType> {
	private static final long serialVersionUID = 1427702667355L;

	public static PopDimActionType dao(){
		return new PopDimActionType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

