package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.asiainfo.biframe.utils.string.DES;



class TestSrv implements Runnable
{
	int count;
	public void service()
	{
		/*int count =0;
		for(int i=0;i<100;i++)count++;
		System.out.println(Thread.currentThread().getId()+":"+count);*/
		count++;
		System.out.println(count);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		service();
	}
}

public class Demo {
	public static void testRead(String dir) throws Exception
	{
		File  d = new File(dir);
		File[] fs = d.listFiles();
		
		ExecutorService es = Executors.newFixedThreadPool(2);
		long s = System.currentTimeMillis();
	    for(final File f:fs)
	    {
	    	
	    	try {
				BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				while(null!=b.readLine())
				{
				}	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    	/*es.execute(new Runnable(){

				@Override
				public void run() {
					long s = System.currentTimeMillis();
					// TODO Auto-generated method stub
					try {
						BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
						while(null!=b.readLine())
						{
						}	
						System.out.println("thread:"+Thread.currentThread().getId()+" process timesss:"+(System.currentTimeMillis()-s));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
	    		
	    	});*/
	    }
		System.out.println("thread:"+Thread.currentThread().getId()+" process timesss:"+(System.currentTimeMillis()-s));

	    //es.shutdown();
	    
	}
	@SuppressWarnings("unchecked")
	public static void testReadLine(File src) throws Exception
	{
		long s = System.currentTimeMillis();
		/*Files.readLines(src, Charset.forName("utf-8"),new LineProcessor<List<String>>() {

			@Override
			public List<String> getResult() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean processLine(String arg0) throws IOException {
				// TODO Auto-generated method stub
				//System.out.println(arg0);
				return true;
			}
		});*/
		BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(src)));
		while(null!=b.readLine())
		{
			
		}
		System.out.println("process time:"+(System.currentTimeMillis()-s));
	}

	 public static void testDes()
	 {
		 String pwd="";
			String pwd1="Wyzq1py!";
			pwd="FAB7CAAC252FBF58";
			try {
				pwd=DES.decrypt(pwd);
				pwd1=DES.encrypt(pwd1);
				System.out.println(pwd);
				//testRead("F:\\yaxin\\POP最终交接文档\\CPE测试数据\\CPE锁网数据文件\\h110002\\");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 private static int num;
	 private static boolean b;
	 static class T1 extends Thread
	 {
		 public void run()
		 {
			  while(!b)
			  {
				  this.yield();
			  }
			  System.out.println(num);
		 }
	 }
	/**
	 * @param args
	 */
	 
	 
	public static void main(String[] args) throws Exception {
		Demo d = new Demo();
		 ThreadLocal<String> s = new ThreadLocal<String>(){ 
			public String initialValue(){System.out.println("Sdfsdf");return "Sss";}
		 };
		 System.out.println(s.get());
    }

}
