package com.chinamobile.pcrf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "ScfPccSoapServiceEndpoint", targetNamespace = "http://www.chinamobile.com/PCRF/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ScfPccSoapServiceEndpoint {

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO addSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO delSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO getSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO getSubscriberAllService(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO getSubscriberAllInf(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO subscribeService(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriptionParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO unSubscribeService(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriptionParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO updateSubscribedService(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriptionParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO updateSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO subscribeUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInUsrSessionPolicyParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO updateUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInUsrSessionPolicyParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO unsubscribeUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInUsrSessionPolicyParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO getSubscriberAllUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO addUsrLocation(
			@WebParam(name = "inPara", partName = "inPara") SInUsrLocationParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO updateUsrLocation(
			@WebParam(name = "inPara", partName = "inPara") SInUsrLocationParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO delUsrLocation(
			@WebParam(name = "inPara", partName = "inPara") SInUsrLocationParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVO
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVO getUsrLocation(
			@WebParam(name = "inPara", partName = "inPara") SInSubscriberParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT addBatSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT updateBatSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT delBatSubscriber(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT addBatService(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT updateBatService(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT delBatService(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT addBatUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT updateBatUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT delBatUsrSessionPolicy(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

	/**
	 * 
	 * @param inPara
	 * @return returns com.chinamobile.pcrf.SReturnVOBAT
	 */
	@WebMethod(operationName = "PCRF_CHKBAT")
	@WebResult(name = "result", partName = "result")
	public SReturnVOBAT pcrfCHKBAT(
			@WebParam(name = "inPara", partName = "inPara") SInBatFileParaVO inPara);

}