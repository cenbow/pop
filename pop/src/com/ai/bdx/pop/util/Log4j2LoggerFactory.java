package com.ai.bdx.pop.util;

import com.jfinal.log.ILoggerFactory;
import com.jfinal.log.Logger;

public class Log4j2LoggerFactory implements ILoggerFactory {

	@Override
	public Logger getLogger(Class<?> paramClass) {
		return new Log4j2Logger(paramClass);
	}

	@Override
	public Logger getLogger(String paramString) {
		return new Log4j2Logger(paramString);
	}

}
