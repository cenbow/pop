package com.ai.bdx.pop.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//import jodd.io.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.hadoop.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.jedis.JedisClientUtil;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.ftp.PropUtil;
import com.ai.bdx.pop.wsclient.util.StringUtil;
import com.google.common.base.Strings;

/**
 * CPE定时变更客户imsi信息，
 * @author 
 *
 */
public class CpeChangeService implements Serializable{

	private static final long serialVersionUID = 4513507713639314486L;
	private static Logger log = 
			LogManager.getLogger(CpeChangeService.class);
	private static String proFilePath;
    private static String proFilePath_bak;
    private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	static{
		proFilePath = PropUtil.getProp("SRC_IMSICHN_PATH", "/config/aibi_pop/ftp.properties");
		File dir=new File(proFilePath);
		if(!dir.exists()){
			dir.mkdir();
		}
		//成功后转移文件路径
		proFilePath_bak = PropUtil.getProp("BAK_IMSICHN_PATH", "/config/aibi_pop/ftp.properties");
		File bakDir=new File(proFilePath_bak);
		if(!bakDir.exists()){
			bakDir.mkdir();
		}
	}
	
	//修正redis中的数据
	private void changeRedisImsiInfo(String proNo,String oldMsi,String newMsi) throws Exception
	{
		//修改锁网关系
		String oldKey=PopConstant.REDIS_UNL_PREFIX+oldMsi;
		String newKey=PopConstant.REDIS_UNL_PREFIX+newMsi;
		Set<String> tmpset = JedisClientUtil.smembers(oldKey);
		if(tmpset!=null&&tmpset.size()>0)
		{
			for(String s:tmpset)
			{
				JedisClientUtil.addKeyValues(newKey, s);
			}
		}
		JedisClientUtil.delKeyValue(oldKey);
		//修改上网位置统计
		oldKey = PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX+oldMsi;
		newKey = PopConstant.REDIS_CPE_USERLOCATION_STATISTICS_PREFIX+newMsi;
		tmpset = JedisClientUtil.zrevrange(oldKey, 0, -1);
		if(tmpset!=null&&tmpset.size()>0)
		{
			for(String s:tmpset)
			{
				
				double score = JedisClientUtil.zscore(oldKey, s);
				JedisClientUtil.zincrby(newKey, score, s);
			}
		}
		JedisClientUtil.delKeyValue(oldKey);
	}
	
	private void changeDbImgiInfo(String proNo,String oldMsi,String newMsi) throws Exception
	{
		/*BasicDataSource ds = new BasicDataSource();
		 ds.setDriverClassName("com.mysql.jdbc.Driver");
		    ds.setUrl("jdbc:mysql://localhost:3306/mcddev");
		    ds.setUsername("mcd");
		    ds.setPassword("mcd");
		    ds.setInitialSize(50);
		    ds.setMaxActive(100);
		    ds.setMaxIdle(30);
		    ds.setMaxWait(10000);
		    jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(ds);*/
		String[] sqls = {
				String.format("update cpe_user_info set subsid='%s',oldsubsid='%s',subsid_change_time='%s' where product_no='%s' and subsid='%s' ",newMsi,oldMsi,new java.sql.Date(new Date().getTime()),proNo,oldMsi),
				String.format("update cpe_user_lock_rel set subsid='%s' where product_no='%s' and subsid='%s' ",newMsi,proNo,oldMsi)
			};
			int[] result = jdbcTemplate.batchUpdate(sqls);
	}
	//修正imsi
	private void changeImsi(String prono,String oldMsi,String newMsi) throws Exception
	{
		changeDbImgiInfo(prono, oldMsi, newMsi);
		changeRedisImsiInfo(prono, oldMsi, newMsi);
	}
	
	private void handleError(File file,List<String> errList)
	{
		PrintWriter out = null;
		if(!errList.isEmpty())
		{
			try {
				String fName = file.getName();
				fName = proFilePath+fName.substring(0,fName.indexOf(".")) +".err";
				out = new PrintWriter(new File(fName));
				for(String s:errList)
				{
				   out.println(s);
				   out.flush();	
				}
			} catch (Exception e) {
				// TODO: handle exception
			   log.error(e.toString());
			}
			finally
			{
				try {
					if(out!=null)
					{
						out.close();
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	
	private void handleOver(File file)
	{
		if(!proFilePath_bak.endsWith(File.separator)) {
			proFilePath_bak += File.separator;
		}
		File bakDir=new File(proFilePath_bak);
		try {
			  FileUtils.copyFileToDirectory(file, bakDir);
			  FileUtils.deleteQuietly(file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void processFile(File file)
	{
		if(file.isDirectory())
		{
			return;
		}
		long t = System.currentTimeMillis();
		BufferedReader in = null;
		String line = null;
		List<String> errList = new ArrayList<String>(); 
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			//去掉第一行数据1111
			
			in.readLine();
			while((line=in.readLine())!=null)
			{
				String[] data = StringUtils.split(line,',');
				String prono = data[0];
				String oldMsi= data[2];
				String newMsi= data[3];
				try {
					changeImsi(prono,oldMsi,newMsi);
				} catch (Exception e) {
					// TODO: handle exception
					log.error(CpeChangeService.class.getName()+e.toString());
					errList.add(line);
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error(CpeChangeService.class.getName()+e.toString());
		}
		finally
		{
			try {
				if(null!=in)
				{
					in.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			handleOver(file);
			String msg = "处理文件:"+file.getName()+"完毕!";
			if(!errList.isEmpty())
			{
				handleError(file,errList);
				msg+=" 出现错误,请检查!";
			}
		    t = System.currentTimeMillis()-t;
		    msg+=" 耗时:"+t*0.001+"秒!";
			log.info(msg);
		}
	}
	
	public void runTask() {
		try{
			log.info("开始本轮扫描imsi变更数据文件... ...");
			if(Strings.isNullOrEmpty(proFilePath)){
				log.error("请source-config.xml文件中设置fileDirectory文件路径");
				return;
			}
			if(!proFilePath.endsWith(File.separator)) {
				proFilePath += File.separator;
			}
			
			if (!new File(proFilePath).exists()) {
				log.error("source-config.xml文件中配置的fileDirectory：" + proFilePath + "不存在！");
			}
			
			File[] listFiles=new File(proFilePath).listFiles();
			if(listFiles != null && listFiles.length > 0){
				for(int i = 0;i < listFiles.length;i++){
                     log.info("开始处理文件:"+listFiles[i].getName());
				     processFile(listFiles[i]);
				}
			}else {
				log.debug("本轮扫描未发现数据文件!");
			}
		}catch(Exception e){
			log.error("",e);
		}finally{
			
		}
	}
	public static void main(String[] args) {
		BasicDataSource ds = new BasicDataSource();
		 ds.setDriverClassName("com.mysql.jdbc.Driver");
		    ds.setUrl("jdbc:mysql://localhost:3306/cpe");
		    ds.setUsername("root");
		    ds.setPassword("root");
		    ds.setInitialSize(50);
		    ds.setMaxActive(100);
		    ds.setMaxIdle(30);
		    ds.setMaxWait(10000);
		    JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(ds);
		String sql = String.format("update cpe_user_info set subsid='%s',oldsubsid='%s',subsid_change_time='%s' where product_no='%s' and subsid='%s' ","2759652855","2705248002774",new java.sql.Date(new Date().getTime()),"13657215945","2705248002774");
		
		try {
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			if(e.getMessage().contains("Duplicate entry"))
			{
				
			}
			else
			{
				 System.out.println(e.toString());
			}
			
		}
	}
	
	
	
}