package com.ailk.bdx.pop.adapter.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 一致性Hash算法 算法详解：http://blog.csdn.net/sparkliang/article/details/5279393
 * 算法实现：https://weblogs.java.net/blog/2007/11/27/consistent-hashing
 *
 * @author Tom White
 *
 * @param <T>
 *            节点类型
 */
public class ConsistentHash<T> {
	/** Hash计算对象，用于自定义hash算法 */
	HashFunc hashFunc;
	/** 复制的节点个数 */
	private final int numberOfReplicas;
	/** 一致性Hash环 */
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	private final ReadWriteLock readReadWriteLock = new ReentrantReadWriteLock();

	private static volatile ConsistentHash instance;

	public static ConsistentHash getInstance(int numberOfReplicas, Collection nodes){
		if(instance == null){
			synchronized(ConsistentHash.class){
				if(instance == null){
					instance = new ConsistentHash(numberOfReplicas,nodes);
				}
			}
		}
		return instance;
	}

	/**
	 * 构造，使用Java默认的Hash算法
	 *
	 * @param numberOfReplicas
	 *            复制的节点个数，增加每个节点的复制节点有利于负载均衡
	 * @param nodes
	 *            节点对象
	 */
	private ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		this.hashFunc = new HashFunc() {
			@Override
			public Integer hash(Object key) {
				String data = key.toString();
				// 默认使用FNV1hash算法
				final int p = 16777619;
				int hash = (int) 2166136261L;
				for (int i = 0; i < data.length(); i++) {
					hash = (hash ^ data.charAt(i)) * p;
				}
				hash += hash << 13;
				hash ^= hash >> 7;
				hash += hash << 3;
				hash ^= hash >> 17;
				hash += hash << 5;
				return hash;
			}
		};
		// 初始化节点
		for (T node : nodes) {
			add(node);
		}
	}

	private ConsistentHash(HashFunc hashFunc, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunc = hashFunc;
		this.numberOfReplicas = numberOfReplicas;
		// 初始化节点
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 增加节点 每增加一个节点，就会在闭环上增加给定复制节点数 例如复制节点数是2，则每调用此方法一次，增加两个虚拟节点，这两个节点指向同一Node
	 * 由于hash算法会调用node的toString方法，故按照toString去重
	 *
	 * @param node
	 */
	public void add(T node) {
		readReadWriteLock.writeLock().lock();
		try{
			for (int i = 0; i < numberOfReplicas; i++) {
				circle.put(hashFunc.hash(node.toString() + i), node);
			}
		}finally{
			readReadWriteLock.writeLock().unlock();
		}
	}

	/**
	 * 移除节点的同时移除相应的虚拟节点
	 *
	 * @param node
	 */
	public void remove(T node) {
		readReadWriteLock.writeLock().lock();
		try{
			for (int i = 0; i < numberOfReplicas; i++) {
				circle.remove(hashFunc.hash(node.toString() + i));
			}
		}finally{
			readReadWriteLock.writeLock().unlock();
		}
	}

	public void clear(){
		readReadWriteLock.writeLock().lock();
		try{
			circle.clear();
		}finally{
			readReadWriteLock.writeLock().unlock();
		}
	}

	public boolean isEmpty(){
		readReadWriteLock.readLock().lock();
		try{
			return circle.size() < 1;
		}finally{
			readReadWriteLock.readLock().unlock();
		}
	}

	/**
	 * 获得一个最近的顺时针节点
	 *
	 * @param key
	 *            为给定键取Hash，取得顺时针方向上最近的一个虚拟节点对应的实际节点
	 * @return 节点对象
	 */
	public T get(Object key) {
		readReadWriteLock.readLock().lock();
		try{
			if (circle.isEmpty()) {
				return null;
			}
			int hash = hashFunc.hash(key);
			if (!circle.containsKey(hash)) {
				SortedMap<Integer, T> tailMap = circle.tailMap(hash); // 返回此映射的部分视图，其键大于等于
				hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
			}
			// 正好命中
			return circle.get(hash);
		}finally{
			readReadWriteLock.readLock().unlock();
		}
	}

	/**
	 * Hash算法对象，用于自定义hash算法
	 *
	 * @author guoyj
	 *
	 */
	public interface HashFunc {
		public Integer hash(Object key);
	}

	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("A");
		set.add("B");
		set.add("C");
		set.add("D");

		Map<String, Integer> map = new HashMap<String, Integer>(4); //个数统计map
		ConsistentHash<String> consistentHash = new ConsistentHash<String>(1000, set);

		BufferedReader br = null;
		String data = null;
		long lineCount = 0;
		long readFileStartTime = System.currentTimeMillis();
		try{
			//br = new BufferedReader(new InputStreamReader(new FileInputStream("D:/workspace/TestMap/nums-jx.txt")));
			//while((data = br.readLine()) != null){
			for(long i=11111111111l; i< 18999999999l; i++){
				String key = consistentHash.get(String.valueOf(i));
				if (map.containsKey(key)) {
					map.put(key, map.get(key) + 1);
				} else {
					map.put(key, 1);
				}
				++lineCount;
			}
			//}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try{
					br.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("文件条数count="+lineCount+
					"，耗时:"+ (System.currentTimeMillis()-readFileStartTime)+"ms");
		}

//		int count = 10000;
//		for (int i = 0; i < count; i++) {
//			String key = consistentHash.get(i);
//			if (map.containsKey(key)) {
//				map.put(consistentHash.get(i), map.get(key) + 1);
//			} else {
//				map.put(consistentHash.get(i), 1);
//			}
////			 System.out.println(key);
//		}
		showServer(map);
		map.clear();
		map = null;
		/*consistentHash.remove("A");
		System.out.println("------- remove A");
		for (int i = 0; i < count; i++) {
			String key = consistentHash.get(i);
			if (map.containsKey(key)) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// System.out.println(key);
		}
		showServer(map);
		map.clear();

		consistentHash.add("E");
		System.out.println("------- add E");
		for (int i = 0; i < count; i++) {
			String key = consistentHash.get(i);
			if (map.containsKey(key)) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// System.out.println(key);
		}
		showServer(map);
		map.clear();

		consistentHash.add("F");
		System.out.println("------- add F    业务量加倍");
		for (int i = 0; i < count; i++) {
			String key = consistentHash.get(i);
			if (map.containsKey(key)) {
				map.put(consistentHash.get(i), map.get(key) + 1);
			} else {
				map.put(consistentHash.get(i), 1);
			}
			// System.out.println(key);
		}
		showServer(map);*/
	}

	public static void showServer(Map<String, Integer> map) {
		for (Entry<String, Integer> m : map.entrySet()) {
			System.out.println("服务器 " + m.getKey() + "----" + m.getValue() + "个");
		}
	}

}
