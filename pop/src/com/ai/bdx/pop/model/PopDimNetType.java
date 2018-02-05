
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_net_type
	id	smallint(6)
	name	varchar(75)
*/
public class PopDimNetType extends PopModel<PopDimNetType> {
	private static final long serialVersionUID = 1427702670976L;

	public static PopDimNetType dao(){
		return new PopDimNetType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

