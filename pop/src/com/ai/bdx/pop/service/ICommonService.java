package com.ai.bdx.pop.service;

public interface ICommonService {
	/**
	 * 记录接口调用日志
	 * @param interfaceId:接口编码
	 * @param invokeType:调用类型
	 * @param invokeParam:参数
	 * @param invokeResult:结果
	 */
	public void writeInterfaceLog(String interfaceId, String invokeType, String invokeParam, String invokeResult);
}
