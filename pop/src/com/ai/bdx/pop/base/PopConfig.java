package com.ai.bdx.pop.base;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import com.ai.bdx.pop.adapter.socket.buffer.PopBuffer;
import com.ai.bdx.pop.adapter.socket.server.InputSocketServer;
import com.ai.bdx.pop.buffer.DisruptorBuffer;
import com.ai.bdx.pop.controller.LoginController;
import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.suite.model.UserGroup;
import com.ai.bdx.pop.suite.model.UserRole;
import com.ai.bdx.pop.suite.model.UserUser;
import com.ai.bdx.pop.util.Log4j2LoggerFactory;
import com.ai.bdx.pop.util.PopConstant;
import com.ai.bdx.pop.util.PopUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.tx.TxByRegex;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;

public class PopConfig extends JFinalConfig {

	private static final Logger log = LogManager.getLogger();
	private static InputSocketServer socketServer = null;
	/**
	 * 配置常量
	 */
	@Override
	public void configConstant(Constants me) {
		// 初始化log4j2
		File file = new File(PathKit.getWebRootPath() + "/WEB-INF/classes/config/aibi_pop/log4j2.xml");
		System.setProperty("log4j.configurationFile", file.toURI().toString());
		System.setProperty("-DLog4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");// 使用异步日志
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.reconfigure();

		loadPropertyFile("config/aibi_pop/pop.properties");//主配置文件

		//设置常量
		me.setError404View("WEB-INF/views/common/error_404.html");
		me.setError500View("WEB-INF/views/common/error_500.html");
		me.setLoggerFactory(new Log4j2LoggerFactory());
		me.setDevMode("dev".equalsIgnoreCase(getProperty("RUN_MODE", "dev")));
		me.setViewType(ViewType.JSP);
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
		me.add("/", LoginController.class);
	}

	/**
	 * 配置插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		String dbType = getProperty("DBTYPE");
		try {
			String jndi = getProperty("JNDI_POP");
			Context context = (Context) new InitialContext().lookup("java:comp/env");
			DataSource datasource = (DataSource) context.lookup(jndi);
			// ActiveRecordPlugin
			AutoTableBindPlugin arp = new AutoTableBindPlugin(datasource);
			arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
			arp.setDialect(PopUtil.getDialectByType(dbType));
			arp.setShowSql(this.getPropertyToBoolean("HIBERNATE_DIALECT.SHOW_SQL", false));
			me.add(arp);

			//SpringPlugin
			//me.add(new SpringPlugin("classpath*:config/**/application*.xml"));
			me.add(new EhCachePlugin(PathKit.getWebRootPath() + "/WEB-INF/classes/config/aibi_pop/ehcache.xml"));
			//增加用户角色数据源 
			jndi = getProperty("JDBC_AIOMNI");
			DataSource ds1 = (DataSource) context.lookup(jndi);
			//AutoTableBindPlugin ap = new AutoTableBindPlugin("suite",datasource,"com/ai/bdx/pop/suite/model/");
			ActiveRecordPlugin ap = new ActiveRecordPlugin("suite",ds1);
		    ap.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		    ap.setDialect(PopUtil.getDialectByType(dbType));
		    ap.addMapping("user_user","userid",UserUser.class);
		    ap.addMapping("user_group","group_id",UserGroup.class);
		    ap.addMapping("user_role","role_id",UserRole.class);
		    me.add(ap);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * JFinal启动后需要加载执行的程序
	 */
	@Override
	public void afterJFinalStart() throws PopException{
		//初始化缓存
		DisruptorBuffer.getInstance().start();
		
		//初始化连接pop_adapter的Socket Server配置
		init();
		
		//启动与pop_adapter通信的ServerSocket
		start();
		
	}
	
	/**
	 * 初始化与pop_adapter通信的Socket Server配置
	 * @throws PopException
	 */
	private static void init() throws PopException{
		String adapterMethod = Configure.getInstance().getProperty("ADAPTER_METHOD");
		log.info("++++++++++ Adapter接入方式为：{}++++++++++", adapterMethod);
		if (PopConstant.ADAPTER_METHOD_AMQP.equalsIgnoreCase(adapterMethod)) {
			//通过消息组件传输
		} else if (PopConstant.ADAPTER_METHOD_SOCKET.equalsIgnoreCase(adapterMethod)) {
			socketServer = InputSocketServer.getInstance();
			try {
				socketServer.init();
			} catch (Exception e) {
				log.error("初始化pop与pop_adapter通信的Socket Server配置失败，没法接收锁网和开户消息！");
			}
		} else {
			throw new PopException("未配置Adapter接入方式");
		}
	}
	
	/**
	 * 启动与pop_adapter通信的ServerSocket
	 */
	private static void start() throws PopException {
		try {
			//接收从pop_adapter组件发来的开户和锁网事件消息缓存
			PopBuffer.getInstance().start();
			
			if (socketServer != null) {
				log.info("++++++++++ 启动pop与pop_adapter通信的ServerSocket++++++++++");
				socketServer.startService();
				log.info("++++++++++pop与pop_adapter通信的ServerSocket已启动++++++++++");
			}
		} catch (Throwable e) {
			throw new PopException("pop与pop_adapter通信的ServerSocket启动失败", e);
		}
	}

	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new TxByRegex(".*save.*"));
		me.add(new TxByRegex(".*update.*"));
		me.add(new TxByRegex(".*delete.*"));
		me.add(new ActionInterceptor());
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new UrlSkipHandler("/websocket/.*", false));
		me.add(new UrlSkipHandler("/asyncServlet/.*", false));
		me.add(new UrlSkipHandler("/services.*", false));
		me.add(new UrlSkipHandler("/aibi-approval/.*", false));
		me.add(new UrlSkipHandler("/aibi_approval/.*", false));
	}

	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 80, "/", 5);
	}
}
