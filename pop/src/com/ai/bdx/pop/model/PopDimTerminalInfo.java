
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_terminal_info
	id	int(11)
	brand_id	int(11)
	brand_name	varchar(75)
	model_id	int(11)
	model_name	varchar(75)
	type_id	smallint(6)
	type_name	varchar(75)
*/
public class PopDimTerminalInfo extends PopModel<PopDimTerminalInfo> {
	private static final long serialVersionUID = 1427702672042L;

	public static PopDimTerminalInfo dao(){
		return new PopDimTerminalInfo();
	}
	public static final String COL_ID = "id";
	public static final String COL_BRAND_ID = "brand_id";
	public static final String COL_BRAND_NAME = "brand_name";
	public static final String COL_MODEL_ID = "model_id";
	public static final String COL_MODEL_NAME = "model_name";
	public static final String COL_TYPE_ID = "type_id";
	public static final String COL_TYPE_NAME = "type_name";
}

