package com.asiainfo.biapp.pop.util.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.asiainfo.biapp.pop.model.CpeUserInfo;
import com.asiainfo.biapp.pop.util.TimeUtil;

public class CpeInstallFile2Bean {
	
	public static synchronized  List<CpeUserInfo> txtToCpeInstallBean(File installFile){
		if(installFile==null || !installFile.exists()){
			return null;
		}
		InputStream installInputStream =null;
		Reader installReader=null;
		BufferedReader installBufferReader=null;
		String line=null;
		List list=null;
		try {
			installInputStream=new FileInputStream(installFile);
			installReader=new InputStreamReader(installInputStream,"utf-8");
			installBufferReader=new BufferedReader(installReader);
			int i=0;
			list=new ArrayList();
			String  re_tel =" /^[1][345678][0-9]{9}$/";//手机号验证
			while(true){
				line = installBufferReader.readLine();
				if(line==null)break;
				System.out.println("第"+(i+1)+"行-------"+line);
				String[] li = line.split(",");
				//从第二行开始
				if(i>0){
					Date createDate = null;
					CpeUserInfo cpeUserInfo = new CpeUserInfo();
					for(int j=0;j<li.length;j++){
						//手机号匹配
					//	if(li[0].matches(re_tel)){
							if(j==0){
								cpeUserInfo.setProductNo(li[j]);
							}
							if(j==1){
								cpeUserInfo.setSubsid(li[j]);
							}
							if(j==2){
								cpeUserInfo.setNetType(li[j]);
							}
							if(j==3){
								cpeUserInfo.setCountryName(li[j]);
							}
							if(j==4){
								cpeUserInfo.setCreateTimeStr(li[j]);
							}
							if(j==5){
								cpeUserInfo.setPlanCode(li[j]);
							}
							if(j==6){
								
							}
							
				//		}
					}
					list.add(cpeUserInfo);
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally{
			if(installBufferReader!=null){
				try {
					installBufferReader.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}
	
}
