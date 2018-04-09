package com.ailk.bdx.pop.adapter.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.bean.BaseConfig;
import com.ailk.bdx.pop.adapter.bean.Content;
import com.ailk.bdx.pop.adapter.bean.FileConfig;
import com.ailk.bdx.pop.adapter.bean.FtpConfig;
import com.ailk.bdx.pop.adapter.bean.HdfsConfig;
import com.ailk.bdx.pop.adapter.bean.SftpConfig;
import com.ailk.bdx.pop.adapter.bean.SocketConfig;
import com.ailk.bdx.pop.adapter.bean.SourceConfig;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Xml配置解析工具
 * @author imp
 *
 */
public class XmlConfigureUtil {
	public static final String FILE = "file";
	public static final String FTP = "ftp";
	public static final String SFTP = "sftp";
	public static final String SOCKET = "socket";
	public static final String HDFS = "hdfs";

	private static final Logger log = LogManager.getLogger();
	private static XmlConfigureUtil configure = new XmlConfigureUtil();
	private final HashMultimap<String, BaseConfig> typeMultimap = HashMultimap.create();
	private static Map<String,String>  laciCacheDara;
	
	private SourceConfig sourceConfig = null;
	private String configFile = "";
	private String LacDataFile = "";

	private XmlConfigureUtil() {
		
		configFile = "/province/{PROVICE}/source-config.xml";
		String path = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
		
		try {
			path = java.net.URLDecoder.decode(path,"utf-8");
 		} catch (UnsupportedEncodingException e1) {
 		}
		String provice = Configure.getInstance().getProperty("PROVINCE");
		log.info("加载"+provice+"<source-config.xml>文件开始....");
		if (!Strings.isNullOrEmpty(provice)) {
			configFile = configFile.replace("{PROVICE}", provice.trim().toLowerCase());
		} else {
			configFile = configFile.replace("{PROVICE}", "default");
		}
		laciCacheDara =  Maps.newHashMap();
		initConfigs(configFile);
		log.info("加载"+provice+"<source-config.xml>文件成功....");
		
		
		/*if(Boolean.valueOf(Configure.getInstance().getProperty("IS_NEED_CONVERT_LACICI_CACHE"))){
			log.info("加载LACCI->逻辑编码至内存开始....");
			configFile =  Configure.getInstance().getProperty("CONVERT_LACICI_FILE_PATH");
			if(!Strings.isNullOrEmpty(configFile)){	
				LacDataFile = configFile;
				if (!Strings.isNullOrEmpty(provice)) {
					LacDataFile = LacDataFile.replace("{PROVICE}", provice.trim().toLowerCase());
				}else{
					LacDataFile = LacDataFile.replace("{PROVICE}", "default");
				}
				initLacData(path+LacDataFile);
			}
			log.info("加载LACCI->逻辑编码至内存成功....");
		}*/
		
		
	}

	 
	public static XmlConfigureUtil getInstance() {
		return configure;
	}

