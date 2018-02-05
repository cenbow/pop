package com.asiainfo.biframe.privilege.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.asiainfo.biframe.manager.context.ContextManager;
import com.asiainfo.biframe.service.IActivator;
import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;

public class PrivilegeActivatorImpl implements IActivator {

	private static Logger log = LogManager.getLogger();

	public void start(ContextManager context) throws Exception {
		try {
			log.debug(" begin PrivilegeActivatorImpl");

			String confFilePath = context.getServletContextEvent().getServletContext()
					.getRealPath("/WEB-INF/classes/config/aibi_privilegeService/privilege.properties");
			Configure.getInstance().addConfFileName("AIBI_PRIVILEGE_PROPERTIES", confFilePath);
			//			context.registerCacheService("privlege-user-cache", UserCache.getInstance());
			//			context.registerCacheService("privlege-user-group-cache",UserGroupDefineCache.getInstance());
			//			context.registerCacheService("privlege-user-role-cache",UserRoleCache.getInstance());
			//			context.registerCacheService("privlege-user-city-cache",UserCityCache.getInstance());
			//			context.registerCacheService("privlege-user-company-cache",UserCompanyCache.getInstance());
			//			context.registerCacheService("privlege-user-duty-cache",UserDutyCache.getInstance());
			//			context.registerCacheService("privlege-sys-resources-type-cache",SysResourceTypeCache.getInstance());
			//			context.registerCacheService("privlege-menu-item-cache",SysMenuItemCache.getInstance());
			//			context.registerCacheService("privlege-user-city-dm-type",UserCityDmTypeCache.getInstance());

			log.debug(" end PrivilegeActivatorImpl");
		} catch (Exception e) {
			log.error(LocaleUtil.getLocaleMessage("privilegeService", "privilegeService.java.loadActivatorFail"), e);
		}
	}

	public void stop(ContextManager context) throws Exception {
	}

}
