package com.ailk.bdx.pop.adapter.dispatch;

/**
 * 分发策略抽象类
 *
 * @author guoyj
 *
 */
public abstract class IDispatchStrategy {


	/**
	 * 分发方法
	 * @param key 目标对象的key值
	 * @return 返回目标归属位置索引
	 */
	public abstract int dispatch(Object key);

	/**
	 * 集群状态变更
	 * @param newIndexArray
	 */
	public abstract void clusterStateChange(int[] newIndexArray);
}
