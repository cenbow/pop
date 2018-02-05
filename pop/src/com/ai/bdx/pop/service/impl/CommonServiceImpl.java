package com.ai.bdx.pop.service.impl;

import java.util.Date;

import jodd.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.model.PopInterfaceLog;
import com.ai.bdx.pop.service.ICommonService;
import com.ai.bdx.pop.util.PopUtil;
import com.google.common.base.Strings;

public class CommonServiceImpl implements ICommonService {
	private static Logger log = LogManager.getLogger();
	@Override
	public void writeInterfaceLog(String interfaceId, String invokeType, String invokeParam, String invokeResult) {
		PopInterfaceLog pil = new PopInterfaceLog();
		try {
			invokeResult = StringUtil.shorten(invokeResult, 240, "...");
			
			pil.set(PopInterfaceLog.COL_ID, PopUtil.generateUUID()).set(PopInterfaceLog.COL_INTERFACE_ID, interfaceId)
					.set(PopInterfaceLog.COL_INVOKE_TYPE, invokeType).set(PopInterfaceLog.COL_INVOKE_TIME, new Date())
					.set(PopInterfaceLog.COL_INVOKE_PARAM, invokeParam)
					.set(PopInterfaceLog.COL_INVOKE_RESULT, invokeResult).save();
		} catch (Exception e) {
			log.warn("保存接口日志异常：", e);
		}
	}
}
