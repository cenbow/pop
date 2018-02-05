/**   
 * @Title: DimProdProductDmServiceJob.java
 * @Package com.ai.bdx.pop.task
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-11 下午6:12:21
 * @version V1.0   
 */
package com.ai.bdx.pop.task;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import com.ai.bdx.pop.wsclient.impl.PopGetDacpHttpClientImpl;

/**
 * @ClassName: DimProdProductDmServiceJob
 * @Description: TODO(POP和DACP同步数据接口， 同步dim_prod_product_dm（每日汇总）)
 * @author jinlong
 * @date 2015-6-11 下午6:12:21
 * 
 */
public class PopGetDimProdProductDmServiceJob  implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -177353373881741768L;

	
	private static Logger log = LogManager.getLogger();
	
	/**
	 * 同步dim_logic_area TASK方法
	 * @Title: runTask
	 * @Description: TODO
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void runTask() {
		try {
			log.debug("======PopGetDimProdProductDmServiceJob======runTask()=======获取DACP同步数据接口数据开始");
			PopGetDacpHttpClientImpl popGetDacpHttpClientImpl = new PopGetDacpHttpClientImpl();
			popGetDacpHttpClientImpl.popGetDimProdProductDm();
			log.debug("======PopGetDimProdProductDmServiceJob======runTask()=======获取DACP同步数据接口数据成功");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.debug("======PopGetDimProdProductDmServiceJob=======runTask()======获取DACP同步数据接口数据异常");
			e.printStackTrace();
		}
		
	}

}
