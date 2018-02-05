package com.ai.bdx.pop.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.exception.PopException;
import com.ai.bdx.pop.util.page.SplitPageBean;
import com.asiainfo.biframe.privilege.IUser;
import com.asiainfo.biframe.privilege.IUserSession;
import com.asiainfo.biframe.utils.i18n.LocaleUtil;
import com.jfinal.core.Controller;

/**
 * jsp直接加上基础路径，省事
 * 
 * @author wanglei
 *
 */
public class PopController extends Controller {
	private static Logger log = LogManager.getLogger();
	protected IUser user;
	protected String userGroupId;
	protected String userId;
	private static final String PAGE_BASE_PATH = "/WEB-INF/views/";
	protected static final String PRIVILEGE_SERVICE_NAME = "userPrivilegeCommonService";

	protected void initAttributes() throws PopException {
		IUserSession userSession = (IUserSession) this.getSessionAttr("biplatform_user");
		log.debug((new StringBuilder()).append("userSession=[").append(userSession).append("]").toString());
		if (userSession == null) {
			String msg = LocaleUtil.getLocaleMessage("privilegeService", "privilegeService.java.notLoginPrompt");
			log.info((new StringBuilder()).append("msg=[").append(msg).append("]").toString());
			throw new PopException(msg);
		} else {
			userId = userSession.getUserID();
			userGroupId = userSession.getGroupId();
			user = userSession.getUser();
		}
	}

	@Override
	public void render(String view) {
		super.render(PAGE_BASE_PATH + view);
	}

	@Override
	public void renderJsp(String view) {
		super.renderJsp(PAGE_BASE_PATH + view);
	}
	
	/**
	 * 
	 * newSplitPage:自动构建分页
	 * @return 
	 * @return SplitPageBean
	 */
	protected SplitPageBean newSplitPage() {
		// 1.获取分页处理当前的页号 pageNo ,pageNo为分页标签自动在页面创建；
		String pageNo = this.getPara("pageNo");
		int pageIndex = (pageNo == null || pageNo.equals("")) ? 1 : Integer.parseInt(pageNo);
		// 2.创建分页对象，初始化 当前页，每页记录数默认为20；
		SplitPageBean splitPage = new SplitPageBean(pageIndex);
		return splitPage;
	}
}
