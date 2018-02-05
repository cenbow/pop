package com.ai.bdx.pop.wsclient.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	private static String productFileTime="";
	public static String getFileName(String instruction) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sdf.format(new Date());
		if ("".equals(productFileTime)) {

				productFileTime = currentTime;
				productFileTime = productFileTime + "_01";
			
		}else {
			if (productFileTime.contains(currentTime)) {
//				if (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length())) + 1 > 9) {
//					productFileTime = productFileTime + (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length())) + 1);
//				} else {
//					productFileTime = productFileTime +  (Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length())) + 1);
//				}
				
				String [] strings = productFileTime.split("_");
				//最后一位SN标记位
				String sn = strings[strings.length-1];
				//最后一位数字+1
				String sn1 = String.valueOf((Integer.parseInt(productFileTime.substring((productFileTime.length()-1),productFileTime.length())) + 1));
				if(sn.startsWith("0") && Integer.valueOf(sn1) != 10){
					for(int i=0;i<strings.length;i++){
						if(i==0){
							productFileTime = strings[0] + "_";
						}
						else if(i==strings.length-1){
							productFileTime = productFileTime + "0" + sn1;
						}
						else{
							productFileTime = productFileTime  + "_"+ strings[i];
						}
					}
				}else{
					for(int i=0;i<strings.length;i++){
						if(i==0){
							productFileTime = strings[0] + "_";
						}
						else if(i==strings.length-1){
							productFileTime = productFileTime  + (Integer.valueOf(sn)+1);
						}
						else{
							productFileTime = productFileTime +"_"+ strings[i];
						}
					}
				}
			}else{
				productFileTime = currentTime;
				productFileTime = productFileTime + "_01";
			}
		}
		return "BatFile_" + instruction + "_" + productFileTime + ".txt";
	}
	
	public static void main(String[] args) {
//		System.out.println(PropUtil.getProp("name", "telnet.properties"));
		Test test = new Test();
		for(int i=0;i<30;i++){
			System.out.println(test.getFileName("test"));
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<30;i++){
			System.out.println(test.getFileName("test"));
		}

	}
}
