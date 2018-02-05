package com.ai.bdx.pop.dao;

import java.sql.SQLNonTransientException;
import java.util.List;

import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;

/**
 * CPE用户销户是变更状态到mysql  DAO接口
 * @author 林
 *
 */
public interface CpeDeleteDao {
	//批量变更用户状态
	public int[] updateCpeUserStatus(List<CpeUserInfo> list)throws MysqlDataAccessExcetion,FileErrorException,Exception;
	//根据用户id查询用户信息
	public CpeUserInfo queryCpeUserInfoBySubsid(String subsid);
	//根据用户id查询用户的策略号（带宽与之对应的那个）
	public List queryStrategyCodeBySubsid(String subsid)throws MysqlDataAccessExcetion;
	/**
	 * 删除一个cpe用户
	 * @param list
	 * @return
	 * @throws SQLNestedException
	 * @throws MysqlDataAccessExcetion
	 * @throws FileErrorException
	 * @throws Exception
	 */
	public int deleteCpeUser(CpeUserInfo cui)throws MysqlDataAccessExcetion;

	/**
	 * 销户成功后删除锁网
	 * @param cui
	 */
	public void deleteUserLockedInfo(CpeUserInfo cui);
	
	
}