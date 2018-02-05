package com.asiainfo.biapp.pop.Exception;

import org.springframework.dao.DataAccessException;

public class MysqlDataAccessExcetion extends DataAccessException {

	/**
	 * Mysql异常
	 */

	public MysqlDataAccessExcetion(String msg) {
		super(msg);
		
	}

}
