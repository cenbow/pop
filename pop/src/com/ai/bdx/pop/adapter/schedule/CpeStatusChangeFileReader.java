package com.ai.bdx.pop.adapter.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.CpeStatusChangeReponse;
import com.ai.bdx.pop.adapter.bean.FileConfig;
import com.ai.bdx.pop.adapter.bean.FtpConfig;
import com.ai.bdx.pop.service.CpeStatusChangeHandler;
import com.asiainfo.biframe.utils.config.Configure;
import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class CpeStatusChangeFileReader {
	private static final Logger log = LogManager.getLogger();
	private String fileName;
	private String justFileName;
	private BaseConfig config;
	private static String charsetName;
	private boolean deleteFile;
	private static String feedbackFileDirectory;
	
	//CPE开户成功的文件存放路径
	private static String processedFileDirectory;
	
	//CPE开户异常文件存放路径
	private static String errorFileDirectory;
	
	private static String feedbackFilePrefix;
	private static String feedbackFileTimeformat;
	private static String fileSuffix;
	private static String feedbackFileSuffix;
	private static final boolean USE_BUFFER_MODULE;
	private CpeStatusChangeHandler cpeStatusChangeHandler;
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	public void setCpeStatusChangeHandler(
			CpeStatusChangeHandler cpeStatusChangeHandler) {
		this.cpeStatusChangeHandler = cpeStatusChangeHandler;
	}

	static {
		USE_BUFFER_MODULE = Boolean.valueOf(
				Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setJustFileName(String justFileName) {
		this.justFileName = justFileName;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
		if (config instanceof FileConfig) {
			FileConfig fileCfg = (FileConfig) config;
			charsetName = fileCfg.getCharsetName();
			deleteFile = fileCfg.isDeleteLocal();
			
			feedbackFileDirectory = fileCfg.getFeedbackFileDirectory();
			processedFileDirectory = fileCfg.getProcessedFileDirectory();
			errorFileDirectory = fileCfg.getErrorFileDirectory();
			feedbackFilePrefix = fileCfg.getFeedbackFilePrefix();
			feedbackFileTimeformat = fileCfg.getFeedbackFileTimeformat();
			fileSuffix = fileCfg.getFileSuffix();
			feedbackFileSuffix = fileCfg.getFeedbackFileSuffix();
		} else if (config instanceof FtpConfig) {
			FtpConfig ftpCfg = (FtpConfig) config;
			charsetName = ftpCfg.getCharsetName();
			deleteFile = ftpCfg.isDeleteLocal();
		}
	}

	public void readByLine() {
		log.info("处理文件：{}",fileName);
		Stopwatch timeWatch = Stopwatch.createStarted();
		final List<CpeStatusChangeReponse> cpeStatusChangeReponseList = new ArrayList<CpeStatusChangeReponse>();
		BufferedReader reader = null;
		boolean processedSuccess = true;
		try {
			lock.lock();
			if (USE_BUFFER_MODULE) {
				
				Files.readLines(new File(fileName), Charset.forName(charsetName), new LineProcessor() {
					@Override
					public boolean processLine(String line) throws IOException {
						//dataBuffer.push(new Message(line, config));
						return true;
					}

					@Override
					public List<String> getResult() {
						return null;
					}
				});
			} else {
				/*Files.readLines(file, Charset.forName(charsetName), new LineProcessor() {
					@Override
					public boolean processLine(String line) throws IOException {
						
						CpeStatusChangeReponse cpeStatusChangeReponse = cpeStatusChangeHandler.handle(line, config);
						if (cpeStatusChangeReponse != null) {
							cpeStatusChangeReponseList.add(cpeStatusChangeReponse);
						}
						return true;
					}

					@Override
					public List<String> getResult() {
						return null;
					}
				});*/
				
				reader = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(fileName)), "UTF-8"));
				
				String line = null;
				while((line = reader.readLine()) != null){
					CpeStatusChangeReponse cpeStatusChangeReponse = cpeStatusChangeHandler.handle(line, config);
					if (cpeStatusChangeReponse != null) {
						cpeStatusChangeReponseList.add(cpeStatusChangeReponse);
						if(cpeStatusChangeReponseList.size() > 0){//将处理后的结果存入BOSS指定的FTP或者本地文件系统上
							writeFeedbackFile2(cpeStatusChangeReponseList);
							processedSuccess = true;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("",e);
			processedSuccess = false;
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					log.error("关闭读取EDR日志文件流失败！",e);
				}
			}
			log.info("文件{}读取完毕,共计{}毫秒",fileName,timeWatch.elapsed(TimeUnit.MILLISECONDS));
			timeWatch = Stopwatch.createStarted();
			if(processedSuccess){
				if (deleteFile) {
					new File(fileName).delete(); // 删除处理完成的文件
					log.info("删除文件{},共计{}毫秒",fileName,timeWatch.elapsed(TimeUnit.MILLISECONDS));
				}else {
					try {
						FileUtils.moveFileToDirectory(new File(fileName), new File(processedFileDirectory), true);
						log.info("将解析完的EDR日志文件：" + fileName + "移入备份目录: " + processedFileDirectory + "成功！");
					} catch (IOException e) {
						log.error("将解析完的EDR日志文件：" + fileName + "移入备份目录: " + processedFileDirectory + "失败！",e);
					}
				}
			}else{
				try {
					FileUtils.moveFileToDirectory(new File(fileName), new File(errorFileDirectory), true);
					log.info("将解析完的EDR日志文件：" + fileName + "移入异常目录: " + errorFileDirectory + "成功！");
				} catch (IOException e) {
					log.error("将解析完的EDR日志文件：" + fileName + "移入异常目录: " + errorFileDirectory + "失败！",e);
				}
			}
			
			lock.unlock();
		}
	}
	
	private void writeFeedbackFile2(List<CpeStatusChangeReponse> cpeStatusChangeReponseList){
		String line;
		String path;
		justFileName = justFileName.substring(0, justFileName.length() - fileSuffix.length());
		if(feedbackFileDirectory != null && feedbackFileDirectory.endsWith(File.separator)){
			path = feedbackFileDirectory + justFileName + feedbackFileSuffix;
		}else{
			path = feedbackFileDirectory + File.separator + justFileName  + feedbackFileSuffix;
		}
		
		for (CpeStatusChangeReponse cpeStatusChangeReponse : cpeStatusChangeReponseList) {
			try {
				line = cpeStatusChangeReponse.getProductNo() + "," 
						+ cpeStatusChangeReponse.getSubsid() + "," 
						+ cpeStatusChangeReponse.getBusiStatus() + "," 
						+ cpeStatusChangeReponse.getNetLockFlag();
				Files.write(line, new File(path), Charset.forName(charsetName));
			} catch (IOException e) {
				log.error("",e);
			}
		}
	}
	
	/**
	 * 在Local上生成反馈数据文件
	 * @param cpeStatusChangeReponseList
	 */
	private void writeFeedbackFile(
			List<CpeStatusChangeReponse> cpeStatusChangeReponseList){
		SimpleDateFormat format = new SimpleDateFormat(feedbackFileTimeformat);
		String yyyyMMddHHmm = format.format(new Date());
		int serialNo = 1;
		String fileName = "";
		
		while(serialNo < 1000){
			
			fileName = feedbackFileDirectory + File.separator 
					+ feedbackFilePrefix 
					+ yyyyMMddHHmm + addPrecursorZero(String.valueOf(serialNo++),3)
					+ "."
					+ feedbackFileSuffix;
			if (!new File(fileName).exists()) {
				try {
					new File(fileName).createNewFile();
				} catch (IOException e) {
					log.error("创建反馈文件失败！",e);
				}
				break;
			}
		}
		
		String line;
		for (CpeStatusChangeReponse cpeStatusChangeReponse : cpeStatusChangeReponseList) {
			try {
				line = cpeStatusChangeReponse.getProductNo() + "," 
						+ cpeStatusChangeReponse.getSubsid() + "," 
						+ cpeStatusChangeReponse.getBusiStatus() + "," 
						+ cpeStatusChangeReponse.getNetLockFlag();
				Files.write(line, new File(fileName), Charset.forName(charsetName));
			} catch (IOException e) {
				log.error("",e);
			}
		}
		
	}
	
	public static String addPrecursorZero(String value,int fixedLength ){
		String format = "%0" + fixedLength + "d";
		String targetValue = value;
		try {
			targetValue = String.format(format, Integer.parseInt(value));
		} catch (Exception e) {
			log.error("value不是数字字符串或者fixedLength小于等于0",e);
		}
		return targetValue;
	}
}
