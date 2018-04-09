package com.ailk.bdx.pop.adapter.startup;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.kafka.CepKafKaProducer;
import com.ailk.bdx.pop.adapter.kafka.CepKafkaSimpleConsumerUtil;
import com.ailk.bdx.pop.adapter.kafka.receiver.ICepMessageReceiveService;
import com.ailk.bdx.pop.adapter.model.SessionInfo;
import com.ailk.bdx.pop.adapter.process.HandlerData2PopObserver;
import com.ailk.bdx.pop.adapter.schedule.RefreshCountryUserLocationCacheSchedule;
import com.ailk.bdx.pop.adapter.schedule.RefreshCpeUserInfoCacheSchedule;
import com.ailk.bdx.pop.adapter.schedule.ScanFileSchedule;
import com.ailk.bdx.pop.adapter.schedule.ScanFtpSchedule;
import com.ailk.bdx.pop.adapter.schedule.ScanHdfsSchedule;
import com.ailk.bdx.pop.adapter.schedule.ScanSftpSchedule;
import com.ailk.bdx.pop.adapter.schedule.interfaces.INioTcpClient;
import com.ailk.bdx.pop.adapter.socket.socket2pop.ConnectPopClient;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.SpringContext;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.collect.HashMultimap;

/**
 * 启动类
 *
 */
public final class Bootstrap {
	private static Logger log = LogManager.getLogger();
	private static Bootstrap daemon = null;
	
	
	public void init() throws Exception {
		// 加载配置项
		log.info("加载SpringContext...");
	 	SpringContext.getSpringApplicationContext();
		// 询问集群状态信息
	 	//getClusterInfo();
	 	initConnect2Pop();
		if(Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"))){
 		 	startBuffer();
		}
		log.info("开始初始化CPE的设备唯一号（SUBSID）缓存>>>>>>");
		new RefreshCpeUserInfoCacheSchedule().start();
		log.info("初始化CPE的设备唯一号（SUBSID）缓存成功！");
		
		/*log.info("开启定时更新CPE的设备唯一号（SUBSID）缓存的定时任务>>>>>>");
		log.info("开启定时更新CPE的设备唯一号（SUBSID）缓存的定时任务成功！");*/
		
		log.info("开启定时更新农村小区的UserLocation缓存的定时任务>>>>>>");
		new RefreshCountryUserLocationCacheSchedule().start();
		log.info("开启定时更新初始化农村小区的UserLocation的定时任务成功！");
	}
 
	private void load(String[] arguments) throws Exception {
 		XmlConfigureUtil.getInstance();
	}

	private void initConnect2Pop() {
		SessionInfo sessionInfo = HandlerData2PopObserver.sessionsMapping.get("1");
		if (sessionInfo == null) {
			//POP应用所在服务器的IP地址
			String popIp = Configure.getInstance().getProperty("POP_IP");
			
			//POP应用与pop_adapter通信的MINA Server的端口号
			String popPort = Configure.getInstance().getProperty("ADAPTER_MINA_SERVER_PORT");
			SessionInfo sessiontmp = new SessionInfo();
			HandlerData2PopObserver.sessionsMapping.put("1", sessiontmp);
			new ConnectPopClient(popIp, popPort, "1").start();
		}
	}
	
	/**
	 * 向集群发消息，获取集群信息
	 */
	private void getClusterInfo(){
		//设置队列监听
		try {
			String fromQueue = Configure.getInstance().getProperty("LISTEN_RESPONSE_QUEUENAME");
			int partNum = Integer.parseInt(Configure.getInstance().getProperty("KAFKA_PARTITIONS_NUM"));
			ICepMessageReceiveService cepAliveSegReceiverImpl = SpringContext.getBean("clusterStatusListener",ICepMessageReceiveService.class);
			CepKafkaSimpleConsumerUtil.getInstance().startAllConsumer(fromQueue,partNum,cepAliveSegReceiverImpl,false);
		} catch (Exception e) {
			log.error("+++++++++++获取pop序号 线程异常+++++++++++", e);
		}
		
		String toQueue = Configure.getInstance().getProperty("START_REQUEST_QUEUENAME");
		CepKafKaProducer cepKafKaProducer = null;
		try {
			Thread.sleep(2000);
			log.info("向集群发消息，获取集群信息开始...");
			cepKafKaProducer = new CepKafKaProducer();
			cepKafKaProducer.send(toQueue, "all");
			log.info("向集群发消息，获取集群信息成功...");
		} catch (Exception e) {
			log.error("向集群发消息，获取集群信息异常", e);
		}  finally{
			if(cepKafKaProducer !=null ){
				cepKafKaProducer.close();
			}
	   }
		
	}

