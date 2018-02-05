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
import java.util.List;

import org.apache.logging.log4j.core.helpers.Strings;
//import java.util.regex.Pattern;

import com.ai.bdx.pop.util.PopConstant;
import com.asiainfo.biapp.pop.model.CpeUserInfo;

public class CpeDeleteFile2Bean {
	public static  synchronized  List<CpeUserInfo> txtToCpeDeleteBean(File ftpFile){
		if(ftpFile==null || !ftpFile.exists()){
			return null;
		}
		InputStream installInputStream =null;
		Reader installReader=null;
		BufferedReader installBufferReader=null;
		String line=null;
		List<CpeUserInfo> list=null;
		try {
			installInputStream=new FileInputStream(ftpFile);
			installReader=new InputStreamReader(installInputStream,"utf-8");
			installBufferReader=new BufferedReader(installReader);
			int i=0;
			list=new ArrayList<CpeUserInfo>();
			CpeUserInfo cpeUserInfo =null;
			//Pattern p  =Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}");//手机号验证
			while(true){
				String[] fields=null;
				line = installBufferReader.readLine();
				if(line==null)break;
				System.out.println("销户文件"+ftpFile.getName()+"第"+(i+1)+"行-------"+line);
				fields = line.split(",");
				//从第二行开始
			if(i>0){
				//手机号匹配
			/*	Matcher m = p.matcher(li[0]);
				if(m.matches()){*/
					cpeUserInfo = new CpeUserInfo();
					
					if(fields.length > 1){
						cpeUserInfo.setProductNo(fields[0]);
					}
					
					if(fields.length > 2){
						cpeUserInfo.setSubsid(fields[1]);
					}
					
					if(fields.length > 3){
						if ("US24".equalsIgnoreCase(fields[2])) {// 欠费销户
							cpeUserInfo.setUserStatus(
									PopConstant.USER_STATUS_ARREARAGE);
						}
						if ("US20".equalsIgnoreCase(fields[2])) {// 销户
							cpeUserInfo.setUserStatus(
									PopConstant.USER_STATUS_CANCEL);
						}
					}
					
					if(Strings.isNotEmpty(cpeUserInfo.getSubsid()) 
							&& (cpeUserInfo.getUserStatus() == PopConstant.USER_STATUS_ARREARAGE 
							    || cpeUserInfo.getUserStatus() == PopConstant.USER_STATUS_CANCEL)){
						list.add(cpeUserInfo);
					}
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
	public static void main(String[] args) {
		
	}
}
