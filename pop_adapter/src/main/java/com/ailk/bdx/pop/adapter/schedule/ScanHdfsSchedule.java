package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.HdfsConfig;
import com.ailk.bdx.pop.adapter.bean.KeyMessage;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.bean.ScanPeriod;
import com.ailk.bdx.pop.adapter.cache.ReadedHdfsCache;
import com.ailk.bdx.pop.adapter.dispatch.IDispatchStrategy;
import com.ailk.bdx.pop.adapter.dispatch.impl.HashDispatchStrategy;
import com.ailk.bdx.pop.adapter.filter.impl.hubei.DefaultDataFilter;
import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;
import com.ailk.bdx.pop.adapter.process.HandlerData2PopObserver;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.gson.Gson;
 

/**
 * 周期性扫描Hdfs目录调度任务
 */
public class ScanHdfsSchedule {
	private static final Logger log = LogManager.getLogger();
	private final HdfsConfig hdfsConfig;
	private final ScheduledExecutorService scheduledExec;
	private final ExecutorService taskExec;
	private IDispatchStrategy hashDispatchStrategy ;
	private int currentAdapterGnNum;
 
 
	public ScanHdfsSchedule(String sourceName) {
		hdfsConfig = (HdfsConfig) XmlConfigureUtil.getInstance().getConfigItem(
				XmlConfigureUtil.HDFS, sourceName);
		scheduledExec = Executors.newSingleThreadScheduledExecutor();
		
		String num = Configure.getInstance().getProperty("HDFS_THREAD_POOL_NUM");
		taskExec=Executors.newFixedThreadPool(Integer.parseInt(num));
		hashDispatchStrategy = new HashDispatchStrategy(hdfsConfig.getAdapterGnTotal());
		currentAdapterGnNum = hdfsConfig.getAdapterGnNum();
	}

