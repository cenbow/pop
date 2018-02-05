package com.ai.bdx.pop.adapter.socket.buffer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.asiainfo.biframe.utils.config.Configure;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 接收从pop_adapter组件发来的开户和锁网事件消息缓存
 * @author zhangyb5
 *
 */
public class PopBuffer {
	private static final int BUFFER_SIZE = 1024 * 1024;
	private static Disruptor<PopEvent> disruptor;
	private static ExecutorService pool;
	private int NUM_WORKERS;//消费线程数
	private boolean enableBuffer = true;
	private PopEventHandler handler = null;

	public static class EventBufferHoder {
		public final static PopBuffer instance = new PopBuffer();
	}

	private PopBuffer() {
		if ("0".equalsIgnoreCase(Configure.getInstance().getProperty("DISRUPTOR_EVENTBUFFER_FLAG"))) {
			enableBuffer = false;
		}
		if (enableBuffer) {
			try {
				NUM_WORKERS = Integer.valueOf(Configure.getInstance()
						.getProperty("DISRUPTOR_EVENTBUFFER_CUSTOMOR_SIZE"));
			} catch (Exception e) {
				NUM_WORKERS = Runtime.getRuntime().availableProcessors() - 1;
			}
			//等待策略
			WaitStrategy waitStrategy = null;
			try {
				waitStrategy = (WaitStrategy) Class.forName(
						"com.lmax.disruptor."
								+ Configure.getInstance().getProperty("DISRUPTOR_EVENTBUFFER_WAIT_STRATEGY"))
						.newInstance();
			} catch (Exception e) {
				waitStrategy = new BlockingWaitStrategy();
			}
			pool = Executors.newFixedThreadPool(NUM_WORKERS);
			disruptor = new Disruptor<PopEvent>(PopEvent.EVENT_FACTORY, BUFFER_SIZE, pool, ProducerType.MULTI,
					waitStrategy);
			PopEventHandler[] workHandlers = new PopEventHandler[NUM_WORKERS];
			for (int i = 0; i < workHandlers.length; i++) {
				workHandlers[i] = new PopEventHandler();
			}
			if (NUM_WORKERS == 1) {//消费工人数=1,系统将使用单消费BatchEventProcessor
				disruptor.handleEventsWith(workHandlers);
			} else {//消费工人数>1,使用消费工人线程池方式多消费,多个WorkProcessor
				disruptor.handleEventsWithWorkerPool(workHandlers);
			}
		} else {
			handler = new PopEventHandler();
		}
	}

	public static PopBuffer getInstance() {
		return EventBufferHoder.instance;
	}

	public void start() {
		if (enableBuffer) {
			disruptor.start();
		}
	}

	public void shutdown() {
		if (enableBuffer) {
			pool.shutdown();
			disruptor.shutdown();
		}
	}

	private EventTranslatorOneArg<PopEvent, String> translator = new EventTranslatorOneArg<PopEvent, String>() {
		@Override
		public void translateTo(PopEvent event, long sequence, String msg) {
			event.setValue(msg);
		}
	};

	public void publish(String msg) {
		if (enableBuffer) {
			disruptor.publishEvent(translator, msg);
		} else {
			try {
				handler.sendEventData(msg);
			} catch (Exception e) {
			}
		}
	}
}