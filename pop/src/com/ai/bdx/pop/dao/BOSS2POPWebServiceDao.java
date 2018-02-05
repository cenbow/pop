package com.ai.bdx.pop.dao;



import java.text.ParseException;

import org.springframework.dao.DataAccessException;

import  com.ai.bdx.pop.bean.Prod;
/**
 * BOSS通过webservice实时接口操作
 * @author lifangyuan
 *
 */
public interface BOSS2POPWebServiceDao {
	
	
	public String bossToPopGprsOtp(Prod prod) throws DataAccessException, ParseException;
	
	
}
