package com.ai.bdx.pop.wsclient.impl;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.exception.PopInterfaceException;
import com.ai.bdx.pop.model.PopInterfaceConfig;
import com.ai.bdx.pop.service.impl.CommonServiceImpl;
import com.ai.bdx.pop.util.InterfaceConstant;
import com.ai.bdx.pop.wsclient.ICmCustomersWsClient;
import com.ai.bdx.pop.wsclient.interfaces.ICocCustomersWsServer;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * 客户群接口
 * @author zhangyb5
 *
 */
public class PopCmCustomersWsClientImpl extends CommonServiceImpl implements ICmCustomersWsClient {
	private static Logger log = LogManager.getLogger();

	private static ICocCustomersWsServer port = null;

	public synchronized static void initPort() throws Exception {
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE);
		JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
		if (pc != null) {
			String interfaceClass = pc.getStr(PopInterfaceConfig.COL_INTERFACE_CLASS);
			String url = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
			if (StringUtil.isNotEmpty(interfaceClass) && StringUtil.isNotEmpty(url)) {
				log.debug("初始化客户群接口");
				svr.setServiceClass(Class.forName(interfaceClass));
				svr.setAddress(url);
				ICocCustomersWsServer coc = (ICocCustomersWsServer) svr.create();
				port = coc;
			} else {
				throw new PopInterfaceException("未配置客户群接口[" + InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE + "]的接口类或接口地址");
			}
		} else {
			throw new PopInterfaceException("未配置客户群接口[" + InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE + "]");
		}
	}

	@Override
	public String getCustomersList(String userId) throws Exception {
		String data = null;
		String invokeResult = "";
		try {
			if (port == null) {
				initPort();
			}
			data = port.getCustomersList(userId, "");
			invokeResult = "success:" + data;
		} catch (Exception e) {
			invokeResult = "获取客户群列表异常：" + e.getMessage();
			log.error("getCustomersList error:",e);
			throw e;
		} finally {
			this.writeInterfaceLog(InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE, InterfaceConstant.INVOKE_TYPE_CLIENT,
					"getCustomersList(userId=" + userId + ") ", invokeResult);
		}
		return data;
	}

	@Override
	public String getTargetCustomersObj(String id) throws Exception {
		String data = null;
		String invokeResult = "";
		try {
			if (port == null) {
				initPort();
			}
			data = port.getTargetCustomersObj(id);
			invokeResult = "success:" + data;
		} catch (Exception e) {
			invokeResult = "获取非周期客户群异常：" + e.getMessage();
			log.error("getTargetCustomersObj error:",e);
			throw e;
		} finally {
			this.writeInterfaceLog(InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE, InterfaceConstant.INVOKE_TYPE_CLIENT,
					"getTargetCustomersObj(customerId=" + id + ") ", invokeResult);
		}
		return data;
	}

	@Override
	public String getTargetCustomersCycleObj(String id, String date) throws Exception {
		String data = null;
		String invokeResult = "";
		try {
			if (port == null) {
				initPort();
			}
			data = port.getTargetCustomersCycleObj(id, date);
			invokeResult = "success:" + data;
		} catch (Exception e) {
			invokeResult = "获取周期性客户群异常：" + e.getMessage();
			log.error("getTargetCustomersCycleObj error:",e);
			throw e;
		} finally {
			this.writeInterfaceLog(InterfaceConstant.CALLWS_CUSTOMERS_CM_CODE, InterfaceConstant.INVOKE_TYPE_CLIENT,
					"getTargetCustomersCycleObj(customerId=" + id + ",date=" + date + ") ", invokeResult);
		}
		return data;
	}

}
