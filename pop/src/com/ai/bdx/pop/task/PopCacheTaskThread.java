package com.ai.bdx.pop.task;

import com.ai.bdx.pop.phonefilter.IPopAvoidBotherFilter;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopTaskCache;
import com.ai.bdx.pop.util.SimpleCache;
import com.ai.bdx.pop.util.SpringContext;

/**
 * 启动缓存cacheThread
 * @author liyz
 * */
public class PopCacheTaskThread  extends Thread{

	@Override
	public void run(){
		IPopAvoidBotherFilter popAvoidBotherFilterImpl = SpringContext.getBean("popAvoidBotherFilterImpl",IPopAvoidBotherFilter.class);
		popAvoidBotherFilterImpl.initBotherData();
		PopTaskCache.getInstance().init();
		SimpleCache.getInstance().put(PopConstant.CACHE_OK, "1");
	}
	
}
