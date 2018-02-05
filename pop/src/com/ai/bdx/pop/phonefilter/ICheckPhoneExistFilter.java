package com.ai.bdx.pop.phonefilter;

/**
 * 检查号码是否存在过滤接口
 * @author guoyj
 *
 */
public interface ICheckPhoneExistFilter {

	/**
	 * 检查号码在key对应map中是否存在，不则加入
	 * @param tabNameKey
	 * @param targetPhone
	 * @return
	 */
	public boolean checkExist(String tabNameKey,String targetPhone);

	/**
	 * 删除tabNameKey对一个缓存
	 * @param tabNameKey
	 * @return
	 */
	public Object removeDataByKey(String tabNameKey);

}
