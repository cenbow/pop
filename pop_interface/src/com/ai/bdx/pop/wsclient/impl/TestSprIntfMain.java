package com.ai.bdx.pop.wsclient.impl;

public class TestSprIntfMain {
	public static void main(String[] args) {
		SendPccInfoServiceImpl impl = new SendPccInfoServiceImpl();
		try {
			if("S".equals(args[3])){
				System.out.println("command is : "+args[0]+" phone is : "+args[1]+" policy_id is : "+args[2]+" single or batch : "+args[3]+" yewu or yonghu: "+args[4]);
				impl.singlePhoneOpt(Short.parseShort(args[0]), args[1], args[2],args[4]);
			}else if("B".equals(args[3])){
				System.out.println("command is : "+args[0]+" table is : "+args[1]+" policy_id is : "+args[2]+" single or batch : "+args[3]+" yewu or yonghu: "+args[4]);
				impl.batchPhonesOpt(Short.parseShort(args[0]), args[1], args[2],args[4]);
			}else{
				impl.pccCheckSignJob();
			}
			
//			impl.singlePhoneOpt((short)10, "13907000000", "12345");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
