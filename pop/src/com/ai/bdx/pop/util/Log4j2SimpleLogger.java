package com.ai.bdx.pop.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.FormattedMessageFactory;

/**
 * 简单的log4j2日志实现类.
 *
 * @author wanglei
 */
public class Log4j2SimpleLogger extends Logger {

	/** The appender name. */
	public String appenderName;

	/**
	 * Instantiates a new log4j2 simple logger.
	 *
	 * @param fileAbsolutePath the file absolute path
	 * @param patternLayout the pattern layout
	 */
	public Log4j2SimpleLogger(String fileAbsolutePath, String patternLayout) {
		super(new AsyncLoggerContext(fileAbsolutePath), fileAbsolutePath, new FormattedMessageFactory());
		PatternLayout layout = PatternLayout.createLayout(patternLayout, null, null, "UTF-8", "true", "true");
		FileAppender appender = FileAppender.createAppender(fileAbsolutePath, "false", "false", "file_appender-"
				+ fileAbsolutePath, "false", "false", "true", null, layout, null, "false", null, null);

		appender.start();
		this.getContext().getConfiguration().getLoggerConfig("root2").addAppender(appender, Level.ALL, null);
		// Set to all levels
		this.getContext().getConfiguration().getLoggerConfig("root2").removeAppender("Console");
		this.setLevel(Level.ALL);
		this.appenderName = appender.getName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		this.getContext().getConfiguration().getLoggerConfig("root2").removeAppender(appenderName);
		super.finalize();
	}
}
