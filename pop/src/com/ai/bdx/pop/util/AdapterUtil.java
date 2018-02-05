package com.ai.bdx.pop.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

import com.asiainfo.biframe.utils.config.Configure;

/**
 * adapterg 工具类
 * @author lyz
 *
 */
public class AdapterUtil {
	
	static Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");  
  
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
