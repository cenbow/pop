package com.ai.bdx.pop.dao;

import java.util.List;

import com.asiainfo.biframe.privilege.IUserCompany;

public interface IMpmUserDeptDao {

	public abstract List<IUserCompany> getDeptAll() throws Exception;

	public abstract IUserCompany getDeptById(String paramString) throws Exception;
}
