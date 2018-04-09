package com.ailk.bdx.pop.adapter.buffer.impl.noblock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.buffer.IDataBuffer;
import com.ailk.bdx.pop.adapter.util.SpringContext;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor实现的缓冲
 *
 * @author guoyj
 *
 */
public class DisruptorBuffer implements IDataBuffer {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;
	private final ExecutorService exec;
	private final Disruptor<ValueEvent> disruptor;
	private final int ringBufferSize;

	public DisruptorBuffer() {
		this(DEFAULT_BUFFER_SIZE, null, -1);
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

	@Override
	public void start() {
		disruptor.start();
	}

	@Override
	public void shutdown() {
		disruptor.shutdown();
		exec.shutdown();
	}

	EventTranslatorOneArg<ValueEvent, Message> eventTranslator = new EventTranslatorOneArg<ValueEvent, Message>() {
		@Override
		public void translateTo(ValueEvent event, long sequence, Message msg) {
			event.setValue(msg);
		}
	};

	@Override
	public void push(Message msg) {
		disruptor.publishEvent(eventTranslator, msg);
	}
}
