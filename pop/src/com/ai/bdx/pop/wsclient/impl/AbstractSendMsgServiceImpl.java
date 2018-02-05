package com.ai.bdx.pop.wsclient.impl;

import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.exception.PopInterfaceException;
import com.ai.bdx.pop.model.PopInterfaceConfig;
import com.ai.bdx.pop.wsclient.ISendMsgClient;
import com.asiainfo.biframe.utils.string.StringUtil;

public abstract class AbstractSendMsgServiceImpl implements ISendMsgClient{
	private static Logger log = LogManager.getLogger();
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Object getWebServicePort(String code) throws Exception {
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(code);
		JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
		if (pc != null) {
			String interfaceClass = pc.getStr(PopInterfaceConfig.COL_INTERFACE_CLASS);
			String url = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
			if (StringUtil.isNotEmpty(interfaceClass) && StringUtil.isNotEmpty(url)) {
				log.debug("初始化客户群接口");
				svr.setServiceClass(Class.forName(interfaceClass));
				svr.setAddress(url);
				return  svr.create();
			} else {
				throw new PopInterfaceException("未配置客户群接口[" + code + "]的接口类或接口地址");
			}
		} else {
			throw new PopInterfaceException("未配置客户群接口[" + code + "]");
		}
	}
	
	public Map<String, Object> sendPoliceyPauseMsg(Map<String, Object> msg,String code) {
		return null;
	}
	public Map<String, Object> sendPoliceyStop(Map<String, Object> msg,String code) {
		return null;
	}
	public Map<String, Object> sendPoliceyInfo(Map<String, Object> msg,String code) {
		return null;
	}

}
