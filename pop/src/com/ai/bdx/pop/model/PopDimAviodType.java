
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_aviod_type
	id	smallint(6)
	name	varchar(75)
*/
public class PopDimAviodType extends PopModel<PopDimAviodType> {
	private static final long serialVersionUID = 1427702667970L;

	public static PopDimAviodType dao(){
		return new PopDimAviodType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

