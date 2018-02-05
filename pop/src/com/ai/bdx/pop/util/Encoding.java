package com.ai.bdx.pop.util;
import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Encoding {


	private static Logger log = LogManager.getLogger();
		public  static  String ecode="UTF-8";
		
		public static String encode(String str){
			 try {
				str=java.net.URLEncoder.encode(str,ecode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return str;
		}
		
		public static String decode(String str){
			 try {
				str=java.net.URLDecoder.decode(str,ecode);
				str=new String(str.getBytes("ISO-8859-1"),ecode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return str;
		}
		public static String decodeURL(String str,String encoding){
			 try {
				 log.debug("input filename="+str+ " encoding="+encoding);
				str=java.net.URLDecoder.decode(str,encoding);
				 log.debug("output filename="+str +" encoding="+encoding);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return str;
		}
	    
		public static void main(String args[]){
			System.out.println(Encoding.decodeURL("province%2Fuser_login%5C%E5%A4%A9%E6%B4%A5%5C2013-08-02%E5%AE%A2%E6%88%B7%E7%AB%AF%E7%99%BB%E9%99%86%E6%98%8E%E7%BB%86.xls",ecode));
		}




}
