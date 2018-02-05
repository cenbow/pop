package com.ai.bdx.pop.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.ai.bdx.pop.bean.ImportCpeLacCiBean;
import com.ai.bdx.pop.dao.BatchStationDao;
import com.ai.bdx.pop.util.PopConstant;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.util.TimeUtil;

public class BatchStationDaoImpl implements BatchStationDao {
	
	private JdbcTemplate jdbcTemplate;
	private static Logger log = LogManager.getLogger();
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	/**
	 * 根据用户id查询用户的策略号（带宽与之对应的那个）
	 */
		public List queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion{
			String sql="select * from cpe_user_info u join dim_net_type_strategy_rel r on u.net_type=r.net_type where u.subsid=?";
			List<Map<String,Object>> list = null;
			System.out.println(subsid);
			try {
				list = jdbcTemplate.queryForList(sql, new String[]{subsid});
			} catch (DataAccessException e) {
				e.printStackTrace();
				log.error("CPE用户"+subsid+"查询用户策略号，Mysql数据库操作错误");
				throw new MysqlDataAccessExcetion("查询用户策略号，Mysql数据库操作错误");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return list;
		}
	/**
	 * 将基站批量割接结果插入或变更日志表中
	 * 该方法未使用
	 */
	@Override
	public int[] insertORupdateUserResetLog(final List<BatchStationBean> list,final List<String> success) throws MysqlDataAccessExcetion,Exception{
		String sql="insert into cpe_user_reset_log(subsid,product_no,lac_ci_old,lac_ci_new,reset_flag,reset_time) values(?,?,?,?,?,?)";
		int[] updates=null;
		if(list!=null){
			updates=new int[list.size()];
			
				if(list.size()==1){
						Object[] args=new Object[6];
						
						args[0]=list.get(0).getSubsid();
						args[1]=list.get(0).getProductNo();
						args[2]=list.get(0).getUserlocationOld();
						args[3]=list.get(0).getUserlocationNew();
						if(success.size()>0){
							args[4]=1; //1表示成功
						}else{
							args[4]=0;//0表示失败
						}
						args[5]=new Date();
						
					try {
						updates[0]=jdbcTemplate.update(sql, args);
					}catch (DataAccessException e) {
						System.out.println("基站割接参数重置失败了:Mysql数据库访问异常");
						log.error("基站割接参数重置失败了:Mysql数据库访问异常");
						e.printStackTrace();
						throw new MysqlDataAccessExcetion("基站割接参数重置失败了：Mysql数据库访问异常"+args[1]);
					}catch (Exception e) {
						System.out.println("基站割接数重置失败了"+args[1]);
						log.error("基站割接参数重置失败了"+args[1]);
						e.printStackTrace();
						throw new Exception("基站割接参数重置失败了"+args[1]);
					}
				}else{	
					try{
							System.out.println("基站割接插入日志"+sql);
							updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
							
							@Override
							public int getBatchSize() {
							
								return 	list.size();
							}
	
							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								ps.setString(1, list.get(i).getSubsid());
								ps.setString(2, list.get(i).getProductNo());
								ps.setString(3, list.get(i).getUserlocationOld());
								ps.setString(4, list.get(i).getUserlocationNew());
								ps.setInt(5, 0);
								ps.setString(6, TimeUtil.getDateString(new Date(), "yyyy-MM-dd hh:mm:ss"));
							}
							
						});
							log.info("插入："+updates.length+"行");
					}catch (DataAccessException e) {
						System.out.println("基站割接参数重置失败了:数据库访问异常");
						log.error("基站割接参数重置失败了:数据库访问异常");
						e.printStackTrace();
						throw new MysqlDataAccessExcetion("基站割接参数重置失败了：数据库访问异常");
					}catch (Exception e) {
						System.out.println("基站割接参数重置失败了:"+e.getMessage());
						log.error("基站割接参数重置失败了:"+e.getMessage());
						e.printStackTrace();
						 
					}
						
						sql="update cpe_user_reset_log set reset_flag=1 where product_no=?";
						
						System.out.println("基站割接更新日志"+sql);
						//先批量插入数据默认为0失败
					try{
						updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
	
							@Override
							public int getBatchSize() {
							
								return 	success.size();
							}
	
							@Override
							public void setValues(PreparedStatement ps, int j)
									throws SQLException {
								
								ps.setString(1, success.get(j));
								
							}
							
						});
						System.out.println("更新："+updates.length+"行");
					}catch (DataAccessException e) {
						System.out.println("基站割接参数重置失败了:Mysql数据库访问异常");
						log.error("基站割接参数重置失败了:Mysql数据库访问异常");
						e.printStackTrace();
						throw new MysqlDataAccessExcetion("基站割接参数重置失败了：Mysql数据库访问异常");
					}catch (Exception e) {
						System.out.println("基站割接参数重置失败了:"+e.getMessage());
						log.error("基站割接参数重置失败了:"+e.getMessage());
						e.printStackTrace();
						throw new Exception("基站割接参数重置失败了:"+e.getMessage());
					}
			}
			sql="select * from cpe_user_reset_log";
			List<Map<String,Object>> queryForList = jdbcTemplate.queryForList(sql);
	
			for (Map<String, Object> map : queryForList) {
				System.out.println("基站割接参数重置手机号"+map.get("product_no")+"日志状态："+map.get("reset_flag"));
			}
			
		}
		return updates;
	}
	
	/**
	 * 将基站批量割接结果,调用pcc成功的更新日志表中
	 */
		@Override
		public int[] updateUserResetLog(final List<BatchStationBean>  beanListForSuccess) throws MysqlDataAccessExcetion,Exception{
			String sql="update cpe_user_reset_log set reset_flag=1,reset_time=? where product_no=? and subsid=? and lac_ci_old=?";
			int[] updates=null;
			if(beanListForSuccess!=null){
				updates=new int[beanListForSuccess.size()];
				
					if(beanListForSuccess.size()==1){
							String phone = beanListForSuccess.get(0).getProductNo();
							String subsid = beanListForSuccess.get(0).getSubsid();
							String lac_ci_old = beanListForSuccess.get(0).getLac_ci_hex_code_old();
						try {
							updates[0]=jdbcTemplate.update(sql, new Object[]{TimeUtil.getDateString(new Date(), "yyyy-MM-dd:HH:mm:ss"),phone,subsid,lac_ci_old});
							log.info("更新："+updates[0]+"行");
						}catch (DataAccessException e) {
							log.error("基站割接参数重置失败了:Mysql数据库访问异常");
							e.printStackTrace();
							throw new MysqlDataAccessExcetion("基站割接参数重置失败了：Mysql数据库访问异常"+phone);
						}catch (Exception e) {
							log.error("基站割接参数重置失败了"+phone);
							e.printStackTrace();
							throw new Exception("基站割接参数重置失败了"+phone);
						}
					}else{	
							log.info("基站割接批量更新日志cpe_user_reset_log表："+sql);
							//先批量插入数据默认为0失败
						try{
							updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
		
								@Override
								public int getBatchSize() {
								
									return 	beanListForSuccess.size();
								}
		
								@Override
								public void setValues(PreparedStatement ps, int j)
										throws SQLException {
									ps.setString(1,TimeUtil.getDateString(new Date(), "yyyy-MM-dd:HH:mm:ss"));
									ps.setString(2, beanListForSuccess.get(j).getProductNo());
									ps.setString(3, beanListForSuccess.get(j).getSubsid());
									ps.setString(4, beanListForSuccess.get(j).getLac_ci_hex_code_old());
									
								}
								
							});
							log.info("批量更新："+updates.length+"行");
						}catch (DataAccessException e) {
							log.error("基站割接参数重置失败了:数据库访问异常");
							e.printStackTrace();
							throw new MysqlDataAccessExcetion("基站割接参数重置失败了：数据库访问异常");
						}catch (Exception e) {
							System.out.println("基站割接参数重置失败了:"+e.getMessage());
							log.error("基站割接参数重置失败了:"+e.getMessage());
							e.printStackTrace();
							throw new Exception("基站割接参数重置失败了:"+e.getMessage());
						}
				}
				
			}
			return updates;
		}
	/**
	 * 根据手机号查询重置日志中是否有记录
	 */
	@Override
	public int queryUserResetLogByMobile(String mobile) {
		String sql="select * from cpe_user_reset_log where product_no=?";
		List<Map<String,Object>> list = null;
		try {
			list = jdbcTemplate.queryForList(sql, new String[]{mobile});
		} catch (DataAccessException e) {
			e.printStackTrace();
			log.error("CPE用户"+mobile+"查询用户重置日志时，Mysql数据库操作错误");
		}
		if(list!=null){
			return list.size();
		}else{
			return 0;
		}
	}
	/**
	 * 	基站割接成功的手机号更新redis后再更新mysql的锁网信息表
	 * @throws MysqlDataAccessExcetion,Exception 
	 */
	@Override
	public int[] updateUserLockedInfo(List<BatchStationBean> beanListForSuccess) throws MysqlDataAccessExcetion,Exception {
		String sql="select lr.subsid,lr.product_no,lr.locked_lac_ci,bs.lac_ci_hex_code_new lac_ci,bs.enodeid_new,bs.lac_dec_id_new lac,bs.ci_dec_id_new ci from cpe_station_chg_import_info bs join cpe_user_lock_rel lr on bs.lac_ci_hex_code_old=lr.locked_lac_ci " +
				" where  lr.product_no=? and lr.subsid=? and lr.user_location=?";
		String updateSQL="update cpe_user_lock_rel set locked_lac_ci=?,user_location=?,station_code=? where product_no=? and subsid=? and locked_lac_ci=?";
		log.info("------基站割接成功后更新msyql锁网信息表");
		int[] update=new int[beanListForSuccess.size()];
		for(int i=0;i<beanListForSuccess.size();i++){
			try{
				//根据lac和ci计算userlocation，关联表，关联条件为lac_ci,product_no
				List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new String[]{beanListForSuccess.get(i).getProductNo(),beanListForSuccess.get(i).getSubsid(),beanListForSuccess.get(i).getUserlocationOld()});
				if(list!=null && list.size()>0){
				
					Map<String,Object> m =(Map<String,Object>) list.get(0);
					String locked_lac_ci = m.get("locked_lac_ci").toString();
					String lac_ci_new=m.get("lac_ci").toString();
					String enodeidNew=m.get("enodeid_new").toString();
					String subsid = m.get("subsid").toString();
					String mobile = m.get("product_no").toString();
					String userLocation=beanListForSuccess.get(i).getUserlocationNew();
					log.info("原userlocation=="+beanListForSuccess.get(i).getUserlocationOld()+"---->更新后的userLocation=="+userLocation);
					update[i]=jdbcTemplate.update(updateSQL, new String[]{lac_ci_new,userLocation,enodeidNew,mobile,subsid,locked_lac_ci});
				}
			
			}catch (DataAccessException e) {
				log.error("基站割接更新CPE锁网信息失败:Mysql数据库访问异常");
				e.printStackTrace();
				throw new MysqlDataAccessExcetion("基站割接更新CPE锁网信息失败：Mysql数据库访问异常");
			}catch (Exception e) {
				log.error("基站割接更新CPE锁网信息失败:"+e.getMessage());
				e.printStackTrace();
				throw new Exception("基站割接更新CPE锁网信息失败:"+e.getMessage());
			}
			
	 }
		return update;
	}
	/**
	 * 参数重置查询未重置，批量基站重置查询dao实现类
	 */
	@Override
	public List<Map<String,Object>> queryBatchStationInfosForNoReset(String startTime,
			String endTime, Integer resetFlag) {
		List<Map<String,Object>> list=null;
		String sql="";
//sql表示未重置
		sql="select * from (select lg.subsid,lg.product_no,lg.reset_flag,ip.lac_dec_id_old,ip.ci_dec_id_old,ip.lac_dec_id_new,ip.ci_dec_id_new,ip.chg_date,ip.import_time,ip.lac_ci_hex_code_old,ip.lac_ci_hex_code_new from cpe_user_reset_log lg join cpe_station_chg_import_info ip"+
					" on lg.lac_ci_old=ip.lac_ci_hex_code_old and lg.lac_ci_new=ip.lac_ci_hex_code_new) log_station join cpe_user_lock_rel lr on log_station.subsid=lr.subsid and log_station.product_no=lr.product_no and log_station.lac_ci_hex_code_old=lr.locked_lac_ci"+
					" where   user_location is not null ";

			try{
			if(!"".equals(endTime) && !"".equals(startTime)){
					sql+=" and log_station.chg_date<=? and log_station.chg_date>=? and log_station.reset_flag=0";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql, new Object[]{endTime,startTime});
			}
			if("".equals(endTime) && !"".equals(startTime)){
					sql+=" and  log_station.chg_date>=? and log_station.reset_flag=0";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql, new Object[]{startTime});
			}
			if(!"".equals(endTime) && "".equals(startTime)){
					sql+=" and  log_station.chg_date<=? and log_station.reset_flag=0";
				list=jdbcTemplate.queryForList(sql, new Object[]{endTime});
			}
			if("".equals(endTime) && "".equals(startTime)){
					sql+=" and log_station.reset_flag=0";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		
			return list;
	}
	/**
	 * 参数重置查询已重置，批量基站重置查询dao实现类
	 */
	@Override
	public List<Map<String,Object>> queryBatchStationInfosForReset(String startTime,
			String endTime, Integer resetFlag) {
		List<Map<String,Object>> list=null;
		String sql="";
				

//表示已重置
		 sql="select * from (select lg.subsid,lg.product_no,lg.reset_flag,ip.lac_dec_id_old,ip.ci_dec_id_old,ip.lac_dec_id_new,ip.ci_dec_id_new,ip.chg_date,ip.import_time,ip.lac_ci_hex_code_old,ip.lac_ci_hex_code_new from cpe_user_reset_log lg join cpe_station_chg_import_info ip"+
					" on lg.lac_ci_old=ip.lac_ci_hex_code_old and lg.lac_ci_new=ip.lac_ci_hex_code_new) log_station join cpe_user_lock_rel lr on log_station.subsid=lr.subsid and log_station.product_no=lr.product_no and log_station.lac_ci_hex_code_new=lr.locked_lac_ci"+
					" where   user_location is not null ";
			try{
			if(!"".equals(endTime) && !"".equals(startTime)){
					sql+=" and log_station.chg_date<=? and log_station.chg_date>=? and log_station.reset_flag=1";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql, new Object[]{endTime,startTime});
			}
			if("".equals(endTime) && !"".equals(startTime)){
					sql+=" and  log_station.chg_date>=? and log_station.reset_flag=1";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql, new Object[]{startTime});
			}
			if(!"".equals(endTime) && "".equals(startTime)){
					sql+=" and  log_station.chg_date<=? and log_station.reset_flag=1";
				list=jdbcTemplate.queryForList(sql, new Object[]{endTime});
			}
			if("".equals(endTime) && "".equals(startTime)){
					sql+=" and log_station.reset_flag=1";
//				sql+=" order by log_station.chg_date";
				list=jdbcTemplate.queryForList(sql);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		
			return list;
	}
	/**
	 * 上传文件存入数据库的cpe_station_chg_import_info中
	 * @throws MysqlDataAccessExcetion,Exception 
	 */
	@Override
	public int[] cpeStationChgImport(final List<ImportBatchCutoverBean> list) throws MysqlDataAccessExcetion,Exception {
		String sql="insert into cpe_station_chg_import_info(lac_ci_hex_code_old,lac_ci_dec_id_old,lac_hex_code_old,lac_dec_id_old,ci_hex_code_old,ci_dec_id_old,"+
                "lac_ci_hex_code_new,lac_ci_dec_id_new,lac_hex_code_new,lac_dec_id_new,ci_hex_code_new,ci_dec_id_new,cell_name_old_new,"+
                "cell_name_new,enodeid_old,enodeid_new,chg_date,import_time,city_name,county_name) "+
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int[] updates=null;
		if(list!=null && list.size()>0){
			updates=new int[list.size()];
			if(list.size()==1){
				ImportBatchCutoverBean cutoverBean = list.get(0);
				try{
				log.info("基站割接单条插入cpe_station_chg_import_info表"+sql);	
				updates[0]=jdbcTemplate.update(sql,new Object[]{cutoverBean.getLac_ci_hex_code_old(),cutoverBean.getLac_ci_dec_id_old(),cutoverBean.getLac_hex_code_old(),cutoverBean.getLac_dec_id_old(),
						cutoverBean.getCi_hex_code_old(),cutoverBean.getCi_dec_id_old(),cutoverBean.getLac_ci_hex_code_new(),cutoverBean.getLac_ci_dec_id_new(),cutoverBean.getLac_hex_code_new(),
						cutoverBean.getLac_dec_id_new(),cutoverBean.getCi_hex_code_new(),cutoverBean.getCi_dec_id_new(),cutoverBean.getCell_name_old(),cutoverBean.getCell_name_new(),cutoverBean.getEnodeid_old(),
						cutoverBean.getEnodeid_new(),cutoverBean.getChg_date(),cutoverBean.getImport_time(),cutoverBean.getCity_name(),cutoverBean.getCounty_name()
									});
				if(updates[0]>0){
					log.info("基站割接导入原"+list.get(0).getLac_ci_hex_code_old()+"--新"+list.get(0).getLac_ci_hex_code_new()+"----->成功");
				}
				}catch (DataAccessException e) {
					log.error("基站割接导入文件数据失败了:Mysql数据库访问异常");
					e.printStackTrace();
					throw new MysqlDataAccessExcetion("基站割接导入文件数据失败了：Mysql数据库访问异常");
				}catch (Exception e) {
					log.error("基站割接导入文件数据失败了");
					e.printStackTrace();
					throw new Exception("基站割接导入文件数据失败了");
				}
			}else{
				try{
					log.info("基站割接批量插入cpe_station_chg_import_info表"+sql);
					updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
					@Override
					public int getBatchSize() {
						return 	list.size();
					}
					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setString(1, list.get(i).getLac_ci_hex_code_old());
						ps.setString(2, list.get(i).getLac_ci_dec_id_old());
						ps.setString(3, list.get(i).getLac_hex_code_old());
						ps.setString(4, list.get(i).getLac_dec_id_old());
						ps.setString(5, list.get(i).getCi_hex_code_old());
						ps.setString(6, list.get(i).getCi_dec_id_old());
						ps.setString(7, list.get(i).getLac_ci_hex_code_new());
						ps.setString(8, list.get(i).getLac_ci_dec_id_new());
						ps.setString(9, list.get(i).getLac_hex_code_new());
						ps.setString(10, list.get(i).getLac_dec_id_new());
						ps.setString(11, list.get(i).getCi_hex_code_new());
						ps.setString(12, list.get(i).getCi_dec_id_new());
						ps.setString(13, list.get(i).getCell_name_old());
						ps.setString(14, list.get(i).getCell_name_new());
						ps.setString(15, list.get(i).getEnodeid_old());
						ps.setString(16, list.get(i).getEnodeid_new());
						ps.setString(17, list.get(i).getChg_date());
						ps.setString(18, list.get(i).getImport_time());
						ps.setString(19, list.get(i).getCity_name());
						ps.setString(20, list.get(i).getCounty_name());
						
					}
					
				});
					log.info("插入cpe_station_chg_import_info表："+updates.length+"行");
				}catch (DataAccessException e) {
					log.error("基站割接导入文件数据失败了:Mysql数据库访问异常");
					e.printStackTrace();
					throw new MysqlDataAccessExcetion("基站割接导入文件数据失败了：Mysql数据库访问异常");
				}catch (Exception e) {
					log.error("基站割接导入文件数据失败了:"+e.getMessage());
					e.printStackTrace();
					throw new Exception("基站割接导入文件数据失败了:"+e.getMessage());
				}
			
			}
	
		}
		return updates;
	}
	
	/**
	 * 基站割接上传文件存入cpe_station_chg_import_info后再存入cpe_user_reset_log表中
	 * 默认是否重置为0，未重置
	 */
	@Override
	public int[] cpeUserResetLogImport(final List<BatchStationBean> list)
			throws MysqlDataAccessExcetion, Exception {
		String sql="insert into cpe_user_reset_log(subsid,product_no,lac_ci_old,lac_ci_new,reset_flag) values(?,?,?,?,?)";
		int[] updates=null;
		if(list!=null && list.size()>0){
			updates=new int[list.size()];
				if(list.size()==1){
						Object[] args=new Object[5];
						args[0]=list.get(0).getSubsid();
						args[1]=list.get(0).getProductNo();
						args[2]=list.get(0).getLac_ci_hex_code_old();
						args[3]=list.get(0).getLac_ci_hex_code_new();
//						args[2]=list.get(0).getUserlocationOld();
//						args[3]=list.get(0).getUserlocationNew();
						args[4]=0;//0表示失败
					try {
						updates[0]=jdbcTemplate.update(sql, args);
					}catch (DataAccessException e) {
						log.error("基站割接导入到日志表中失败了:Mysql数据库访问异常");
						e.printStackTrace();
						throw new MysqlDataAccessExcetion("基站割接导入到日志表中失败了：Mysql数据库访问异常");
					}catch (Exception e) {
						log.error("基站割接导入到日志表中失败了"+args[1]);
						e.printStackTrace();
						throw new Exception("基站割接导入到日志表中失败了"+args[1]);
					}
				}else{	
					try{
							log.info("基站割接插入日志cpe_user_reset_log表："+sql);
							updates=jdbcTemplate.batchUpdate(sql,  new BatchPreparedStatementSetter(){
							@Override
							public int getBatchSize() {
								return 	list.size();
							}
							@Override
							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								ps.setString(1, list.get(i).getSubsid());
								ps.setString(2, list.get(i).getProductNo());
								ps.setString(3, list.get(i).getLac_ci_hex_code_old());
								ps.setString(4, list.get(i).getLac_ci_hex_code_new());
								ps.setInt(5, 0);
							}
							
						});
							log.info("插入："+updates.length+"行");
					}catch (DataAccessException e) {
						log.error("基站割接导入到日志表失败了:Mysql数据库访问异常");
						e.printStackTrace();
						throw new MysqlDataAccessExcetion("基站割接导入到日志表失败了：Mysql数据库访问异常");
					}catch (Exception e) {
						log.error("基站割接导入到日志表失败了:"+e.getMessage());
						e.printStackTrace();
						throw new Exception("基站割接导入到日志表中失败了"+e.getMessage());
					}
					
				}
			}
		return updates;
	}
	/**
	 * 根据原ac和ci及基站id到小区同步表中查询lac_ci的十六进制和十进制
	 * @param stationCode
	 * @param lac_dec_id
	 * @param ci_dec_id
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryLacCiByXiaoQuTable(String stationCode,
			String lac_dec_id, String ci_dec_id) {
		List<Map<String,Object>> list = null;
		String sql="select * from dim_cpe_lac_ci where station_code=? and lac_dec_id=? and ci_dec_id=?";
		try{
			list = this.jdbcTemplate.queryForList(sql,
					new Object[]{stationCode, lac_dec_id,ci_dec_id});
		}catch(Exception e){
			log.error("查询Mysql数据库小区同步表时操作错误！");
			e.printStackTrace();
		}finally{
			return list;
		}
	
	
	}

	/**
	 * 根据原来的lac_ci和新的lac_ci主键在导入表查询
	 * @param lac_ci_hex_code_old
	 * @param lac_ci_hex_code_new
	 * @return
	 */
	@Override
	public List<Map<String,Object>> queryLacCiByImportTable(
			String lac_ci_hex_code_old, String lac_ci_hex_code_new) {
		List<Map<String,Object>> list =null;
		String sql="select * from cpe_station_chg_import_info where lac_ci_hex_code_old=? and lac_ci_hex_code_new=? ";
		try{
			list = this.jdbcTemplate.query(sql,
					new Object[]{lac_ci_hex_code_old,lac_ci_hex_code_new},
					new BeanPropertyRowMapper(ImportBatchCutoverBean.class));
		}catch(Exception e){
			log.error("查询Mysql数据库基站割接表时操作错误！"+e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	@Override
	public List<Map<String,Object>> queryResetFlagByLogTable(
			String lac_ci_hex_code_old, String lac_ci_hex_code_new) {
		List<Map<String,Object>> list =null;
	String sql="select * from cpe_user_reset_log where lac_ci_old=? and lac_ci_new=?";
	try{
		list = this.jdbcTemplate.queryForList(sql,
				new Object[]{lac_ci_hex_code_old,lac_ci_hex_code_new});
	}catch(Exception e){
		log.info("查询Mysql数据库割接日志表时操作错误！"+e.getMessage());
		e.printStackTrace();
	}
		return list;
	}
	// 根据原来的lac_ci和新的lac_ci多表关联获得日志信息，为文件导入时插入日志信息做准备
	@Override
	public List<BatchStationBean> getResetLogBylac_ci(
			String lac_ci_hex_code_old, String lac_ci_hex_code_new) {
		List<BatchStationBean> listBean=new ArrayList<BatchStationBean>();
		BatchStationBean batchStationBean = null;
		List<Map<String,Object>> list =null;
		String sql="select lr.subsid,lr.product_no,lr.locked_lac_ci,bs.lac_ci_hex_code_new lac_ci_new,bs.enodeid_new,bs.lac_dec_id_new lac_new,bs.ci_dec_id_new ci_new,bs.chg_date,bs.import_time from cpe_station_chg_import_info bs join cpe_user_lock_rel lr on bs.lac_ci_hex_code_old=lr.locked_lac_ci "
				+" where  bs.lac_ci_hex_code_old=? and bs.lac_ci_hex_code_new=?";
		try{
			list= jdbcTemplate.queryForList(sql, new String[]{lac_ci_hex_code_old,lac_ci_hex_code_new});
			
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				batchStationBean = new BatchStationBean();
				Map<String,Object> m =(Map<String,Object>) list.get(i);
				String lac_ci_old = m.get("locked_lac_ci").toString();
				String lac_ci_new=m.get("lac_ci_new").toString();
				String enodeidNew=m.get("enodeid_new").toString();
				String subsid = m.get("subsid").toString();
				String productNo = m.get("product_no").toString();
				String cutoverTime="";
				if(m.get("chg_date")!=null){
					cutoverTime = sdf.format((Date)m.get("chg_date"));
				}
				String importTime="";
				if(m.get("import_time")!=null){
					importTime = sdf.format((Date)m.get("import_time"));
				}

				batchStationBean.setSubsid(subsid);
				batchStationBean.setProductNo(productNo);
				batchStationBean.setLac_ci_hex_code_old(lac_ci_old);
				batchStationBean.setLac_ci_hex_code_new(lac_ci_new);
				batchStationBean.setCutoverTime(cutoverTime);
				batchStationBean.setImportTime(importTime);
				batchStationBean.setResetFlag(0);		//0表示未重置
//				log.info("查询出的基站割接bean"+batchStationBean.toString());
				listBean.add(batchStationBean);
				}
			}
		}catch(Exception e){
			log.error("查询基站割接(参数重置)关联查询时操作错误！"+e.getMessage());
//			return null;
		}
//		finally{
//			return batchStationBean;
//		}
		return listBean;
	
	}
	
	/**
	 *更新割接日志表-导入表中有的日志表中没有
	 */
	@Override
	public List<Map<String,Object>> queryBatchStationTableBefore() {
		List<Map<String,Object>> list =null;
		String sql="select * from (select ip.lac_dec_id_old,ip.ci_dec_id_old,ip.lac_dec_id_new,ip.ci_dec_id_new,ip.chg_date,ip.import_time,ip.lac_ci_hex_code_old,ip.lac_ci_hex_code_new,lg.subsid,lg.product_no from cpe_user_reset_log lg right join cpe_station_chg_import_info ip "+
        "on lg.lac_ci_old=ip.lac_ci_hex_code_old and lg.lac_ci_new=ip.lac_ci_hex_code_new where lg.subsid is null and lg.product_no is null)update_station join cpe_user_lock_rel lr on update_station.lac_ci_hex_code_old=lr.locked_lac_ci";
		try {
			list=jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			log.error("查询基站割接(参数重置)更新日志表前先关联查询时操作错误！"+e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *更新割接日志表-锁网表中有的日志表中没有
	 */
	@Override
	public List<Map<String,Object>> queryBatchStationLogTableBefore() {
		List<Map<String,Object>> list =null;
		String sql= "select iplr.*,lg.reset_flag from (select lr.subsid,lr.product_no,lr.locked_lac_ci,ip.lac_dec_id_old,ip.ci_dec_id_old,ip.lac_dec_id_new,ip.ci_dec_id_new,ip.chg_date,ip.import_time,ip.lac_ci_hex_code_old,ip.lac_ci_hex_code_new from cpe_station_chg_import_info ip join cpe_user_lock_rel lr"+ 
		        " on ip.lac_ci_hex_code_old=lr.locked_lac_ci) iplr left join cpe_user_reset_log lg"+ 
		        " on iplr.subsid=lg.subsid and iplr.product_no=lg.product_no and iplr.lac_ci_hex_code_old=lg.lac_ci_old"+
		        " where lg.reset_flag is null";
		try {
			list=jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			log.error("查询基站割接(参数重置)更新日志表前先关联查询时操作错误！"+e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	@Override
	public List<Map<String,Object>> getSelectedBatchStationInfosByIds(
			String subsid, String productNo, String user_location) {
		List<Map<String,Object>>list=null;
		String	sql="select * from (select lg.subsid,lg.product_no,lg.reset_flag,lg.lac_ci_old,lg.lac_ci_new,ip.lac_dec_id_old,ip.ci_dec_id_old,ip.lac_dec_id_new,ip.ci_dec_id_new,ip.chg_date,ip.import_time,ip.lac_ci_hex_code_old from cpe_user_reset_log lg join cpe_station_chg_import_info ip"+
				" on lg.lac_ci_old=ip.lac_ci_hex_code_old and lg.lac_ci_new=ip.lac_ci_hex_code_new) log_station join cpe_user_lock_rel lr on log_station.subsid=lr.subsid and log_station.product_no=lr.product_no and log_station.lac_ci_hex_code_old=lr.locked_lac_ci"+
				" where   lr.subsid=? and lr.product_no=? and lr.user_location=?";
			
		try{
			list=jdbcTemplate.queryForList(sql, new String[]{subsid,productNo,user_location});
		}catch(Exception e){
			log.error("查询基站割接(参数重置)根据前台页面勾选行对应查询时操作错误！"+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	
}
