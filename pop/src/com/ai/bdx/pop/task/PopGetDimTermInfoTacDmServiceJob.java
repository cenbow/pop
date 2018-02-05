/**   
 * @Title: PopGetDimTermInfoTacDmServiceJob.java
 * @Package com.ai.bdx.pop.task
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-12 下午3:07:38
 * @version V1.0   
 */
package com.ai.bdx.pop.task;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import com.ai.bdx.pop.wsclient.impl.PopGetDacpHttpClientImpl;

/**
 * @ClassName: PopGetDimTermInfoTacDmServiceJob
 * @Description: TODO(POP和DACP同步数据接口， 同步dim_term_info_tac_dm_yyyymm（每日汇总）)
 * @author jinlong
 * @date 2015-6-12 下午3:07:38
 * 
 */
public class PopGetDimTermInfoTacDmServiceJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5180147996239396900L;
	private static Logger log = LogManager.getLogger();
	
	/**
	 * 同步dim_term_info_tac_dm_yyyymm TASK方法
	 * @Title: runTask
	 * @Description: TODO
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void runTask() {
		try {
			log.debug("======PopGetDimTermInfoTacDmServiceJob======runTask()===popGetDimTermInfoTacDm()====获取DACP同步数据接口数据开始");
			PopGetDacpHttpClientImpl popGetDacpHttpClientImpl = new PopGetDacpHttpClientImpl();
			popGetDacpHttpClientImpl.popGetDimTermInfoTacDm();
			log.debug("======PopGetDimTermInfoTacDmServiceJob======runTask()===popGetDimTermInfoTacDm()====获取DACP同步数据接口数据成功");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.debug("======PopGetDimProdProductDmServiceJob======runTask()===popGetDimTermInfoTacDm()===获取DACP同步数据接口数据异常");
			e.printStackTrace();
		}
		
	}
	

}
