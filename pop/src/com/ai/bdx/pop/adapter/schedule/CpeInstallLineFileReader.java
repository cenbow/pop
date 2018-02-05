package com.ai.bdx.pop.adapter.schedule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.FileConfig;
import com.ai.bdx.pop.adapter.bean.FtpConfig;
import com.ai.bdx.pop.adapter.handle.ICpeInstallDataHandler;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.Pop2BossCpeInstallResponse;
import com.ai.bdx.pop.service.ICpeManagerService;
import com.ai.bdx.pop.util.PopConstant;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class CpeInstallLineFileReader {
	private static final Logger log = 
			LogManager.getLogger(CpeInstallLineFileReader.class);
	private ICpeInstallDataHandler cpeInstallDataHandler;
	private ICpeManagerService cpeManagerService;
	private String fileName;
	private String absolutePath;
	private BaseConfig config;
	
	private String charsetName;
	private boolean deleteFile;
	
	private String feedbackFileDirectory;
	//CPE开户成功的文件存放路径
	private static String processedFileDirectory;
	//CPE开户异常文件存放路径
	private static String errorFileDirectory;
	private String feedbackFilePrefix;
	private String feedbackFileTimeformat;
	private String feedbackFileSuffix;
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	public void setCpeInstallDataHandler(
			ICpeInstallDataHandler cpeInstallDataHandler) {
		this.cpeInstallDataHandler = cpeInstallDataHandler;
	}

	public void setCpeManagerService(ICpeManagerService cpeManagerService) {
		this.cpeManagerService = cpeManagerService;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
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
			feedbackFileSuffix = fileCfg.getFeedbackFileSuffix();
		} else if (config instanceof FtpConfig) {
			FtpConfig ftpCfg = (FtpConfig) config;
			charsetName = ftpCfg.getCharsetName();
			deleteFile = ftpCfg.isDeleteLocal();
		}
	}
	
	public void dealByLine() {
		lock.lock();
		log.info("处理文件：{}",absolutePath);
		Stopwatch timeWatch = Stopwatch.createStarted();
		final List<CpeUserInfo> cpeUserInfoList = new ArrayList<CpeUserInfo>();
		final Pop2BossCpeInstallResponse reponse = new Pop2BossCpeInstallResponse();
		try {
			
			/*
			 * 解析BOSS提供的CPE开户文件
			 */
			Files.readLines(new File(absolutePath), Charset.forName(charsetName), new LineProcessor() {
				public Long lineNum = new Long(0);
				private Pop2BossCpeInstallResponse returnReponse;
				private CpeUserInfo cpeUserInfo;
				@Override
				public boolean processLine(String line) throws IOException {
					if(lineNum++ == 0){
						returnReponse = cpeInstallDataHandler.analyseFirstLine(line, config);
						reponse.setCreateTime(returnReponse.getCreateTime());
						reponse.setCpeCount(returnReponse.getCpeCount());
						reponse.setResultFlag(returnReponse.getResultFlag());
					}else {
						cpeUserInfo = cpeInstallDataHandler.analyse(line, config);
					}
					if (cpeUserInfo != null) {
						cpeUserInfoList.add(cpeUserInfo);
					}
					return true;
				}

				@Override
				public List<String> getResult() {
					return null;
				}
			});
			
			if(!Strings.isNullOrEmpty(reponse.getCreateTime()) && 
					cpeUserInfoList != null && cpeUserInfoList.size() > 0){//将处理后的结果存入BOSS指定的FTP或者本地文件系统上
				/*
				 * 批量记录CPE开户信息
				 */
				try{
					cpeManagerService.batchUpdate(cpeUserInfoList,fileName);
					reponse.setResultFlag(String.valueOf(PopConstant.ANALYSIS_SUCCESS));
				}catch(Exception e){
					String msg = e.getMessage();
					//重复插入，忽略错误
					if(!msg.contains("Duplicate entry"))
					{
						log.error(e.getMessage());
						reponse.setResultFlag(String.valueOf(PopConstant.ANALYSIS_FAILURE));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			reponse.setResultFlag(String.valueOf(PopConstant.ANALYSIS_FAILURE));
		} finally {
			log.info("文件{}读取完毕,共计{}毫秒",absolutePath,timeWatch.elapsed(TimeUnit.MILLISECONDS));
			timeWatch = Stopwatch.createStarted();
			//writeFeedbackFile(reponse,fileName);
			if (String.valueOf(PopConstant.ANALYSIS_SUCCESS).equals(reponse.getResultFlag())) {
				if (deleteFile) {
					new File(absolutePath).delete(); // 删除处理完成的文件
					log.info("删除文件{},共计{}毫秒",absolutePath,timeWatch.elapsed(TimeUnit.MILLISECONDS));
				}else {
					try {
						FileUtils.moveFileToDirectory(new File(absolutePath), new File(processedFileDirectory), true);
						log.info("将解析完的CPE开户数据文件：" + absolutePath + "移入备份目录: " + processedFileDirectory + "成功！");
					} catch (IOException e) {
						log.error("将解析完的CPE开户数据文件：" + absolutePath + "移入备份目录: " + processedFileDirectory + "失败！",e);
					}
				}
			}else{
				try {
					log.error("将解析异常的CPE开户数据文件：" + absolutePath + "移入异常目录: " + errorFileDirectory + "！");
					FileUtils.moveFileToDirectory(new File(absolutePath), new File(errorFileDirectory), true);
				} catch (IOException e) {
					log.error("将解析异常的CPE开户数据文件：" + absolutePath + "移入异常目录: " + errorFileDirectory + "失败！",e);
				}
			}
			lock.unlock();
		}
	}
	
	
	/**
	 * 在Local上生成反馈数据文件
	 * 该方法现在未使用
	 * hpa 
	 * @param cpeStatusChangeReponseList
	 */
	private void writeFeedbackFile(Pop2BossCpeInstallResponse reponse){
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
		try {
			line = reponse.getCreateTime() + "," 
					+ reponse.getCpeCount() + "," 
					+ reponse.getResultFlag();
			Files.write(line, new File(fileName), Charset.forName(charsetName));
		} catch (IOException e) {
			log.error(e);
		}
		
	}
	
	/**
	 * 在Local上生成反馈数据文件
	 * 该方法现在未使用
	 * hpa 
	 * @param cpeStatusChangeReponseList
	 */
	private void writeFeedbackFile(Pop2BossCpeInstallResponse reponse,String fileName){
		fileName = feedbackFileDirectory + File.separator 
				+ fileName
				+ "."
				+ feedbackFileSuffix;
		if (!new File(fileName).exists()) {
			try {
				new File(fileName).createNewFile();
			} catch (IOException e) {
					log.error("创建反馈文件失败！",e);
			}
		}
		
		String line;
		try {
			line = reponse.getCreateTime() + "," 
					+ reponse.getCpeCount() + "," 
					+ reponse.getResultFlag();
			Files.write(line, new File(fileName), Charset.forName(charsetName));
		} catch (IOException e) {
			log.error(e);
		}
		
	}
	
	/**
	 * 该方法现在未使用
	 * hpa 
	 * @param value
	 * @param fixedLength
	 * @return
	 */
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
