package com.ailk.bdx.pop.adapter.handle.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.KeyMessage;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.dispatch.IDispatchStrategy;
import com.ailk.bdx.pop.adapter.filter.IDataFilter;
import com.ailk.bdx.pop.adapter.kafka.CepKafKaProducer;
import com.ailk.bdx.pop.adapter.process.HandlerData2PopObserver;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class DefaultDataHandle extends AbstractDataHandle {
	private static final String ROUNTINGKEY_PREFIX = Configure.getInstance().getProperty("ROUNTINGKEY_PREFIX");
	private static final String ADAPTER_JOIN_POP_FOR_MINA = Configure.getInstance().getProperty("ADAPTER_JOIN_POP_FOR_MINA");
	
	private static final Logger log = LogManager.getLogger();
	private IDataFilter dataFilter;
	private IDispatchStrategy dispatchStrategy;
	private CepKafKaProducer cepKafKaProducer;
	
	public DefaultDataHandle() {
		cepKafKaProducer = new CepKafKaProducer();
	}

	public void setDataFilter(IDataFilter dataFilter) {
		this.dataFilter = dataFilter;
	}

	public void setDispatchStrategy(IDispatchStrategy dispatchStrategy) {
		this.dispatchStrategy = dispatchStrategy;
	}


	@Override
	public void handle(Message msg) {
		//判断是否一个接口内对接多数据源
		String eventId = "";
		if(StringUtils.isNotEmpty(msg.getInterIdToEventId())){
			  //如果对接多个,每条数据会带上自己eventId的值
			  eventId = msg.getInterIdToEventId();
		}else{
			  //如果统一一个数据源发过来,message不会存放eventId的值,此值从配置文件读取,
			  eventId = msg.getConfig().getEventId();
		}
		KeyMessage keyMsg = dataFilter.adapte(msg);
		if (keyMsg != null) {
			try{
				//int index = dispatchStrategy.dispatch(keyMsg.getKey());
				int index = 1;
				log.debug("+++++当前线程名:"+Thread.currentThread().getName()+"内容信息:{},dispatch:{}",keyMsg.getValue(),index);
				//if (index != -1) {
					log.debug("解析CPE 位置信息成功！开始发送给POP...");
					sendToPop(keyMsg,index,eventId);
				//}
			}catch(Exception e){
				log.error("分发异常:",e);
			}
 			
		}
		
	}
 
	
	/**
	 *  keyMsg adapter经过适配返回的key-value,
	 *  index: pop索引,
	 *  eventId:数据源ID
	 * */
	public void sendToPop(KeyMessage keyMsg,int index,String eventId){
		// 判断是对接kafka 还是 直接接pop
		if ("1".equals(ADAPTER_JOIN_POP_FOR_MINA)) {
			// 直接接pop
			Map<String, String> dataMap = Maps.newHashMap();
			dataMap.put("ipSeq", "1");// pop是单机版
			// dataMap.put("message",
			// Joiner.on("").join(eventId,",",keyMsg.getValue()));
			dataMap.put("message", keyMsg.getValue());
			HandlerData2PopObserver.sendMessageToPop(dataMap);
		} else {
			cepKafKaProducer.send(ROUNTINGKEY_PREFIX + index, Joiner.on("")
					.join(eventId, ",", keyMsg.getValue()));
		}
	}
	 
}