	public Runnable scanHdfsTask() {
		return new Runnable() {
			public void run() {
				String[] hsfsIp = hdfsConfig.getHdfsIp().split("_");
				String dst0 = "hdfs://"+hsfsIp[0]+":"+hdfsConfig.getHdfsPort()+hdfsConfig.getHdfsCatalog()+"/";
				String dst1 = "hdfs://"+hsfsIp[1]+":"+hdfsConfig.getHdfsPort()+hdfsConfig.getHdfsCatalog()+"/";
				Configuration conf = new Configuration();
				//获取当前系统时间
				Date now = new Date(); 
				//获取延迟时间
				int delayMin = Integer.parseInt(hdfsConfig.getDelayMin());
				
				//扫描上一小时的大数据平台上ODS28_LTEC1_S1MME_HM目录中的上网位置日志文件
				long delayTime=((now.getTime()/1000)-60*delayMin)*1000;//减去delayMin 分钟
				now.setTime(delayTime);
				
				SimpleDateFormat monthFormat = new SimpleDateFormat("yyyyMM");
				SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat hourFormat = new SimpleDateFormat("yyyyMMddHH");
				 
				String monthId=monthFormat.format(now);
				String dayId = dayFormat.format(now);
				String hourId = hourFormat.format(now);
				
				//按时间替换获取当前hdfs文件路径
				String  currentDst = dst0.replaceAll("\\?", monthId).replaceAll("\\#", dayId).replaceAll("\\$", hourId);		 		
		 		//String  currentDst = "hdfs://"+hdfsConfig.getHdfsIp()+":"+hdfsConfig.getHdfsPort()+"/user/hive/ocdc/dc.db/ods28_gprs_bh_hm/month_id=201504/day_id=20150421/hour_id=2015042120/";
		 		//String currentDst = "hdfs://192.168.211.10:9000/usr/hive/warehouse/hive.db/ods28_ltec1_s1mme_hm/month_id=201606/day_id=20160616/hour_id=2016061612/";
		 		//String currentDst = "hdfs://192.168.211.10:9000/usr/hive/warehouse/hive.db/ods28_ltec1_s1mme_hm/month_id=201605/day_id=20160530/hour_id=2016053001/";
				log.info("本轮扫描HDFS的目录是：" + currentDst);
		 		FileSystem fs = null;	 		
		 		try {
		 			
		 			fs= FileSystem.get(URI.create(currentDst), conf);
		 			log.info("连接成功!");
		 			boolean exist = true;
					//判断文件夹是否生成
		 			try{
		 				exist = fs.exists(new Path(currentDst));
		 			}catch (IOException e) {
						log.error(hsfsIp[0]+":"+hdfsConfig.getHdfsPort()+"上已宕机，切换节点");
						exist = false;
		 			} 
					if(!exist){
						currentDst = dst1.replaceAll("\\?", monthId).replaceAll("\\#", dayId).replaceAll("\\$", hourId);
						fs= FileSystem.get(URI.create(currentDst), conf);
						exist = fs.exists(new Path(currentDst));
						if(!exist){
							log.info("文件夹尚未生成,待下个轮训周期读取....");
							return;
						}					 
					}
					
					
				 	FileStatus fileList[] = fs.listStatus(new Path(currentDst));
					int size = fileList.length;
					log.info("本轮扫描HDFS的目录有：" + size + " 个文件。");
					int readFileCount = 0;
					for (int i = 0; i < size; i++) {
						String fileName = fileList[i].getPath().getName();
						String filePath = currentDst + fileName;
						log.info("开始处理第 " + (i + 1) + " 个文件，filePath：" + filePath);
						// 判断是否是AVL 结尾
						if (fileName.endsWith(hdfsConfig.getFileSuffix().toLowerCase())
								|| fileName.endsWith(hdfsConfig.getFileSuffix().toUpperCase())) {
							
							int index = hashDispatchStrategy.dispatch(fileName);
							//log.info("当前文件名：" + fileName + "，负责读取adapter序号：" + index);
							if (currentAdapterGnNum == index) {
								// 从缓存判断是否读取过
								try {
									if (!ReadedHdfsCache.getInstance().isReaded(fileName,hourId,
											hdfsConfig.getScanPeriod().getDelayPeriodUnitForHdfs())) {
										log.info("开启多线程读取数据...");
										log.info("开始读取文件名称：" + filePath + "，文件大小：" + fileList[i].getLen() + ">>> >>>");
										ReadHdfsThread readHdfsThread = new ReadHdfsThread(
												hdfsConfig, fileName,
												currentDst, filePath);
										taskExec.execute(readHdfsThread);
										readFileCount++;
									} else {
										log.info("文件：" + filePath + "已经读取过！");
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
						log.info("size:" + size + "---index:" + i);
					}
					log.info("pop_adapter（序号： " + currentAdapterGnNum + "）本轮成功地扫描了HDFS目录（" + currentDst + "）下的"+readFileCount+"个文件!");
				} catch (Exception e) {
				log.error("读取hdfs文件错误",e);
			} 
	 	}
	  };
			
	}
	public void test()
	  {
		final Splitter splitterComma = Splitter.on("\t");
		final DefaultDataFilter d111= new DefaultDataFilter();
		File file = new File("E:\\A_F-SCA-BH2.0-1001-4G-2015042114-999.AVL");
		try {
			Files.readLines(file, Charset.forName("utf-8"), new LineProcessor() {
				@Override
				public boolean processLine(String line) throws IOException {
					List<String> splitList = Splitter.on("\t").splitToList(line);
					System.out.println(splitList.toString());
					Message m = new Message(line,hdfsConfig);
					d111.adapte(m);
					return true;
				}

				@Override
				public List<String> getResult() {
					return null;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
	
	 public void test2()
	  {
	    String dst = "hdfs://" + this.hdfsConfig.getHdfsIp() + ":" + this.hdfsConfig.getHdfsPort() + this.hdfsConfig.getHdfsCatalog();
	    Configuration conf = new Configuration();
	    try
	    {
	      log.info(dst);
	      log.info("连接hadoop机器..");
	      FileSystem fs = FileSystem.get(URI.create(dst), conf);
	      FileStatus[] fileList = fs.listStatus(new Path(dst));
	      int size = fileList.length;
	      for (int i = 0; i < size; i++) {
	        log.info("name:" + fileList[i].getPath().getName() + 
	          "/t/tsize:" + fileList[i].getLen());
	      }
	      fs.close();
	      log.info("连接hadoop成功..");
	    } catch (IOException e) {
	      log.error("", e);
	    }
	  }
	
	public void start(){
		ScanPeriod sp = hdfsConfig.getScanPeriod();
		//test();
		//test2();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        scheduledExec.scheduleAtFixedRate(scanHdfsTask(), 0, sp.getPeriod(), TimeUnit.valueOf(sp.getTimeUnit()));
		//scheduledExec.scheduleAtFixedRate(runTaskTest(), 0, 60, TimeUnit.valueOf("SECONDS"));
	}
	
	/**
	 * 测试使用方法
	 * @return
	 */
	public Runnable runTaskTest(){
		return new Runnable(){

			@Override
			public void run() {
				log.info("runTaskTest()");
				CpeLockNetInfo cpeLockNetInfo = new CpeLockNetInfo();
				
				//lify's student
				cpeLockNetInfo.setSubsid("867368020091339");
				//手机号码
				//姚琳
				cpeLockNetInfo.setProductNo("18271265291");
				
				//小区userLocation
				List<String> userLocations = new ArrayList<String>();
				userLocations.add("130-4600029167164184578");
				//userLocations.add("130-4600028731112707329");
				//userLocations.add("130-4600028931112498689");
				//userLocations.add("130-4600028931112498690");
				//userLocations.add("130-4600028931112498691");
				//userLocations.add("130-4600029180077116675");
				//userLocations.add("130-4600028851074865025");
				cpeLockNetInfo.setUserLocations(userLocations);
				
				KeyMessage keyMsg = new KeyMessage("1", new Gson().toJson(cpeLockNetInfo));
				// 直接接pop
				Map<String, String> dataMap = Maps.newHashMap();
				dataMap.put("ipSeq", "1");// pop是单机版
				// dataMap.put("message",
				// Joiner.on("").join(eventId,",",keyMsg.getValue()));
				dataMap.put("message", keyMsg.getValue());
				
				HandlerData2PopObserver.sendMessageToPop(dataMap);
				log.info("HandlerData2PopObserver.sendMessageToPop(" + new Gson().toJson(dataMap) + ")");
			}
			
		};
	}
 
}
