
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_area_type
	id	smallint(6)
	name	varchar(75)
*/
public class PopDimAreaType extends PopModel<PopDimAreaType> {
	private static final long serialVersionUID = 1427702667543L;

	public static PopDimAreaType dao(){
		return new PopDimAreaType();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

