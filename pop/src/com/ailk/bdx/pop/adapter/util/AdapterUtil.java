package com.ailk.bdx.pop.adapter.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

/**
 * adapterg 工具类
 * @author lyz
 *
 */
public class AdapterUtil {
	
	
	private static int minaClientNum;
	
	static{
		minaClientNum = Integer.parseInt(Configure.getInstance().getProperty("ADAPTER_MINA_CLIENT_NUM"));
	}
	
	static Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");  
  
	/**
	 * 获取当前线程mina客户端索引
	 * @param currentNum
	 * @return
	 */
	public static int getNextIndex(Integer currentIndex) {
	   if(currentIndex==null|| currentIndex == 0 ||currentIndex == minaClientNum-1 ){
		   return 0;
	   }else{
		   return ++currentIndex;
	   }
	}
	
	/**
	 * 是否是合法的IP:PORT格式
	 * @return
	 */
	public static boolean isIpPort(String ipPort){
		return p.matcher(ipPort).matches();
	}
	
	/**对象转byte[]
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] objectToBytes(Object obj) throws Exception{
        byte[] bytes;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
		try {
			bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} finally {
			bo.close();
			oo.close();
		}
        return bytes;
    }
    
    /**byte[]转对象
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object bytesToObject(byte[] bytes) throws Exception{
        ObjectInputStream sIn = null;
        ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(bytes);
			sIn = new ObjectInputStream(in);
		} finally {
			in.close();
			sIn.close();
		}
        return sIn.readObject();
    }
	
}
