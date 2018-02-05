package com.ai.bdx.pop.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.jfinal.log.Logger;

public class Log4j2Logger extends Logger {
	private org.apache.logging.log4j.Logger log;

	public Log4j2Logger(Class<?> clazz) {
		this.log = LogManager.getLogger(clazz);
	}

	public Log4j2Logger(String name) {
		this.log = LogManager.getLogger(name);
	}

	@Override
	public void debug(String arg0) {
		this.log.log(Level.DEBUG, arg0);
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		this.log.log(Level.DEBUG, arg0, arg1);
	}

	@Override
	public void error(String arg0) {
		this.log.log(Level.ERROR, arg0);
	}

	@Override
	public void error(String arg0, Throwable arg1) {
		this.log.log(Level.ERROR, arg0, arg1);
	}

	@Override
	public void fatal(String arg0) {
		this.log.log(Level.FATAL, arg0);
	}

	@Override
	public void fatal(String arg0, Throwable arg1) {
		this.log.log(Level.FATAL, arg0, arg1);
	}

	@Override
	public void info(String arg0) {
		this.log.log(Level.INFO, arg0);
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		this.log.log(Level.INFO, arg0, arg1);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return this.log.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return this.log.isFatalEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return this.log.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return this.log.isWarnEnabled();
	}

	@Override
	public void warn(String arg0) {
		this.log.log(Level.WARN, arg0);
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		this.log.log(Level.WARN, arg0, arg1);
	}

}