	private synchronized void initConfigs(String fileName){
		InputStream configStream = getClass().getResourceAsStream(fileName);
		log.debug("Load fileName:{}\r\n",configFile);
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("file", FileConfig.class);
		xstream.alias("ftp", FtpConfig.class);
		xstream.alias("sftp", SftpConfig.class);
		xstream.alias("socket", SocketConfig.class);
		xstream.alias("hdfs", HdfsConfig.class);
		xstream.alias("sourceConfig", SourceConfig.class);
		xstream.alias("content", Content.class);
		
	 
		try {
			sourceConfig = (SourceConfig) xstream.fromXML(configStream);
			List<BaseConfig> items = getAllConfigItems();
			if(items != null && !items.isEmpty()){
				for (int i = 0, n = items.size(); i < n; i++) {
					BaseConfig obj = items.get(i);
					if (obj instanceof FileConfig) {
						FileConfig fileCfg = (FileConfig)obj;
						typeMultimap.put(FILE, fileCfg);
					} else if (obj instanceof FtpConfig) {
						FtpConfig ftpCfg = (FtpConfig)obj;
						typeMultimap.put(FTP, ftpCfg);
					} else if(obj instanceof SftpConfig){
						SftpConfig sftpCfg = (SftpConfig) obj;
						typeMultimap.put(SFTP, sftpCfg);
					} else if (obj instanceof SocketConfig) {
						SocketConfig socketCfg = (SocketConfig)obj;
						List<Content> list = socketCfg.getContents();
					    Map<String,Content> kvContent =	socketCfg.getKvContent();
						if(null != list && list.size() !=0 ){
						    for(Content c:list){
								kvContent.put(c.getId(), c);
							}
						}
					    typeMultimap.put(SOCKET, socketCfg);
					}else if (obj instanceof HdfsConfig) {
						HdfsConfig hdfsConfig = (HdfsConfig)obj;
						typeMultimap.put(HDFS, hdfsConfig);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("parameter file not found:" + fileName + "\r\n");
		}
	}

	
	private synchronized void initLacData(String fileName){
		
		String  LacCiId="";
	    String  LogicId="";
		
		if(typeMultimap != null){
			Set<String> keys = typeMultimap.keySet();
			for(String key : keys){
				if(XmlConfigureUtil.FILE.equals(key)){ //本地文件处理
					Set<BaseConfig> values = typeMultimap.get(key);
					for(BaseConfig config : values){
						LacCiId = config.getLacCiId();
						LogicId = config.getLogicId();
					}
				}else if(XmlConfigureUtil.FTP.equals(key)){ //ftp处理方式
					Set<BaseConfig> values = typeMultimap.get(key);
					for(BaseConfig config : values){
						LacCiId = config.getLacCiId();
						LogicId = config.getLogicId();
					}
				}else if(XmlConfigureUtil.SFTP.equals(key)){ //sftp处理方式
					Set<BaseConfig> values = typeMultimap.get(key);
					for(BaseConfig config : values){
						LacCiId = config.getLacCiId();
						LogicId = config.getLogicId();
					}
				}else if(XmlConfigureUtil.HDFS.equals(key)){//HDFS
					Set<BaseConfig> values = typeMultimap.get(key);
					for(BaseConfig config : values){
						LacCiId = config.getLacCiId();
						LogicId = config.getLogicId();
					}
				}else if(XmlConfigureUtil.SOCKET.equals(key)){ //socket处理方式
					Set<BaseConfig> values = typeMultimap.get(key);
					for(BaseConfig config : values){
						LacCiId = config.getLacCiId();
						LogicId = config.getLogicId();
					}
				}
			}
		}
		   final String finalLogicId = LogicId;
		   final String finalLacCiId = LacCiId;
		try {
			File file = new File(fileName);
			Files.readLines(file, Charset.forName("utf-8"), new LineProcessor() {
				public boolean processLine(String line) throws IOException {
						String[] messages = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ",");
						String[] socketLogicIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(finalLogicId, ",");
						StringBuffer socketLogicIdValue = new StringBuffer();
						for(int i=0;i<socketLogicIds.length;i++){
							int index = Integer.parseInt(socketLogicIds[i]);
							socketLogicIdValue.append(messages[index].equalsIgnoreCase("null")?"":messages[index]);
						}
						
						StringBuffer socketLacCiIdKey = new StringBuffer();
						int socketLacCiIdIndex = Integer.parseInt(finalLacCiId);
						socketLacCiIdKey.append(messages[socketLacCiIdIndex]);
						laciCacheDara.put(socketLacCiIdKey.toString(), socketLogicIdValue.toString());
						return true;
				}

				public List<String> getResult() {
					return null;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("parameter file not found:" + fileName + "\r\n");
		}
		
	}
	
	/**
	 * 获取所有的配置项
	 * @param type
	 * @return
	 */
	public List<BaseConfig> getAllConfigItems() {
		return sourceConfig.getItems();
	}

	/**
	 * 获取所有的分类配置项
	 * @return
	 */
	public HashMultimap<String, BaseConfig> getMultConfigItems(){
		return typeMultimap;
	}

	/**
	 * 获取指定类型的配置项
	 * @param type
	 * @return
	 */
	public Set<BaseConfig> getConfigItemsByType(String type){
		return typeMultimap.get(type);
	}

	/**
	 * 获取某具体配置项
	 * @param type
	 * @param name
	 * @return
	 */
	public BaseConfig getConfigItem(String type,String name){
		BaseConfig target = null;
		Set<BaseConfig> typeItems = getConfigItemsByType(type);
		for(BaseConfig baseConfig : typeItems){
			if(name.equalsIgnoreCase(baseConfig.getName())){
				target = baseConfig;
			}
		}
		return target;
	}
	
	
	public static synchronized Map<String,String> getLaciCache(){
		return  laciCacheDara;
	}
}
