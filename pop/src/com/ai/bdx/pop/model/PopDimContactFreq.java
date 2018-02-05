
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_dim_contact_freq
	id	smallint(5)	--接触频次ID
	name	varchar(75)	--接触频次名称
*/
public class PopDimContactFreq extends PopModel<PopDimContactFreq> {
	private static final long serialVersionUID = 1428550097648L;

	public static PopDimContactFreq dao(){
		return new PopDimContactFreq();
	}
	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
}

