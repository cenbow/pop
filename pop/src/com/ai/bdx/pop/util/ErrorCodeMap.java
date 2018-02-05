package com.ai.bdx.pop.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 异常错误码与中文描述对照
 * 命名规则：前缀POP，后接四位数字码
 * 0xxx：系统公用异常信息
 * 1xxx：策略配置
 * 2xxx：策略管理
 * 3xxx：策略流程
 * 4xxx：策略派发
 * @author zhangyb5
 *
 */
public class ErrorCodeMap {
	static Map<String, String> errorCodeMap = Maps.newConcurrentMap();
	static {
		errorCodeMap.put("POP0000", "系统异常");
		
		errorCodeMap.put("POP1001", " 创建策略{0}任务出错,步骤:通过policyId 查询 -策略基本信息bean异常!");
		errorCodeMap.put("POP1002", " 创建策略{0}任务出错,步骤:创建任务表异常!");
		errorCodeMap.put("POP1003", " 创建策略{0}任务出错,步骤:通过policyId查询所有的规则异常!");
		errorCodeMap.put("POP1004", " 创建策略{0}任务出错,步骤:根据时间拆分任务异常!");
		
		
		errorCodeMap.put("POP2001", " task可否重启做校验-异常!");
		errorCodeMap.put("POP2002", " task可否重启做校验-不通过!");
		errorCodeMap.put("POP2003", " 重启任务失败!");
		
		
		errorCodeMap.put("POP3001", " 初始化task任务,清表失败!");
		errorCodeMap.put("POP3002", " 提取静态客户群失败!");
		errorCodeMap.put("POP3003", " 向cep发送指令失败!");
		errorCodeMap.put("POP3004", " 缓存静态客户群失败!");
		errorCodeMap.put("POP3005", " 调用pcc接口失败!");
		errorCodeMap.put("POP3006", " 数据入库失败!");
		
		
		errorCodeMap.put("POP4000", "策略派发失败-未知系统异常");
		errorCodeMap.put("POP4010", "策略派发失败-COC客户群Webservice接口异常");
		errorCodeMap.put("POP4011", "策略派发失败-COC客户群基础数据不合法");
		errorCodeMap.put("POP4012", "策略派发失败-COC客户群清单数据未生成");
		errorCodeMap.put("POP4013", "策略派发失败-COC客户群清单数据源不可用");
		errorCodeMap.put("POP4020", "策略派发失败-向PCC派发策略基本信息异常");
		errorCodeMap.put("POP4021", "策略派发失败-向PCC派发策略客户清单信息异常");
		
		errorCodeMap.put("POP5001", "向cep发暂停指令异常");
		errorCodeMap.put("POP5002", "关闭kafka监听异常");
		errorCodeMap.put("POP5003", "暂停更改表状态异常");
		
		errorCodeMap.put("POP5004", "向cep发启动指令异常");
		errorCodeMap.put("POP5005", "向CEP发消息注册启动事件异常");
		errorCodeMap.put("POP5006", "启动更改表状态异常");
		
		errorCodeMap.put("POP5007", "向cep发送终止指令异常");
		errorCodeMap.put("POP5009", "终止更改表状态异常");
		
		
		
	}

	public static String getMsg(String errorCode) {
		return errorCodeMap.get(errorCode);
	}
}
