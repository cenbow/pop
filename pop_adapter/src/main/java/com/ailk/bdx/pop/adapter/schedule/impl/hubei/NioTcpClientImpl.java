package com.ailk.bdx.pop.adapter.schedule.impl.hubei;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ailk.bdx.pop.adapter.bean.SocketConfig;
import com.ailk.bdx.pop.adapter.code.factory.McCdrCodecFactory;
import com.ailk.bdx.pop.adapter.model.McCdrDetail;
 
import com.ailk.bdx.pop.adapter.schedule.McdSocketClientHandler;
import com.ailk.bdx.pop.adapter.schedule.interfaces.IMcdSocketClientHandler;
import com.ailk.bdx.pop.adapter.schedule.interfaces.INioTcpClient;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.SpringContext;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;

public class NioTcpClientImpl extends Thread   implements INioTcpClient{
	private static final Logger log = LogManager.getLogger();
	private IoSession session;
	private IoConnector connector;
	private SocketConfig socketConfig;
	private ConnectFuture connFuture;
	private String sourceName;
	 

	public NioTcpClientImpl() {
	}
	
	@Override
	public void run(){
	 
		log.debug("TcpClient::TcpClient");
		log.debug("Created a tcp connector");
		connector = new NioSocketConnector();
		
		connector.setConnectTimeoutMillis(socketConfig.getTimeOutMills());

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new McCdrCodecFactory(Charset.forName(socketConfig.getSocketCharset()))));
		//无界线程池,可以进行自动线程回收
		//connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));  
	 	 connector.getFilterChain().addLast("threadPool",new ExecutorFilter(Executors.newFixedThreadPool(Integer.parseInt(Configure.getInstance().getProperty("RECEIVE_MINA_SERVER_THREAD_POOL_NUM")))));
	 	
	 	IoBuffer.setUseDirectBuffer(false);   
		log.debug("Setting the handler");
		
		connector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
				@Override
				public void sessionClosed(NextFilter nextFilter, IoSession sessionn) throws Exception {
					for (;;) {
						try {
							Thread.sleep(3000);
							connFuture = connector.connect(new InetSocketAddress(socketConfig.getSocketIp(),socketConfig.getSocketPort()));
							connFuture.awaitUninterruptibly();
							session = connFuture.getSession();
							if (session.isConnected()) {
								log.info("重连SCA服务器成功了!");
								Thread.sleep(2000);
								validateIdentity();
								log.info("发送用户名密码完毕...");
								log.info("系统会源源不断的接收到SCA发送的数据,具体可把log级别改成debug查看数据(数据量很大),前提请确保当前时间点SCA是否有数据生成!...");
								break;
							}
						} catch (Exception e) {
							log.error("SCA服务器连接失败,3秒钟继续重新连接服务器," + e.getMessage());
						}
					}
				}
			});

		SocketSessionConfig cfg = (SocketSessionConfig) connector.getSessionConfig();
		// 设置读取缓冲区的大小
		int readbufSize = socketConfig.getReadbufSize();  
		if (readbufSize != 0) {
			cfg.setReadBufferSize(readbufSize);
		}
		 
		// 设置缓冲区的大小
		int receivebuffer = socketConfig.getReceivebuffer();
		if (receivebuffer != 0) {
			cfg.setReceiveBufferSize(receivebuffer);
		}
		 IMcdSocketClientHandler reader = SpringContext.getBean("mcdSocketClientHandler", IMcdSocketClientHandler.class);
		 reader.setFileConfig(this.sourceName);
		 connector.setHandler(reader);
		 for (;;) {
			try {
				Thread.sleep(5000);
				connFuture = connector.connect(new InetSocketAddress(socketConfig.getSocketIp(),socketConfig.getSocketPort()));
				connFuture.awaitUninterruptibly();
				log.info("准备连接server...");
				session = connFuture.getSession();
				if (session.isConnected()) {
					log.info("连接server成功了!");
					Thread.sleep(2000);
					log.info("发送用户名密码...");
					validateIdentity();
					log.info("发送用户名密码完毕...");
					log.info("系统会源源不断的接收到SCA发送的数据,具体可把log级别改成debug查看数据(数据量很大),前提请确保当前时间点SCA是否有数据生成!...");
					break;
				}
			} catch (Exception e) {
				log.error("连接Server失败,3秒钟继续重新连接服务器," + e.getMessage());
			}
		}
		 
	}

	/**
	 * 连接成功后发送数据
	 * @throws InterruptedException
	 */
//	private void sendData() throws InterruptedException {
//		//依据实际情况编写逻辑
//		for (int i = 0; i < 30; i++) {
//			try {
//				long free = Runtime.getRuntime().freeMemory();
//				session.write("当前空闲内存:" + free);
//				Thread.sleep(1000L);
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new InterruptedException(e.getMessage());
//			}
//		}
//	}
	
	
	public void validateIdentity(){
		//发出身份验证
		McCdrDetail	msg = new McCdrDetail();
		msg.setHeadType((byte)1);
		char[] u = new char[16];
		String username = socketConfig.getSocketUserName();
		char[] a = username.toCharArray();
		for(int i=0;i<a.length;i++){
			u[i] = a[i];
		}
		msg.setUserName(u);
		
		char[] p = new char[16];
		String password = socketConfig.getSocketPassword();
		char[] b = password.toCharArray();
		for(int i=0;i<b.length;i++){
			p[i] = b[i];
		}
		msg.setPassWord(p);
		
		String column =socketConfig.getContent().getSourceFileField();
		msg.setColumnStr(column);
		session.write(msg);
	}

	@Override
	public void connnect(String sourceName) {
		socketConfig = (SocketConfig)XmlConfigureUtil.getInstance().getConfigItem(XmlConfigureUtil.SOCKET, sourceName);
		this.sourceName = sourceName;
		this.start();
	}

}
