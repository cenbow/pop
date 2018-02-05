
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_camp_channel
	id	smallint(6)
	name	varchar(75)
*/
public class PopDimCampChannel extends PopModel<PopDimCampChannel> {
	private static final long serialVersionUID = 1427702670091L;

	public static PopDimCampChannel dao(){
		return new PopDimCampChannel();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

