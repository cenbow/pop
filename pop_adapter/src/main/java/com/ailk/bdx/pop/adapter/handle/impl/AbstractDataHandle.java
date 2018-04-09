package com.ailk.bdx.pop.adapter.handle.impl;

import java.util.Collection;

import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.handle.IDataHandle;

public abstract class AbstractDataHandle implements IDataHandle {

	@Override
	public void handleCollection(Collection<Message> valueList) {
		for(Message value : valueList){
			handle(value);
		}
	}
}
