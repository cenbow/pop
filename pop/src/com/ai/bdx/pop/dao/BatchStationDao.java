package com.ai.bdx.pop.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.ai.bdx.pop.bean.ImportCpeLacCiBean;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;

public interface BatchStationDao {
	/**
	 * 根据用户id查询用户的策略号（带宽与之对应的那个）
	 * @param subsid
	 * @return
	 * @throws MysqlDataAccessExcetion
	 */
	public List queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion;
	/**
	 * 将基站批量割接结果插入或变更日志表中
	 * @param list
	 * @param success
	 * @return
	 * @throws MysqlDataAccessExcetion
	 * @throws Exception
	 */
	public int[]  insertORupdateUserResetLog(List<BatchStationBean> list, List<String> success)throws MysqlDataAccessExcetion,Exception;
	/**
	 * 查询基站割接日志表中是否有记录
	 * @param mobile
	 * @return
	 */
	public int queryUserResetLogByMobile(String mobile);
	/**
	 * 基站割接成功的手机号更新redis后再更新mysql中锁网表
	 * @param beanListForSuccess
	 * @return
	 * @throws MysqlDataAccessExcetion
	 * @throws Exception
	 */
	public int[] updateUserLockedInfo(List<BatchStationBean> beanListForSuccess)throws MysqlDataAccessExcetion,Exception;
	/**
	 * 基站割接成功的手机号更新redis后再更新mysql中日志表，成功的更新为1
	 * @param success
	 * @return
	 * @throws MysqlDataAccessExcetion
	 * @throws Exception
	 */
	int[] updateUserResetLog(final List<BatchStationBean>  beanListForSuccess) throws MysqlDataAccessExcetion,Exception;
	/**
	 * 参数重置查询未重置，批量基站重置查询dao接口
	 * @param startTime
	 * @param endTime
	 * @param resetFalg
	 * @return
	 */
	public List<Map<String,Object>> queryBatchStationInfosForNoReset(String startTime,String endTime,Integer resetFalg);
	/**
	 * 参数重置查询已重置，批量基站重置查询dao接口
	 * @param startTime
	 * @param endTime
	 * @param resetFalg
	 * @return
	 */
	public List<Map<String,Object>> queryBatchStationInfosForReset(String startTime,String endTime,Integer resetFalg);
	
	
/**
 * 基站割接上传文件存入mysql的cpe_station_chg_import_info表中
 * @param list
 * @return
 * @throws MysqlDataAccessExcetion
 * @throws Exception
 */
	public int[] cpeStationChgImport(List<ImportBatchCutoverBean> list)throws MysqlDataAccessExcetion,Exception ;
	
	/**
	 * 基站割接上传文件存入cpe_user_reset_log表中
	 * @param list
	 * @return
	 * @throws MysqlDataAccessExcetion
	 * @throws Exception
	 */
	public int[] cpeUserResetLogImport(List<BatchStationBean> list)throws MysqlDataAccessExcetion,Exception ;
	
	/**
	 * 根据原ac和ci及基站id到小区同步表中查询lac_ci的十六进制和十进制
	 * @param stationCode
	 * @param lac_dec_id
	 * @param ci_dec_id
	 * @return
	 */
	public List<Map<String,Object>> queryLacCiByXiaoQuTable(String stationCode,String lac_dec_id,String ci_dec_id);
	
	/**
	 * 根据原来的lac_ci和新的lac_ci主键在导入表查询
	 * @param lac_ci_hex_code_old
	 * @param lac_ci_hex_code_new
	 * @return
	 */
	public List<Map<String,Object>> queryLacCiByImportTable(String lac_ci_hex_code_old,String lac_ci_hex_code_new);
	
	/**
	 * 根据原来的lac_ci和新的lac_ci在日志表查询
	 * @param lac_ci_hex_code_old
	 * @param lac_ci_hex_code_new
	 * @return
	 */
	public List<Map<String,Object>> queryResetFlagByLogTable(String lac_ci_hex_code_old,String lac_ci_hex_code_new);

	/**
	 * 根据原来的lac_ci和新的lac_ci多表关联获得日志信息，为文件导入时插入日志信息做准备
	 * @param lac_ci_hex_code_old
	 * @param lac_ci_hex_code_new
	 * @return
	 */
	public List<BatchStationBean> getResetLogBylac_ci(String lac_ci_hex_code_old,String lac_ci_hex_code_new);

	/**
	 *查询时更新割接日志表-导入表中有的日志表中没有
	 */
	public List<Map<String,Object>> queryBatchStationTableBefore();
/**
 * 根据ids查询勾选的集合
 * @param subsid
 * @param productNo
 * @param lac_ci_old
 * @return
 */
	public List<Map<String,Object>> getSelectedBatchStationInfosByIds(String subsid,String productNo,String user_location);

	/**
	 * 查询时更新割接日志表-锁网表中有的日志表中没有
	 * @return
	 */
	public List<Map<String,Object>>queryBatchStationLogTableBefore();




}
