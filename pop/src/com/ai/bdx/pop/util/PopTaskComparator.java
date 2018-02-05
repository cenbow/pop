package com.ai.bdx.pop.util;

import java.io.Serializable;
import java.util.Comparator;

import com.ai.bdx.pop.bean.PopTaskBean;
 

/**
 * 后台派单任务优先级比较
 * @author zhangyb5
 *
 */
public class PopTaskComparator implements Comparator<PopTaskBean>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7788527754428152480L;

	@Override
	public int compare(PopTaskBean task1, PopTaskBean task2) {
		int flag = task1.getExecStatus()+"".compareTo(task2.getExecStatus()+"");
		if (flag == 0) {
			flag = task1.getExecDate().compareTo(task2.getExecDate());
			if (flag == 0) {
				flag = task1.getTaskId().compareTo(task2.getTaskId());
			}
		}
		return flag;
	}

}
