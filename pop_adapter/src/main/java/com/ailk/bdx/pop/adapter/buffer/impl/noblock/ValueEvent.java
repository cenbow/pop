package com.ailk.bdx.pop.adapter.buffer.impl.noblock;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.lmax.disruptor.EventFactory;

/**
 * Disruptor生产者，按行读文件消息包装类
 * @author guoyj
 *
 */
public class ValueEvent {
	private Message value;

	public Message getValue() {
		return value;
	}

	public void setValue(Message value) {
		this.value = value;
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
}
