package com.ailk.bdx.pop.adapter.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.ailk.bdx.pop.adapter.util.SpringContext;

class Test1 implements Runnable
{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
public class Demo1 {
	private static final ApplicationContext springContext = SpringContext.getSpringApplicationContext();
	public static void main(String[] args) {
		//testExe();
		testExePool();
	}
	private static void testExePool() {
		// TODO Auto-generated method stub
		ExecutorService s = Executors.newFixedThreadPool(5);
     }
	private static void testExe() {
		// TODO Auto-generated method stub
		ScheduledExecutorService e = Executors.newSingleThreadScheduledExecutor();
		e.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("hello~~");
			}}, 0, 2, TimeUnit.SECONDS);
	}
}
