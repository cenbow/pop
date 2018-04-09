package com.ailk.bdx.pop.adapter.filter.impl.hubei;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ailk.bdx.pop.adapter.bean.Content;
import com.ailk.bdx.pop.adapter.bean.KeyMessage;
import com.ailk.bdx.pop.adapter.bean.Message;
import com.ailk.bdx.pop.adapter.cache.interfaces.IPhoneConvertCache;
import com.ailk.bdx.pop.adapter.filter.IDataFilter;
import com.ailk.bdx.pop.adapter.model.custom.CpeLockNetInfo;
import com.ailk.bdx.pop.adapter.util.XmlConfigureUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
/**
 * 默认过滤接口实现类
 *
 */
public class DefaultDataFilter implements IDataFilter {

	private static final Splitter splitterComma = Splitter.on(",");
	private static final Splitter splitterUnderline = Splitter.on("_");
	private static final Logger log = LogManager.getLogger();
	private IPhoneConvertCache phoneConvertCache;

	public IPhoneConvertCache getPhoneConvertCache() {
		return phoneConvertCache;
	}

	public void setPhoneConvertCache(IPhoneConvertCache phoneConvertCache) {
		this.phoneConvertCache = phoneConvertCache;
	}
	
	@Override
	public KeyMessage adapte(Message msg){
		String value = msg.getValue();
		Content content = msg.getConfig().getContent();
		String targetIndex = content.getTargetFieldIndex();
		String targetFieldExtends = content.getTargetFieldExtends();
		List<String> indexSplit = splitterComma.splitToList(targetIndex);
		StringBuilder builder = new StringBuilder();
		String[] splitList;
		
		if("t".equals(content.getSplit())){
		      splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(value,"\t");
		}else{
			  splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(value, content.getSplit());
			  for(int i=0;i<splitList.length;i++){
					splitList[i] =splitList[i].trim();
			  }
		}
		//key字段索引
		int sourceKeyIndex = content.getSourceKeyIndex();
		
	    String phoneNo=""; //事件人手机号吗
		if(!Strings.isNullOrEmpty(content.getExtendIndex())){
			//获取eventIdIndex 
			int eventIdIndex = content.getEventidIndex();
		    String exentId = splitList[eventIdIndex];
		    String activeEventIDs = content.getActiveEventIDs();
		    String passiveEventIDs = content.getPassiveEventIDs();	
		    
		    List<String>  activeEventIDsList =  Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(activeEventIDs,","));
		    List<String>  passiveEventIDsList =  Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(passiveEventIDs,","));
			//phone
		    String phoneStr = content.getExtendPhoneIndex();
		    String[] phoneArray = phoneStr.split(",");
		    String callingPhoneIndex = phoneArray[0];
			String calledPhoneIndex = phoneArray[1];
			String callingNum =  splitList[Integer.parseInt(callingPhoneIndex)];
			String calledNum = splitList[Integer.parseInt(calledPhoneIndex)];			
			//判断事件类型 获取事件人手机号
			if(activeEventIDsList!=null && activeEventIDsList.contains(exentId)){
			 //主动类型 那么 主叫号码 是 事件人手机号 
				phoneNo = Strings.isNullOrEmpty(callingNum)?"":callingNum;
			}else if(passiveEventIDsList!=null && passiveEventIDsList.contains(exentId)){
			 //被动类型 那么 被叫号码 是 事件人手机号
				phoneNo = Strings.isNullOrEmpty(calledNum)?"":calledNum;
			}else{
				log.warn("++++未知事件类型:"+exentId+"+++++++++");
				return null; 
			}
			//手机号去86
		    if(!Strings.isNullOrEmpty(phoneNo)&&phoneNo.length()==13&&phoneNo.startsWith("86")){
		    	phoneNo =phoneNo.substring(2, phoneNo.length());
			}

		    if(Strings.isNullOrEmpty(phoneNo)){
		    	return null;
		    }
		    
