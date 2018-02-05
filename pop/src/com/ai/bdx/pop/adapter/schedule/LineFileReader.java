package com.ai.bdx.pop.adapter.schedule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.Pop2BossCpeInstallResponse;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.ftp.ApacheFtpUtil;
import com.ai.bdx.pop.util.ftp.FtpConfig;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.google.common.base.Joiner;
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
	private FtpConfig config;
	
	ApacheFtpUtil apacheFtp = null;
	private String charsetName;
	private boolean deleteFile;
	private static final boolean USE_BUFFER_MODULE;// 是否需要缓存模块
	private String split = ",";
	private static String dbType;
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	static {
		USE_BUFFER_MODULE = Boolean.valueOf(Configure.getInstance().getProperty("USE_BUFFER_MODULE"));
		dbType = Configure.getInstance().getProperty("DBTYPE");
	}

	public LineFileReader() {
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public void setApacheFtp(ApacheFtpUtil apacheFtp) {
		this.apacheFtp = apacheFtp;
	}

	public void setConfig(FtpConfig config) {
		this.config = config;
		if(!Strings.isNullOrEmpty(config.getSPLIT())){
			split = config.getSPLIT();
		}
		charsetName = config.getFTP_ENCODE();
		String is_del_local = config.getISDELETE_REMOTE();
		if(StringUtil.isNotEmpty(is_del_local) && "1".equals(is_del_local)){
			deleteFile = true;
		}else {
			deleteFile = false;
		}
		
	}

	public void readByLine() {
		
		log.info("处理文件：{}",file.getName());
		Stopwatch timeWatch = Stopwatch.createStarted();
		final List<CpeUserInfo> cpeUserInfoList = new ArrayList<CpeUserInfo>();
		final Pop2BossCpeInstallResponse pop2BossCpeInstallBackInfo = new Pop2BossCpeInstallResponse();
		String[] splitList;
		try {
			if (USE_BUFFER_MODULE) {
				Files.readLines(file, Charset.forName(charsetName), new LineProcessor() {
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
				Files.readLines(file, Charset.forName(charsetName), new LineProcessor() {
					CpeUserInfo cpeUserInfo;
					@Override
					public boolean processLine(String line) throws IOException {
						//dataHandle.handle(new Message(line, config));
						String[] splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(line,split);
						if(splitList.length == 2){
							pop2BossCpeInstallBackInfo.setCreateTime(splitList[0]);
							pop2BossCpeInstallBackInfo.setCpeCount(splitList[1]);
						}else if(splitList.length >= 7){
							cpeUserInfo = new CpeUserInfo(); 
							cpeUserInfo.setProductNo(splitList[0]);
							cpeUserInfo.setSubsid(splitList[1]);
							cpeUserInfo.setNetType(splitList[2]);
							cpeUserInfo.setCountryName(splitList[3]);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							try {
								cpeUserInfo.setCreateTime(new Timestamp(sdf.parse(splitList[4]).getTime()));
							} catch (ParseException e) {
								log.error("解析" + file.getName() + "文件失败." + e);
							}
							cpeUserInfo.setPlanCode(splitList[5]);
							
							cpeUserInfoList.add(cpeUserInfo);
						}
						return true;
					}

					@Override
					public List<String> getResult() {
						return null;
					}
				});
			}
			
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO CPE_USER_INFO(" +
					"PRODUCT_NO," +
					"SUBSID," +
					"NET_TYPE," +
					"COUNTRY_NAME," +
					"CREATE_TIME," +
					"PLAN_CODE," +
					"USER_STATUS," +
					"NET_LOCK_FLAG," +
					"BUSI_TYPE," +
					"OP_STATUS)");
			sql.append(" VALUES(?,?,?,?,?,?,");
			sql.append(PopConstant.USER_STATUS_NORMAL + ",");
			sql.append(PopConstant.NET_LOCK_FLAG_UNLOCK + ",");
			sql.append(PopConstant.BUSI_TYPE_NORMAL + ",");
			sql.append(PopConstant.OP_STATUS_OPENING_ACCOUNT + ")");
			
			jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){

				@Override
				public int getBatchSize() {
					return cpeUserInfoList.size();
				}

				@Override
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					 ps.setString(1, cpeUserInfoList.get(i).getProductNo());
					 ps.setString(2, cpeUserInfoList.get(i).getSubsid());
					 ps.setString(3, cpeUserInfoList.get(i).getNetType());
					 ps.setString(4, cpeUserInfoList.get(i).getCountryName());
					 ps.setTimestamp(5, cpeUserInfoList.get(i).getCreateTime());
					 ps.setString(6, cpeUserInfoList.get(i).getPlanCode());
				}
				
			});
			pop2BossCpeInstallBackInfo.setResultFlag(String.valueOf(PopConstant.ANALYSIS_SUCCESS));
			
		} catch (IOException e) {
			log.error("",e);
			pop2BossCpeInstallBackInfo.setResultFlag(String.valueOf(PopConstant.ANALYSIS_FAILURE));
		} finally {
			log.info("文件{}读取完毕,共计{}毫秒",file.getName(),timeWatch.elapsed(TimeUnit.MILLISECONDS));
			timeWatch = Stopwatch.createStarted();
			if (deleteFile) {
				file.delete(); // 临时文件删掉
				//FileUtils.deleteQuietly(file);
				log.info("删除文件{},共计{}毫秒",file.getName(),timeWatch.elapsed(TimeUnit.MILLISECONDS));
			}
			File file = InstallCallback(pop2BossCpeInstallBackInfo);
			try {
				apacheFtp.upload(new FileInputStream(file), file.getName());
			} catch (Exception e) {
				log.error("",e);
			}
		}
	}

	/**
	 * CPE用户开户回调方法
	 */
	private File InstallCallback(Pop2BossCpeInstallResponse pop2BossCpeInstallBackInfo){
		String backPath = config.getLOCAL_PATH() + File.separator + "back";
		String backFileName = file.getName() + config.getBACKFILESUFFIX();
		File backFile = new File(backPath + File.separator + backFileName);
		//创建文件
		if(!backFile.exists()) {
			try {
				backFile.createNewFile();
			} catch (IOException e) {
				log.error("",e);
			}
		}
		List<String> list = new ArrayList<String>();
		list.add(pop2BossCpeInstallBackInfo.getCreateTime());
		list.add(pop2BossCpeInstallBackInfo.getCpeCount());
		list.add("1");
		try {
			Files.write(Joiner.on(",").join(list), backFile, Charset.forName(config.getFTP_ENCODE()));
		} catch (IOException e) {
			log.error("",e);
		}
		return backFile;
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

