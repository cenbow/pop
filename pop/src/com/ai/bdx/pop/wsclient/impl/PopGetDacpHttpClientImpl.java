/**   
 * @Title: POPGetDACPHTTPClientImpl.java
 * @Package com.ai.bdx.pop.wsclient.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com   
 * @date 2015-6-10 下午4:35:10
 * @version V1.0   
 */
package com.ai.bdx.pop.wsclient.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ai.bdx.pop.bean.DimLogicAreaBean;
import com.ai.bdx.pop.bean.DimProdProductDmBean;
import com.ai.bdx.pop.bean.DimTermInfoTacDmBean;
import com.ai.bdx.pop.exception.PopInterfaceException;
import com.ai.bdx.pop.model.PopInterfaceConfig;
import com.ai.bdx.pop.util.InterfaceConstant;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.wsclient.PopGetDacpHttpClient;
import com.asiainfo.biframe.utils.string.StringUtil;

/**
 * @ClassName: POPGetDACPHTTPClientImpl
 * @Description: TODO( POP和DACP同步数据接口)
 * @author jinlong
 * @date 2015-6-10 下午4:35:10
 * 
 */
public class PopGetDacpHttpClientImpl implements PopGetDacpHttpClient  {
	
	private static Logger log = LogManager.getLogger();

