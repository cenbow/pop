package com.ai.bdx.pop.controller;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.ai.bdx.pop.base.PopController;

import com.ai.bdx.pop.service.CpeSelectSubsidService;
import com.ai.bdx.pop.util.CpeUserInfo;
import com.ai.bdx.pop.util.SpringContext;

public class CpeSelectSubsidController extends PopController{
	private static Logger log = LogManager.getLogger(CpeSelectSubsidController.class);

	/*
	 * 按单个号码查询页面的方法
	 */
	public void selectSubsid(){
		
		//获取CPE编码
		String subsid = this.getPara("subsid");
		//获取手机号
		String productNo = this.getPara("productNo");
		//获取城市名
		String cityName = this.getPara("cityName");
		try {
			cityName = new String(cityName.getBytes("iso8859-1"),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//获取业务状态
		int busiType = this.getParaToInt("busiType");
		List<CpeUserInfo> cpeUserInfoList = new ArrayList<CpeUserInfo>();
		CpeSelectSubsidService cpeSelectService=SpringContext.getBean("cpeSelectSubsidService",CpeSelectSubsidService.class);
		//调用service层
		cpeUserInfoList = cpeSelectService.selectCpeSubsidService(subsid,productNo,cityName,busiType);
		setAttr("cpeUserInfoList",cpeUserInfoList);
		setAttr("subsid", subsid);
		setAttr("productNo", productNo);
		setAttr("cityName", cityName);
		setAttr("busiType", busiType);
		render("cpeManager/selectSubsid.jsp");
	}
	
	/*
	 * 从首页进入单个号码查询页面
	 */
	public void getSubsid() {
		log.info("成功进入单号码查询页面");
		render("cpeManager/selectSubsid.jsp?"+this.getRequest().getQueryString());
	}
	
}
