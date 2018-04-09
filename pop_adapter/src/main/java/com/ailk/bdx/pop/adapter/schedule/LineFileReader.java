package com.ailk.bdx.pop.adapter.schedule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.bean.FileConfig;
import com.ailk.bdx.pop.adapter.bean.FtpConfig;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.bean.SftpConfig;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;
import com.ailk.bdx.pop.adapter.model.SessionInfo;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

/**
 * 按照行读文件类
 *
 * @author guoyj
 *
 */
public class LineFileReader {
	private static final Logger log = LogManager.getLogger();
	private File file;
	private BaseConfig config;
	private String charsetName;
	private boolean deleteFile;
	private static final boolean USE_BUFFER_MODULE;// 是否需要缓存模块
	private IDataHandle dataHandle;
	private IDataBuffer dataBuffer;
	
	static {
		USE_BUFFER_MODULE = Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
	}

	public LineFileReader() {
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
		if (config instanceof FileConfig) {
			FileConfig fileCfg = (FileConfig) config;
			charsetName = fileCfg.getCharsetName();
			deleteFile = fileCfg.isDeleteLocal();
		} else if (config instanceof FtpConfig) {
			FtpConfig ftpCfg = (FtpConfig) config;
			charsetName = ftpCfg.getCharsetName();
			deleteFile = ftpCfg.isDeleteLocal();
		} else if(config instanceof SftpConfig){
			SftpConfig SftpConfig = (SftpConfig) config;
			charsetName = SftpConfig.getCharsetName();
			deleteFile = SftpConfig.isDeleteLocal();
		}
	}

	public void setDataHandle(IDataHandle dataHandle) {
		this.dataHandle = dataHandle;
	}

	public void setDataBuffer(IDataBuffer dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public void readByLine() {
		
		log.info("处理文件：{}",file.getName());
		Stopwatch timeWatch = Stopwatch.createStarted();
		try {
			if (USE_BUFFER_MODULE) {
				Files.readLines(file, Charset.forName(charsetName), new LineProcessor() {
					@Override
					public boolean processLine(String line) throws IOException {
						dataBuffer.push(new Message(line, config));
						return true;
					}

					@Override
					public List<String> getResult() {
						return null;
					}
				});
			} else {
				Files.readLines(file, Charset.forName(charsetName), new LineProcessor() {
					@Override
					public boolean processLine(String line) throws IOException {
						dataHandle.handle(new Message(line, config));
						return true;
					}

					@Override
					public List<String> getResult() {
						return null;
					}
				});
			  
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.info("文件{}读取完毕,共计{}毫秒",file.getName(),timeWatch.elapsed(TimeUnit.MILLISECONDS));
			timeWatch = Stopwatch.createStarted();
			if (deleteFile) {
				file.delete(); // 临时文件删掉
				//FileUtils.deleteQuietly(file);
				log.info("删除文件{},共计{}毫秒",file.getName(),timeWatch.elapsed(TimeUnit.MILLISECONDS));
			}
		}
	}

	public static void main(String[] args) {
		final Splitter splitterComma = Splitter.on("\t");
		final Splitter splitterCommamm = Splitter.on(",");
		
		File file = new File("D:\\ftpdown\\A9401320150810144200.AVL");
		Stopwatch timeWatch = Stopwatch.createStarted();
		
		
		try {
			Files.readLines(file, Charset.forName("utf-8"), new LineProcessor() {
				int j = 1;
				@Override
				public boolean processLine(String line) throws IOException {
					List<String> indexSplit = splitterCommamm.splitToList(line);
					
						
						
							j++;
							System.out.println(j);
							//log.debug("手机号:{}",indexSplit.get(2));
					
					return true;
				}

				@Override
				public List<String> getResult() {
					return null;
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			file.delete();
			log.debug("删除文件耗时{}秒-------------:",timeWatch.elapsed(TimeUnit.SECONDS));
		}
	}

}

