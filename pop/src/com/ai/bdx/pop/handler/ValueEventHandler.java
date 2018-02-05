package com.ai.bdx.pop.handler;

 
import net.sf.json.JSONObject;

import com.ai.bdx.pop.buffer.IDataHandle;
import com.ai.bdx.pop.buffer.ValueEvent;
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
			JSONObject value = event.getValue();
			dataHandle.handle(value);
		}
	}
	@Override
	public void onEvent(ValueEvent event) throws Exception {
		this.onEvent(event, -1L, false);
	}
}
