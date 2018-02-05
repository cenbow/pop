package com.ai.bdx.pop.adapter.socket.buffer;

import com.lmax.disruptor.EventFactory;

/**
 * 原始事件数据
 * @author zhangyb5
 *
 */
public class PopEvent {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public final static EventFactory<PopEvent> EVENT_FACTORY = new EventFactory<PopEvent>() {
		public PopEvent newInstance() {
			return new PopEvent();
		}
	};
}
