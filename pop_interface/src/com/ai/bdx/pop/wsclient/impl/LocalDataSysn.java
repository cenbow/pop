package com.ai.bdx.pop.wsclient.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;

import com.ai.bdx.pop.wsclient.bean.CpeLacCi;
import com.ai.bdx.pop.wsclient.bean.CpeLacCiInfoDm;
import com.ai.bdx.pop.wsclient.bean.CpeStation;
import com.ai.bdx.pop.wsclient.bean.CpetStation;
import com.ai.bdx.pop.wsclient.util.PoiExcelUtil;
import com.ai.bdx.pop.wsclient.util.PropUtil;

/**
 * 本地数据同步类
 * @author Administrator
 *
 */
public class LocalDataSysn {
	private static Logger log = Logger.getLogger(LocalDataSysn.class
			.getName());
	
	
	/**
	 * 生成usrlocation
	 * @param cgi
	 * 小区CGI
	 * @param tac
	 * 小区tac
	 * @return
	 */
		public static String createUsrLocation(String cgi,String tac){
			String usrlocation = "0";
			for(int i=tac.length();i<5;i++){
				tac="0"+tac;
			}
			String[] strs = cgi.split("-");
			String str1 = strs[0];//
			String str2 = strs[1];
			String enodebid = strs[2];
			String cellid = strs[3];
			String eci = Integer.toString(Integer.parseInt(enodebid)*256+Integer.parseInt(cellid));
			for(int i=eci.length();i<9;i++){
				eci="0"+eci;
			}
			usrlocation="130-"+str1+str2+tac+eci;
			return usrlocation;
		}
		
		/**
		 * 建立POP数据库连接
		 * @return
		 */
		public Connection getConnection(){
			Connection conn = null;
			String url = PropUtil.getProp("url", "mysql.properties");
			String local_url = PropUtil.getProp("local_url", "mysql.properties");
			try {
				Class.forName("com.mysql.jdbc.Driver"); 
				System.out.println("成功加载MySQL驱动程序");
				//conn = DriverManager.getConnection(url);
				conn = DriverManager.getConnection(local_url);
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				log.info("加载mysql失败");
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.info("数据库连接失败");
				e.printStackTrace();
			}
			return conn;
		}
		
		
		/**
		 * 建立网优数据库连接
		 * @return
		 */
		public Connection getConnectiontoDm(){
			Connection conn = null;
			String dmurl = PropUtil.getProp("dmurl", "mysql.properties");
			String driver = PropUtil.getProp("driver", "mysql.properties");
			try {
				Class.forName(driver); 
				System.out.println("成功加载MySQL驱动程序");
				conn = DriverManager.getConnection(dmurl);
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				log.info("加载mysql失败");
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.info("数据库连接失败");
				e.printStackTrace();
			}
			return conn;
		}
		