	/* (non-Javadoc)
	 * @see com.ai.bdx.pop.wsclient.POPGetDACPHTTPClient#dimLogicArea()
	 */
	@Override
	public void dimLogicArea() throws JSONException{
		// TODO Auto-generated method stub
		BufferedReader br = null;
		StringBuilder _buffer = new StringBuilder();
		//获取接口返回JSON信息
		_buffer = getjsonbuffer(InterfaceConstant.AIBI_GET_DACP_DIM_LOGIC_AREA);//AIBI_GET_DACP_DIM_LOGIC_AREA
		
		try {
			//jinl test
//			StringBuilder _buffer = new StringBuilder();
//			_buffer.append("{\"state\":{\"code\":\"0\",\"message\":\"成功\"},\"result\":[{\"logic_area_id\":88888888,\"logic_area_name\":\"测试数据\",\"logic_area_desc\":\"-9\",\"city_id\":\"HB.WH\",\"county_id\":\"-9\",\"logic_area_type_id\":1009,\"logic_area_type_name\":\"其他\",\"logic_area_sub_type_id\":41,\"logic_area_sub_type_name\":\"其他\",\"valid_time\":\"20131201\",\"invalid_time\":\"20991231\",\"update_time\":\"20131201\",\"status\":1}]}");
			if (_buffer.length() > 0) {// {"response":{"state":{"code":"0","message":"成功"},"SESSION_KEY":"rpIoWoZky9SUS7iukHWMr2GzEXkgsJ1R"}}
				//从JSON串中获取同步数据
				List<DimLogicAreaBean> list = new ArrayList();
				JSONObject jsonObject = new JSONObject(_buffer.toString());
				JSONObject jsonResp = jsonObject.getJSONObject("state");
				JSONArray result = jsonObject.getJSONArray("result");
				//json转换成list
				list=jsonToList(result);
				
				if(CollectionUtils.isNotEmpty(list)){
					//清表 dim_logic_area
					JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
					String deleteSql = new String();
					deleteSql = "truncate table dim_logic_area_jinltest";
					jdbcTemplate.execute(deleteSql);
					//批量插入表数据 dim_logic_area
					insertIntoTable(list,jdbcTemplate);

				}
				log.debug("同步 dim_logic_area数据表..成功!");
				// System.out.println("http请求用时秒：" + time);
				// System.out.println("hive数据条数：" + num);
				System.out.println("======pOPGetDACPHTTPClientImpl.dimLogicArea()=============获取DACP同步数据接口数据成功");
			}
			
		} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("======pOPGetDACPHTTPClientImpl.dimLogicArea()=============获取DACP同步数据接口数据异常");
				e.printStackTrace();
			}
	}
	
	/**
	 * @Title: popGetDimProdProductDm
	 * @Description: TODO  POP和DACP同步数据接口， 同步DIM_PROD_PRODUCT_DM_YYYYMMDD（每日汇总）
	 * @param @throws JSONException    
	 * @return void 
	 * @throws
	 */
	public void popGetDimProdProductDm() throws JSONException{
		// TODO Auto-generated method stub
		BufferedReader br = null;
		StringBuilder _buffer = new StringBuilder();
		//获取接口返回JSON信息
		_buffer = getjsonbuffer(InterfaceConstant.AIBI_DACP_DIM_PROD_PRODUCT_DM);
				
		try {
			//jinl test
//			StringBuilder _buffer = new StringBuilder();
//			_buffer.append("{\"state\":{\"code\":\"0\",\"message\":\"成功\"},\"result\":[{\"prod_id\":\" \",\"prod_name\":\"ProdType_Person\"");
//			_buffer.append(",\"prod_type\":\"GSM\",\"net_type\":\"0\",\"main_prod_flag\":null,\"prod_kind\":\"ProdSource_Self\",\"prod_src\":\"0\"");
//			_buffer.append(",\"prod_pkg_type\":null,\"eff_time\":1577808000000,\"exp_time\":null,\"mang_catalog\":\"ProdClass_Bill_Group\"");
//			_buffer.append(",\"sell_catalog\":\"\",\"create_org_id\":null,\"create_time\":null,\"state\":null,\"state_time\":null,\"prod_pricing_mode\":null");
//			_buffer.append(",\"prov_prod_flag\":null,\"happy_fmly_flag\":0,\"mail_card_flag\":0,\"official_flag\":0,\"free_flag\":0,\"test_card_flag\":0");
//			_buffer.append(",\"staff_card_flag\":0,\"public_phone_flag\":0,\"data_card_flag\":0,\"m2m_flag\":0,\"wireless_phone_flag\":0,\"flow_flag\":0");
//			_buffer.append(",\"infod_flag\":0,\"main_price_flag\":0,\"brand_id\":\"-99\",\"stat_month\":\"201505\",\"stat_date\":\"20150505\"}]}");

			if (_buffer.length() > 0) {// {"response":{"state":{"code":"0","message":"成功"},"SESSION_KEY":"rpIoWoZky9SUS7iukHWMr2GzEXkgsJ1R"}}
				//从JSON串中获取同步数据
				List<DimProdProductDmBean> list = new ArrayList();
				JSONObject jsonObject = new JSONObject(_buffer.toString());
				JSONObject jsonResp = jsonObject.getJSONObject("state");
				JSONArray result = jsonObject.getJSONArray("result");
				//json转换成list
				list=jsonToListDimProdProductDm(result);
				
				if(CollectionUtils.isNotEmpty(list)){
					//清表 dim_logic_area
					JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
					String deleteSql = new String();
					deleteSql = "truncate table dim_prod_product_dm_yyyymmdd_jinltest";
					jdbcTemplate.execute(deleteSql);
					//批量插入表数据 dim_prod_product_dm_yyyymmdd
					insertIntoTableDimProdProductDm(list,jdbcTemplate);

				}
				log.debug("同步 dim_prod_product_dm_yyyymmdd 数据表..成功!");
				// System.out.println("http请求用时秒：" + time);
				// System.out.println("hive数据条数：" + num);
				System.out.println("======pOPGetDACPHTTPClientImpl.popGetDimProdProductDm()=============获取DACP同步数据接口数据成功");
			}
			
		} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("======pOPGetDACPHTTPClientImpl.popGetDimProdProductDm()=============获取DACP同步数据接口数据异常");
				e.printStackTrace();
			}
	}
	
	/**
	 * @Title: popGetDimTermInfoTacDm
	 * @Description: TODO POP和DACP同步数据接口， 同步dim_term_info_tac_dm_yyyymm（每日汇总）
	 * @param @throws JSONException    
	 * @return void 
	 * @throws
	 */
	public void popGetDimTermInfoTacDm() throws JSONException{
		// TODO Auto-generated method stub
		BufferedReader br = null;
		StringBuilder _buffer = new StringBuilder();
		//获取接口返回JSON信息
		_buffer = getjsonbuffer(InterfaceConstant.AIBI_DACP_DIM_TERM_INFO_TAC_MM);
		
		try {
			//jinl test
//			StringBuilder _buffer = new StringBuilder();
//			_buffer.append("{\"state\":{\"code\":\"0\",\"message\":\"成功\"},\"result\":[{\"term_id\":\"1\",\"tac\":\"1\"");
//			_buffer.append(",\"manufacturer\":\"1\",\"term_brand\":\"1\",\"model\":\"1\",\"iscustom_flag\":0,\"term_type\":\"1\"");
//			_buffer.append(",\"dualsim_flag\":0,\"dualmode_type\":\"1\",\"supwcdma_flag\":0,\"gsm_flag\":0");
//			_buffer.append(",\"cdma2000_flag\":0,\"td_flag\":0,\"mms_flag\":0,\"gprs_flag\":0,\"edge_flag\":0,\"wifi_flag\":0");
//			_buffer.append(",\"issmart_flag\":0,\"ios\":\"1\",\"ios_version\":\"1\",\"main_screen_size\":\"1\",\"listing_time\":\"1\",\"listing_price\":\"1\"");
//			_buffer.append(",\"current_price\":\"1\",\"current_up_time\":\"1\",\"hspa_flag\":0,\"lte_fdd_flag\":0,\"lte_tdd_flag\":0,\"lte_single_card_dual_flag\":0");
//			_buffer.append(",\"volte_flag\":0,\"csfb_flag\":0,\"wap_flag\":0,\"www_flag\":0,\"gps_flag\":0");
//			_buffer.append(",\"pre_camera_pix\":0,\"bac_camera_pix\":0,\"screen_pix\":\"1\",\"color_screen_depth\":0,\"write_input\":\"1\"");
//			_buffer.append(",\"touch_type\":\"1\",\"term_style\":\"1\",\"java_flag\":0,\"usb_flag\":0,\"bluetooth_flag\":0");
//			_buffer.append(",\"infrared_flag\":0,\"dou_card_daul_flag\":0,\"stat_month\":0,\"stat_date\":0}]}");
			
			if (_buffer.length() > 0) {// {"response":{"state":{"code":"0","message":"成功"},"SESSION_KEY":"rpIoWoZky9SUS7iukHWMr2GzEXkgsJ1R"}}
				//从JSON串中获取同步数据
				List<DimTermInfoTacDmBean> list = new ArrayList();
				JSONObject jsonObject = new JSONObject(_buffer.toString());
				JSONObject jsonResp = jsonObject.getJSONObject("state");
				JSONArray result = jsonObject.getJSONArray("result");
				//json转换成list
				list=jsonToListDimTermInfoTacDm(result);
				
				if(CollectionUtils.isNotEmpty(list)){
					//清表 dim_logic_area
					JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate", JdbcTemplate.class);
					String deleteSql = new String();
					deleteSql = "truncate table dim_term_info_tac_dm_yyyymm_jinltest";
					jdbcTemplate.execute(deleteSql);
					//批量插入表数据 dim_term_info_tac_dm_yyyymm
					insertIntoTableDimTermInfoTacDm(list,jdbcTemplate);

				}
				log.debug("同步 dim_term_info_tac_dm_yyyymm 数据表..成功!");
				// System.out.println("http请求用时秒：" + time);
				// System.out.println("hive数据条数：" + num);
				System.out.println("======pOPGetDACPHTTPClientImpl.popGetDimTermInfoTacDm()=============获取DACP同步数据接口数据成功");
			}
			
		} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("======pOPGetDACPHTTPClientImpl.popGetDimTermInfoTacDm()=============获取DACP同步数据接口数据异常");
				e.printStackTrace();
			}
	}
	
	
	/**
	 * 批量同步dim_logic_area 表
	 * @Title: insertIntoTable
	 * @Description: TODO
	 * @param @param beanList
	 * @param @param jdbcTemplate    
	 * @return void 
	 * @throws
	 */
	private void insertIntoTable(final List<DimLogicAreaBean> beanList,JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		//dim_logic_area_jinltest为测试表，联调时用正常表dim_logic_area
		sql.append(" INSERT INTO dim_logic_area_jinltest");
		sql.append("(logic_area_id, logic_area_name, logic_area_desc, city_id, county_id, logic_area_type_id, logic_area_type_name" +
				", logic_area_sub_type_id, logic_area_sub_type_name, valid_time,invalid_time,update_time,status) ");
		sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				 ps.setBigDecimal(1,  BigDecimal.valueOf(Long.valueOf(beanList.get(i).getLogicAreaId())));
				 ps.setString(2, beanList.get(i).getLogicAreaName());
				 ps.setString(3, beanList.get(i).getLogicAreaDesc());
				 ps.setString(4, beanList.get(i).getCityId());
				 ps.setString(5, beanList.get(i).getCountyId());
				 ps.setShort(6, Short.valueOf(beanList.get(i).getLogicAreaTypeId()));
				 ps.setString(7, beanList.get(i).getLogicAreaTypeName());
				 ps.setShort(8,  Short.valueOf(beanList.get(i).getLogicAreaSubTypeId()));
				 ps.setString(9,  beanList.get(i).getLogicAreaSubTypeName());
				 ps.setString(10,  beanList.get(i).getValidTime());
				 ps.setString(11,  beanList.get(i).getInvalidTime());
				 ps.setString(12,  beanList.get(i).getUpdateTime());
				 ps.setShort(13, Short.valueOf(beanList.get(i).getStatus()));
			}
			public int getBatchSize() {
				return beanList.size();
			}
			
		  });
	}
	
	/**
	 * 批量同步dim_prod_product_dm_yyyymmdd 表
	 * @Title: insertIntoTableDimProdProductDm
	 * @Description: TODO
	 * @param @param beanList
	 * @param @param jdbcTemplate    
	 * @return void 
	 * @throws
	 */
	private void insertIntoTableDimProdProductDm(final List<DimProdProductDmBean> beanList,JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		//dim_prod_product_dm_yyyymmdd_jinltest为测试表，联调时用正常表dim_prod_product_dm_yyyymmdd
		sql.append(" INSERT INTO dim_prod_product_dm_yyyymmdd_jinltest");
		sql.append("(prod_id, prod_name, prod_type, net_type, main_prod_flag, prod_kind, prod_src " +
				", prod_pkg_type, eff_time, exp_time,mang_catalog,sell_catalog,create_org_id " +
				", create_time, state, state_time,prod_pricing_mode,prov_prod_flag,happy_fmly_flag " +
				", mail_card_flag, official_flag, free_flag,test_card_flag,staff_card_flag,public_phone_flag" +
				", data_card_flag, m2m_flag, wireless_phone_flag,flow_flag,infod_flag,main_price_flag" +
				", brand_id, stat_month, stat_date) ");
		sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
//		{"prod_id":" ","prod_name":"ProdType_Person","prod_type":"GSM",
//		"net_type":"0","main_prod_flag":null,"prod_kind":"ProdSource_Self",
//		"prod_src":"0","prod_pkg_type":null,"eff_time":1577808000000,"exp_time":null,
//		"mang_catalog":"ProdClass_Bill_Group","sell_catalog":"","create_org_id":null,
//		"create_time":null,"state":null,"state_time":null,"prod_pricing_mode":null,
//		"prov_prod_flag":null,"happy_fmly_flag":0,"mail_card_flag":0,"official_flag":0,
//		"free_flag":0,"test_card_flag":0,"staff_card_flag":0,"public_phone_flag":0,"data_card_flag":0
//		,"m2m_flag":0,"wireless_phone_flag":0,"flow_flag":0,"infod_flag":0,"main_price_flag":0,
//		"brand_id":"-99","stat_month":"201505","stat_date":"20150505"}
		
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				 ps.setString(1,  beanList.get(i).getProdId());
				 ps.setString(2, beanList.get(i).getProdName());
				 ps.setString(3, beanList.get(i).getProdType());
				 ps.setString(4, beanList.get(i).getNetType());
				 if(StringUtil.isEmpty(beanList.get(i).getMainProdFlag())||"null".equals(beanList.get(i).getMainProdFlag())){
					 ps.setNull(5, Types.INTEGER);
				 }else{
					 ps.setShort(5, Short.valueOf(beanList.get(i).getMainProdFlag()));
				 }
				 ps.setString(6, beanList.get(i).getProdKind());
				 ps.setString(7, beanList.get(i).getProdSrc());
				 if(StringUtil.isEmpty(beanList.get(i).getProdPkgType())||"null".equals(beanList.get(i).getProdPkgType())){
					 ps.setNull(8, Types.INTEGER);
				 }else{
					 ps.setShort(8,  Short.valueOf(beanList.get(i).getProdPkgType()));
				 }
				 ps.setTimestamp(9,(java.sql.Timestamp)beanList.get(i).getEffTime());
				 if(StringUtil.isEmpty(beanList.get(i).getExpTime())||"null".equals(beanList.get(i).getExpTime())){
					 ps.setNull(10, Types.TIMESTAMP);
				 }else{
					 ps.setTimestamp(10,  (java.sql.Timestamp)beanList.get(i).getExpTime());
				 }
				 
				 ps.setString(11,  beanList.get(i).getMangCatalog());
				 ps.setString(12,  beanList.get(i).getSellCatalog());
				 ps.setString(13, beanList.get(i).getCreateOrgId());
				 ps.setTimestamp(14,  (java.sql.Timestamp)beanList.get(i).getCreateTime());
				 ps.setString(15,  beanList.get(i).getState());
				 ps.setTimestamp(16,  (java.sql.Timestamp)beanList.get(i).getStateTime());
				 if(StringUtil.isEmpty(beanList.get(i).getProdPricingMode())||"null".equals(beanList.get(i).getProdPricingMode())){
					 ps.setNull(17, Types.INTEGER);
				 }else{
					 ps.setShort(17,  Short.valueOf(beanList.get(i).getProdPricingMode()));
				 }
				 if(StringUtil.isEmpty(beanList.get(i).getProvProdFlag())||"null".equals(beanList.get(i).getProvProdFlag())){
					 ps.setNull(18, Types.INTEGER);
				 }else{
					 ps.setShort(18,  Short.valueOf(beanList.get(i).getProvProdFlag()));
				 }
				 ps.setShort(19,  Short.valueOf(beanList.get(i).getHappyFmlyFlag()));
				 ps.setShort(20,  Short.valueOf(beanList.get(i).getMailCardFlag()));
				 ps.setShort(21,  Short.valueOf(beanList.get(i).getOfficialFlag()));
				 ps.setShort(22,  Short.valueOf(beanList.get(i).getFreeFlag()));
				 ps.setShort(23,  Short.valueOf(beanList.get(i).getTestCardFlag()));
				 ps.setShort(24,  Short.valueOf(beanList.get(i).getStaffCardFlag()));
				 ps.setShort(25,  Short.valueOf(beanList.get(i).getPublicPhoneFlag()));
				 ps.setShort(26,  Short.valueOf(beanList.get(i).getDataCardFlag()));
				 ps.setShort(27,  Short.valueOf(beanList.get(i).getM2mFlag()));
				 ps.setShort(28,  Short.valueOf(beanList.get(i).getWireless_phoneFlag()));
				 ps.setShort(29,  Short.valueOf(beanList.get(i).getFlowFlag()));
				 ps.setShort(30,  Short.valueOf(beanList.get(i).getInfodFlag()));
				 ps.setShort(31,  Short.valueOf(beanList.get(i).getMainPriceFlag()));
				 ps.setString(32,  beanList.get(i).getBrandId());
				 ps.setString(33,  beanList.get(i).getStatMonth());
				 ps.setString(34,  beanList.get(i).getStatDate());
			}
			public int getBatchSize() {
				return beanList.size();
			}
			
		  });
	}
	
	/**
	 * @Title: insertIntoTableDimTermInfoTacDm
	 * @Description: TODO
	 * @param @param beanList
	 * @param @param jdbcTemplate    
	 * @return void 
	 * @throws
	 */
	private void insertIntoTableDimTermInfoTacDm(final List<DimTermInfoTacDmBean> beanList,JdbcTemplate jdbcTemplate) {
		StringBuffer sql = new StringBuffer();
		//dim_term_info_tac_dm_yyyymm_jinltest为测试表，联调时用正常表dim_term_info_tac_dm_yyyymm
		sql.append(" INSERT INTO dim_term_info_tac_dm_yyyymm_jinltest");
		sql.append("(term_id, tac, manufacturer, term_brand, model, ");
		sql.append("iscustom_flag, term_type, dualsim_flag, dualmode_type, supwcdma_flag, ");
		sql.append("gsm_flag, cdma2000_flag, td_flag, mms_flag, gprs_flag, ");
		sql.append("edge_flag, wifi_flag, issmart_flag, ios, ios_version,");
		sql.append("main_screen_size, listing_time, listing_price, current_price, current_up_time, ");
		sql.append("hspa_flag, lte_fdd_flag, lte_tdd_flag, lte_single_card_dual_flag,volte_flag, ");
		sql.append("csfb_flag, wap_flag, www_flag, gps_flag, pre_camera_pix, ");
		sql.append("bac_camera_pix, screen_pix, color_screen_depth, write_input, touch_type, ");
		sql.append("term_style, java_flag, usb_flag, bluetooth_flag, infrared_flag, ");
		sql.append("dou_card_daul_flag, stat_month, stat_date )");
		sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				 ps.setString(1, beanList.get(i).getTermId());
				 ps.setString(2, beanList.get(i).getTac());
				 ps.setString(3, beanList.get(i).getManufacturer());
				 ps.setString(4, beanList.get(i).getTermBrand());
				 ps.setString(5, beanList.get(i).getModel());
				 if(StringUtil.isEmpty(beanList.get(i).getIscustomFlag())||"null".equals(beanList.get(i).getIscustomFlag())){
					 ps.setNull(6, Types.INTEGER);
				 }else{
					 ps.setShort(6, Short.valueOf(beanList.get(i).getIscustomFlag()));
				 }
				 ps.setString(7, beanList.get(i).getTermType());
				 if(StringUtil.isEmpty(beanList.get(i).getDualsimFlag())||"null".equals(beanList.get(i).getDualsimFlag())){
					 ps.setNull(8, Types.INTEGER);
				 }else{
					 ps.setShort(8,  Short.valueOf(beanList.get(i).getDualsimFlag()));
				 }
				 ps.setString(9, beanList.get(i).getDualmodeType());
				 if(StringUtil.isEmpty(beanList.get(i).getSupwcdmaFlag())||"null".equals(beanList.get(i).getSupwcdmaFlag())){
					 ps.setNull(10, Types.TIMESTAMP);
				 }else{
					 ps.setShort(10,  Short.valueOf(beanList.get(i).getSupwcdmaFlag()));
				 }
				 
				 ps.setShort(11,  Short.valueOf(beanList.get(i).getGsmFlag()));
				 ps.setShort(12,  Short.valueOf(beanList.get(i).getCdma2000Flag()));
				 ps.setShort(13,  Short.valueOf(beanList.get(i).getTdFlag()));
				 ps.setShort(14,  Short.valueOf(beanList.get(i).getMmsFlag()));
				 ps.setShort(15,  Short.valueOf(beanList.get(i).getGprsFlag()));
				 ps.setShort(16,  Short.valueOf(beanList.get(i).getEdgeFlag()));
				 if(StringUtil.isEmpty(beanList.get(i).getWifiFlag())||"null".equals(beanList.get(i).getWifiFlag())){
					 ps.setNull(17, Types.INTEGER);
				 }else{
					 ps.setShort(17,  Short.valueOf(beanList.get(i).getWifiFlag()));
				 }
				 if(StringUtil.isEmpty(beanList.get(i).getIssmartFlag())||"null".equals(beanList.get(i).getIssmartFlag())){
					 ps.setNull(18, Types.INTEGER);
				 }else{
					 ps.setShort(18,  Short.valueOf(beanList.get(i).getIssmartFlag()));
				 }
				 ps.setString(19,  beanList.get(i).getIos());
				 ps.setString(20,  beanList.get(i).getIosVersion());
				 ps.setString(21,  beanList.get(i).getMainScreenSize());
				 ps.setString(22,  beanList.get(i).getListingTime());
				 ps.setString(23,  beanList.get(i).getListingPrice());
				 ps.setString(24,  beanList.get(i).getCurrentPrice());
				 ps.setString(25,  beanList.get(i).getCurrentUpTime());
				 ps.setShort(26,  Short.valueOf(beanList.get(i).getHspaFlag()));
				 ps.setShort(27,  Short.valueOf(beanList.get(i).getLteFddFlag()));
				 ps.setShort(28,  Short.valueOf(beanList.get(i).getLteTddFlag()));
				 ps.setShort(29,  Short.valueOf(beanList.get(i).getLteSingleCardDualFlag()));
				 ps.setShort(30,  Short.valueOf(beanList.get(i).getVolteFlag()));
				 ps.setShort(31,  Short.valueOf(beanList.get(i).getCsfbFlag()));
				 ps.setShort(32,  Short.valueOf(beanList.get(i).getWapFlag()));
				 ps.setShort(33,  Short.valueOf(beanList.get(i).getWwwFlag()));
				 ps.setShort(34,  Short.valueOf(beanList.get(i).getGpsFlag()));
				 ps.setShort(35,  Short.valueOf(beanList.get(i).getPreCameraPix()));//bigint
				 ps.setShort(36,  Short.valueOf(beanList.get(i).getBacCameraPix()));//bigint
				 ps.setShort(37,  Short.valueOf(beanList.get(i).getScreenPix()));
				 ps.setShort(38,  Short.valueOf(beanList.get(i).getColorScreenDepth()));
				 ps.setShort(39,  Short.valueOf(beanList.get(i).getWriteInput()));
				 ps.setShort(40,  Short.valueOf(beanList.get(i).getTouchType()));
				 ps.setShort(41,  Short.valueOf(beanList.get(i).getTermStyle()));
				 ps.setShort(42,  Short.valueOf(beanList.get(i).getJavaFlag()));
				 ps.setShort(43,  Short.valueOf(beanList.get(i).getUsbFlag()));
				 ps.setShort(44,  Short.valueOf(beanList.get(i).getBluetoothFlag()));
				 ps.setShort(45,  Short.valueOf(beanList.get(i).getInfraredFlag()));
				 ps.setShort(46,  Short.valueOf(beanList.get(i).getDouCardDaulFlag()));
				 ps.setShort(47,  Short.valueOf(beanList.get(i).getStatMonth()));
				 ps.setShort(48,  Short.valueOf(beanList.get(i).getStatDate()));
				 
			}
			public int getBatchSize() {
				return beanList.size();
			}
			
		  });
	}
	
	/**
	 * @Title: jsonToList
	 * @Description: jsonToList 用于将获取到的json串转换成 List<DimLogicAreaBean>返回
	 * @param @param result
	 * @param @return
	 * @param @throws JSONException    
	 * @return List<DimLogicAreaBean> 
	 * @throws
	 */
	public List<DimLogicAreaBean> jsonToList (JSONArray result) throws JSONException{
		List<DimLogicAreaBean> list = new ArrayList();
		int num = result.length();
		for (int i = 0; i < num; i++) {
			JSONObject jObject = result.getJSONObject(i);
			System.out.println(jObject.toString());
			DimLogicAreaBean dimLogicAreaBean = new DimLogicAreaBean();
				String logic_area_id = jObject.getString("logic_area_id");
				String logic_area_name = jObject.getString("logic_area_name");
				String logic_area_desc = jObject.getString("logic_area_desc");
				String city_id = jObject.getString("city_id");
				String county_id = jObject.getString("county_id");
				String logic_area_type_id = jObject.getString("logic_area_type_id");
				String logic_area_type_name = jObject.getString("logic_area_type_name");
				String logic_area_sub_type_id = jObject.getString("logic_area_sub_type_id");
				String logic_area_sub_type_name = jObject.getString("logic_area_sub_type_name");
				String valid_time = jObject.getString("valid_time");
				String invalid_time = jObject.getString("invalid_time");
				String update_time = jObject.getString("update_time");
				String status = jObject.getString("status");
				dimLogicAreaBean.setLogicAreaId(logic_area_id);
				dimLogicAreaBean.setLogicAreaName(logic_area_name);
				dimLogicAreaBean.setLogicAreaDesc(logic_area_desc);
				dimLogicAreaBean.setCityId(city_id);
				dimLogicAreaBean.setCountyId(county_id);
				dimLogicAreaBean.setLogicAreaTypeId(logic_area_type_id);
				dimLogicAreaBean.setLogicAreaTypeName(logic_area_type_name);
				dimLogicAreaBean.setLogicAreaSubTypeId(logic_area_sub_type_id);
				dimLogicAreaBean.setLogicAreaSubTypeName(logic_area_sub_type_name);
				dimLogicAreaBean.setValidTime(valid_time);
				dimLogicAreaBean.setInvalidTime(invalid_time);
				dimLogicAreaBean.setUpdateTime(update_time);
				dimLogicAreaBean.setStatus(status);
				list.add(dimLogicAreaBean);
			
		}
		return list;
	}
	
	/**
	 * @Title: jsonToList
	 * @Description: jsonToList 用于将获取到的json串转换成 List<DimProdProductDmBean>返回
	 * @param @param result
	 * @param @return
	 * @param @throws JSONException    
	 * @return List<DimLogicAreaBean> 
	 * @throws
	 */
	public List<DimProdProductDmBean> jsonToListDimProdProductDm (JSONArray result) throws JSONException{
		List<DimProdProductDmBean> list = new ArrayList();
		int num = result.length();
		for (int i = 0; i < num; i++) {
			JSONObject jObject = result.getJSONObject(i);
			System.out.println(jObject.toString());
			DimProdProductDmBean dimProdProductDmBean = new DimProdProductDmBean();
			
//			{"prod_id":" ","prod_name":"ProdType_Person","prod_type":"GSM",
//				"net_type":"0","main_prod_flag":null,"prod_kind":"ProdSource_Self",
//				"prod_src":"0","prod_pkg_type":null,"eff_time":1577808000000,"exp_time":null,
//				"mang_catalog":"ProdClass_Bill_Group","sell_catalog":"","create_org_id":null,
//				"create_time":null,"state":null,"state_time":null,"prod_pricing_mode":null,
//				"prov_prod_flag":null,"happy_fmly_flag":0,"mail_card_flag":0,"official_flag":0,
//				"free_flag":0,"test_card_flag":0,"staff_card_flag":0,"public_phone_flag":0,"data_card_flag":0
//				,"m2m_flag":0,"wireless_phone_flag":0,"flow_flag":0,"infod_flag":0,"main_price_flag":0,
//				"brand_id":"-99","stat_month":"201505","stat_date":"20150505"}

				dimProdProductDmBean.setProdId(jObject.getString("prod_id"));
				dimProdProductDmBean.setProdName(jObject.getString("prod_name"));
				dimProdProductDmBean.setProdType(jObject.getString("prod_type"));
				dimProdProductDmBean.setNetType(jObject.getString("net_type"));
				dimProdProductDmBean.setMainProdFlag(jObject.getString("main_prod_flag"));
				dimProdProductDmBean.setProdKind(jObject.getString("prod_kind"));
				dimProdProductDmBean.setProdSrc(jObject.getString("prod_src"));
				dimProdProductDmBean.setProdPkgType(jObject.getString("prod_pkg_type"));
				if(!"null".equals(jObject.getString("eff_time"))){
					dimProdProductDmBean.setEffTime(new Timestamp(Long.valueOf(jObject.getString("eff_time"))));
				}
				if(!"null".equals(jObject.getString("exp_time"))){
					dimProdProductDmBean.setExpTime(new Timestamp(Long.valueOf(jObject.getString("exp_time"))));
				}
				dimProdProductDmBean.setMangCatalog(jObject.getString("mang_catalog"));
				dimProdProductDmBean.setSellCatalog(jObject.getString("sell_catalog"));
				dimProdProductDmBean.setCreateOrgId(jObject.getString("create_org_id"));
				if(!"null".equals(jObject.getString("create_time"))){
					dimProdProductDmBean.setCreateTime(new Timestamp(jObject.getLong("create_time")));
				}
				dimProdProductDmBean.setState(jObject.getString("state"));
				if(!"null".equals(jObject.getString("state_time"))){
					dimProdProductDmBean.setStateTime(new Timestamp(jObject.getLong(("state_time"))));
				}
				dimProdProductDmBean.setProdPricingMode(jObject.getString("prod_pricing_mode"));
				dimProdProductDmBean.setProvProdFlag(jObject.getString("prov_prod_flag"));
				dimProdProductDmBean.setHappyFmlyFlag(jObject.getString("happy_fmly_flag"));
				dimProdProductDmBean.setMailCardFlag(jObject.getString("mail_card_flag"));
				dimProdProductDmBean.setOfficialFlag(jObject.getString("official_flag"));
				dimProdProductDmBean.setFreeFlag(jObject.getString("free_flag"));
				dimProdProductDmBean.setTestCardFlag(jObject.getString("test_card_flag"));
				dimProdProductDmBean.setStaffCardFlag(jObject.getString("staff_card_flag"));
				dimProdProductDmBean.setPublicPhoneFlag(jObject.getString("public_phone_flag"));
				dimProdProductDmBean.setDataCardFlag(jObject.getString("data_card_flag"));
				dimProdProductDmBean.setM2mFlag(jObject.getString("m2m_flag"));
				dimProdProductDmBean.setWireless_phoneFlag(jObject.getString("wireless_phone_flag"));
				dimProdProductDmBean.setFlowFlag(jObject.getString("flow_flag"));
				dimProdProductDmBean.setInfodFlag(jObject.getString("infod_flag"));
				dimProdProductDmBean.setMainPriceFlag(jObject.getString("main_price_flag"));
				dimProdProductDmBean.setBrandId(jObject.getString("brand_id"));
				dimProdProductDmBean.setStatMonth(jObject.getString("stat_month"));
				dimProdProductDmBean.setStatDate(jObject.getString("stat_date"));
				list.add(dimProdProductDmBean);
			
		}
		return list;
	}
	
	/**
	 * @Title: jsonToListDimTermInfoTacDm
	 * @Description: TODO
	 * @param @param result
	 * @param @return
	 * @param @throws JSONException    
	 * @return List<DimTermInfoTacDmBean> 
	 * @throws
	 */
	public List<DimTermInfoTacDmBean> jsonToListDimTermInfoTacDm (JSONArray result) throws JSONException{
		List<DimTermInfoTacDmBean> list = new ArrayList();
		int num = result.length();
		for (int i = 0; i < num; i++) {
			JSONObject jObject = result.getJSONObject(i);
			System.out.println(jObject.toString());
			DimTermInfoTacDmBean dimTermInfoTacDmBean = new DimTermInfoTacDmBean();

			dimTermInfoTacDmBean.setTermId(jObject.getString("term_id"));
			dimTermInfoTacDmBean.setTac(jObject.getString("tac"));
			dimTermInfoTacDmBean.setManufacturer(jObject.getString("manufacturer"));
			dimTermInfoTacDmBean.setTermBrand(jObject.getString("term_brand"));
			dimTermInfoTacDmBean.setModel(jObject.getString("model"));
			dimTermInfoTacDmBean.setIscustomFlag(jObject.getString("iscustom_flag"));
			dimTermInfoTacDmBean.setTermType(jObject.getString("term_type"));
			dimTermInfoTacDmBean.setDualsimFlag(jObject.getString("dualsim_flag"));
			dimTermInfoTacDmBean.setDualmodeType(jObject.getString("dualmode_type"));
			dimTermInfoTacDmBean.setSupwcdmaFlag(jObject.getString("supwcdma_flag"));
			
			dimTermInfoTacDmBean.setGsmFlag(jObject.getString("gsm_flag"));
			dimTermInfoTacDmBean.setCdma2000Flag(jObject.getString("cdma2000_flag"));
			dimTermInfoTacDmBean.setTdFlag(jObject.getString("td_flag"));
			dimTermInfoTacDmBean.setMmsFlag(jObject.getString("mms_flag"));
			dimTermInfoTacDmBean.setGprsFlag(jObject.getString("gprs_flag"));
			dimTermInfoTacDmBean.setEdgeFlag(jObject.getString("edge_flag"));
			dimTermInfoTacDmBean.setWifiFlag(jObject.getString("wifi_flag"));
			dimTermInfoTacDmBean.setIssmartFlag(jObject.getString("issmart_flag"));
			dimTermInfoTacDmBean.setIos(jObject.getString("ios"));
			dimTermInfoTacDmBean.setIosVersion(jObject.getString("ios_version"));
			
			dimTermInfoTacDmBean.setMainScreenSize(jObject.getString("main_screen_size"));
			dimTermInfoTacDmBean.setListingTime(jObject.getString("listing_time"));
			dimTermInfoTacDmBean.setListingPrice(jObject.getString("listing_price"));
			dimTermInfoTacDmBean.setCurrentPrice(jObject.getString("current_price"));
			dimTermInfoTacDmBean.setCurrentUpTime(jObject.getString("current_up_time"));
			dimTermInfoTacDmBean.setHspaFlag(jObject.getString("hspa_flag"));
			dimTermInfoTacDmBean.setLteFddFlag(jObject.getString("lte_fdd_flag"));
			dimTermInfoTacDmBean.setLteTddFlag(jObject.getString("lte_tdd_flag"));
			dimTermInfoTacDmBean.setLteSingleCardDualFlag(jObject.getString("lte_single_card_dual_flag"));
			dimTermInfoTacDmBean.setVolteFlag(jObject.getString("volte_flag"));
			
			dimTermInfoTacDmBean.setCsfbFlag(jObject.getString("csfb_flag"));
			dimTermInfoTacDmBean.setWapFlag(jObject.getString("wap_flag"));
			dimTermInfoTacDmBean.setWwwFlag(jObject.getString("www_flag"));
			dimTermInfoTacDmBean.setGpsFlag(jObject.getString("gps_flag"));
			dimTermInfoTacDmBean.setPreCameraPix(jObject.getString("pre_camera_pix"));
			dimTermInfoTacDmBean.setBacCameraPix(jObject.getString("bac_camera_pix"));
			dimTermInfoTacDmBean.setScreenPix(jObject.getString("screen_pix"));
			dimTermInfoTacDmBean.setColorScreenDepth(jObject.getString("color_screen_depth"));
			dimTermInfoTacDmBean.setWriteInput(jObject.getString("write_input"));
			dimTermInfoTacDmBean.setTouchType(jObject.getString("touch_type"));
			
			dimTermInfoTacDmBean.setTermStyle(jObject.getString("term_style"));
			dimTermInfoTacDmBean.setJavaFlag(jObject.getString("java_flag"));
			dimTermInfoTacDmBean.setUsbFlag(jObject.getString("usb_flag"));
			dimTermInfoTacDmBean.setBluetoothFlag(jObject.getString("bluetooth_flag"));
			dimTermInfoTacDmBean.setInfraredFlag(jObject.getString("infrared_flag"));
			dimTermInfoTacDmBean.setDouCardDaulFlag(jObject.getString("dou_card_daul_flag"));
			dimTermInfoTacDmBean.setStatMonth(jObject.getString("stat_month"));
			dimTermInfoTacDmBean.setStatDate(jObject.getString("stat_date"));
			
			list.add(dimTermInfoTacDmBean);
			
		}
		return list;
	}
	
	/**
	 * @Title: getjsonbuffer
	 * @Description: TODO 获取接口请求返回的JSON信息
	 * @param @param interface_flag
	 * @param @return
	 * @param @throws JSONException    
	 * @return StringBuilder 
	 * @throws
	 */
	public StringBuilder getjsonbuffer(String interface_flag) throws JSONException{
		StringBuilder _buffer = new StringBuilder();
		//根据传递参数获取地址
		String requestUri="";
		String requestUriParmDate="";
		if(InterfaceConstant.AIBI_GET_DACP_DIM_LOGIC_AREA.equals(interface_flag)){
				//Example:requestUri="http://10.31.101.160:8080/dacp/api/query/DIM_LOGIC_AREA?sessionKey=";
				//接口表中获取接口地址
				PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
						InterfaceConstant.AIBI_GET_DACP_DIM_LOGIC_AREA);
				requestUri = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
				if (StringUtil.isNotEmpty(requestUri)) {
					log.debug("初始化 调用DACP同步DIM_LOGIC_AREA接口 "+requestUri);
				} else {
					throw new PopInterfaceException("未配置调用DACP同步DIM_LOGIC_AREA接口[" + InterfaceConstant.AIBI_GET_DACP_DIM_LOGIC_AREA + "]的接口类或接口地址");
				}
				
		}else if(InterfaceConstant.AIBI_DACP_DIM_PROD_PRODUCT_DM.equals(interface_flag)){
				//Example:requestUri="http://10.31.101.160:8080/dacp/api/query/DIM_PROD_PRODUCT_DM?sessionKey=";
				//Example:requestUriParmDate="&date=YYYYMMDD";
				//接口表中获取接口地址    请求参数里面要加获取的sessionKey和date格式YYYYMMDD
				PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
						InterfaceConstant.AIBI_DACP_DIM_PROD_PRODUCT_DM);
				requestUri = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
				requestUriParmDate=pc.getStr(PopInterfaceConfig.COL_INTERFACE_PARAM);
				if (StringUtil.isNotEmpty(requestUri)&&StringUtil.isNotEmpty(requestUriParmDate) ) {
					log.debug("初始化 调用DACP同步DIM_PROD_PRODUCT_DM接口 "+requestUri+" requestUriParmDate "+requestUriParmDate);
				} else {
					throw new PopInterfaceException("未配置调用DACP同步DIM_PROD_PRODUCT_DM接口[" + InterfaceConstant.AIBI_DACP_DIM_PROD_PRODUCT_DM + "]的接口类或接口地址");
				}
		}else if(InterfaceConstant.AIBI_DACP_DIM_TERM_INFO_TAC_MM.equals(interface_flag)){
				//Example:requestUri="http://10.31.101.160:8080/dacp/api/query/DIM_TERM_INFO_TAC_MM?sessionKey=";
				//Example:requestUriParmDate="&date=YYYYMM";
			    //接口表中获取接口地址    请求参数里面要加获取的sessionKey和date格式YYYYMM
				PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
						InterfaceConstant.AIBI_DACP_DIM_TERM_INFO_TAC_MM);
				requestUri = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
				requestUriParmDate=pc.getStr(PopInterfaceConfig.COL_INTERFACE_PARAM);
				if (StringUtil.isNotEmpty(requestUri)&&StringUtil.isNotEmpty(requestUriParmDate) ) {
					log.debug("初始化 调用DACP同步DIM_TERM_INFO_TAC_MM接口 "+requestUri+" requestUriParmDate "+requestUriParmDate);
				} else {
					throw new PopInterfaceException("未配置调用DACP同步DIM_TERM_INFO_TAC_MM接口[" + InterfaceConstant.AIBI_DACP_DIM_TERM_INFO_TAC_MM + "]的接口类或接口地址");
				}
		}
		
		String httpGetUri = "";
		PopInterfaceConfig pc = (PopInterfaceConfig) PopInterfaceConfig.dao().findById(
				InterfaceConstant.AIBI_DACP_SESSION);//AIBI_DACP_SESSION
		httpGetUri = pc.getStr(PopInterfaceConfig.COL_INTERFACE_ADDRESS);
		if (StringUtil.isNotEmpty(httpGetUri)) {
			log.debug("初始化 调用DACP获取AIBI_DACP_SESSION接口 "+httpGetUri);
		} else {
			throw new PopInterfaceException("未配置调用DACP获取AIBI_DACP_SESSION接口[" + InterfaceConstant.AIBI_DACP_SESSION + "]的接口类或接口地址");
		}
		
		BufferedReader br = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		//Example:"http://10.31.101.160:8080/dacp/api?method=user.login&USER_ACCOUNT=bi_pop&USER_PWD=bdx_bi_pop"
		HttpGet httpGet = new HttpGet(httpGetUri);
		int timeOut = 100 * 1000;// 设置请求等待超时时间 hive数据11万条耗时57秒
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse res = null;
		HttpContext localContext = new BasicHttpContext();// 保持同一个session
		
		try {
			res = httpclient.execute(httpGet, localContext);
			HttpEntity entity = null;
			httpGet.getAllHeaders();
			if (res.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
				entity = res.getEntity();
				br = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"));
				String line = "";
				for (line = br.readLine(); line != null; line = br.readLine()) {
					_buffer.append(line);
				}
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (null != res) {
					res.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String sessionKey = null;
		if (_buffer.length() > 0) {// {"response":{"state":{"code":"0","message":"成功"},"SESSION_KEY":"rpIoWoZky9SUS7iukHWMr2GzEXkgsJ1R"}}
			JSONObject jsonObject = new JSONObject(_buffer.toString());
			JSONObject jsonResp = jsonObject.getJSONObject("response");
			sessionKey = jsonResp.getString("SESSION_KEY");
		}

		URI uri = null;
		try {
			requestUri = requestUri + sessionKey;
			if(requestUriParmDate.length()>0){
				requestUri = requestUri + requestUriParmDate;
			}
			uri = new URI(requestUri);
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		httpGet.setURI(uri);
		CloseableHttpResponse res1 = null;
		_buffer.delete(0, _buffer.length());
		// long time1,time2,time = 0;
		try {
			// time1 = System.currentTimeMillis();
			res1 = httpclient.execute(httpGet, localContext);
			// time2=System.currentTimeMillis();
			// time=(time2-time1)/1000;
			HttpEntity entity = null;
			if (res1.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
				entity = res1.getEntity();
				BufferedReader br1 = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"));
				String line = "";
				for (line = br1.readLine(); line != null; line = br1.readLine()) {
					_buffer.append(line);
				}
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != br) {
					br.close();
				}
				if (null != res) {
					res.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return _buffer;
	}
	

}
