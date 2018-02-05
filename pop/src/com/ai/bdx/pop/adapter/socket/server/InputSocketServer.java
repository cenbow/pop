package com.ai.bdx.pop.adapter.socket.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.ai.bdx.pop.adapter.socket.code.factory.InputSocketCodeFactory;
import com.ai.bdx.pop.util.PopUtil;
import com.asiainfo.biframe.utils.config.Configure;

/**
 * @description
 * @author lyz
 */

public class InputSocketServer {
	private static final Logger log = LogManager.getLogger();
	/** 中转server host **/
	private InetAddress serverAddr;
	/** 中转Socket Server的端口 */
	private int serverPort;
	/** 创建Acceptor **/
	private NioSocketAcceptor acceptor;

	private static class TransferServerHolder {
		private static final InputSocketServer INSTANCE = new InputSocketServer();
	}

	public static final InputSocketServer getInstance() {
		return TransferServerHolder.INSTANCE;
	}

	private InputSocketServer() {
		try {
			this.acceptor = new NioSocketAcceptor(Integer.parseInt(
					Configure.getInstance().getProperty("RECEIVE_MINA_ADAPTER_THREAD_POOL_NUM")));
		} catch (Exception e) {
			log.error("构造SocketServer时出现异常：", e);
		}
	}

	/**
	 * 初始化Socket Server配置
	 * @throws Exception
	 */
	public void init() throws Exception {
		this.serverAddr = InetAddress.getByName(PopUtil.getEsperInstanceHostAddress());
		this.serverPort = Integer.parseInt(Configure.getInstance().getProperty("SOCKET_PORT"));
		//	this.serverAddr = InetAddress.getByName("127.0.0.1");
		//	this.serverPort = 5186; 
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new InputSocketCodeFactory(Charset.forName("UTF-8"))));
		// acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" )))); 
		// acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		
		//添加过滤器；RECEIVE_MINA_ADAPTER_THREAD_POOL_NUM：pop接收adapter线程数
		acceptor.getFilterChain().addLast(
				"threadPool",new ExecutorFilter(Executors.newFixedThreadPool(
						Integer.parseInt(Configure.getInstance().getProperty(
						"RECEIVE_MINA_ADAPTER_THREAD_POOL_NUM")))));

		IoBuffer.setUseDirectBuffer(false);
		
		IoBuffer.setAllocator(new SimpleBufferAllocator());
		
		//添加业务逻辑处理器类
		acceptor.setHandler(new InputSocketHandler());
		
		//mina 读取缓存大小
		int readBufferSize = Integer.parseInt(
				Configure.getInstance().getProperty("MINA_READ_BUFFER_SIZE"));
		
		//MINA 接收adapter缓冲大小
		int receiveBufferSize = Integer.parseInt(
				Configure.getInstance().getProperty("MINA_RECEIVE_BUFFER_SIZE"));
		
		//设置读取数据的缓冲区大小
		acceptor.getSessionConfig().setReadBufferSize(readBufferSize);
		
		//设置接收数据的缓冲区大小
		acceptor.getSessionConfig().setReceiveBufferSize(receiveBufferSize);
		
		log.info(this.serverAddr + "++++readBufferSize" + readBufferSize + "++++++++++++++++++");
		log.info(this.serverAddr + "++++receiveBufferSize" + receiveBufferSize + "++++++++++++++++++");
		
		//读写通道10秒内无操作进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100);
		
		//设置的是主服务监听的端口可以重用
		acceptor.setReuseAddress(true);
	}

	/**
	 * 
	 * @throws IOException
	 * @description 开启ServerSocket
	 * @exception
	 * @see
	 */
	public void startService() throws Exception {
		if (acceptor == null) {
			init();
		}
		acceptor.bind(new InetSocketAddress(this.serverAddr, this.serverPort));
		log.info(this.serverAddr + " POP服务器服务已经成功启动与pop_adapter通信的SocketServer ，端口号：" + this.serverPort);
	}

	public void stopService() throws Exception {
		if (acceptor != null && acceptor.isActive()) {
			acceptor.dispose();
		}
		log.info(this.serverAddr + " POP服务器服务已经成功停止SocketServer ，端口号：" + this.serverPort);
	}

	public static void main(String[] args) throws Exception {
		try {
			InputSocketServer.getInstance().startService();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
