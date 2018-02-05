package com.ai.bdx.pop.adapter.dao;

import java.util.List;

import com.ai.bdx.pop.bean.CpeAccessibleLacCiBean;
import com.ai.bdx.pop.bean.DimCpeLacCi;


public interface IDimCpeLacCiDao {
	/**
	 * 根据小区lacCi查询小区信息
	 * @param lacCi
	 * @return
	 */
	public int queryCountByLacCi(String lacCi)
		throws Exception;
	
	public int query(DimCpeLacCi dimCpeLacCi)throws Exception;
	
	/**
	 * 查询一个CPE的可接入小区
	 * @return
	 * @throws Exception
	 */
	public List<CpeAccessibleLacCiBean> queryAccessibleLacCi(CpeAccessibleLacCiBean cpeAccessibleLacCiBean)throws Exception;
	
}
