package com.ai.bdx.pop.buffer;

import net.sf.json.JSONObject;

import com.lmax.disruptor.EventFactory;

/**
 * Disruptor生产者，按行读文件消息包装类
 * @author guoyj
 *
 */
public class ValueEvent {
	private JSONObject value;

	public JSONObject getValue() {
		return value;
	}

	public void setValue(JSONObject value) {
		this.value = value;
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
}