		   value = value+","+phoneNo;
		   //将此追加到splitList 和  indexSplit 中
	   	   splitList = StringUtils.splitByWholeSeparatorPreserveAllTokens(value, content.getSplit());
	   	   targetIndex = targetIndex +","+content.getExtendIndex();
	       indexSplit = splitterComma.splitToList(targetIndex);
			
		}				
		//最后获取key(当前人手机号)
		String key=splitList[sourceKeyIndex];
		List<String> areaArrays = null;
		if(Strings.isNullOrEmpty(key)){
			return null;
		}else{
			if(key.length()==13&&key.startsWith("86")){
				key =key.substring(2, key.length());
			}
			if(key.length()<11){
				return null;		
			}else if(!Strings.isNullOrEmpty(content.getPhoneControlAreaNums())){
				String phoneNums = content.getPhoneControlAreaNums();
				String[] phoneNumsArrays = phoneNums.split(",");
				String areaStr = "";
				for(String subPhone:phoneNumsArrays){				
					String subKey=key.substring(0,Integer.parseInt(subPhone));
					areaStr = (String) phoneConvertCache.get(subKey);
					if(!Strings.isNullOrEmpty(areaStr)){
						break;
					}
				}
				String proRegStr = Strings.isNullOrEmpty(areaStr)?content.getDefaultProregNo():areaStr;
				areaArrays = splitterUnderline.splitToList(proRegStr);//第一个代表省份编码，第二个代表区域编码
			}
			
		}
		
		
		//常规处理
		for (int i = 0, n = indexSplit.size(); i < n; i++) {
			String index = indexSplit.get(i);
			if(!index.contains("_")){
			    String v="";
			    try{
			       v = splitList[Integer.valueOf(indexSplit.get(i))];
			    }catch (Exception e) {
			    	//如若是数据问题 直接返回null
					return null; 
				}
			    if(index.equals(sourceKeyIndex+"")){
			    	   //手机号去86
				    if(v.length()==13&&v.startsWith("86")){
						v = v.substring(2, v.length());
					}
			    }
			    if(Strings.isNullOrEmpty(v) || "null".equals(v)){
			    	v="";
			    }
				builder.append(v);
				if(i < n-1){
					builder.append(",");
				}
			}else{
				if(index.contains("f")){
					//翻译
					StringBuffer tmpSb = new StringBuffer(); 
					String indexs= index.substring(0, index.lastIndexOf("f"));
					List<String> underSplitList = splitterUnderline.splitToList(indexs);
					for (int t = 0, s = underSplitList.size(); t < s; t++) {
						tmpSb.append(splitList[Integer.valueOf(underSplitList.get(t))]);
					}
					String logicVlue = XmlConfigureUtil.getInstance().getLaciCache().get(tmpSb.toString());
					if(Strings.isNullOrEmpty(logicVlue)){
						logicVlue = "";
					}
					builder.append(logicVlue);
					builder.append(",");
				}else if(index.contains("p")){
					//是否需要省市编码
					builder.append(areaArrays.get(0));
					builder.append(",");
				}else if(index.contains("c")){
					//是否需要区域编码
					builder.append(areaArrays.get(1));
					builder.append(",");
				}else if(index.contains("l")){
					//是否截取最后两位
					String indexs= index.substring(0, index.lastIndexOf("_"));
					//套餐数
					String gprsValue = Strings.isNullOrEmpty(splitList[Integer.valueOf(indexs)])?"":splitList[Integer.valueOf(indexs)];
					if(gprsValue.contains("KB")||gprsValue.contains("kb")){
						gprsValue = gprsValue.substring(0, gprsValue.length()-2);
					}
					builder.append(gprsValue);
					builder.append(",");
				}else if(index.contains("s")){	
					//是否需要截取字段长度,默认值是128
					String indexs= index.substring(0, index.lastIndexOf("_"));
					String urlAll = Strings.isNullOrEmpty(splitList[Integer.valueOf(indexs)])?"":splitList[Integer.valueOf(indexs)];
					if(urlAll.contains(",")){
						urlAll.replaceAll(",", content.getReplaceAcceUrlComma());
					}
					if(urlAll.length()>Integer.parseInt(content.getSubAcceUrlNums())){
						urlAll = urlAll.substring(0, Integer.parseInt(content.getSubAcceUrlNums()));
					}
					builder.append(urlAll);
					builder.append(",");
				}else{
					List<String> underSplitList = splitterUnderline.splitToList(index);
					for (int t = 0, s = underSplitList.size(); t < s; t++) {
						String v = splitList[Integer.valueOf(underSplitList.get(t))];
						if(Strings.isNullOrEmpty(v)){
							v="";
						}
						builder.append(v);
						if(t < s-1){
						}else{
							builder.append(",");
						}
					}
				}
				
			}
		}
 		
		if(!Strings.isNullOrEmpty(targetFieldExtends)){	
			List<String> targetExtends = splitterUnderline.splitToList(targetFieldExtends);
			if(targetExtends.size()==1){
				builder.append(",");
				builder.append(areaArrays.get(Integer.parseInt(targetExtends.get(0))));
			}else{
				for (int i = 0, n = areaArrays.size(); i < n; i++) {
					builder.append(",");
					builder.append(areaArrays.get(i));
				}
			}				
		}
		
		if(builder.toString().lastIndexOf(",") == builder.length() -1){
			   builder.deleteCharAt(builder.length() - 1);
			}
		String targetValue = builder.toString();
		log.debug("手机号---"+key);
		log.debug("targetValue---"+targetValue);
		KeyMessage keyMsg = new KeyMessage(key,targetValue);
		return keyMsg;
	}
	
}
