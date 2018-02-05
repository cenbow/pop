package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;

public class CpeUserLockRel extends PopModel<CpeUserLockRel> {
	private static final long serialVersionUID = 1L;
	public static CpeUserLockRel dao(){
		return new CpeUserLockRel();
	}
	public static final String LAC_CI_HEX_CODE = "lac_ci_hex_code";
	public static final String LAC_CI_DEC_ID = "lac_ci_dec_id";
	public static final String LAC_HEX_CODE = "lac_hex_code";
	public static final String LAC_DEC_ID = "lac_dec_id";
	public static final String CI_HEX_CODE = "ci_hex_code";
	public static final String CI_DEC_ID = "ci_dec_id";
	public static final String CELL_NAME = "cell_name";
	public static final String USER_LOCATION = "user_location";
}
