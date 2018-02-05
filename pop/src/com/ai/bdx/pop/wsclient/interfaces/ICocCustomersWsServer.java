package com.ai.bdx.pop.wsclient.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by Apache CXF 2.6.11
 * 2014-01-14T11:07:41.118+08:00
 * Generated source version: 2.6.11
 * 
 */
@WebService(targetNamespace = "http://webservice.cm.biapp.ailk.com/", name = "ICocCustomersWsServer")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ICocCustomersWsServer {

	@WebResult(name = "return", targetNamespace = "http://webservice.cm.biapp.ailk.com/", partName = "return")
	@WebMethod
	public java.lang.String getTargetCustomersObj(@WebParam(partName = "id", name = "id") java.lang.String id);

	@WebResult(name = "return", targetNamespace = "http://webservice.cm.biapp.ailk.com/", partName = "return")
	@WebMethod
	public java.lang.String getCustomersList(@WebParam(partName = "userId", name = "userId") java.lang.String userId,
			@WebParam(partName = "sysId", name = "sysId") java.lang.String sysId);

	@WebResult(name = "return", targetNamespace = "http://webservice.cm.biapp.ailk.com/", partName = "return")
	@WebMethod
	public java.lang.String getTargetCustomersCycleObj(@WebParam(partName = "id", name = "id") java.lang.String id,
			@WebParam(partName = "date", name = "date") java.lang.String date);

}