	private void startBuffer(){
		IDataBuffer dataBuffer = SpringContext.getBean("dataBuffer",IDataBuffer.class);
		dataBuffer.start();
	}

	public void start() throws Exception {
		HashMultimap<String, BaseConfig> multiConfig = XmlConfigureUtil.getInstance().getMultConfigItems();
		if(multiConfig != null){
			Set<String> keys = multiConfig.keySet();
			for(String key : keys){
				if(XmlConfigureUtil.FILE.equals(key)){ //本地文件处理
					log.info("接口方式为file...开始启动...");
					Set<BaseConfig> values = multiConfig.get(key);
					for(BaseConfig config : values){
						new ScanFileSchedule(config.getName()).start();
					}
				}else if(XmlConfigureUtil.FTP.equals(key)){ //ftp处理方式
					log.info("接口方式为ftp...开始启动...");
					Set<BaseConfig> values = multiConfig.get(key);
					for(BaseConfig config : values){
						new ScanFtpSchedule(config.getName()).start();
					}
				}else if(XmlConfigureUtil.SFTP.equals(key)){ //sftp处理方式
					log.info("接口方式为sftp...开始启动...");
					Set<BaseConfig> values = multiConfig.get(key);
					for(BaseConfig config : values){
						new ScanSftpSchedule(config.getName()).start();
					}
				}else if(XmlConfigureUtil.HDFS.equals(key)){//HDFS
					log.info("接口方式为HDFS...开始启动...");
					Set<BaseConfig> values = multiConfig.get(key);
					for(BaseConfig config : values){
						new ScanHdfsSchedule(config.getName()).start();
					}
				}else if(XmlConfigureUtil.SOCKET.equals(key)){ //socket处理方式
					log.info("接口方式为SOCKET,开始启动...");
					Set<BaseConfig> values = multiConfig.get(key);
					for(BaseConfig config : values){
						INioTcpClient nioTcpClient = SpringContext.getBean("nioTcpClient",INioTcpClient.class);
						nioTcpClient.connnect(config.getName());
					}
				}
			}
		}
	}

	public void stop() throws Exception {
		IDataBuffer dataBuffer = SpringContext.getBean("dataBuffer",IDataBuffer.class);
		dataBuffer.shutdown();
	}

	public void destroy() {
		SpringContext.destroy();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("args:" + args);
		System.out.println("args:" + args);
		if (daemon == null) {
			daemon = new Bootstrap();
			try {
				//为adapter注册一个端口号
				/*int port = Integer.parseInt(Configure.getInstance().getProperty("ADAPTER_PORT"));
				LocateRegistry.createRegistry(port);*/
				daemon.init();
			} catch (Throwable t) {
				t.printStackTrace();
				daemon.destroy();
				System.exit(0);
			}
		}

		try {
			String command = "start";
			if (args.length > 0) {
				command = args[(args.length - 1)];
			}

			if (command.equals("startd")) {
				args[(args.length - 1)] = "start";
				daemon.load(args);
				daemon.start();
			} else if (command.equals("stopd")) {
				args[(args.length - 1)] = "stop";
				daemon.stop();
			} else if (command.equals("start")) {
				daemon.load(args);
				daemon.start();
			} else if (command.equals("stop")) {
				daemon.stop();
			} else {
				log.warn("Bootstrap: command \"" + command + "\" does not exist.");
			}
		} catch (Throwable t) {
			t.printStackTrace();
			daemon.destroy();
		}
	}
}
