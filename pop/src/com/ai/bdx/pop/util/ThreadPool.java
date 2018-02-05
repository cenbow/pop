package com.ai.bdx.pop.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jfinal.kit.PropKit;

/**
 *
 * 线程池(控制线程执行，保证系统性能)
 *
 * @author wang lei
 *
 */
public class ThreadPool {
	private static final Logger log = LogManager.getLogger();
	/** The service. */
	private final ExecutorService service;

	/**
	 * The Class ThreadPoolHolder.
	 */
	static class ThreadPoolHolder {

		/** The instance. */
		static ThreadPool instance = new ThreadPool();
	}

	/**
	 * 单例构造方法.
	 *
	 * @return single instance of ThreadPool
	 */
	public static ThreadPool getInstance() {
		return ThreadPoolHolder.instance;
	}

	/**
	 * 私有构造方法
	 */
	private ThreadPool() {
		// 根据处理器数量创建线程池。虽然多线程并不保证能够提升性能，但适量地开线程一般可以从系统骗取更多资源。
		int threadNum = Runtime.getRuntime().availableProcessors() * 2;
		//根据配置文件配置数量
		try {
			threadNum = PropKit.getInt("THREAD_NUM", threadNum);
		} catch (Exception e) {
			//不做处理
		}

		service = Executors.newFixedThreadPool(threadNum);
	}

	/**
	 * 直接构造线程队列方法(不能保证单例，不推荐系统中使用).
	 *
	 * @param nThreads the n threads
	 * @deprecated
	 */
	@Deprecated
	public ThreadPool(int nThreads) {
		service = Executors.newFixedThreadPool(nThreads);
	}

	/**
	 * 获得现在队列大小.
	 *
	 * @return int
	 */
	public int getSize() {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) service;
		return pool.getQueue().size();
	}

	/*
	 *
	 *执行任务
	 */
	/**
	 * Execute.
	 *
	 * @param r the r
	 */
	public void execute(Runnable r) {
		try {
			service.execute(r);
		} catch (Exception ex) {
			log.error("", ex);
		}
	}

	/*
	 *
	 *执行任务 支持执行Callable类型的任务
	 */
	/**
	 * Execute.
	 *
	 * @param r the r
	 */
	public void execute(Callable r) throws Exception {
		try {
			service.submit(r);
		} catch (Exception ex) {
			log.error("", ex);
			throw ex;
		}
	}

	/**
	 * Shutdown now.
	 *
	 * @return the list
	 */
	public List<Runnable> shutdownNow() {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) service;
		return pool.shutdownNow();
	}
}
