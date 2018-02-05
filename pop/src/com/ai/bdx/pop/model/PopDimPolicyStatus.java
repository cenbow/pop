package com.ai.bdx.pop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.base.PopModel;

/**
 * pop_dim_policy_status id smallint(6) name varchar(75)
 */
public class PopDimPolicyStatus extends PopModel<PopDimPolicyStatus> {
	private static final long serialVersionUID = 1427702671412L;

	public static final String COL_ID = "id";
	public static final String COL_NAME = "name";
	private static Map<String, String> VALUE_MAP = null;

	public static PopDimPolicyStatus dao() {
		return new PopDimPolicyStatus();
	}

	/**
	 * id-name转换
	 * 
	 * @param dimPolicyTypes
	 */
	public static final void fillTypes(List<PopDimPolicyStatus> list) {
		if (!list.isEmpty()) {
			VALUE_MAP = new HashMap<String, String>();
			for (PopDimPolicyStatus m : list) {
				VALUE_MAP.put(m.get(COL_ID).toString(), m.get(COL_NAME)
						.toString());
			}
		}
	}

	/**
	 * id-name转换
	 * 
	 * @param id
	 * @return
	 */
	public static final String getTypeName(String id) {
		if (null == VALUE_MAP) {
			return "";
		}
		return VALUE_MAP.get(id);
	}

}
