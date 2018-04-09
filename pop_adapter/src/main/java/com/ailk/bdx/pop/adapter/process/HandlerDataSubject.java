 
package com.ailk.bdx.pop.adapter.process;

import java.util.Map;
import java.util.Observable;

/**
 * @description 被观察者
 * @author lyz
 */
public class HandlerDataSubject extends Observable 
{
	/**
	 * 
	 * @description 每次过来数据都会唤醒观察者
	 * @param s
	 * @exception
	 * @see
	 */
	public void addData(Map<String,String> s){
		setChanged(); 
		//唤醒观察者
		notifyObservers(s);
	}
}
