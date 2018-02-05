package com.ai.bdx.pop.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
/**
 * 基站批量割接service接口
 * @author 林
 *
 */
public interface BatchStationService {
	/**
	 * 批量参数重置
	 * @param beanList
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws MysqlDataAccessExcetion
	 * @throws JedisConnectionException
	 * @throws Exception
	 */
	public Map<String,Object> cpeBsBatchcg(List<BatchStationBean> beanList,List<Map<String,Object>> list) throws SQLException, MysqlDataAccessExcetion,JedisConnectionException, Exception;
	/**
	 * 根据用户id查询用户的策略号（带宽与之对应的那个）
	 * @param subsid
	 * @return
	 * @throws MysqlDataAccessExcetion
	 */
	public List queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion;
	
	/**
	 * 查询基站割接日志表中是否有记录
	 * @param mobile
	 * @return
	 */
		public int queryUserResetLogByMobile(String mobile);
	/**
	 * 将List<BatchStationBean>集合转换成调用pcc接口的集合格式
	 * @param list
	 * @return
	 */
		public List<Map<String, Object>> batchStation2List(List<BatchStationBean> list);
	//测试
		public   List<BatchStationBean>  batchTest();
	/**
	 * 参数重置查询，批量基站重置查询service接口
	 * @param startTime
	 * @param endTime
	 * @param resetFalg
	 * @return
	 */
	public List<BatchStationBean> getBatchStationInfos(String startTime,String endTime,Integer resetFalg);

	/**
	 * 参数重置查询，批量基站重置查询service接口
	 * @param startTime
	 * @param endTime
	 * @param resetFalg
	 * @return
	 */
	public List<BatchStationBean> getBatchStationInfosForNoReset(String startTime,String endTime,Integer resetFalg);
	/**
	 * 参数重置查询，批量基站重置查询service接口
	 * @param startTime
	 * @param endTime
	 * @param resetFalg
	 * @return
	 */
	public List<BatchStationBean> getBatchStationInfosForReset(String startTime,String endTime,Integer resetFalg);
	/**
	 * 上传文件service
	 * @param stationExcel
	 */
	public  String cpeBatchStationImport(List<ImportBatchCutoverBean> stationExcel) throws MysqlDataAccessExcetion, Exception;
	
	/**
	 *更新割接日志表-导入表中有的日志表中没有
	 */
	public void updateBatchStationTableBefore();
	
	/**
	 *更新割接日志表-锁网表中有的日志表中没有
	 */
	public void updateBatchStationLogTableBefore();
	
	/**
	 * 根据ids查询勾选的BatchStationBean集合
	 * @param subsid
	 * @param productNo
	 * @param lac_ci_old
	 * @return
	 */
	public BatchStationBean getSelectedBatchStationInfosByIds(String subsid,String productNo,String user_location);

	/**
	 * 获取成功的基站割接bean
	 * @param beanList
	 * @param success
	 * @return
	 */
	public List<BatchStationBean> getBeanListForSuccess(List<BatchStationBean>beanList,List<String> success);
/**
 * 基站割接成功后更新对应的redis小区位置信息
 * @param beanListForSuccess
 */
	public void updateRedisInfoForSuccess(List<Map<String,Object>> mobileListForSuccess);

	/**
	 * 成功的手机mobileList
	 * @param mobileList
	 * @param success
	 * @return
	 */
	public List<Map<String, Object>> getMobileListForSuccess(List<Map<String, Object>>mobileList,List<String> success);
	
	/**
	 * 批量基站变更
	 * @param beanList
	 * @return 
	 * @throws Exception
	 */
	public List<String> cpeBatchStationChange(List<BatchStationBean> beanList) throws Exception;

}
