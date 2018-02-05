package com.ai.bdx.pop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

import com.asiainfo.biframe.utils.config.Configure;

public class TelnetUtil {
	/** 新建一个TelnetClient对象 */
	private TelnetClient telnetClient = new TelnetClient();
	/** 系统标示符号 */
	private final String osTag = "\r\n";
	/** get Value 系统标示符号 */
	private final String getValOsTag = "END\r\n";
	/** 输入流，接收返回信息 */
	private InputStream in;
	/** 向 服务器写入 命令 */
	private PrintStream out;

	/**
	 * @param ip : telnet的IP地址
	 * @param port : 端口号，默认11211
	 */
	public TelnetUtil(String ip, Integer port) {
		try {
			telnetClient.connect(ip, port);
			in = telnetClient.getInputStream();
			out = new PrintStream(telnetClient.getOutputStream());
		} catch (Exception e) {
			System.out.println("[telnet] connect error: connect to [" + ip + ":" + port + "] fail!");
		}
	}

	public TelnetUtil(String ip) {
		try {
			telnetClient.connect(ip, 11211);
			in = telnetClient.getInputStream();
			out = new PrintStream(telnetClient.getOutputStream());
		} catch (Exception e) {
			System.out.println("[telnet] connect error: connect to [" + ip + ":" + 11211 + "] fail!");
		}
	}

	/**
	 * 执行telnet命令
	 *
	 * @param command
	 * @return
	 */
	public String execute(String command) {
		write(command);
		StringBuffer sb = new StringBuffer();
		String osTagX = osTag;
		if (command.startsWith("get")) {
			osTagX = getValOsTag;
		}
		try {
			char ch = (char) in.read();
			int isEnd = 0;
			while (true) {
				sb.append(ch);
				if (ch == osTagX.charAt(isEnd)) {
					isEnd++;
					if (sb.toString().endsWith(osTagX) && isEnd == osTagX.length())
						return sb.toString();
				} else {
					isEnd = 0;
				}
				ch = (char) in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "error! when the program execute";
	}

	/**
	 * 向telnet命令行输入命令
	 *
	 * @param command
	 */
	public void write(String command) {
		try {
			out.println(command);
			out.flush();
			System.out.println("[telnet] 打印本次执行的telnet命令:" + command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭Telnet连接
	 */
	public void disconnect() {
		try {
			Thread.sleep(10);
			telnetClient.disconnect();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 用于测试
	 *
	 * @param url
	 * @param port
	 */
	public static void testGet(String url, Integer port,String command) {
		System.out.println("----------------------------" + url + ":" + port + "----------------------------");
		TelnetUtil telnetTest = new TelnetUtil(url, port);
		//String result = telnetTest.execute("get apl");
		//System.out.println(result);
		String result2 = telnetTest.execute(command);
		System.out.println(result2);
		telnetTest.disconnect();
	}

	/**
	 * 存储服务器正在清空缓存服务器缓存
	 * @param url
	 * @param port
	 */
	public static void clearCache(String url, Integer port) {
		System.out.println("[telnet] 存储服务器正在清空缓存服务器缓存[" + url + ":" + port + "]----------------------------");
		TelnetUtil telnetTest = new TelnetUtil(url, port);
		String result = telnetTest.execute("flush_all");
		System.out.println(result);
		telnetTest.disconnect();
	}
	
	/** 
	* 以行为单位读取文件，常用于读面向行的格式化文件 
	* @param fileName 文件名 
	*/
	public static void readFileByLines(String ruleId ) {
    	String filePath= Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
		FileReader fileReader;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(filePath+ruleId+".txt");
			
			String hostIp=Configure.getInstance().getProperty("HUAWEIHOSTIP");
	    	Integer hostPort=Integer.parseInt(Configure.getInstance().getProperty("HUAWEIHOSTPORT"));
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(fileReader);
			String tempString = null;
			int line = 1;
			//一次读入一行，直到读入null为文件结束 
			while ((tempString = reader.readLine()) != null) {
				testGet(hostIp,hostPort,tempString);
				clearCache(hostIp,hostPort);
				//显示行号 
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		
		clearCache("192.168.101.193", 11211);
		clearCache("192.168.101.193", 12000);
	}

	
	
	
}