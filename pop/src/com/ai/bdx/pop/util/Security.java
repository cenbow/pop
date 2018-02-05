package com.ai.bdx.pop.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.asiainfo.biframe.utils.string.DES;

public class Security {
	 public static final String CPE_TOKEN_KEY = "E3350A55F898A856";
	
	 public static String encrypt(String input, String key){  
	        byte[] crypted = null;  
	        try{  
	            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");  
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
	            cipher.init(Cipher.ENCRYPT_MODE, skey);  
	            crypted = cipher.doFinal(input.getBytes());  
	        }catch(Exception e){  
	        System.out.println(e.toString());  
	    }  
	    return new String(Base64.encodeBase64(crypted));  
	}  
	   
	    public static String decrypt(String input, String key){  
	        byte[] output = null;  
	        try{  
	            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");  
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
	            cipher.init(Cipher.DECRYPT_MODE, skey);  
	            output = cipher.doFinal(Base64.decodeBase64(input));  
	            }catch(Exception e){  
	            System.out.println(e.toString());  
	        }  
	        return new String(output);  
	    }  
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String key = "E3350A55F898A856";  
        String data = "admin";  
        System.out.println(Security.encrypt(data, key));  
        System.out.println(Security.decrypt("g4TqwsemErAcAdXxGD2+/g==", key)); 
        //System.out.println(DES.encrypt("xiangfeng"));
	}

}
