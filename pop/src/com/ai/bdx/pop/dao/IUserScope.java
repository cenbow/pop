package com.ai.bdx.pop.dao;

/**
 * 用户地市权限接口
 * @author guoyj
 *
 */
public abstract interface IUserScope {

	public abstract String getStaffId();

	public abstract String getProvScope();

	public abstract String getCityScope();

	public abstract String getCountryScope();

	public abstract String getAreaScope();

	public abstract String getSub1AreaScope();

	public abstract String getSub2AreaScope();

}
