package com.ai.bdx.pop.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.base.PopController;

/**
 * 基站批量割接
 * 
 * @author 林
 * 
 */
public class Test1Controller extends PopController {
	private static final Logger log = LogManager
			.getLogger(BatchStationCutoverController.class);

	public void test() {
		log.info("进入基站割接页面.......");
		render("/cpeManager/test.jsp");
	}
}
