package com.ai.bdx.pop.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import redis.clients.jedis.exceptions.JedisConnectionException;

import com.asiainfo.biapp.pop.Exception.DelSubscriberException;
import com.asiainfo.biapp.pop.Exception.FileErrorException;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.model.CpeUserInfo;
/**
 * CPE用户销户是变更状态到mysql  SERVICE接口
 * @author 林
 *
 */
public interface CpeDeleteService {
	/**
	 * 删除多个cpe用户
	 * @param list
	 * @return
	 * @throws SQLNestedException
	 * @throws JedisConnectionException
	 * @throws MysqlDataAccessExcetion
	 * @throws FileErrorException
	 * @throws Exception
	 */
	public Map updateCpeUserStatus(List<CpeUserInfo> list)throws JedisConnectionException,MysqlDataAccessExcetion,FileErrorException,Exception;
	public List queryStrategyCodeBySubsid(String subsid)throws Exception;
	/**
	 * 删除一个cpe用户
	 * @param cui
	 * @return
	 * @throws SQLNestedException
	 * @throws JedisConnectionException
	 * @throws MysqlDataAccessExcetion
	 * @throws FileErrorException
	 * @throws RemoteException 
	 * @throws Exception
	 */
	public String cpeDeleteCpeUser(CpeUserInfo cui)throws MysqlDataAccessExcetion,FileErrorException,DelSubscriberException,SQLException, RemoteException;
}
