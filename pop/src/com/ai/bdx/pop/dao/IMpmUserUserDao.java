package com.ai.bdx.pop.dao;

import java.util.List;

import com.ai.bdx.pop.model.privilege.LkgStaff;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.sysmanage.model.SearchCondition;

public interface IMpmUserUserDao {

	public abstract List<IUser> listUsers();

	public abstract IUser findById(String id);

	public abstract LkgStaff findEntityById(String id);

	public abstract List<IUser> findAll(SearchCondition paramSearchCondition);
}
