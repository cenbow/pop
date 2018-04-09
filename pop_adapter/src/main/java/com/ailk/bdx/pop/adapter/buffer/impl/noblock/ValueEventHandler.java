package com.ailk.bdx.pop.adapter.buffer.impl.noblock;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * Disruptor消费者
 * @author guoyj
 *
 */
public class ValueEventHandler implements EventHandler<ValueEvent>, WorkHandler<ValueEvent> {
	private IDataHandle dataHandle;

	public void setDataHandle(IDataHandle dataHandle) {
		this.dataHandle = dataHandle;
	}
	/**
	 * @param event 生产者向Disruptor发布资源生产的事件
	 * @param sequence 这个事件在ringbuffer中的序列号
	 * @param endOfBatch 指明该事件是不是ringbuffer中的最后一个事件
	 * @throws Exception
	 */
	@Override
	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		if(event != null){
			Message value = event.getValue();
			dataHandle.handle(value);
		}
	}
	@Override
	public void onEvent(ValueEvent event) throws Exception {
		this.onEvent(event, -1L, false);
	}
}
