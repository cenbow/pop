package com.ai.bdx.pop.wsclient.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;

public class PccCheckSignThread  extends Thread{
	private static Logger log = Logger.getLogger(PccCheckSignThread.class
			.getName());
	private int index = 0;
	private List<Map<String, String>> returnList = new ArrayList<Map<String,String>>();
	private List<Map<String, String>> signList = new ArrayList<Map<String,String>>();
	private SprBusinessBean sprBean= new SprBusinessBean();
	private String policyType;
	public PccCheckSignThread(List<Map<String, String>> returnList,List<Map<String, String>> signList,SprBusinessBean sprBean,String policyType){
		this.returnList = returnList;
		this.signList = signList;
		this.sprBean = sprBean;
		this.policyType = policyType;
	}
	public void run() {
		//查询开户进程执行状态
		SendPccInfoServiceImpl sendPccInfoServiceImpl = new SendPccInfoServiceImpl();
		log.info("begin to check the process");
		boolean flag = false;
		while(index!=returnList.size()){
			if(returnList!=null && returnList.size()>0){
				for(int i=0;i<returnList.size();i++){
					Map<String, String> reMap = returnList.get(i);
					log.info("TaskID: "+reMap.get("TaskID")+" Ep_Sn: "+reMap.get("Ep_Sn")+" operateTime: "+reMap.get("operateTime"));
					String result=sendPccInfoServiceImpl.PCRF_CHKBAT(reMap.get("TaskID"), reMap.get("Ep_Sn"), sprBean, reMap.get("operateTime"));
					if("0".equals(result)){
						index++;
					}
					if((i==returnList.size()-1) && index!=returnList.size()){
						index=0;
					}
					if(!"0".equals(result) && !"-1".equals(result)){
						flag=true;
					}
				}
			}
			if(flag){
				break;
			}
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("end to check the process and begin to sign");
		
		//业务批量策略
		// 签约生成调用接口
		if(!flag){
			//业务策略
			if("1".equals(policyType)){
				log.info("begin to addBatService----------->");
				int res2Count = 0;
				if (signList != null && signList.size() > 0) {
					for (int j = 0; j < signList.size(); j++) {
						Map<String, String> reMap2 = signList.get(j);
						String reString = sendPccInfoServiceImpl.addBatService(reMap2.get("fileName"),
								reMap2.get("oprTime"),sprBean);
						if ("0".equals(reString)) {
							res2Count++;
							log
									.info("批量文件名：" + reMap2.get("fileName")
											+ "， 调用接口成功");
						} else {
							log
									.info("批量文件名：" + reMap2.get("fileName")
											+ "， 调用接口失败");
						}
					}
				}
				log.info("end to addBatService----------->");
			}
			//用户策略
			if("2".equals(policyType)){
				log.info("begin to addBatUsrSessionPolicy----------->");
				int res2Count = 0;
				if (signList != null && signList.size() > 0) {
					for (int j = 0; j < signList.size(); j++) {
						Map<String, String> reMap2 = signList.get(j);
						String reString = sendPccInfoServiceImpl.addBatUsrSessionPolicy(reMap2.get("fileName"),
								reMap2.get("oprTime"),sprBean);
						if ("0".equals(reString)) {
							res2Count++;
							log
									.info("批量文件名：" + reMap2.get("fileName")
											+ "， 调用接口成功");
						} else {
							log
									.info("批量文件名：" + reMap2.get("fileName")
											+ "， 调用接口失败");
						}
					}
				}
				log.info("end to addBatUsrSessionPolicy----------->");
			}
		}
	}
}
