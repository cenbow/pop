package com.ailk.bdx.pop.adapter.socket.socket2pop;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ailk.bdx.pop.adapter.code.factory.TranCodeFactory;
import com.ailk.bdx.pop.adapter.model.SessionInfo;
import com.ailk.bdx.pop.adapter.process.HandlerData2PopObserver;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.SpringContext;
import com.google.common.base.Strings;

public class ConnectPopClient extends Thread {
	private static final Logger log = LogManager.getLogger();
	private IoSession session;
	private IoConnector connector;
	//private SocketConnector connector;
    private String ip;
    private int port;
    private String localIp;
    private int localPort;
	private ConnectFuture connFuture;
    private String ipSeq;
	private boolean minaTcpNoDelayFlag;
	private int minaSendBufferSize;
	private int minaClientNum;
	public static byte[] lock = new byte[0];
	

	public ConnectPopClient(String ip,String port,String ipSeq) {
	 this.ip = ip;
	 this.ipSeq = ipSeq;
	 try{
		 //this.port = Integer.valueOf(port);
		 this.port = Integer.parseInt(port);

	 } catch(Exception e) {
		 log.error("pop socket端口配置不正确！");
	 }
	 
	 this.localIp = Configure.getInstance().getProperty("LOCAL_IP");
	 try{
		 this.localPort = Integer.parseInt(Configure.getInstance().getProperty("LOCAL_PORT"));
	 } catch (Exception e) {
		 this.localPort = 0;
	 }
     
     String minaSendBuffer = Configure.getInstance().getProperty("MINA_SEND_BUFFER_SIZE");
	 String minaTcpNoDelay = Configure.getInstance().getProperty("MINA_TCP_NO_DELAY");
	 minaClientNum = Integer.parseInt(Configure.getInstance().getProperty("ADAPTER_MINA_CLIENT_NUM"));
	 
	 
	 minaSendBufferSize = Integer.parseInt(minaSendBuffer);
	 minaTcpNoDelayFlag=Boolean.parseBoolean(minaTcpNoDelay);
	
	}
	
	@Override
	public void run(){
	 
		log.debug("TcpClient::TcpClient");
		log.debug("Created a tcp connector");
		
		connector = new NioSocketConnector();
		//设置连接超时
		connector.setConnectTimeoutMillis(30000);
		//长连接
		//connector.getSessionConfig().setKeepAlive(true);
 
		LoggingFilter loggingFilter = new LoggingFilter();
		loggingFilter.setSessionOpenedLogLevel(LogLevel.ERROR);
		connector.getFilterChain().addLast("logger",loggingFilter);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TranCodeFactory(Charset.forName("utf-8"))));
		connector.getFilterChain().addLast("threadPool",
				new ExecutorFilter(
						Executors.newFixedThreadPool(
								Integer.parseInt(
										Configure.getInstance().getProperty("RECEIVE_MINA_SERVER_THREAD_POOL_NUM"))))); 
		//读写都空闲时间10ms
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		connector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
				@Override
				public void sessionClosed(NextFilter nextFilter, IoSession sessionn) throws Exception {
					for (;;) {
						try {
						 synchronized(lock){
							Thread.sleep(30000);
							log.info("++++++++重连pop-server开始1...+++++++++++++");
							if(Strings.isNullOrEmpty(localIp)) {
								connFuture = connector.connect(new InetSocketAddress(ip,port));
							} else {
								connFuture = connector.connect(new InetSocketAddress(ip,port),new InetSocketAddress(localIp,localPort));
							}
							connFuture.awaitUninterruptibly();
							session = connFuture.getSession();
							SessionInfo sessionInfo ;
							if (session.isConnected()) {
								log.info("++++++++重连pop-server开始2...+++++++++++++");
								log.info("+++++++++++minaTcpNoDelay设置为"+minaTcpNoDelayFlag+"+++++++++++++++++++");
								((SocketSessionConfig) session.getConfig()).setTcpNoDelay(minaTcpNoDelayFlag);
								log.info("+++++++++++SendBufferSize设置为"+minaSendBufferSize+"+++++++++++++++++++");
								((SocketSessionConfig) session.getConfig()).setSendBufferSize(minaSendBufferSize);
								session.setAttribute("flag",ipSeq);
							    sessionInfo = HandlerData2PopObserver.sessionsMapping.get(ipSeq);
							    int index = sessionInfo.getIndex();
								sessionInfo.getSessions().set(index, session);
								sessionInfo.setDate(new Date());
								log.info("++++++++重连pop-server开始3...+++++++++++++当前index:"+index);
								sessionInfo.setIndex(++index);
								int currentSessionSize = sessionInfo.getSessions().size();
								if(index == minaClientNum){
									sessionInfo.setIndex(0);
									log.info("+++++++++++++++++++++重连Pop服务器成功了!+++ipSeq:"+ipSeq+"++客户端数量:"+currentSessionSize+"++++++++++");
								}	
							    break;
							 
							 }
						 
						 }
						} catch (Exception e) {
							log.error("++++++++++++++++++++++++Pop服务器连接失败,30s后继续重新连接服务器," + e.getMessage());
						}
					}
				}
			});
 
		// 设置读取缓冲区的大小
