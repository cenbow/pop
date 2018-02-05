package com.ai.bdx.pop.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.DimCpeStation;
import com.ai.bdx.pop.service.CpeSelectStationService;
import com.ai.bdx.pop.util.SpringContext;

public class CpeSelectStationController extends PopController{
	private static Logger log = LogManager.getLogger(CpeSelectStationController.class);

	/*
	 * 按基站，城市查询的方法
	 */
	public void selectStation(){
		
		//获取基站名
		String stationName = this.getPara("stationName");
		try {
			stationName = new String(stationName.getBytes("iso8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取城市名
		String cityName = this.getPara("cityName");
		//转换编码格式
		try {
			cityName = new String(cityName.getBytes("iso8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<DimCpeStation> dimCpeStationList = new ArrayList<DimCpeStation>();
		//初始化spring容器
		CpeSelectStationService cpeSelectService=SpringContext.getBean("cpeSelectStationService",CpeSelectStationService.class);
		//调用service层
		dimCpeStationList = cpeSelectService.selectCpeStationService(stationName,cityName);
		setAttr("dimCpeStationList",dimCpeStationList);
		setAttr("stationName", stationName);
		setAttr("cityName", cityName);
		render("cpeManager/selectStationName.jsp");
	}
	
	/*
	 * 从首页进入基站查询页面
	 */
	public void getStation() {
		log.info("成功进入基站查询页面");
		render("cpeManager/selectStationName.jsp?"+this.getRequest().getQueryString());
	}
}
