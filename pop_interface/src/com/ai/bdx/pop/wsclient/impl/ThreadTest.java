package com.ai.bdx.pop.wsclient.impl;

public class ThreadTest extends Thread{
	private int num = 0;
	private int index = 0;
	public ThreadTest(int num,int index){
		this.num = num;
		this.index = index;
	}
	
	public void run() {
		while(index!=3){
			for(int i=0;i<num;i++){
				index++;
				System.out.println(index);
			}
		}
	}
}