//		int readbufSize =  1024*1024*10;
//		cfg.setReadBufferSize(readbufSize);
//		// 设置缓冲区的大小
//		int receivebuffer =  1024*1024*10;
//		cfg.setReceiveBufferSize(receivebuffer);
		
		TransferClientHandler reader = SpringContext.getBean("transferClientHandler", TransferClientHandler.class);
		connector.setHandler(reader);
		for (;;) {
			try {
				Thread.sleep(3000);
				List<IoSession> sessions = new ArrayList();
				boolean flag = false;
				for(int i=0;i<minaClientNum;i++){
				if(Strings.isNullOrEmpty(localIp)) {
					connFuture = connector.connect(new InetSocketAddress(ip,port));
				} else {
					connFuture = connector.connect(new InetSocketAddress(ip,port),new InetSocketAddress(localIp,localPort));
				}
				connFuture.awaitUninterruptibly();
				session = connFuture.getSession();
				SessionInfo sessionInfo ;
				if (session.isConnected()) {
					log.info("+++++++++++minaTcpNoDelay设置为"+minaTcpNoDelayFlag+"+++++++++++++++++++");
					((SocketSessionConfig) session.getConfig()).setTcpNoDelay(minaTcpNoDelayFlag);
					log.info("+++++++++++SendBufferSize设置为"+minaSendBufferSize+"+++++++++++++++++++");
					((SocketSessionConfig) session.getConfig()).setSendBufferSize(minaSendBufferSize);
					session.setAttribute("flag",ipSeq);
					log.info("+++++++++++ipSeq:"+ipSeq+"+++++++++++++++++++");
					sessions.add(session);
				    sessionInfo = HandlerData2PopObserver.sessionsMapping.get(ipSeq);
					if(sessionInfo == null){
						sessionInfo = new SessionInfo();
						sessionInfo.setSessions(sessions);
						sessionInfo.setDate(new Date());
						HandlerData2PopObserver.sessionsMapping.put(ipSeq, sessionInfo);
					}else if(i == 0){
						sessionInfo.setSessions(sessions);
						sessionInfo.setDate(new Date());
					}else{
						sessionInfo.setSessions(sessions);
						sessionInfo.setDate(new Date());
					}
					int currentSessionSize = sessionInfo.getSessions().size();
					if(currentSessionSize == minaClientNum){
						log.info("++++++连接Pop服务器成功了!++++++++ipSeq:"+ipSeq+"++客户端数量:"+currentSessionSize);
						flag = true;
					  break;
					}
				 }
				}
				 if(flag){
					 break;
				 }
				
			} catch (Exception e) {
				log.error("++++++++++++++++连接Pop-Server失败+ipSeq:"+ipSeq+",ip:+"+ip+"--port:"+port+",3秒钟继续重新连接服务器," + e.getMessage());
			}
		}
		 
	}

 

}
