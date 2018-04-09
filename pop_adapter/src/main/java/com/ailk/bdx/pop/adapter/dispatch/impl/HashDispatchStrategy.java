package com.ailk.bdx.pop.adapter.dispatch.impl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ailk.bdx.pop.adapter.dispatch.IDispatchStrategy;

/**
 * Hash路由分发策略（默认实现算法,需要单例）
 *
 * @author guoyj
 *
 */
public class HashDispatchStrategy extends IDispatchStrategy {
	// 集群中初始状态节点数组
	private static int[] initIndexArray;
	// 当前节点数组
	private volatile int[] aliveIndexArray;
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public HashDispatchStrategy() {
		this(0);//初始0集群
	}

	public HashDispatchStrategy(int size){
		initIndexArray = new int[size];
		aliveIndexArray = new int[size];
		for (int i = 0; i < size; i++) {
			initIndexArray[i] = i+1;
			aliveIndexArray[i] = i+1;
		}
	}

	@Override
	public int dispatch(Object key) {
		if (key == null) {
			throw new IllegalArgumentException("目标键值为空");
		}

		int targetDispatch = 0;
		readWriteLock.readLock().lock();
		try {
			int initConsumerSize = initIndexArray.length;
			int currentConsumerSize = aliveIndexArray.length;
			if (currentConsumerSize < 1 || initConsumerSize < 1) {
				return -1;
			}

			if (currentConsumerSize == initConsumerSize) { // 集群稳定
				targetDispatch = indexQuery(key, initIndexArray);
			} else if (currentConsumerSize < initConsumerSize) { // 集群宕机
				// 先按照初始节点计算Index
				int checkDispatch = indexQuery(key, initIndexArray);

				boolean isDead = true; // 判断索引是否无效
				for (int i = 0; i < currentConsumerSize; i++) {
					// 如果是或者的节点，直接返回索引
					if (checkDispatch == aliveIndexArray[i]) {
						isDead = false;
						return checkDispatch;
					}
				}
				// 如果不在活着的节点中，alive节点组成ConsistentHash环,计算新的索引,查找对应临近位置,重新分配
				if (isDead) {
					targetDispatch = indexQuery(key, aliveIndexArray);
				}
			} else { // 集群新加机器
				targetDispatch = indexQuery(key, aliveIndexArray);

				// 状态机重置
				initIndexArray = new int[currentConsumerSize];
				System.arraycopy(aliveIndexArray, 0, initIndexArray, 0,
						currentConsumerSize);
			}
		} finally {
			readWriteLock.readLock().unlock();
		}
		return targetDispatch;
	}

	static int hash(int h) {
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) {
		return Math.abs(h % length);
	}

	/**
	 * hash计算
	 *
	 * @param key
	 * @param size
	 * @return
	 */
	private int doCalculate(Object key, int size) {
		int hash = hash(key.hashCode());
		return indexFor(hash, size);
	}

	private int indexQuery(Object key, int[] checkArray) {
		int index = doCalculate(key, checkArray.length);
		return checkArray[index];
	}

	/**
	 * 集群状态监听，自动重置aliveIndexArray AMQP异步调用,格式{1,2,3,4}
	 */
	/*public void listenClusterState(String clusterStates) {
		if (clusterStates != null && clusterStates.length() > 0) {
			readWriteLock.writeLock().lock();
			try {
				JSONArray jsonArray = JSON.parseArray(clusterStates);
				int size = jsonArray.size();
				aliveIndexArray = new int[size];
				for (int i = 0; i < size; i++) {
					aliveIndexArray[i] = jsonArray.getIntValue(i);
				}
			} finally {
				readWriteLock.writeLock().unlock();
			}
		}
	}*/

	@Override
	public void clusterStateChange(int[] newIndexArray){
		readWriteLock.writeLock().lock();
		try {
			aliveIndexArray = newIndexArray;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}
	
	public static void main(String[] argv) {
		System.out.println("aaa");
	}
}
