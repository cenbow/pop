package com.ai.bdx.pop.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;

public class LoginController extends PopController {
	private static final Logger log = LogManager.getLogger();

	public void index() {
		render("login/login.jsp");
	}

	public void verifyUser() {
		render("login/verifyUser.jsp");
	}
	
	public void createVerifyCode(){
		HttpSession session = getSession();
		String vation = this.getPara("vation");
		session.setAttribute("vation", vation);
		this.renderJson("succ");
	}
}