		/**
		 * 关闭连接
		 * @param rs
		 * @param stmt
		 * @param conn
		 */
		public void closeConnection(ResultSet rs,Statement stmt,Connection conn){
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * 插入一条记录
		 * @param cpeStation
		 * @param conn
		 * @return
		 */
		public int insertCpeStation(CpeStation cpeStation,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_STATION (STATION_CODE , STATION_NAME , COUNTRY_NAME , TOWN_NAME , COUNTY_NAME , CITY_NAME , COUNTY_ID , CITY_ID) "
					    + " VALUES ( '"+cpeStation.getStationCode()+"','"+cpeStation.getStationName()+"','"+cpeStation.getCountryName()+"','"+cpeStation.getTownName()+"','"
					    + cpeStation.getCountyName()+"','"+cpeStation.getCityName()+"','"+cpeStation.getCountyId()+"','"+cpeStation.getCityId()+"')";
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		/**
		 * 插入多条基站信息
		 * @param cpeStations
		 * @param conn
		 * @return
		 */
		public int insertCpeStations(List<CpeStation> cpeStations,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				CpeStation cpeStation = cpeStations.get(0);
				String values = " ( '"+cpeStation.getStationCode()+"','"+cpeStation.getStationName()+"','"+cpeStation.getCountryName()+"','"+cpeStation.getTownName()+"','"
					    + cpeStation.getCountyName()+"','"+cpeStation.getCityName()+"','"+cpeStation.getCountyId()+"','"+cpeStation.getCityId()+"')";
				for (int i = 1; i < cpeStations.size(); i++) {
					CpeStation cpeStationi = cpeStations.get(i);
					 values = values+","+"( '"+cpeStationi.getStationCode()+"','"+cpeStationi.getStationName()+"','"+cpeStationi.getCountryName()+"','"+cpeStationi.getTownName()+"','"
							    + cpeStationi.getCountyName()+"','"+cpeStationi.getCityName()+"','"+cpeStationi.getCountyId()+"','"+cpeStationi.getCityId()+"')";
				}
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_STATION (STATION_CODE , STATION_NAME , COUNTRY_NAME , TOWN_NAME , COUNTY_NAME , CITY_NAME , COUNTY_ID , CITY_ID) VALUES ";
				_sql = _sql+ values;
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		/**
		 * 插入一条dim_cpe_lac_ci
		 * @param cpeStation
		 * @param conn
		 * @return
		 */
		public int insertCpeLacCi(CpeLacCi cpeLacCi,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_LAC_CI (LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,STATION_CODE,STATION_NAME,COUNTRY_NAME,TOWN_NAME,COUNTY_ID,CITY_ID,USER_LOCATION) "
					    + " VALUES ('"+cpeLacCi.getLac_ci_hex_code()+"','"+cpeLacCi.getLac_ci_dec_id()+"','"+cpeLacCi.getLac_hex_code()+"','"+cpeLacCi.getLac_dec_id()+"','"+ cpeLacCi.getCi_hex_code()+"','"+cpeLacCi.getCi_dec_id()+"','"+cpeLacCi.getCell_name()
					    + "','"+cpeLacCi.getStation_code()+"','"+cpeLacCi.getStation_name()+"','"+cpeLacCi.getCountry_name()+"','"+cpeLacCi.getTown_name()+"','"+cpeLacCi.getCounty_id()+ "','"+cpeLacCi.getCity_id()+"','"+cpeLacCi.getUser_location()+"')"; 
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		
		/**
		 * 插入多条dim_cpe_lac_ci
		 * @param cpeStations
		 * @param conn
		 * @return
		 */
		public int insertCpeLacCis(List<CpeLacCi> cpeLacCis,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				CpeLacCi cpeLacCi = cpeLacCis.get(0);
				String values = "('"+cpeLacCi.getLac_ci_hex_code()+"','"+cpeLacCi.getLac_ci_dec_id()+"','"+cpeLacCi.getLac_hex_code()+"','"+cpeLacCi.getLac_dec_id()+"','"+ cpeLacCi.getCi_hex_code()+"','"+cpeLacCi.getCi_dec_id()+"','"+cpeLacCi.getCell_name()
					    + "','"+cpeLacCi.getStation_code()+"','"+cpeLacCi.getStation_name()+"','"+cpeLacCi.getCountry_name()+"','"+cpeLacCi.getTown_name()+"','"+cpeLacCi.getCounty_id()+ "','"+cpeLacCi.getCity_id()+"','"+cpeLacCi.getUser_location()+"')";
				for (int i = 1; i < cpeLacCis.size(); i++) {
					CpeLacCi cpeLacCii = cpeLacCis.get(i);
					 values = values+","+"('"+cpeLacCii.getLac_ci_hex_code()+"','"+cpeLacCii.getLac_ci_dec_id()+"','"+cpeLacCii.getLac_hex_code()+"','"+cpeLacCii.getLac_dec_id()+"','"+ cpeLacCii.getCi_hex_code()+"','"+cpeLacCii.getCi_dec_id()+"','"+cpeLacCi.getCell_name()
					    + "','"+cpeLacCii.getStation_code()+"','"+cpeLacCii.getStation_name()+"','"+cpeLacCii.getCountry_name()+"','"+cpeLacCii.getTown_name()+"','"+cpeLacCii.getCounty_id()+ "','"+cpeLacCii.getCity_id()+"','"+cpeLacCii.getUser_location()+"')";
				}
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_LAC_CI (LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,STATION_CODE,STATION_NAME,COUNTRY_NAME,TOWN_NAME,COUNTY_ID,CITY_ID,USER_LOCATION) VALUES ";
				_sql = _sql+ values;
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		/**
		 * 从excel中读取经分数据
		 * @param filepath 文件路径
		 * @return CpetStationlist集合
		 */
		public List<CpetStation> readCpetStationExcel(String filepath){
			List<CpetStation> cpetStations = new ArrayList<CpetStation>();
			File file = new File(filepath);
			Workbook wb = PoiExcelUtil.getWorkbook(file);
			int sheets = wb.getNumberOfSheets();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (int i = 0; i < sheets; i++)
			{
				HashMap<String, Object> map = PoiExcelUtil.getExcelObjectData(file,i);
				Integer num = Integer.parseInt(map.get("rowSize").toString());
				for (int j = 1; j < num; j++) {
					Date date = new Date();
					int k = 1;
					try{
						if(String.valueOf(map.get(j+"/8")).replace(".0", "").endsWith("1"))
						{
							CpetStation c = new CpetStation();
							String createTime = sdf.format(date);
							//	c.setEnodebId(String.valueOf(map.get(j+"/0")));k++;
							c.setProvinceName(String.valueOf(map.get(j+"/1")));k++;
							c.setCityName(String.valueOf(map.get(j+"/2")));k++;
							c.setCountyName(String.valueOf(map.get(j+"/3")));k++;
							c.setTownName(String.valueOf(map.get(j+"/4")));k++;
							c.setCountryName(String.valueOf(map.get(j+"/5")));k++;
							c.setStationName(String.valueOf(map.get(j+"/6")));k++;
							c.setCell_id(String.valueOf(map.get(j+"/7")).replace(".0", ""));k++;
							c.setStatus(String.valueOf(map.get(j+"/8")).replace(".0", ""));k++;
							c.setUserLocation(String.valueOf(map.get(j+"/9")));
							c.setCreateTime(createTime);
							cpetStations.add(c);
							}
					}catch(Exception e){
						log.info("第"+(j+1)+"行,第"+k+"列数据异常");
						e.printStackTrace();
						return null;
					}
				}
			}
			return cpetStations;
		}
		
		
		/**
		 * 从excel中读取网优数据
		 * @param filepath 文件路径
		 * @return CpeLacCiInfoDm集合
		 */
		public List<CpeLacCiInfoDm> readCpeLacCiInfoDmExcel(String filepath){
			List<CpeLacCiInfoDm> cpeLacCiInfoDms = new ArrayList<CpeLacCiInfoDm>();
			File file = new File(filepath);
			Workbook wb = PoiExcelUtil.getWorkbook(file);
			int sheets = wb.getNumberOfSheets();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (int i = 0; i < sheets; i++)
			{
				HashMap<String, Object> map = PoiExcelUtil.getExcelObjectData(file,i);
				Integer num = Integer.parseInt(map.get("rowSize").toString());
				int s = 0;
				for (int j = 0; j < num; j++) {
					Date date = new Date();
					int k = 1;
					try{
						if(map.get(j+"/0")!=null){
						CpeLacCiInfoDm c = new CpeLacCiInfoDm();
						String createTime = sdf.format(date);
						if(map.get(j+"/0") instanceof Double){
							c.setLac_ci_hex_code(new BigDecimal(String.valueOf(map.get(j+"/0"))).toPlainString().replace(".0", ""));
						}else if(map.get(j+"/0") instanceof String){
							c.setLac_ci_hex_code(String.valueOf(map.get(j+"/0")));
						}k++;
						if(map.get(j+"/1") instanceof Double){
							c.setLac_ci_dec_id(new BigDecimal(String.valueOf(map.get(j+"/1"))).toPlainString().replace(".0", ""));
						}else if(map.get(j+"/1") instanceof String){
							c.setLac_ci_dec_id(String.valueOf(map.get(j+"/1")));
						}k++;
						if(map.get(j+"/2") instanceof Double){
							c.setLac_hex_code(new BigDecimal(String.valueOf(map.get(j+"/2"))).toPlainString().replace(".0", ""));k++;
						}else if(map.get(j+"/2") instanceof String){
							c.setLac_hex_code(String.valueOf(map.get(j+"/2")));
						}k++;
						if(map.get(j+"/3") instanceof Double){
							c.setCi_hex_code(new BigDecimal(String.valueOf(map.get(j+"/3"))).toPlainString().replace(".0", ""));k++;
						}else if(map.get(j+"/3") instanceof String){
							c.setCi_hex_code(String.valueOf(map.get(j+"/3")));
						}k++;
						if(map.get(j+"/4") instanceof Double){
							c.setLac_dec_id(new BigDecimal(String.valueOf(map.get(j+"/4"))).toPlainString().replace(".0", ""));k++;
						}else if(map.get(j+"/4") instanceof String){
							c.setLac_dec_id(String.valueOf(map.get(j+"/4")));
						}k++;
						if(map.get(j+"/5") instanceof Double){
							c.setCi_dec_id(new BigDecimal(String.valueOf(map.get(j+"/5"))).toPlainString().replace(".0", ""));k++;
						}else if(map.get(j+"/5") instanceof String){
							c.setCi_dec_id(String.valueOf(map.get(j+"/5")));
						}k++;
						c.setCell_name(String.valueOf(map.get(j+"/7")));k++;
						c.setCgi(String.valueOf(map.get(j+"/8")));k++;
						c.setStation_code(String.valueOf(map.get(j+"/9")));k++;
						c.setStation_name(String.valueOf(map.get(j+"/10")));k=k+2;
						if(map.get(j+"/12") instanceof Double){
							c.setCity_id(new BigDecimal(String.valueOf(map.get(j+"/12"))).toPlainString().replace(".0", ""));k++;
						}else if(map.get(j+"/12") instanceof String){
							c.setCity_id(String.valueOf(map.get(j+"/12")));
						}k=k+2;
						c.setCounty_id(String.valueOf(map.get(j+"/14")));
						c.setCreate_time(createTime);
						c.setUser_location(createUsrLocation(c.getCgi(), c.getLac_dec_id()));
						cpeLacCiInfoDms.add(c);
						}
						s++;
						
					}catch(Exception e){
						System.out.println(s);
						log.info("第"+(s+1)+"行,第"+k+"列数据异常");
						e.printStackTrace();
						return null;
					}
				}
			}
			return cpeLacCiInfoDms;
		}
		
		
		/**
		 * 插入一条记录经分
		 * @param cpeStation
		 * @param conn
		 * @return
		 */
		public int insertCpetStation(CpetStation cpetStation,Connection conn){
			Statement stmt =null;
			int result = 0; 	
			try {
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_T_STATION (USER_LOCATION,PROVINCE_NAME, STATION_NAME , COUNTRY_NAME , TOWN_NAME , COUNTY_NAME , CITY_NAME,CELL_ID,CREATE_TIME) "
					    + " VALUES ( '"+cpetStation.getUserLocation()+"','"+cpetStation.getProvinceName()+"','"+cpetStation.getStationName()+"','"+cpetStation.getCountryName()+"','"+cpetStation.getTownName()+"','"
					    + cpetStation.getCountyName()+"','"+cpetStation.getCityName()+"','"+cpetStation.getCell_id()+"','"+cpetStation.getCreateTime()+"')";
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
	 	public int insertCpetStation(CpetStation cpetStation){
	 		Connection conn=null;
	 		int result = 0; 
	 		conn = getConnection();
	 		result = insertCpetStation(cpetStation,conn);
	 		closeConnection(null,null,conn);
	 		return result;
	 	}
		
		/**
		 * 插入多条基站信息
		 * @param cpeStations
		 * @param conn
		 * @return
		 */
		public int insertCpetStations(List<CpetStation> cpetStations,Connection conn){
			Statement stmt =null;
			log.info("cpetStations size is "+cpetStations.size());
			int result = 0; 
			try {
				CpetStation cpetStation = cpetStations.get(0);
				String values = " ( '"+cpetStation.getUserLocation()+"','"+cpetStation.getEnodebId()+"','"+cpetStation.getProvinceName()+"','"+cpetStation.getStationName()+"','"+cpetStation.getCountryName()+"','"+cpetStation.getTownName()+"','"
					    + cpetStation.getCountyName()+"','"+cpetStation.getCityName()+"','"+cpetStation.getCell_id()+"','"+cpetStation.getCellName()+"','"+cpetStation.getStatus()+"','"+cpetStation.getCreateTime()+"')";
				for (int i = 1; i < cpetStations.size(); i++) {
					System.out.println(i);
					CpetStation cpetStationi = cpetStations.get(i);
					 values = values+","+"( '"+cpetStationi.getUserLocation()+"','"+cpetStationi.getEnodebId()+"','"+cpetStationi.getProvinceName()+"','"+cpetStationi.getStationName()+"','"+cpetStationi.getCountryName()+"','"+cpetStationi.getTownName()+"','"
					    + cpetStationi.getCountyName()+"','"+cpetStationi.getCityName()+"','"+cpetStationi.getCell_id()+"','"+cpetStationi.getCellName()+"','"+cpetStationi.getStatus()+"','"+cpetStationi.getCreateTime()+"')";
				}
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_T_STATION (USER_LOCATION,ENODEB_ID,PROVINCE_NAME, STATION_NAME , COUNTRY_NAME , TOWN_NAME , COUNTY_NAME , CITY_NAME,CELL_ID,CELL_NAME,STATUS,CREATE_TIME) VALUES ";
				_sql = _sql+ values;
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		
		public int insertCpetStations(List<CpetStation> cpetStations){
	 		Connection conn=null;
	 		int result = 0; 
	 		conn = getConnection();
	 		result = insertCpetStations(cpetStations,conn);
	 		closeConnection(null,null,conn);
	 		return result;
	 	}
		
		public int insertCpetStationsByexcel(String filepath){
	 		Connection conn=null;
	 		int result = 0; 
	 		conn = getConnection();
	 		List<CpetStation> cpetStations = readCpetStationExcel(filepath);
	 		result = insertCpetStations(cpetStations, conn);
	 		closeConnection(null,null,conn);
	 		return result;
	 	}
		/**
		 * 插入一条dim_cpe_lac_ci_INFO_DM
		 * @param cpeStation
		 * @param conn
		 * @return
		 */
		public int insertCpeLacCiInfoDm(CpeLacCiInfoDm cpeLacCiInfoDm,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_LAC_CI_INFO_DM (LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,CGI,STATION_CODE,STATION_NAME,COUNTY_ID,CITY_ID,USER_LOCATION,CREATE_TIME) "
					    + " VALUES ('"+cpeLacCiInfoDm.getLac_ci_hex_code()+"','"+cpeLacCiInfoDm.getLac_ci_dec_id()+"','"+cpeLacCiInfoDm.getLac_hex_code()+"','"+cpeLacCiInfoDm.getLac_dec_id()+"','"+ cpeLacCiInfoDm.getCi_hex_code()+"','"+cpeLacCiInfoDm.getCi_dec_id()+"','"+cpeLacCiInfoDm.getCell_name()+"','"+cpeLacCiInfoDm.getCgi()
					    + "','"+cpeLacCiInfoDm.getStation_code()+"','"+cpeLacCiInfoDm.getStation_name()+"','"+cpeLacCiInfoDm.getCounty_id()+ "','"+cpeLacCiInfoDm.getCity_id()+"','"+cpeLacCiInfoDm.getUser_location()+"','"+cpeLacCiInfoDm.getCreate_time()+"')"; 
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		public List<CpeLacCiInfoDm> getCpeLacCiInfoDmslocal()
		{
			List<CpeLacCiInfoDm> cpeLacCiInfoDms = new ArrayList<CpeLacCiInfoDm>();
			Connection conn = null;
			Statement stmt =null;
			ResultSet rs = null;
			try {
				conn = getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,CGI,STATION_CODE,STATION_NAME,COUNTRY_NAME,COUNTY_ID,CITY_ID,USER_LOCATION FROM DIM_CPE_LAC_CI_INFO_DM ";
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					CpeLacCiInfoDm cpeLacCiInfoDm = new CpeLacCiInfoDm();
					cpeLacCiInfoDm.setLac_ci_hex_code(rs.getString("LAC_CI_HEX_CODE"));
					cpeLacCiInfoDm.setLac_ci_dec_id(rs.getString("LAC_CI_DEC_ID"));
					cpeLacCiInfoDm.setLac_hex_code(rs.getString("LAC_HEX_CODE"));
					cpeLacCiInfoDm.setLac_dec_id(rs.getString("LAC_DEC_ID"));
					cpeLacCiInfoDm.setCi_hex_code(rs.getString("CI_HEX_CODE"));
					cpeLacCiInfoDm.setCi_dec_id(rs.getString("CI_DEC_ID"));
					cpeLacCiInfoDm.setCell_name(rs.getString("CELL_NAME"));
					cpeLacCiInfoDm.setCgi(rs.getString("CGI"));
					cpeLacCiInfoDm.setStation_code(rs.getString("STATION_CODE"));
					cpeLacCiInfoDm.setStation_name(rs.getString("STATION_NAME"));
					cpeLacCiInfoDm.setCity_id(rs.getString("CITY_ID"));
					cpeLacCiInfoDm.setCounty_id(rs.getString("COUNTY_ID"));
					cpeLacCiInfoDm.setCountry_name(rs.getString("CELL_NAME"));
					cpeLacCiInfoDm.setUser_location(createUsrLocation(cpeLacCiInfoDm.getCgi(), cpeLacCiInfoDm.getLac_ci_dec_id()));
					cpeLacCiInfoDms.add(cpeLacCiInfoDm);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(rs, stmt, conn);
			}
			
			return cpeLacCiInfoDms;
		}
		
		public List<CpeLacCiInfoDm> getLacCiInfoDms()
		{
			List<CpeLacCiInfoDm> cpeLacCiInfoDms = new ArrayList<CpeLacCiInfoDm>();
			Connection conn = null;
			Statement stmt =null;
			ResultSet rs = null;
			try {
				conn = getConnectiontoDm();
				stmt = conn.createStatement();
				String sql = "SELECT LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,CGI,STATION_CODE,STATION_NAME,COUNTRY_NAME,COUNTY_CODE,REGION_ID,USER_LOCATION FROM DIM_LAC_CI_INFO_DM ";
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					CpeLacCiInfoDm cpeLacCiInfoDm = new CpeLacCiInfoDm();
					cpeLacCiInfoDm.setLac_ci_hex_code(rs.getString("LAC_CI_HEX_CODE"));
					cpeLacCiInfoDm.setLac_ci_dec_id(rs.getString("LAC_CI_DEC_ID"));
					cpeLacCiInfoDm.setLac_hex_code(rs.getString("LAC_HEX_CODE"));
					cpeLacCiInfoDm.setLac_dec_id(rs.getString("LAC_DEC_ID"));
					cpeLacCiInfoDm.setCi_hex_code(rs.getString("CI_HEX_CODE"));
					cpeLacCiInfoDm.setCi_dec_id(rs.getString("CI_DEC_ID"));
					cpeLacCiInfoDm.setCell_name(rs.getString("CELL_NAME"));
					cpeLacCiInfoDm.setCgi(rs.getString("CGI"));
					cpeLacCiInfoDm.setStation_code(rs.getString("STATION_CODE"));
					cpeLacCiInfoDm.setStation_name(rs.getString("STATION_NAME"));
					cpeLacCiInfoDm.setCity_id(rs.getString("REGION_ID"));
					cpeLacCiInfoDm.setCounty_id(rs.getString("COUNTY_CODE"));
					cpeLacCiInfoDm.setCountry_name(rs.getString("CELL_NAME"));
					cpeLacCiInfoDm.setUser_location(createUsrLocation(cpeLacCiInfoDm.getCgi(), cpeLacCiInfoDm.getLac_ci_dec_id()));
					cpeLacCiInfoDms.add(cpeLacCiInfoDm);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(rs, stmt, conn);
			}
			
			return cpeLacCiInfoDms;
		}
		
		/**
		 * 插入多条dim_cpe_lac_ci_INFO_DM
		 * @param cpeStations
		 * @param conn
		 * @return
		 */
		public int insertCpeLacCiInfoDms(List<CpeLacCiInfoDm> cpeLacCiInfoDms,Connection conn){
			Statement stmt =null;
			int result = 0; 
			try {
				CpeLacCiInfoDm cpeLacCiInfoDm = cpeLacCiInfoDms.get(0);
				String values = "('"+cpeLacCiInfoDm.getLac_ci_hex_code()+"','"+cpeLacCiInfoDm.getLac_ci_dec_id()+"','"+cpeLacCiInfoDm.getLac_hex_code()+"','"+cpeLacCiInfoDm.getLac_dec_id()+"','"+ cpeLacCiInfoDm.getCi_hex_code()+"','"+cpeLacCiInfoDm.getCi_dec_id()+"','"+cpeLacCiInfoDm.getCell_name()+"','"+cpeLacCiInfoDm.getCgi()
					    + "','"+cpeLacCiInfoDm.getStation_code()+"','"+cpeLacCiInfoDm.getStation_name()+"','"+cpeLacCiInfoDm.getCounty_id()+ "','"+cpeLacCiInfoDm.getCity_id()+"','"+cpeLacCiInfoDm.getUser_location()+"','"+cpeLacCiInfoDm.getCreate_time()+"')";
				for (int i = 1; i < cpeLacCiInfoDms.size(); i++) {
					CpeLacCiInfoDm cpeLacCiInfoDmi = cpeLacCiInfoDms.get(i);
					 values = values+","+"('"+cpeLacCiInfoDmi.getLac_ci_hex_code()+"','"+cpeLacCiInfoDmi.getLac_ci_dec_id()+"','"+cpeLacCiInfoDmi.getLac_hex_code()+"','"+cpeLacCiInfoDmi.getLac_dec_id()+"','"+ cpeLacCiInfoDmi.getCi_hex_code()+"','"+cpeLacCiInfoDmi.getCi_dec_id()+"','"+cpeLacCiInfoDmi.getCell_name()+"','"+cpeLacCiInfoDmi.getCgi()
					    + "','"+cpeLacCiInfoDmi.getStation_code()+"','"+cpeLacCiInfoDmi.getStation_name()+"','"+cpeLacCiInfoDmi.getCounty_id()+ "','"+cpeLacCiInfoDmi.getCity_id()+"','"+cpeLacCiInfoDmi.getUser_location()+"','"+cpeLacCiInfoDmi.getCreate_time()+"')";
				}
				stmt = conn.createStatement();
				String _sql = " INSERT INTO DIM_CPE_LAC_CI_INFO_DM (LAC_CI_HEX_CODE,LAC_CI_DEC_ID,LAC_HEX_CODE,LAC_DEC_ID,CI_HEX_CODE,CI_DEC_ID,CELL_NAME,CGI,STATION_CODE,STATION_NAME,COUNTY_ID,CITY_ID,USER_LOCATION,CREATE_TIME) VALUES ";
				_sql = _sql+ values;
				log.info(_sql);
				result=stmt.executeUpdate(_sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(null, stmt, null);
			}
			return result;
		}
		
		public int insertCpeLacCiInfoDms(List<CpeLacCiInfoDm> cpeLacCiInfoDms){
			int result = 0;
			Connection conn=null;
			conn = getConnection();
			result = insertCpeLacCiInfoDms(cpeLacCiInfoDms, conn);
			closeConnection(null, null, conn);
			return result;
		}
		
		public CpetStation getCpetStationBean(String usrlocation,Connection conn){
			CpetStation cpestationBean = new CpetStation();
			Statement stmt=null;
			ResultSet rs=null;
			try {
				
				String _sql = "SELECT USER_LOCATION ,ENODEB_ID ,PROVINCE_NAME, STATION_NAME , COUNTRY_NAME , TOWN_NAME , COUNTY_NAME ," +
							" CITY_NAME , CELL_ID ,CELL_NAME FROM DIM_CPE_T_STATION WHERE USER_LOCATION ='"+usrlocation+"' ";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(_sql);
				while (rs.next()) {
					cpestationBean.setUserLocation(rs.getString("USER_LOCATION"));
					cpestationBean.setEnodebId(rs.getString("ENODEB_ID"));
					cpestationBean.setProvinceName(rs.getString("PROVINCE_NAME"));
					cpestationBean.setStationName(rs.getString("STATION_NAME"));
					cpestationBean.setCountryName(rs.getString("COUNTRY_NAME"));
					cpestationBean.setTownName(rs.getString("TOWN_NAME"));
					cpestationBean.setCountyName(rs.getString("COUNTY_NAME"));
					cpestationBean.setCityName(rs.getString("CITY_NAME"));
					cpestationBean.setCell_id(rs.getString("CELL_ID"));
					cpestationBean.setCellName(rs.getString("CELL_NAME"));
					
					log.info(" | STATION_NAME: "+ cpestationBean.getStationName()
						   + " | COUNTRY_NAME: "+ cpestationBean.getCountryName()
						   + " | TOWN_NAME: " + cpestationBean.getTownName()
						   + " | COUNTY_NAME: " + cpestationBean.getCountyName()
						   + " | CITY_NAME: " + cpestationBean.getCityName()
						   + " | CELL_ID: " + cpestationBean.getCell_id());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(rs, stmt, null);
			}
			
			return cpestationBean;
		}
		
		public CpetStation getCpetStationBean(String usrlocation){
			Connection conn = null;
			CpetStation cpetStation = new CpetStation();
			conn = getConnection();
			cpetStation = getCpetStationBean(usrlocation,conn);
			closeConnection(null, null, conn);
			return cpetStation;
			
		}
		
		public List<CpeStation> getLocalstaData(Connection conn){
			List<CpeStation> cpeStations = new ArrayList<CpeStation>();
			Statement stmt=null;
			ResultSet rs=null;
			try {
				stmt = conn.createStatement();
				String  cpeStationsql = "SELECT DISTINCT C.STATION_CODE,C.STATION_NAME,C.COUNTRY_NAME,C.TOWN_NAME ,C.COUNTY_NAME,C.CITY_NAME,C.COUNTY_ID,C.CITY_ID  FROM (SELECT A.PROVINCE_NAME, A.STATION_NAME , A.COUNTRY_NAME , A.TOWN_NAME , A.COUNTY_NAME ,A.CITY_NAME ,A.CELL_NAME,B.STATION_CODE,B.COUNTY_ID,B.CITY_ID "+
						  " FROM DIM_CPE_T_STATION A ,dim_cpe_lac_ci_info_dm B WHERE A.user_location = B.user_location) c";
				rs = stmt.executeQuery(cpeStationsql);
				while(rs.next()){
					CpeStation cpestationBean = new CpeStation();
					cpestationBean.setStationCode(rs.getString("STATION_CODE"));
					cpestationBean.setStationName(rs.getString("STATION_NAME"));
					cpestationBean.setCountryName(rs.getString("COUNTRY_NAME"));
					cpestationBean.setTownName(rs.getString("TOWN_NAME"));
					cpestationBean.setCountyName(rs.getString("COUNTY_NAME"));
					cpestationBean.setCityName(rs.getString("CITY_NAME"));
					cpestationBean.setCountyId(rs.getString("COUNTY_ID"));
					cpestationBean.setCityId(rs.getString("CITY_ID"));
					cpeStations.add(cpestationBean);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(rs, stmt, null);
			}
			
			return cpeStations;
		}
		/**
		 * 本地数据同步，查询lac_ci信息
		 * @param conn
		 * @return
		 */
		public List<CpeLacCi> getLocallacData(Connection conn){
			List<CpeLacCi> datamap = new ArrayList<CpeLacCi>();
			Statement stmt=null;
			ResultSet rs=null;
			try {
				stmt = conn.createStatement();
				String cpelac_cisql = " SELECT A.STATION_NAME , A.COUNTRY_NAME , A.TOWN_NAME , A.COUNTY_NAME ,A.CITY_NAME,B.LAC_CI_HEX_CODE,B.LAC_CI_DEC_ID,B.LAC_HEX_CODE,B.LAC_DEC_ID,B.CI_HEX_CODE,B.CI_DEC_ID,B.STATION_CODE,B.COUNTY_ID,B.CITY_ID,B.USER_LOCATION ,B.CELL_NAME "
						+ " FROM DIM_CPE_T_STATION A ,dim_cpe_lac_ci_info_dm B WHERE A.user_location = B.user_location ";
				rs = stmt.executeQuery(cpelac_cisql);
				while(rs.next()){
					CpeLacCi cpeLacCiInfoDm = new CpeLacCi();
					cpeLacCiInfoDm.setLac_ci_hex_code(rs.getString("LAC_CI_HEX_CODE"));
					cpeLacCiInfoDm.setLac_ci_dec_id(rs.getString("LAC_CI_DEC_ID"));
					cpeLacCiInfoDm.setLac_hex_code(rs.getString("LAC_HEX_CODE"));
					cpeLacCiInfoDm.setLac_dec_id(rs.getString("LAC_DEC_ID"));
					cpeLacCiInfoDm.setCi_hex_code(rs.getString("CI_HEX_CODE"));
					cpeLacCiInfoDm.setCi_dec_id(rs.getString("CI_DEC_ID"));
					cpeLacCiInfoDm.setCell_name(rs.getString("CELL_NAME"));
					cpeLacCiInfoDm.setTown_name(rs.getString("TOWN_NAME"));
					cpeLacCiInfoDm.setStation_code(rs.getString("STATION_CODE"));
					cpeLacCiInfoDm.setStation_name(rs.getString("STATION_NAME"));
					cpeLacCiInfoDm.setCity_id(rs.getString("CITY_ID"));
					cpeLacCiInfoDm.setCounty_id(rs.getString("COUNTY_ID"));
					cpeLacCiInfoDm.setCountry_name(rs.getString("COUNTRY_NAME"));
					cpeLacCiInfoDm.setUser_location(rs.getString("USER_LOCATION"));
					datamap.add(cpeLacCiInfoDm);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				closeConnection(rs, stmt, null);
			}
			
			
			return datamap;
		}
		
		public void insertCpestat(String filePatn){
			Connection conn=null;
	 		conn = getConnection();
			//String filePatn = "D:\\cpetstation.xls";
			List<CpetStation> cpetStations = readCpetStationExcel(filePatn);
			if((cpetStations!=null)&&(cpetStations.size()!=0)){
				for (CpetStation cpetStation : cpetStations) {
					insertCpetStation(cpetStation,conn);
				}
			}
			closeConnection(null,null,conn);
		}
		
	public void insertLacByexce(String excelpath){
		Connection conn = getConnection();
		List<CpeLacCiInfoDm>  lacs = readCpeLacCiInfoDmExcel(excelpath);
		if(lacs.size()>0){
			for(CpeLacCiInfoDm lacd :lacs){
				insertCpeLacCiInfoDm(lacd, conn);
			}
		}
		closeConnection(null, null, conn);
		
	}
	
	
	public void insertLac(){
		Connection conn = getConnection();
		List<CpeLacCi>  lacs = getLocallacData(conn);
		if(lacs.size()>0){
			for(CpeLacCi lacd :lacs){
				insertCpeLacCi(lacd, conn);
			}
		}
		closeConnection(null, null, conn);
		
	}
	
	public void insertStation(){
		Connection conn = getConnection();
		List<CpeStation>  lacs = getLocalstaData(conn);
		if(lacs.size()>0){
			for(CpeStation lacd :lacs){
				insertCpeStation(lacd, conn);
			}
		}
		closeConnection(null, null, conn);
	}
	public static void main(String[] args) {
		LocalDataSysn sysn = new LocalDataSysn();
		/*Connection conn = sysn.getConnection();
		List<CpeLacCi> cpeLacCis = sysn.getLocallacData(conn);
		List<CpeStation> cpeStations = sysn.getLocalstaData(conn);
		sysn.insertCpeLacCis(cpeLacCis, conn);
		sysn.closeConnection(null, null, conn);
		//System.out.println(cpeLacCis);
		System.out.println(cpeStations);*/
	//	sysn.insertLacByexce("D:\\lac_ci13.xls");
	//	sysn.insertLac();
		//String path="D:\\cell1.xls";
		//sysn.insertCpestat("D:\\c.xls");
		sysn.insertStation();
	}
		
}
