package com.ailk.bdx.pop.adapter.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * adapterg 工具类
 * @author lyz
 *
 */
public class AdapterUtil {
	private static final Logger log = LogManager.getLogger(AdapterUtil.class);
	
	private static int minaClientNum;
	
	static Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");  
  
	/**
	 * 获取当前线程mina客户端索引
	 * @param currentNum
	 * @return
	 */
	public static int getNextIndex(Integer currentIndex) {
		minaClientNum = Integer.parseInt(Configure.getInstance().getProperty("ADAPTER_MINA_CLIENT_NUM"));
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
    
	public static String convertTac_Eci2UserLocation(String tac,String eci) throws Exception{
		return "130-46000" + addPrecursorZero(tac,5) + addPrecursorZero(eci,9);
	}
	
	public static String convertTacEci2UserLocation(String tacEci) throws Exception{
		return "130-46000" + tacEci;
	}
	
	/**
	 * 将数字字符串前面补0生成定长数字字符串
	 * @param value 目标数字字符串
	 * @param fixedLength 要生成的定长数字字符串的总长
	 * @return
	 */
	public static String addPrecursorZero(String value,int fixedLength )
			throws Exception{
		String format = "%0" + fixedLength + "d";
		String targetValue = value;
		try {
			targetValue = String.format(format, Integer.parseInt(value));
		} catch (Exception e) {
			throw new Exception();
		}
		return targetValue;
	}
	
	public static void main (String[] args) throws Exception{
		//System.out.println(convertTac_Eci2UserLocation("28738","4294967295"));
		System.out.println(addPrecursorZero("4294967295",9));
	}
	
}
