package com.ai.bdx.pop.buffer;

import java.io.File;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.util.ClassLookupUtils;
import com.ai.bdx.pop.util.SpringContext;
import com.google.common.base.Strings;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor消息无阻塞转发，可配置多个handler实现一对多处理
 * @author guoyj
 *
 */
public class DisruptorBuffer implements IDataBuffer{
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;
	private final ExecutorService exec;
	private final Disruptor<ValueEvent> disruptor;
	private final int ringBufferSize;
	
	public static class DisruptorBufferHoder {
		public final static DisruptorBuffer instance = new DisruptorBuffer();
	}
 

	private DisruptorBuffer() {
		this(DEFAULT_BUFFER_SIZE, "SleepingWaitStrategy", -1);
	}

	public static DisruptorBuffer getInstance() {
		return DisruptorBufferHoder.instance;
	}
	
	
	@SuppressWarnings("unchecked")
	public DisruptorBuffer(int ringBufferSize, String strWaitStrategy, int customerSize) {
		//缓冲环大小
		this.ringBufferSize = ringBufferSize > 0 ? ringBufferSize : DEFAULT_BUFFER_SIZE;
		//等待策略
		WaitStrategy waitStrategy = null;
		try {
			if (StringUtils.isNotEmpty(strWaitStrategy)) {
				waitStrategy = (WaitStrategy) Class.forName("com.lmax.disruptor." + strWaitStrategy).newInstance();
			}
		} catch (Exception e) {
			waitStrategy = new BlockingWaitStrategy();
		}
		//消费线程大小
		customerSize = customerSize > 0 ? customerSize : Runtime.getRuntime().availableProcessors() - 1;
	
		exec = Executors.newFixedThreadPool(customerSize);
		disruptor = new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, this.ringBufferSize, exec, ProducerType.MULTI,
				waitStrategy);
	
		if (customerSize == 1) {
			//消费工人数=1,系统将使用单消费BatchEventProcessor
			EventHandler<ValueEvent> eventHandler = SpringContext.getBean("eventHandler", EventHandler.class);
			disruptor.handleEventsWith(eventHandler);
		} else {
			//消费工人数>1,使用消费工人线程池方式多消费,多个WorkProcessor
			WorkHandler<ValueEvent>[] workHandlers = new WorkHandler[customerSize];
			for (int i = 0; i < workHandlers.length; i++) {
				workHandlers[i] = SpringContext.getBean("eventHandler", WorkHandler.class);
			}
			disruptor.handleEventsWithWorkerPool(workHandlers);
		}
	}

	public void start() {
		disruptor.start();
	}

	public void shutdown() {
		disruptor.shutdown();
		exec.shutdown();
	}

	EventTranslatorOneArg<ValueEvent, JSONObject> eventTranslator = new EventTranslatorOneArg<ValueEvent, JSONObject>() {
		@Override
		public void translateTo(ValueEvent event, long sequence, JSONObject msg) {
			event.setValue(msg);
		}
	};

	/**
	 * 发送消息方法
	 * @param msg
	 */
	public void push(JSONObject msg) {
		disruptor.publishEvent(eventTranslator, msg);
	}
}
