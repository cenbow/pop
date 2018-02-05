/**   
 * @Title: PopGetDimLogicAreaServiceJob.java
 * @Package com.ai.bdx.pop.task
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-10 下午3:40:27
 * @version V1.0   
 */
package com.ai.bdx.pop.task;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.bean.DimLogicAreaBean;
import com.ai.bdx.pop.wsclient.PopGetDacpHttpClient;
import com.ai.bdx.pop.wsclient.impl.PopGetDacpHttpClientImpl;

/**
 * @ClassName: PopGetDimLogicAreaServiceJob
 * @Description: TODO( POP和DACP同步数据接口， 同步dim_logic_area（每日汇总）)
 * @author jinlong
 * @date 2015-6-10 下午3:40:27
 * 
 */
public class PopGetDimLogicAreaServiceJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6377777453828445606L;
	private static Logger log = LogManager.getLogger();
	
	private PopGetDacpHttpClient popGetDacpHttpClient;

	public PopGetDacpHttpClient getPopGetDacpHttpClient() {
		return popGetDacpHttpClient;
	}

	public void setPopGetDacpHttpClient(PopGetDacpHttpClient popGetDacpHttpClient) {
		this.popGetDacpHttpClient = popGetDacpHttpClient;
	}

	/**
	 * 同步dim_logic_area TASK方法
	 * @Title: runTask
	 * @Description: TODO
	 * @param     
	 * @return void 
	 * @throws
	 */
	public void runTask() {
			log.debug("======PopGetDimLogicAreaServiceJob======runTask()=======获取DACP同步数据接口数据开始");
		try{
			PopGetDacpHttpClientImpl popGetDacpHttpClientImpl = new PopGetDacpHttpClientImpl();
			popGetDacpHttpClientImpl.dimLogicArea();
			log.debug("======PopGetDimLogicAreaServiceJob======runTask()=======获取DACP同步数据接口数据成功");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("======PopGetDimLogicAreaServiceJob=======runTask()======获取DACP同步数据接口数据异常");
			e.printStackTrace();
		}
		
	}
	
	
	
}
