
package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

/** pop_avoid_custinfo
	product_no	varchar(11)
	avoid_type_id	smallint(6)
*/
public class PopAvoidCustinfo extends PopModel<PopAvoidCustinfo> {
	private static final long serialVersionUID = 1427702667174L;

	public static PopAvoidCustinfo dao(){
		return new PopAvoidCustinfo();
	}
	public static final String COL_PRODUCT_NO = "product_no";
	public static final String COL_AVOID_TYPE_ID = "avoid_type_id";
}

