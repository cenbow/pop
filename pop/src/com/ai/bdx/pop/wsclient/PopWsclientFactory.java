package com.ai.bdx.pop.wsclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.wsclient.impl.PopCmCustomersWsClientImpl;

/**
 * 接口统一工厂
 * @author zhangyb5
 *
 */
public class PopWsclientFactory {

	private static Logger log = LogManager.getLogger();

	private static ICmCustomersWsClient cocCustomersWsClient;

	/**
	 * 得到调用coc客户群 ws的客户端；
	 * @return
	 */
	public static ICmCustomersWsClient getCmCustomersWsClient() throws Exception {

		if (cocCustomersWsClient == null) {
			cocCustomersWsClient = new PopCmCustomersWsClientImpl();
		}

		return cocCustomersWsClient;
	}

}
