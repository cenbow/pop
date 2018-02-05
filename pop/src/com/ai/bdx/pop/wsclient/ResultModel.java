package com.ai.bdx.pop.wsclient;

public class ResultModel {
	//返回结果代码（0-成功，1-失败）
	private String resultCode;
	//返回结果信息
	private String message;
	
	public ResultModel(){
		
	}
	public ResultModel(String resultCode,String message){
		this.resultCode = resultCode;
		this.message=message;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
