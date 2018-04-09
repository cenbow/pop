package com.ailk.bdx.pop.adapter.dispatch.impl;

import java.util.HashSet;
import java.util.Set;

import com.ailk.bdx.pop.adapter.dispatch.IDispatchStrategy;
import com.ailk.bdx.pop.adapter.util.ConsistentHash;

/**
 * 一致性hash实现的分发策略
 *
 * @author guoyj
 *
 */
public class ConsistentHashDispatchStrategy extends IDispatchStrategy {
	private final ConsistentHash<Integer> consistentHash;

	public ConsistentHashDispatchStrategy(){
		this(0);
	}

	public ConsistentHashDispatchStrategy(int n) {
		Set<Integer> serverSet = new HashSet<Integer>();//初始空集群
		for (int i = 0; i < n; i++) {
			serverSet.add(i+1);
		}
		consistentHash = ConsistentHash.getInstance(1000, serverSet);
	}

	@Override
	public int dispatch(Object key) {
		if (key == null) {
			throw new IllegalArgumentException("目标键值为空");
		}
		if (consistentHash.isEmpty()) {
			return -1;
		}
		return consistentHash.get(key);

	}

	@Override
	public void clusterStateChange(int[] newIndexArray){
		consistentHash.clear();
		for (int i = 0, size = newIndexArray.length; i < size; i++) {
			int value = newIndexArray[i];
			consistentHash.add(value);
		}
	}

}
