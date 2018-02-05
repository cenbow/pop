package com.ai.bdx.pop.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class PopParameterizedRowMapper<T> implements ParameterizedRowMapper<T> {
	private Class<T> clazz;

	public PopParameterizedRowMapper(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T mapRow(ResultSet rs, int paramInt) throws SQLException {
		try {
			T obj = clazz.newInstance();
			PopUtil.resultSetToBean(rs, obj);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
