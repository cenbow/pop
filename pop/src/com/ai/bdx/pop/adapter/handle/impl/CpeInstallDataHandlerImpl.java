package com.ai.bdx.pop.adapter.handle.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.adapter.bean.BaseConfig;
import com.ai.bdx.pop.adapter.bean.Content;
import com.ai.bdx.pop.adapter.handle.ICpeInstallDataHandler;
import com.ai.bdx.pop.bean.CpeUserInfo;
import com.ai.bdx.pop.bean.Pop2BossCpeInstallResponse;
import com.google.common.base.Strings;

public class CpeInstallDataHandlerImpl implements ICpeInstallDataHandler{
	private static final Logger log = LogManager.getLogger(CpeInstallDataHandlerImpl.class);
	
	@Override
	public CpeUserInfo analyse(String line,BaseConfig config) {
		Content content = config.getContent();
		String split = content.getSplit();
		if(Strings.isNullOrEmpty(split)){
			log.error("请在source-config.xml中设置split文本分隔符！");
			return null;
		}
		CpeUserInfo cpeUserInfo = null;
		String[] splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(line,split);
		if(splitList == null || splitList.length != 7){
			log.error("BOSS提供的CPE开户数据文件(非首行)数据不符合规范，请检查！line: " + line );
			return null;
		}
		if(splitList.length == 7){
			cpeUserInfo = new CpeUserInfo(); 
			cpeUserInfo.setProductNo(splitList[0]);
			cpeUserInfo.setSubsid(splitList[6]);
			cpeUserInfo.setNetType(splitList[2]);
			cpeUserInfo.setCountryName(splitList[3]);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				cpeUserInfo.setCreateTime(new Timestamp(sdf.parse(splitList[4]).getTime()));
			} catch (ParseException e) {
				log.error("BOSS提供的CPE开户数据文件(非首行)的数据行的开户时间字段不符合规范，请检查！line: " + line );
			}
			cpeUserInfo.setPlanCode(splitList[5]);
		}
		
		return cpeUserInfo;
	}

	@Override
	public Pop2BossCpeInstallResponse analyseFirstLine(String line, BaseConfig config) {
		Content content = config.getContent();
		String split = content.getSplit();
		if(Strings.isNullOrEmpty(split)){
			log.error("请在source-config.xml中设置split文本分隔符！");
			return null;
		}
		Pop2BossCpeInstallResponse reponse = null;
		String[] splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(line,split);
		if (splitList == null || splitList.length != 2 ) {
			log.error("BOSS提供的CPE开户数据文件首行数据不符合规范，请检查！line: " + line );
			return null;
		}
		reponse = new Pop2BossCpeInstallResponse();
		if(splitList.length == 2){
			reponse.setCreateTime(splitList[0]);
			reponse.setCpeCount(splitList[1]);
		}
		
		return reponse;
	}

}
