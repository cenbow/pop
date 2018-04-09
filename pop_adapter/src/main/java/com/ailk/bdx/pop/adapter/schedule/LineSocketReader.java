package com.ailk.bdx.pop.adapter.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.bean.SocketConfig;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;

public class LineSocketReader extends IoHandlerAdapter{
	private static final Logger log = LogManager.getLogger();
	private SocketConfig socketConfig;
	private static final boolean USE_BUFFER_MODULE;// 是否需要缓存模块
	private IDataHandle dataHandle;
	private IDataBuffer dataBuffer;

	static {
		USE_BUFFER_MODULE = Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
	}

	public LineSocketReader(){
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
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String line = message.toString();
		log.debug("客户端收到消息 :{}",line);
		if (USE_BUFFER_MODULE) {
			dataBuffer.push(new Message(line,socketConfig));
		}else{
			dataHandle.handle(new Message(line,socketConfig));
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		log.debug("Message sent...");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.debug("Session closed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		log.debug("Session created...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		log.debug("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.debug("Session opened...");
	}
}
