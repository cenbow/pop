package com.ailk.bdx.pop.adapter.process;

import java.util.Map;

 
/**
 * @description 
 * @author lyz
 */
 
public class SynDataProcesser {

	private static SynDataProcesser processer = new SynDataProcesser();
	
	private HandlerDataSubject subject = null;
	
	public static SynDataProcesser getInstance() {
		return processer;
	}

	private HandlerData2PopObserver mcdOberver = null;

	public SynDataProcesser() {
		subject = new HandlerDataSubject();
		mcdOberver = new HandlerData2PopObserver();
		subject.addObserver(mcdOberver);
	}

	public void addData(Map<String, String> dataMap) {
		subject.addData(dataMap);
	}
 
}
