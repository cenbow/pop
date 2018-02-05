package com.ai.bdx.pop.base;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.CaseFormat;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

/**
 * 自动完成表名和类名映射插件
 * 
 * @author wanglei
 *
 */
public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private static final Logger log = LogManager.getLogger();

	private static final String PACKAGE_PATH = "com/ai/bdx/pop/model/";
	private static final String CLASS_SUFFIX = ".class";

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, int transactionLevel) {
		super(dataSourceProvider, transactionLevel);
		// TODO Auto-generated constructor stub
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
		super(dataSourceProvider);
		// TODO Auto-generated constructor stub
	}

	public AutoTableBindPlugin(DataSource dataSource, int transactionLevel) {
		super(dataSource, transactionLevel);
		// TODO Auto-generated constructor stub
	}

	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean start() {
		try {
			String url = this.getClass().getClassLoader().getResource("/").getPath() + PACKAGE_PATH;
			try {
				url = java.net.URLDecoder.decode(url, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				log.error("", e1);
			}
			File file = new File(url);
			File[] arrayFile = file.listFiles();
			if (arrayFile != null && arrayFile.length > 0) {
				for (File fileIn : arrayFile) {
					if (fileIn.isFile()) {
						String modelName = fileIn.getName().substring(0,
								fileIn.getName().length() - CLASS_SUFFIX.length());
						Class clazz = Class.forName(PACKAGE_PATH.replace("/", ".") + modelName);
						String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
						this.addMapping(tableName, "id", clazz);
						log.debug("注册Model映射:  " + tableName + " —> " + modelName);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.error("", e);
		}

		return super.start();

	}
}
