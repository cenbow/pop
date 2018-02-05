package com.ai.bdx.pop.bean;


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
import com.ai.bdx.pop.util.CpeUserInfo;

public class CpeBroadbandFileBean {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static  synchronized  List<CpeUserInfo> txtToCpeDeleteBean(File ftpFile){
		if(ftpFile==null || !ftpFile.exists()){
			return null;
		}
		InputStream installInputStream =null;
		Reader installReader=null;
		BufferedReader installBufferReader=null;
		String line=null;
		List list=null;
		try {
			installInputStream=new FileInputStream(ftpFile);
			installReader=new InputStreamReader(installInputStream,"utf-8");
			installBufferReader=new BufferedReader(installReader);
			int i=0;
			list=new ArrayList();
			//String  re_tel =" /^[1][345678][0-9]{9}$/";//手机号验证
			while(true){
				//读取文件
				line = installBufferReader.readLine();
				if(line==null)break;
				System.out.println("文件"+ftpFile.getName()+"第"+(i+1)+"行-------"+line);
				//以逗号分隔
				String[] li = line.split(",");
				//从第二行开始
				if(i>0){
					CpeUserInfo cpeUserInfo = new CpeUserInfo();
					cpeUserInfo.setProductNo(li[0]);
					//因与BOSS的接口调整，subsid改为第5个字段
					cpeUserInfo.setSubsid(li[4]);
					cpeUserInfo.setNetType(li[2]);
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
			
			if(installReader!=null){
				try {
					installReader.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			if(installInputStream!=null){
				try {
					installInputStream.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}
}
