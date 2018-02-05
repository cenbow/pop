package com.ai.bdx.pop.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.service.CpeSelectCountryService;
import com.ai.bdx.pop.util.SpringContext;

public class CpeSelectCountryController extends PopController{
	private static Logger log = LogManager.getLogger(CpeSelectCountryController.class);
	/*
	 * 按村庄查询的方法
	 */
	public void selectCountry(){
		
		//获取城市名称
		String cityName = this.getPara("cityName");
		//城市转码
		try{
			cityName = new String(cityName.getBytes("iso8859-1"),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取村庄名称
		String countryName = this.getPara("countryName");
		//村庄转码
		try{
			countryName = new String(countryName.getBytes("iso8859-1"),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<DimCpeStation> dimCpecountryList = new ArrayList<DimCpeStation>();
		CpeSelectCountryService cpeSelectService=SpringContext.getBean("cpeSelectCountryService",CpeSelectCountryService.class);
		//调用service层的村庄查询方法
		dimCpecountryList = cpeSelectService.selectCpeCountryService(countryName, cityName);
		setAttr("dimCpecountryList", dimCpecountryList);
		setAttr("countryName", countryName);
		setAttr("cityName", cityName);
		render("cpeManager/selectCountry.jsp");
		
	}
	/*
	 * 从首页进入村庄查询一面的方法
	 */
	public void getCountry() {
		log.info("成功进入村庄查询页面");
		render("cpeManager/selectCountry.jsp?"+this.getRequest().getQueryString());
	}
 
}
