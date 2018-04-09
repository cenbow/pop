package com.ailk.bdx.pop.adapter.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.bean.HdfsConfig;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;
import com.ailk.bdx.pop.adapter.util.Configure;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

/**
 * 按照行读文件类
 *
 * @author guoyj
 *
 */
public class LineHdfsReader {
	private FSDataInputStream fs;
	private BaseConfig config;
	private String charsetName;
	private static final boolean USE_BUFFER_MODULE;// 是否需要缓存模块
	private IDataHandle dataHandle;
	private IDataBuffer dataBuffer;
	private static final Logger log = LogManager.getLogger();
	private String currentDst;
	private String filePath;
	
	static {
		USE_BUFFER_MODULE = Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
	}

	public LineHdfsReader(){
	}
 
	public String getCurrentDst() {
		return currentDst;
	}

	public void setCurrentDst(String currentDst) {
		this.currentDst = currentDst;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FSDataInputStream getFs() {
		return fs;
	}

	public void setFs(FSDataInputStream fs) {
		this.fs = fs;
	}

	public void setConfig(BaseConfig config) {
		this.config = config;
	    HdfsConfig hdfsConfig = (HdfsConfig)config;
	    charsetName = hdfsConfig.getCharsetName();
	}

	public void setDataHandle(IDataHandle dataHandle) {
		this.dataHandle = dataHandle;
	}

	public void setDataBuffer(IDataBuffer dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public void readByLine(String fileName) {
		BufferedReader bis = null;
		FSDataInputStream fis=null;
		FileSystem fsNew = null;
		try{
			long beg= System.currentTimeMillis(); 
			Configuration conf = new Configuration();
			fsNew = FileSystem.get(URI.create(currentDst), conf);
			fis = fsNew.open(new Path(filePath));
			bis = new BufferedReader(new InputStreamReader(fis,charsetName));
			if (USE_BUFFER_MODULE) {
				String line;  
				while ((line = bis.readLine()) != null) {
					try{
						dataBuffer.push(new Message(line,config));
					}catch(Exception e){
							log.error("处理数据异常:"+line);
					}
				 }         

			}else{
				String line; 
				while ((line = bis.readLine()) != null) {  
					try{
						dataHandle.handle(new Message(line,config));
					 }catch(Exception e){
						log.error("处理数据异常:"+line);
					}
				 }   
			}
			
			long end= System.currentTimeMillis(); 
			log.info("+++fileName:"+fileName+"-读取完毕!总计耗时:"+(end-beg)/1000+"s");
		}catch (IOException e) {
			log.error("从hdfs读数据出现异常,文件名:"+fileName+".",e);
			e.printStackTrace();
		} finally {
			try {
				if(bis !=null){
					bis.close();
				}
				if(fis != null){
					fis.close();
				} 
			} catch (IOException e) {
				log.error("关闭流异常:"+e);
			} 
		}
	}
 
	
	public static void main(String[] args){
		final Splitter splitterComma = Splitter.on("\t");
		File file = new File("F:\\A_F-SCA-CR1.0-BH-1001-2014062014-0556.AVL");
		try {
			Files.readLines(file, Charset.forName("utf-8"), new LineProcessor() {
				@Override
				public boolean processLine(String line) throws IOException {
					List<String> indexSplit = splitterComma.splitToList(line);
					int j=3;
					for(int i=0;i<indexSplit.size();i++){
						System.out.println(j+"---"+indexSplit.get(i));
						j++;
					} 
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
	
}
