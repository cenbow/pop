package com.ailk.bdx.pop.adapter.schedule;
 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.bean.SocketConfig;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;
import com.ailk.bdx.pop.adapter.model.McCdrDetail;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.MinaParseUtil;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
 

 

 
public class McdSocketClientHandler extends IoHandlerAdapter {
	
 
	//private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(McdSocketClientHandler.class);
	private static final Logger log = LogManager.getLogger();
	
	private SocketConfig socketConfig;
	private static final boolean USE_BUFFER_MODULE;// 是否需要缓存模块
	private IDataHandle dataHandle;
	private IDataBuffer dataBuffer;
	
	static {
		USE_BUFFER_MODULE = Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
	}

	public McdSocketClientHandler(){
	}

	
	public void setFileConfig(String sourceName) {
		socketConfig = (SocketConfig)XmlConfigureUtil.getInstance().getConfigItem(XmlConfigureUtil.SOCKET, sourceName);
	}
	public void setDataHandle(IDataHandle dataHandle) {
		this.dataHandle = dataHandle;
	}

	public void setDataBuffer(IDataBuffer dataBuffer) {
		this.dataBuffer = dataBuffer;
	}	
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.info("客户端连接成功："+session.getRemoteAddress());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.info("客户端发送信息异常:"+cause.toString());
	}
	 
	 

	// 当客户端发送消息到达时
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		McCdrDetail mc = (McCdrDetail)message;
		if(mc.getHeadType()==2){
			log.info("===============身份验证成功开始通话==========");
		}
		if(mc.getHeadType()==4){
			String detail = MinaParseUtil.getMessageDetailForStr(mc.getMessage());
			log.debug("+++++当前线程名:"+Thread.currentThread().getName()+"+++++Sca服务器端发来的消息："+mc.getMessage());
			if (USE_BUFFER_MODULE) {
				dataBuffer.push(new Message(detail,socketConfig));
			}else{
				dataHandle.handle(new Message(detail,socketConfig));
			}
			
		}
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.info("客户端与服务端断开连接");
	}
	//这个方法的调用在sessionOpened之前发生
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}
}
