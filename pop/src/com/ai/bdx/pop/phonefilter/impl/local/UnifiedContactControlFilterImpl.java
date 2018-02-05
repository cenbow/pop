package com.ai.bdx.pop.phonefilter.impl.local;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.phonefilter.IDynamicStaticUserMatchFilter;
import com.ai.bdx.pop.phonefilter.IPopAvoidBotherFilter;
import com.ai.bdx.pop.phonefilter.IUnifiedContactControlFilter;
import com.ai.bdx.pop.util.ContactControlUtil;
import com.ai.bdx.pop.util.PopLogUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.string.StringUtil;
import com.ai.bdx.pop.buffer.DisruptorBuffer;
import com.ai.bdx.pop.buffer.IDataBuffer;

public class UnifiedContactControlFilterImpl implements IUnifiedContactControlFilter {
	private static Logger log = LogManager.getLogger();
	
	
	private IDynamicStaticUserMatchFilter dynamicStaticUserMatchFilter;
	
	
	private IPopAvoidBotherFilter popAvoidBotherFilter;
	
 
	public IPopAvoidBotherFilter getPopAvoidBotherFilter() {
		return popAvoidBotherFilter;
	}

	public void setPopAvoidBotherFilter(IPopAvoidBotherFilter popAvoidBotherFilter) {
		this.popAvoidBotherFilter = popAvoidBotherFilter;
	}

	public IDynamicStaticUserMatchFilter getDynamicStaticUserMatchFilter() {
		return dynamicStaticUserMatchFilter;
	}

	public void setDynamicStaticUserMatchFilter(
			IDynamicStaticUserMatchFilter dynamicStaticUserMatchFilter) {
		this.dynamicStaticUserMatchFilter = dynamicStaticUserMatchFilter;
	}



	@Override
	public void realTimeContactControl(String activityCode, JSONObject message)
			throws Exception {
		String productNo = message.getString(Configure.getInstance().getProperty("CEP_MESSAGE_KEY"));
		if (StringUtil.isNotEmpty(productNo)) {
			long t1 = System.currentTimeMillis();

			if (!ContactControlUtil.checkActiveStatus(activityCode)) {
				PopLogUtil.log(activityCode, productNo + " 活动状态控制");
				return;
			}

			//动静态用户匹配
			if (!dynamicStaticUserMatchFilter.matchUserAccount(activityCode, productNo)) {
				//PopLogUtil.log(activityCode, productNo + " 未与静态目标群匹配");
				return;
			}
		 
			//免打扰控制
			if (!popAvoidBotherFilter.allowPassFilter(activityCode, productNo)) {
				//PopLogUtil.log(activityCode, productNo + " 免打扰控制");
				return;
			}
		  
			//将匹配成功的用户信息存入队列和数据库
		//	log.debug("存储接触成功的用户（队列和数据库）");
			message.put("rule_id", activityCode); //将活动编码放入json对象中
			DisruptorBuffer.getInstance().push(message);
			log.debug("用户:" + productNo + ",处理共计耗时：" + (System.currentTimeMillis() - t1) + " ms.");
		}
		
	}

	 

 

}
